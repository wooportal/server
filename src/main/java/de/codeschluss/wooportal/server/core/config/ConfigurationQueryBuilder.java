package de.codeschluss.wooportal.server.core.config;

import com.querydsl.core.types.dsl.BooleanExpression;

import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.config.QConfigurationEntity;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

import org.springframework.stereotype.Service;

/**
 * The Class ConfigurationQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class ConfigurationQueryBuilder extends QueryBuilder<QConfigurationEntity> {
  
  public ConfigurationQueryBuilder() {
    super(QConfigurationEntity.configurationEntity);
  }

  public BooleanExpression withItem(String item) {
    return query.item.eq(item);
  }

  @Override
  public BooleanExpression search(FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.item.likeIgnoreCase(filter);
  }
  
}
