package de.codeschluss.wooportal.server.core.location.model.address;

import de.codeschluss.wooportal.server.core.location.model.BingMapResult;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The Class BingMapAddressResult.
 * 
 * @author Valmir Etemi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class BingMapAddressResult extends BingMapResult {

  private List<AddressResourceSet> resourceSets;
}
