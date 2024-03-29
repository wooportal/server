package de.codeschluss.wooportal.server.core.i18n;

import de.codeschluss.wooportal.server.core.entity.BaseEntity;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationHelper;
import de.codeschluss.wooportal.server.core.i18n.translation.TranslationService;
import de.codeschluss.wooportal.server.core.utils.PageUtils;
import java.util.List;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class TranslationInterceptor.
 *
 * @author Valmir Etemi
 */
@Component
@Aspect
public class TranslationInterceptor {

  @Pointcut("execution(* de.codeschluss.wooportal.server.core.repository.DataRepository+.save(..))")
  private void save() {
  }

  @Pointcut(
      "execution(public * de.codeschluss.wooportal.server.core.repository.DataRepository+.findOne(..))")
  private void findOne() {
  }
  
  @Pointcut(
      "execution(* de.codeschluss.wooportal.server.core.repository.DataRepository+.findAll(..))")
  private void findAll() {
  }

  @Pointcut(
      "execution(public * de.codeschluss.wooportal.server.core.api.AssemblerHelper.toResource(..))")
  private void toResource() {
  }
  
  @Pointcut(
      "execution(public * de.codeschluss.wooportal.server.core.api.AssemblerHelper.resourceWithEmbeddable(..))")
  private void resourceWithEmbeddable() {
  }

  /** The translation service. */
  @Autowired
  private TranslationService translationService;

  /**
   * Replace iterable with translations.
   *
   * @param pjp
   *          the pjp
   * @return the object
   * @throws Throwable
   *           the throwable
   */
  @Around("findAll()")
  public Object replaceIterableWithTranslations(ProceedingJoinPoint pjp) throws Throwable {
    Object result = pjp.proceed();
    if (result instanceof Iterable<?>) {
      List<?> list = PageUtils.convertToList(result);
      if (!list.isEmpty() && TranslationHelper.isLocalizable(list.get(0))) {
        translationService.localizeList(list);
      }
    }
    return result;
  }

  /**
   * Replace single with translation.
   *
   * @param pjp
   *          the pjp
   * @return the object
   * @throws Throwable
   *           the throwable
   */
  @Around("findOne()")
  public Object replaceSingleEntityWithTranslation(ProceedingJoinPoint pjp) throws Throwable {
    Object result = pjp.proceed();
    if (result instanceof Optional<?> && ((Optional<?>) result).isPresent()) {
      Object entity = ((Optional<?>) result).get();
      if (TranslationHelper.isLocalizable(entity)) {
        translationService.localizeSingle(entity);
        return Optional.of(entity);
      }
    }
    return result;
  }


  /**
   * Replace single resource with translation.
   *
   * @param pjp the pjp
   * @return the object
   * @throws Throwable the throwable
   */
  @Around("toResource() || resourceWithEmbeddable()")
  public Object replaceSingleResourceWithTranslation(ProceedingJoinPoint pjp) throws Throwable {
    Object entity = pjp.getArgs()[0];
    if (TranslationHelper.isLocalizable(entity)) {
      translationService.localizeSingle(entity);
    }
    return pjp.proceed();
  }

  /**
   * Save localizable.
   *
   * @param <E>
   *          the element type
   * @param pjp
   *          the pjp
   * @return the object
   * @throws Throwable
   *           the throwable
   */
  @Around("save()")
  public <E extends BaseEntity> Object saveTranslation(ProceedingJoinPoint pjp)
      throws Throwable {
    pjp.proceed();
    Object savedEntity = pjp.getArgs()[0];
    if (TranslationHelper.isLocalizable(savedEntity)) {
      translationService.save(savedEntity);
    }
    return savedEntity;
  }
}
