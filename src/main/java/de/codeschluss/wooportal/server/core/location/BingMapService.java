package de.codeschluss.wooportal.server.core.location;

import de.codeschluss.wooportal.server.components.address.AddressEntity;
import de.codeschluss.wooportal.server.core.exception.NotFoundException;
import de.codeschluss.wooportal.server.core.i18n.language.LanguageService;
import de.codeschluss.wooportal.server.core.location.dto.Coordinate;
import de.codeschluss.wooportal.server.core.location.dto.LocationParam;
import de.codeschluss.wooportal.server.core.location.dto.TravelMode;
import de.codeschluss.wooportal.server.core.location.model.BingMapResult;
import de.codeschluss.wooportal.server.core.location.model.address.Address;
import de.codeschluss.wooportal.server.core.location.model.address.AddressResource;
import de.codeschluss.wooportal.server.core.location.model.address.AddressResourceSet;
import de.codeschluss.wooportal.server.core.location.model.address.BingMapAddressResult;
import de.codeschluss.wooportal.server.core.location.model.address.Point;
import de.codeschluss.wooportal.server.core.location.model.route.BingMapLocationResult;
import de.codeschluss.wooportal.server.core.location.model.route.RouteResource;
import de.codeschluss.wooportal.server.core.location.model.route.RouteResourceSet;
import java.net.URI;
import java.util.List;
import javax.naming.ServiceUnavailableException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The Class BingMapService.
 * 
 * @author Valmir Etemi
 *
 */
@Service
public class BingMapService {

  /** The config. */
  private final LocationConfiguration config;
  
  /** The language service. */
  private final LanguageService languageService;

  /** The geo location client. */
  private final WebClient geoLocationClient;

  /**
   * Instantiates a new bing map service.
   *
   * @param config the config
   * @param languageService the language service
   */
  public BingMapService(
      LocationConfiguration config,
      LanguageService languageService) {
    this.config = config;
    this.languageService = languageService;
    this.geoLocationClient = WebClient.create();
  }

  /**
   * Retrieve external address.
   *
   * @param newAddress the new address
   * @return the address entity
   * @throws ServiceUnavailableException the service unavailable exception
   */
  public AddressEntity retrieveExternalAddress(AddressEntity newAddress)
      throws ServiceUnavailableException, NotFoundException {
    BingMapAddressResult result = geoLocationClient
        .method(HttpMethod.GET)
        .uri(createAddressUri(newAddress))
        .retrieve().bodyToMono(BingMapAddressResult.class).block();
    isValid(result);

    return transformResultToAddress(result, newAddress);
  }

  /**
   * Creates the address uri.
   *
   * @param newAddress the new address
   * @return the uri
   */
  private URI createAddressUri(AddressEntity newAddress) {
    return UriComponentsBuilder.fromUriString(config.getAddressUrl())
        .path(newAddress.getPostalCode()).path("/" + newAddress.getPlace())
        .path("/" + newAddress.getStreet() + " " + newAddress.getHouseNumber())
        .queryParam("key", config.getServiceSubscriptionKey()).build().encode().toUri();
  }

  /**
   * Transform result to address.
   *
   * @param result the result
   * @param givenAddress the given address
   * @return the address entity
   */
  private AddressEntity transformResultToAddress(
      BingMapAddressResult result, AddressEntity givenAddress) {
    for (AddressResourceSet resourceSet : result.getResourceSets()) {
      if (resourceSet.getEstimatedTotal() > 0) {
        for (AddressResource resource : resourceSet.getResources()) {
          if (resource.getConfidence().equals("High") || resource.getConfidence().equals("high")) {
            return createAddress(givenAddress, resource.getAddress(), resource.getPoint());
          }
        }
      }
    }
    throw new NotFoundException("Address not found");
  }

  /**
   * Creates the address.
   *
   * @param givenAddress the given address
   * @param address the address
   * @param point the point
   * @return the address entity
   */
  private AddressEntity createAddress(AddressEntity givenAddress, Address address, Point point) {
    AddressEntity newAddress = new AddressEntity();

    newAddress.setPostalCode(address.getPostalCode());
    newAddress.setPlace(address.getLocality());
    newAddress.setHouseNumber(address.getHouseNumber());
    newAddress.setStreet(address.getStreet());
    newAddress.setSuburb(givenAddress.getSuburb());
    newAddress.setLatitude(point.getLatitude());
    newAddress.setLongitude(point.getLongitude());

    return newAddress;
  }

  /**
   * Calculate route.
   *
   * @param params the params
   * @return the bing map location result
   * @throws ServiceUnavailableException the service unavailable exception
   */
  public RouteResource calculateRoute(LocationParam params) 
      throws ServiceUnavailableException, NotFoundException {
    try {
      BingMapLocationResult result = geoLocationClient
          .method(HttpMethod.GET)
          .uri(createRouteUri(params))
          .retrieve().bodyToMono(BingMapLocationResult.class).block();
      isValid(result);

      return extractRouteResult(result);
    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND
          && params.getTravelMode() == TravelMode.TRANSIT) {
        params.setTravelMode(TravelMode.DRIVING);
        return calculateRoute(params);
      }
      throw new ServiceUnavailableException(e.getMessage());
    }
  }

  /**
   * Creates the route uri.
   *
   * @param params the params
   * @return the uri
   */
  private URI createRouteUri(LocationParam params) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(config.getRoutesUrl());

    addTravelMode(builder, params.getTravelMode());
    addStartPoint(builder, params.getStartPoint());
    addVia(builder, params.getVia());
    addLocale(builder);
    addTargetPoint(builder, params.getVia(), params.getTargetPoint());
    addKey(builder);
    
    return builder.build().encode().toUri();
    
  }

  /**
   * Adds the travel mode.
   *
   * @param builder the builder
   * @param travelMode the travel mode
   */
  private void addTravelMode(UriComponentsBuilder builder, TravelMode travelMode) {
    if (travelMode != null && !travelMode.name().isEmpty()) {
      builder.path("/" + travelMode.name().toLowerCase());
    }
  }

  /**
   * Adds the start point.
   *
   * @param builder the builder
   * @param startPoint the start point
   */
  private void addStartPoint(UriComponentsBuilder builder, Coordinate startPoint) {
    builder.queryParam("wayPoint.0", startPoint.toString());
  }

  /**
   * Adds the via.
   *
   * @param builder the builder
   * @param via the via
   */
  private void addVia(UriComponentsBuilder builder, List<Coordinate> via) {
    if (via != null && !via.isEmpty()) {
      for (int i = 0; i < via.size(); i++) {
        builder.queryParam("wayPoint." + (i + 1), via.get(i).toString());
      }
    }
  }
  
  /**
   * Adds the locale.
   *
   * @param builder the builder
   */
  private void addLocale(UriComponentsBuilder builder) {
    builder.queryParam("culture", languageService.getCurrentRequestLocales().get(0));
  }
  
  /**
   * Adds the key.
   *
   * @param builder the builder
   */
  private void addKey(UriComponentsBuilder builder) {
    builder.queryParam("key", config.getServiceSubscriptionKey());
  }
  
  /**
   * Extract route result.
   *
   * @param result the result
   * @return the route resource
   */
  private RouteResource extractRouteResult(BingMapLocationResult result) {
    for (RouteResourceSet resourceSet : result.getResourceSets()) {
      if (resourceSet.getEstimatedTotal() > 0) {
        return resourceSet.getResources().get(0);
      }
    }
    throw new NotFoundException("Route not found");
  }
  
  /**
   * Adds the target point.
   *
   * @param builder the builder
   * @param via the via
   * @param targetPoint the target point
   */
  private void addTargetPoint(
      UriComponentsBuilder builder, 
      List<Coordinate> via,
      Coordinate targetPoint) {
    if (via == null || via.isEmpty()) {
      builder.queryParam("wayPoint.1", targetPoint.toString());
    } else {
      builder.queryParam("wayPoint." + via.size(), targetPoint.toString()); 
    }
  }

  /**
   * Checks if is valid.
   *
   * @param result the result
   * @throws ServiceUnavailableException the service unavailable exception
   */
  private void isValid(BingMapResult result) throws ServiceUnavailableException {
    if (result.getStatusCode() != 200
        || !result.getAuthenticationResultCode().equals("ValidCredentials")) {
      throw new ServiceUnavailableException("External API is not available");
    }
  }


}
