package de.codeschluss.wooportal.server.components.organisation;

import java.util.List;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import de.codeschluss.wooportal.server.core.security.services.AuthorizationService;
import de.codeschluss.wooportal.server.core.utils.PageUtils;

@Component
@Aspect
public class OrganisationFilterInterceptor {
  
  @Pointcut(
      "execution(* de.codeschluss.wooportal.server.components.organisation.OrganisationRepository+.findAll(..))")
  private void findAll() { }
  
  @Autowired
  protected AuthorizationService authService;
  
  @SuppressWarnings("unchecked")
  @Around("findAll()")
  public Object filterOutNotApproved(ProceedingJoinPoint pjp) throws Throwable {
    Object result = pjp.proceed();
    if (result instanceof Iterable<?> && !authService.isSuperUser()) {
      List<OrganisationEntity> list = (List<OrganisationEntity>) PageUtils.convertToList(result);
      if (result instanceof Page<?>) {
        return new PageImpl<>(
            list.stream().filter(e -> e.isApproved()).collect(Collectors.toList()), 
            ((Page<?>) result).getPageable(), 
            ((Page<?>) result).getTotalElements());
      }
      list.removeIf(e -> !e.isApproved());
    }
    return result;
  }
}
