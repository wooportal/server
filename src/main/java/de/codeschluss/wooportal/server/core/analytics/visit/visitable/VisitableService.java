package de.codeschluss.wooportal.server.core.analytics.visit.visitable;

import java.util.List;
import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.analytics.visit.VisitHelper;
import de.codeschluss.wooportal.server.core.analytics.visit.page.PageService;
import de.codeschluss.wooportal.server.core.analytics.visit.visitor.VisitorEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitor.VisitorService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.repository.DataRepository;
import de.codeschluss.wooportal.server.core.repository.RepositoryService;
import de.codeschluss.wooportal.server.core.security.jwt.JwtUserDetails;

@Service
public class VisitableService<V extends VisitableEntity<?>> {
  
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
    createVisitable(entity);
  }
  
  public <E extends BaseEntity> void saveOverviewVisit(String overview)
      throws Throwable {
    var entity = pageService.getByName(overview);
    createVisitable(entity);
  }

  @SuppressWarnings("unchecked")
  private void createVisitable(BaseEntity parent) 
      throws Throwable {
    var visitableClass = VisitHelper.getVisitableType(parent);
    var visitableRepo = getRepository(visitableClass);
    var visitor = (VisitorEntity) Hibernate.unproxy(createVisitor());
    
    var findByParentAndVisitor = visitableRepo.getClass().getMethod(
        "findByParentAndVisitor", parent.getClass().getSuperclass(), visitor.getClass());

    var visitable = (V) findByParentAndVisitor.invoke(visitableRepo, parent, visitor);

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
  
  public Integer calculateVisitors(BaseEntity entity) 
      throws Throwable {
    var visitables = getVisitables(entity);
    return visitables != null && !visitables.isEmpty() ? visitables.size() : 0;
  }
  
  public Integer calculateVisitors(CrudController<?,?> controller) 
      throws Throwable {
    var visitables = getVisitables(
        pageService.getByName(VisitHelper.getVisitableOverview(controller)));
    return visitables != null && !visitables.isEmpty() ? visitables.size() : 0;
  }
  
  public Integer calculateVisits(BaseEntity entity) 
      throws Throwable {
    var visitables = getVisitables(entity);
    return visitables != null && !visitables.isEmpty() 
        ? visitables.stream().map(v -> v.getVisits()).reduce(0, Integer::sum)
        : 0;
  }
  
  public Integer calculateVisits(CrudController<?,?> controller) 
      throws Throwable {
    var visitables = getVisitables(
        pageService.getByName(VisitHelper.getVisitableOverview(controller)));
    return visitables != null && !visitables.isEmpty() 
        ? visitables.stream().map(v -> v.getVisits()).reduce(0, Integer::sum)
        : 0;
  }
  
  @SuppressWarnings("unchecked")
  private List<V> getVisitables(BaseEntity entity) throws Throwable {
    var visitableRepo = getRepository(VisitHelper.getVisitableType(entity));
    
    var findByParent = visitableRepo.getClass().getMethod(
        "findByParent", entity.getClass().getSuperclass());

    return (List<V>) findByParent.invoke(visitableRepo, entity);
  }

  public VisitableRepository<?> getRepository(Class<VisitableEntity<?>> visitableClass) {
    DataRepository<?> repo = repoService.getRepository(visitableClass);
    if (!(repo instanceof VisitableRepository<?>)) {
      throw new RuntimeException(
          "Repository of " + repo.getClass().toString() + " must inherit from " + VisitableRepository.class);
    }
    return (VisitableRepository<?>) repo;
  }

  public boolean isValidVisitor() {
    return !isPrivateIpAddress() && !isSuperUser();
  }

  private boolean isPrivateIpAddress() {
    return request.getRemoteAddr().startsWith("192.")
        || request.getRemoteAddr().startsWith("172.")
        || request.getRemoteAddr().startsWith("10.");
  }
  
  private boolean isSuperUser() {
    var userPrincipal = request.getUserPrincipal();
    if (userPrincipal != null && userPrincipal instanceof JwtUserDetails) {
      return ((JwtUserDetails) userPrincipal).isSuperUser(); 
    }
    return false;
  }

}
