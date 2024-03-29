package de.codeschluss.wooportal.server.core.analytics.visit;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import de.codeschluss.wooportal.server.core.analytics.visit.annotations.Visitable;
import de.codeschluss.wooportal.server.core.analytics.visit.visitable.VisitableEntity;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.entity.BaseEntity;

public class VisitHelper {

  @SuppressWarnings("unchecked")
  public static <E extends BaseEntity> boolean isVisitable(Class<?> controllerClass) {
    if (controllerClass != null && CrudController.class.isAssignableFrom(controllerClass)) {
      var entityClass = (Class<E>) getEntityClass(controllerClass);
      return entityClass != null && entityClass.getDeclaredAnnotation(Visitable.class) != null;
    }
    return false;
  }
  
  @SuppressWarnings("unchecked")
  public static <E extends BaseEntity> String getVisitableOverview(Class<?> controllerClass) {
    if (controllerClass != null && CrudController.class.isAssignableFrom(controllerClass)) {
      var entityClass = (Class<E>) getEntityClass(controllerClass);
      if (entityClass != null ) {
        var annotation = entityClass.getDeclaredAnnotation(Visitable.class);
        if (annotation != null && !annotation.overview().isBlank()) {
          return annotation.overview(); 
        }
      }
    }
    return null;
  }
  
  public static Type getEntityClass(Class<?> controllerClass) {
    if (controllerClass != null) {
      var genericClass = controllerClass.getGenericSuperclass();
      if (genericClass != null && genericClass instanceof ParameterizedType) {
        return ((ParameterizedType) genericClass).getActualTypeArguments()[0];
      }
    }
    return null;
  }
  
  public static Class<VisitableEntity<?>> getVisitableType(Object entity) {
    for (Field field : entity.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      Class<VisitableEntity<?>> visitorType = getVisitorType(field.getGenericType());
      if (visitorType != null) {
        return visitorType;
      }
    }
    throw new RuntimeException(
        "Missing VisitorEntity for given entity: " + entity.getClass());
  }

  @SuppressWarnings("unchecked")
  private static Class<VisitableEntity<?>> getVisitorType(Type fieldType) {
    if (fieldType instanceof ParameterizedType) {
      ParameterizedType pt = (ParameterizedType) fieldType;
      Class<?> genericType = (Class<?>) pt.getActualTypeArguments()[0];
      if (VisitableEntity.class.isAssignableFrom(genericType)) {
        return (Class<VisitableEntity<?>>) genericType;
      }
    }
    return null;
  }  
  
}
