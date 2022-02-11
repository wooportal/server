package de.codeschluss.wooportal.server.core.analytics.visit;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.entities.VisitorRepository;
import de.codeschluss.wooportal.server.core.analytics.visit.page.PageService;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.repository.DataRepository;
import de.codeschluss.wooportal.server.core.repository.RepositoryService;
import de.codeschluss.wooportal.server.core.security.jwt.JwtUserDetails;

@Service
public class VisitorService {
  
  @Autowired
  private RepositoryService repoService;
  
  @Autowired
  private PageService pageService;
  
  @Autowired
  protected HttpServletRequest request;
  
  @Autowired
  public VisitorService(
      RepositoryService repoService,
      PageService pageService) {
    this.repoService = repoService;
    this.pageService = pageService;
  }

  public <E extends BaseEntity> void saveEntityVisit(E entity)
      throws Throwable {
    VisitorEntity<?> visitor = getVisitor(VisitorHelper.getVisitorType(entity), entity);

    visitor.addVisit();
    visitor.setUserAgent(request.getHeader("User-Agent"));
    visitor.setIpAddress(request.getRemoteAddr());
    visitor.setParent(entity);
    
    repoService.save(visitor);
  }
  
  public <E extends BaseEntity> void saveOverviewVisit(String overview)
      throws Throwable {
    var entity = pageService.getByName(overview);
    VisitorEntity<?> visitor = getVisitor(VisitorHelper.getVisitorType(entity), entity);

    visitor.addVisit();
    visitor.setUserAgent(request.getHeader("User-Agent"));
    visitor.setIpAddress(request.getRemoteAddr());
    visitor.setParent(entity);
    
    repoService.save(visitor);
  }
  
  @SuppressWarnings("unchecked")
  private <V extends VisitorEntity<?>> V getVisitor(
      Class<VisitorEntity<?>> visitorClass, Object parent) throws Throwable {
    DataRepository<V> repo = repoService.getRepository(visitorClass);
    if (repo instanceof VisitorRepository<?>) {
      var visitorRepo = (VisitorRepository<?>) repo;
      
      var findByParentAndIpAddress = visitorRepo.getClass().getMethod(
          "findByParentAndIpAddress", parent.getClass().getSuperclass(), String.class);

      V existingVisitor = (V) findByParentAndIpAddress.invoke(visitorRepo,
          parent, request.getRemoteAddr());

      return existingVisitor != null 
          ? existingVisitor
          : (V) visitorClass.getDeclaredConstructor().newInstance();
    }
    throw new RuntimeException(
        "Repository of " + repo.getClass().toString() + " must inherit from " + VisitorRepository.class);
  }

  public boolean isValidVisitor() {
    return true;
//   return isNoPrivateIpAddress() && isNoSuperUser();
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
