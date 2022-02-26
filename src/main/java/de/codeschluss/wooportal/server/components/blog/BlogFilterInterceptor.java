package de.codeschluss.wooportal.server.components.blog;

import java.util.List;
import java.util.Optional;
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
import com.icegreen.greenmail.imap.AuthorizationException;
import de.codeschluss.wooportal.server.core.security.jwt.JwtUserDetails;
import de.codeschluss.wooportal.server.core.utils.PageUtils;

@Component
@Aspect
public class BlogFilterInterceptor {

  @Pointcut(
      "execution(public * de.codeschluss.wooportal.server.components.blog.BlogRepository+.findOne(..))")
  private void findOne() { }
  
  @Pointcut(
      "execution(* de.codeschluss.wooportal.server.components.blog.BlogRepository+.findAll(..))")
  private void findAll() { }

  @Pointcut(
      "execution(public * de.codeschluss.wooportal.server.core.api.AssemblerHelper.toResource(..))")
  private void toResource() {}
  
  @Pointcut(
      "execution(public * de.codeschluss.wooportal.server.core.api.AssemblerHelper.resourceWithEmbeddable(..))")
  private void resourceWithEmbeddable() { }
  
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
  
  @SuppressWarnings("unchecked")
  @Around("findOne()")
  public Object checkAuthorization(ProceedingJoinPoint pjp) throws Throwable {
    Object result = pjp.proceed();
    if (result instanceof Optional<?> && ((Optional<?>) result).isPresent() && !isSuperUser()) {
      BlogEntity entity = ((Optional<BlogEntity>) result).get();
      if (!entity.getApproved()) {
        throw new AuthorizationException("blog with id: "+ entity.getId() + " is not approved");
      }
    }
    return result;
  }
  
  @Around("toResource() || resourceWithEmbeddable()")
  public Object replaceSingleResourceWithTranslation(ProceedingJoinPoint pjp) throws Throwable {
    Object entity = pjp.getArgs()[0];
    if (entity instanceof BlogEntity && !((BlogEntity) entity).getApproved() && !isSuperUser()) {
      throw new AuthorizationException("blog with id: "+ ((BlogEntity) entity).getId() + " is not approved");
    }
    return pjp.proceed();
  }
  
  private boolean isSuperUser() {
    var userPrincipal = request.getUserPrincipal();
    if (userPrincipal != null && userPrincipal instanceof JwtUserDetails) {
      return ((JwtUserDetails) userPrincipal).isSuperUser(); 
    }
    return false;
  }
}
