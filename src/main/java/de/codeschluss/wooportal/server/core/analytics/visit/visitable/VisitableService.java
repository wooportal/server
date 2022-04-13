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
import de.codeschluss.wooportal.server.core.config.GeneralPropertyConfiguration;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.repository.DataRepository;
import de.codeschluss.wooportal.server.core.repository.RepositoryService;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;

@Service
public class VisitableService<V extends VisitableEntity<?>> {
  
  @Autowired
  private GeneralPropertyConfiguration generalConfig;
  
  @Autowired
  private PageService pageService;
  
  @Autowired
  protected HttpServletRequest request;
  
  @Autowired
  protected AuthorizationService authService;
  
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
  private void createVisitable(BaseEntity parent) {
    try {
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
    } catch(Exception ignore) { }

  }
  
  private VisitorEntity createVisitor() throws ServiceUnavailableException {
    var visitor = new VisitorEntity();
    visitor.setUserAgent(request.getHeader("User-Agent"));
    visitor.setIpAddress(retrieveUserAddress());
    return visitorService.add(visitor);
  }
  
  public List<VisitableEntity<?>> getVisitablesForEntity(BaseEntity entity) 
      throws Throwable {
    var result = getVisitables(entity);
    if (result != null && !result.isEmpty()) {
      return result;
    }
    throw new NotFoundException("no visitors so far");
  }
  
  public List<VisitableEntity<?>> getVisitablesForOverview(CrudController<?,?> controller) 
      throws Throwable {
    return getVisitables(
        pageService.getByName(VisitHelper.getVisitableOverview(controller.getClass())));
  }
  
  @SuppressWarnings("unchecked")
  private List<VisitableEntity<?>> getVisitables(BaseEntity entity) throws Throwable {
    var visitableRepo = getRepository(VisitHelper.getVisitableType(entity));
    
    var findByParent = visitableRepo.getClass().getMethod(
        "findByParent", entity.getClass().getSuperclass());

    return (List<VisitableEntity<?>>) findByParent.invoke(visitableRepo, entity);
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
    return !isPrivateIpAddress() && !authService.isSuperUser();
  }

  private boolean isPrivateIpAddress() {
    var ipAddress = retrieveUserAddress();
    return ipAddress.startsWith("192.")
        || ipAddress.startsWith("172.")
        || ipAddress.startsWith("127.")
        || ipAddress.startsWith("10.");
  }
  
  private String retrieveUserAddress() {
    var ipAddress = request.getHeader(generalConfig.getClientIpHeader());
    return ipAddress != null && !ipAddress.isBlank()
        ? ipAddress
        : request.getRemoteAddr();
  }
}
