package de.codeschluss.wooportal.server.core.analytics.visit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableEntity;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableService;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;

@Component
@Aspect
public class VisitInterceptor<V extends VisitableEntity<?>> {

  @Pointcut("execution(* de.codeschluss.wooportal.server.core.api.CrudController+.readOne(..))")
  private void readOne() { }

  @Pointcut("execution(* de.codeschluss.wooportal.server.core.api.CrudController+.readAll(..))")
  private void readAll() { }

  @Autowired
  private VisitableService<V> visitableService;
  
  @SuppressWarnings("unchecked")
  @Around("readOne()")
  public <E extends BaseEntity> Object saveVisitForEntity(ProceedingJoinPoint pjp) 
        throws Throwable {
    Object result = pjp.proceed();
    if (visitableService.isValidVisitor() && VisitHelper.isVisitable(pjp.getThis())) {
      if (result != null) {
        visitableService.saveEntityVisit((E) ((Resource<?>) result).getContent());
      }
    }
    return result;
  }

  @Around("readAll()")
  public <E extends BaseEntity>  Object saveVisitForOverviewPage(ProceedingJoinPoint pjp)
        throws Throwable {
    Object result = pjp.proceed();
    var overviewName = VisitHelper.getVisitableOverview(pjp.getThis());
    if (visitableService.isValidVisitor() && overviewName != null) {
      visitableService.saveOverviewVisit(overviewName);
    }
    return result;
  }
}
