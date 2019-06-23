package de.codeschluss.wooportal.server.components.user;

import com.querydsl.core.types.dsl.BooleanExpression;

import de.codeschluss.wooportal.server.components.provider.ProviderEntity;
import de.codeschluss.wooportal.server.components.user.QUserEntity;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.service.QueryBuilder;

import java.util.List;

import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class UserQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class UserQueryBuilder extends QueryBuilder<QUserEntity> {
  
  /**
   * Instantiates a new user query builder.
   */
  public UserQueryBuilder() {
    super(QUserEntity.userEntity, "username");
  }

  /**
   * With username.
   *
   * @param username the username
   * @return the boolean expression
   */
  public BooleanExpression withUsername(String username) {
    return query.username.eq(username);
  }

  /**
   * As superuser.
   *
   * @return the boolean expression
   */
  public BooleanExpression asSuperuser() {
    return query.superuser.eq(true);
  }
  
  /**
   * With any of providers.
   *
   * @param providers the providers
   * @return the boolean expression
   */
  public BooleanExpression withAnyOfProviders(List<ProviderEntity> providers) {
    return query.providers.any().in(providers);
  }

  /**
   * Fuzzy search.
   *
   * @param params the params
   * @return the boolean expression
   */
  @Override
  public BooleanExpression search(FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.name.likeIgnoreCase(filter)
        .or(query.username.likeIgnoreCase(filter))
        .or(query.phone.likeIgnoreCase(filter));
  }
}
