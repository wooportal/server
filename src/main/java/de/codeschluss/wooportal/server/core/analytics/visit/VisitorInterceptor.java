package de.codeschluss.wooportal.server.core.analytics.visit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;

@Component
@Aspect
public class VisitorInterceptor {

  @Pointcut("execution(* de.codeschluss.wooportal.server.core.api.CrudController+.readOne(..))")
  private void readOne() { }

  @Pointcut("execution(* de.codeschluss.wooportal.server.core.api.CrudController+.readAll(..))")
  private void readAll() { }

  @Autowired
  private VisitorService visitorService;
  
  @SuppressWarnings("unchecked")
  @Around("readOne()")
  public <E extends BaseEntity> Object saveVisitForEntity(ProceedingJoinPoint pjp) 
        throws Throwable {
    Object result = pjp.proceed();
    if (visitorService.isValidVisitor() && VisitorHelper.isVisitable(pjp.getThis())) {
      if (result != null) {
        visitorService.saveEntityVisit((E) ((Resource<?>) result).getContent());
      }
    }
    return result;
  }

  @Around("readAll()")
  public <E extends BaseEntity>  Object saveVisitForOverviewPage(ProceedingJoinPoint pjp)
        throws Throwable {
    Object result = pjp.proceed();
    var overviewName = VisitorHelper.getVisitableOverview(pjp.getThis());
    if (visitorService.isValidVisitor() && overviewName != null) {
      visitorService.saveOverviewVisit(overviewName);
    }
    return result;
  }
}
