package de.codeschluss.wooportal.server.core.utils;

import java.util.List;
import org.springframework.data.domain.Page;

public class PageUtils {
  
  public static List<?> convertToList(Object result) {
    return result instanceof Page<?> ? ((Page<?>) result).getContent() : (List<?>) result;
  }

}
