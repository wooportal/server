package de.codeschluss.portal.components.address;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import de.codeschluss.portal.core.common.QueryBuilder;
import de.codeschluss.portal.core.utils.FilterSortPaginate;

import java.util.List;

import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class AddressQueryBuilder.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class AddressQueryBuilder extends QueryBuilder<QAddressEntity> {
  
  /**
   * Instantiates a new address query builder.
   */
  public AddressQueryBuilder() {
    super(QAddressEntity.addressEntity);
  }

  /**
   * With address.
   *
   * @param address the address
   * @return the boolean expression
   */
  public BooleanExpression withAddress(AddressEntity address) {
    BooleanExpression isHousnumber = query.houseNumber.eq(address.getHouseNumber());
    BooleanExpression isPlace = query.place.eq(address.getPlace());
    BooleanExpression isPostalCode = query.postalCode.eq(address.getPostalCode());
    BooleanExpression isStreet = query.street.eq(address.getStreet());
    
    return isHousnumber
        .and(isPlace)
        .and(isPostalCode)
        .and(isStreet);
  }

  /**
   * With any organisation id.
   *
   * @param orgaId the orga id
   * @return the predicate
   */
  public BooleanExpression withAnyOrganisationId(String orgaId) {
    return query.organisations.any().id.eq(orgaId);
  }

  /**
   * With any activity id.
   *
   * @param activityId the activity id
   * @return the predicate
   */
  public BooleanExpression withAnyActivityId(String activityId) {
    return query.activities.any().id.eq(activityId);
  }

  /**
   * Fuzzy search.
   *
   * @param params the params
   * @return the predicate
   */
  @Override
  public BooleanExpression search(FilterSortPaginate params) {
    String filter = prepareFilter(params.getFilter());
    return query.houseNumber.likeIgnoreCase(filter)
        .or(query.place.likeIgnoreCase(filter))
        .or(query.postalCode.likeIgnoreCase(filter))
        .or(query.street.likeIgnoreCase(filter));
  }

  /* (non-Javadoc)
   * @see de.codeschluss.portal.core.common.QueryBuilder#withIdsIn(java.util.List)
   */
  @Override
  public Predicate withIdsIn(List<String> ids) {
    // TODO Auto-generated method stub
    return null;
  }

}
