package de.codeschluss.wooportal.server.components.address;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.naming.ServiceUnavailableException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import de.codeschluss.wooportal.server.components.activity.ActivityService;
import de.codeschluss.wooportal.server.components.organisation.OrganisationService;
import de.codeschluss.wooportal.server.components.suburb.SuburbService;
import de.codeschluss.wooportal.server.core.api.CrudController;
import de.codeschluss.wooportal.server.core.api.dto.FilterSortPaginate;
import de.codeschluss.wooportal.server.core.api.dto.StringPrimitive;
import de.codeschluss.wooportal.server.core.exception.BadParamsException;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.security.permissions.Authenticated;
import de.codeschluss.wooportal.server.core.security.permissions.SuperUserPermission;

// TODO: Auto-generated Javadoc
/**
 * The Class AddressController.
 * 
 * @author Valmir Etemi
 *
 */
@RestController
public class AddressController extends CrudController<AddressEntity, AddressService> {

  private final ActivityService activityService;
  
  private final OrganisationService organisationService;
  
  /** The suburb service. */
  private final SuburbService suburbService;

  /**
   * Instantiates a new address controller.
   *
   * @param service
   *          the service
   * @param suburbService
   *          the suburb service
   */
  public AddressController(
      AddressService service, 
      ActivityService activityService,
      OrganisationService organisationService,
      SuburbService suburbService) {
    super(service);
    this.activityService = activityService;
    this.organisationService = organisationService;
    this.suburbService = suburbService;
  }

  @Override
  @GetMapping("/addresses")
  public ResponseEntity<?> readAll(FilterSortPaginate params) {
    return super.readAll(params);
  }

  @Override
  @GetMapping("/addresses/{addressId}")
  public Resource<AddressEntity> readOne(@PathVariable String addressId) {
    return super.readOne(addressId);
  }

  @Override
  @PostMapping("/addresses")
  @Authenticated
  public ResponseEntity<?> create(@RequestBody AddressEntity newAddress) throws Exception {
    if (!service.validCreateFieldConstraints(newAddress)) {
      throw new BadParamsException("Required Fields not satisfied");
    }

    try {
      newAddress.setSuburb(suburbService.getById(newAddress.getSuburbId()));
    } catch (NotFoundException e) {
      throw new BadParamsException("Need existing Suburb");
    }

    Resource<AddressEntity> resource = service.addResource(newAddress);
    return created(new URI(resource.getId().expand().getHref())).body(resource);
  }

  @Override
  @PutMapping("/addresses/{addressId}")
  @SuperUserPermission
  public ResponseEntity<?> update(@RequestBody AddressEntity newAddress,
      @PathVariable String addressId) throws URISyntaxException {
    return super.update(newAddress, addressId);
  }

  @Override
  @DeleteMapping("/addresses/{addressId}")
  @SuperUserPermission
  public ResponseEntity<?> delete(@PathVariable String addressId) {
    return super.delete(addressId);
  }

  /**
   * Read suburb.
   *
   * @param addressId
   *          the address id
   * @return the response entity
   */
  @GetMapping("/addresses/{addressId}/suburb")
  public ResponseEntity<Resource<?>> readSuburb(@PathVariable String addressId) {
    return ok(suburbService.getResourceByAddress(addressId));
  }

  /**
   * Update suburb.
   *
   * @param addressId
   *          the address id
   * @param suburbId
   *          the suburb id
   * @return the response entity
   */
  @PutMapping("/addresses/{addressId}/suburb")
  @SuperUserPermission
  public ResponseEntity<Resource<?>> updateSuburb(@PathVariable String addressId,
      @RequestBody StringPrimitive suburbId) {
    if (service.existsById(addressId) && suburbService.existsById(suburbId.getValue())) {
      service.updateSuburb(addressId, suburbService.getById(suburbId.getValue()));
      return ok(suburbService.getResourceByAddress(addressId));
    } else {
      throw new BadParamsException("Address or Suburb with given ID do not exist!");
    }
  }
  
  @GetMapping("/addresses/{addressId}/activities")
  public ResponseEntity<Resources<?>> readActivities(@PathVariable String addressId) {
    try {
      return ok(activityService.getResourcesByAddress(addressId));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @GetMapping("/addresses/{addressId}/organisations")
  public ResponseEntity<Resources<?>> readOrganisations(@PathVariable String addressId) {
    try {
      return ok(organisationService.getResourcesByAddress(addressId));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @PostMapping("/addresses/lookup")
  @SuperUserPermission
  public ResponseEntity<AddressEntity> lookup(@RequestBody AddressEntity address) 
      throws ServiceUnavailableException, NotFoundException {
    return ok(service.lookup(address));
  }
  
}
