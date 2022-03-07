package de.codeschluss.wooportal.server.components.blog;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import de.codeschluss.wooportal.server.core.security.jwt.JwtUserDetails;
import de.codeschluss.wooportal.server.core.utils.PageUtils;

@Component
@Aspect
public class BlogFilterInterceptor {
  
  @Pointcut(
      "execution(* de.codeschluss.wooportal.server.components.blog.BlogRepository+.findAll(..))")
  private void findAll() { }
  
  @Autowired
  protected HttpServletRequest request;
  
  @SuppressWarnings("unchecked")
  @Around("findAll()")
  public Object listFilterOutNotApproved(ProceedingJoinPoint pjp) throws Throwable {
    Object result = pjp.proceed();
    if (result instanceof Iterable<?> && !isSuperUser()) {
      List<BlogEntity> list = (List<BlogEntity>) PageUtils.convertToList(result);
      if (result instanceof Page<?>) {
        return new PageImpl<>(
            list.stream().filter(e -> e.getApproved()).collect(Collectors.toList()), 
            ((Page<?>) result).getPageable(), 
            ((Page<?>) result).getTotalElements());
      }
      list.removeIf(e -> !e.getApproved());
    }
    return result;
  }
  
  private boolean isSuperUser() {
    var userPrincipal = request.getUserPrincipal();
    if (userPrincipal != null && userPrincipal instanceof JwtUserDetails) {
      return ((JwtUserDetails) userPrincipal).isSuperUser(); 
    }
    return false;
  }
}
