package de.codeschluss.wooportal.server.core.location.model.address;

import java.util.List;
import lombok.Data;

/**
 * The Class ResourceSet.
 * 
 * @author Valmir Etemi
 *
 */
@Data
public class AddressResourceSet {

  private Integer estimatedTotal;
  private List<AddressResource> resources;
  
}
