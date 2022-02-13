package de.codeschluss.wooportal.server.core.analytics.visit.visitable;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.analytics.visit.VisitHelper;
import de.codeschluss.wooportal.server.core.analytics.visit.page.PageService;
import de.codeschluss.wooportal.server.core.analytics.visit.visitor.VisitorEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitor.VisitorService;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.repository.DataRepository;
import de.codeschluss.wooportal.server.core.repository.RepositoryService;
import de.codeschluss.wooportal.server.core.security.jwt.JwtUserDetails;

@Service
public class VisitableService {
  
  @Autowired
  private PageService pageService;
  
  @Autowired
  protected HttpServletRequest request;
  
  @Autowired
  private RepositoryService repoService;
  
  @Autowired
  private VisitorService visitorService;

  public <E extends BaseEntity> void saveEntityVisit(E entity)
      throws Throwable {
    createVisitable(VisitHelper.getVisitableType(entity), entity);
  }
  
  public <E extends BaseEntity> void saveOverviewVisit(String overview)
      throws Throwable {
    var entity = pageService.getByName(overview);
    createVisitable(VisitHelper.getVisitableType(entity), entity);
  }

  @SuppressWarnings("unchecked")
  private <V extends VisitableEntity<?>> void createVisitable(
      Class<VisitableEntity<?>> visitableClass, BaseEntity parent) throws Throwable {
    DataRepository<V> repo = repoService.getRepository(visitableClass);
    if (!(repo instanceof VisitableRepository<?>)) {
      throw new RuntimeException(
          "Repository of " + repo.getClass().toString() + " must inherit from " + VisitableRepository.class);
    }
    var visitableRepo = (VisitableRepository<?>) repo;
    var visitor = (VisitorEntity) Hibernate.unproxy(createVisitor());
    
    var findByParentAndVisitor = visitableRepo.getClass().getMethod(
        "findByParentAndVisitor", parent.getClass().getSuperclass(), visitor.getClass());

    V visitable = (V) findByParentAndVisitor.invoke(visitableRepo, parent, visitor);

    if (visitable == null) {
      visitable = (V) visitableClass.getDeclaredConstructor().newInstance();
      visitable.setVisitor(visitor);
      visitable.setParent(parent);
    }
    
    visitable.addVisit();
    repoService.save(visitable);
  }
  
  private VisitorEntity createVisitor() throws ServiceUnavailableException {
    var visitor = new VisitorEntity();
    visitor.setUserAgent(request.getHeader("User-Agent"));
    visitor.setIpAddress(request.getRemoteAddr());
    return visitorService.add(visitor);
  }

  public boolean isValidVisitor() {
    return isNoPrivateIpAddress() && isNoSuperUser();
  }

  private boolean isNoPrivateIpAddress() {
    return !request.getRemoteAddr().startsWith("192.")
        && !request.getRemoteAddr().startsWith("172.")
        && !request.getRemoteAddr().startsWith("10.");
  }
  
  private boolean isNoSuperUser() {
    JwtUserDetails jwtUserDetails = (JwtUserDetails) request.getUserPrincipal();
    return jwtUserDetails == null || !jwtUserDetails.isSuperUser();
  }

}
