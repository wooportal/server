package de.codeschluss.wupportal.provider;

import java.net.URISyntaxException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.codeschluss.wupportal.activity.ActivityService;
import de.codeschluss.wupportal.base.CrudController;
import de.codeschluss.wupportal.exception.MethodNotAllowedException;
import de.codeschluss.wupportal.security.permissions.SuperUserPermission;
import de.codeschluss.wupportal.utils.FilterSortPaginate;
import io.swagger.models.HttpMethod;

public class ProviderController extends CrudController<ProviderEntity, ProviderResourceAssembler, ProviderService>{

	private final ActivityService activityService;
	
	public ProviderController(
			ProviderService service, 
			ProviderResourceAssembler assembler,
			ActivityService activityService
			) {
		super(service, assembler);
		this.activityService = activityService;
	}
	
	@GetMapping("/providers")
	@SuperUserPermission
	public ResponseEntity<?> findAll(FilterSortPaginate params) {
		//TODO: Is this function really necessary? Method not allowed instead?
		return super.findAll(params);
	}

	@GetMapping("/providers/{id}")
	@SuperUserPermission
	public Resource<ProviderEntity> findOne(@PathVariable String id) {
		//TODO: Is this function really necessary? Method not allowed instead?
		return super.findOne(id);
	}
	
	@GetMapping("/providers/{id}/activities")
	public ResponseEntity<?> findActivitiesByProvider(@PathVariable String id, FilterSortPaginate params) {
		validateRequest(params);
		
		Sort sort = params.createSort("id");
		if (params.getPage() == null && params.getSize() == null) {
			return ResponseEntity.ok(
					assembler.toListSubResource(
							activityService.getActivitiesByProviderId(sort, id),
							DummyInvocationUtils.methodOn(this.getClass()).findActivitiesByProvider(id, params)));
		}
		
		PageRequest pageRequest = PageRequest.of(params.getPage(), params.getSize(), sort);
		return ResponseEntity.ok(
				assembler.toPagedSubResource(params,
						activityService.getPagedActivitiesByProviderId(pageRequest, id)));
	}
	
	
	// ########## Not allowed methods ##########
	
	@PostMapping("/providers")
	public ResponseEntity<?> add(@RequestBody ProviderEntity newProvider) throws URISyntaxException {
		throw new MethodNotAllowedException("Method " + HttpMethod.POST.toString() + " is not allowed on Providers. Use Users resources instead");
	}
	
	@PutMapping("/providers/{id}")
	public ResponseEntity<?> update(@RequestBody ProviderEntity newProvider, @PathVariable String id) throws URISyntaxException {
		throw new MethodNotAllowedException("Method " + HttpMethod.PUT.toString() + " is not allowed on Providers");
	}
	
	@DeleteMapping("/providers/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		throw new MethodNotAllowedException("Method " + HttpMethod.DELETE.toString() + " is not allowed on Providers. Use Users or Organisations resources instead");
	}

}