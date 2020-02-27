
package de.codeschluss.wooportal.server.core.location.model.route;

import de.codeschluss.wooportal.server.core.location.model.BingMapResult;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class BingMapLocationResult extends BingMapResult {
  
  private List<RouteResourceSet> resourceSets;
}
