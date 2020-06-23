package de.codeschluss.wooportal.server.components.address;

import de.codeschluss.wooportal.server.components.suburb.SuburbEntity;
import de.codeschluss.wooportal.server.core.api.PagingAndSortingAssembler;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.location.BingMapService;
import de.codeschluss.wooportal.server.core.mail.MailService;
import de.codeschluss.wooportal.server.core.service.ResourceDataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.ServiceUnavailableException;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

// TODO: Auto-generated Javadoc
/**
 * The Class AddressService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class AddressService extends ResourceDataService<AddressEntity, AddressQueryBuilder> {
  
  /** The map service. */
  private final BingMapService mapService;
  
  /** The mail service. */
  private final MailService mailService;

  /**
   * Instantiates a new address service.
   *
   * @param repo the repo
   * @param entities the entities
   * @param assembler the assembler
   * @param bingMapService the bing map service
   * @param mailService the mail service
   */
  public AddressService(
      AddressRepository repo, 
      AddressQueryBuilder entities,
      PagingAndSortingAssembler assembler, 
      BingMapService bingMapService,
      MailService mailService) {
    super(repo, entities, assembler);
    this.mapService = bingMapService;
    this.mailService = mailService;
  }

  @Override
  public AddressEntity getExisting(AddressEntity address) {
    List<AddressEntity> addresses = repo.findAll(entities.withAddress(address));
    if (addresses != null && !addresses.isEmpty()) {
      return addresses.get(0);
    }
    return null;
  }
  
  @Override
  public boolean validCreateFieldConstraints(AddressEntity newAddress) {
    return validBaseFields(newAddress)
        && newAddress.getSuburbId() != null && !newAddress.getSuburbId().isEmpty();
  }
  
  @Override
  public boolean validUpdateFieldConstraints(AddressEntity newAddress) {
    return validBaseFields(newAddress);
  }

  /**
   * Valid base fields.
   *
   * @param newAddress the new address
   * @return true, if successful
   */
  private boolean validBaseFields(AddressEntity newAddress) {
    return newAddress.getPlace() != null && !newAddress.getPlace().isEmpty()
        && newAddress.getPostalCode() != null && !newAddress.getPostalCode().isEmpty()
        && newAddress.getStreet() != null && !newAddress.getStreet().isEmpty();
  }

  /**
   * Gets the resources with suburbs by organisation.
   *
   * @param orgaId
   *          the orga id
   * @return the resources with suburbs by organisation
   */
  public Resource<?> getResourcesByOrganisation(String orgaId) {
    AddressEntity address = repo.findOne(entities.withAnyOrganisationId(orgaId))
        .orElseThrow(() -> new NotFoundException(orgaId));
    return assembler.toResource(address);
  }

  /**
   * Gets the resources with suburbs by activity.
   *
   * @param activityId
   *          the activity id
   * @return the resources with suburbs by activity
   */
  public Resource<?> getResourcesByActivity(String activityId) {
    AddressEntity address = repo.findOne(entities.withAnyActivityId(activityId))
        .orElseThrow(() -> new NotFoundException(activityId));
    return assembler.toResource(address);
  }

  @Override
  public AddressEntity update(String id, AddressEntity newAddress) {
    return repo.findById(id).map(address -> {
      address.setHouseNumber(newAddress.getHouseNumber());
      address.setLatitude(newAddress.getLatitude());
      address.setLongitude(newAddress.getLongitude());
      address.setPlace(newAddress.getPlace());
      address.setPostalCode(newAddress.getPostalCode());
      address.setStreet(newAddress.getStreet());
      return repo.save(address);
    }).orElseGet(() -> {
      newAddress.setId(id);
      return repo.save(newAddress);
    });
  }

  /**
   * < Update suburb.
   *
   * @param addressId
   *          the address id
   * @param suburb
   *          the suburb
   * @return the address entity
   */
  public AddressEntity updateSuburb(String addressId, SuburbEntity suburb) {
    AddressEntity address = repo.findById(addressId)
        .orElseThrow(() -> new NotFoundException(addressId));
    address.setSuburb(suburb);
    return repo.save(address);
  }
  
  @Override
  public AddressEntity add(AddressEntity newAddress) throws ServiceUnavailableException {
    AddressEntity existing = getExisting(newAddress);
    if (existing != null) {
      return existing;
    }
    
    try {
      newAddress = lookup(newAddress);
      existing = getExisting(newAddress);
      if (existing != null) {
        return existing;
      }
    } catch (NotFoundException ex) {
      sendNotFoundAddressMail(newAddress);
    }
    return repo.save(newAddress);
  }
  
  public AddressEntity lookup(AddressEntity newAddress) 
      throws ServiceUnavailableException, NotFoundException {
    return mapService.retrieveExternalAddress(newAddress);
  }

  private void sendNotFoundAddressMail(AddressEntity newAddress) {
    Map<String, Object> model = new HashMap<>();
    model.put("street", newAddress.getStreet());
    model.put("houseNumber", newAddress.getHouseNumber());
    model.put("postalCode", newAddress.getPostalCode());
    model.put("place", newAddress.getPlace());
    String subject = "Adresse nicht gefunden";

    mailService.sendEmail(
        subject, 
        "addressnotfound.ftl", 
        model);
  }
}
