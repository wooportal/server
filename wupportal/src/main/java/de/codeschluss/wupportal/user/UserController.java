package de.codeschluss.wupportal.user;

import java.net.URISyntaxException;

import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.codeschluss.wupportal.base.CrudController;
import de.codeschluss.wupportal.base.PagingAndSortingAssembler;
import de.codeschluss.wupportal.provider.ProviderEntity;
import de.codeschluss.wupportal.provider.ProviderService;

@RestController
public class UserController extends CrudController<UserEntity, PagingAndSortingAssembler<UserEntity>, UserService>{

	private final ProviderService providerService;

	public UserController(UserService userService,
			ProviderService providerService,
			UserResourceAssembler userAssembler) {
		super(userService, userAssembler);
		this.providerService = providerService;
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> findAll(@RequestParam(required = false) String filter,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction direction,
			@RequestParam(value = "sort", defaultValue = "username", required = false) String... sortProperties) {
		return super.findAll(filter, page, size, direction, sortProperties);
	}

	@GetMapping("/users/{id}")
	public Resource<UserEntity> findOne(@PathVariable String id) {
		return super.findOne(id);
	}
	
	@PostMapping("/users")
	public ResponseEntity<?> add(@RequestBody UserEntity newUser) throws URISyntaxException {
		return super.add(newUser);
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<?> update(@RequestBody UserEntity newUser, @PathVariable String id) throws URISyntaxException {
		return super.update(newUser, id);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		return super.delete(id);
	}
	
	@GetMapping("/users/{id}/providers")
	public Resources<ProviderEntity> findProvidersByUser(@PathVariable String id) {
		return assembler.toSubResource(
				providerService.getProvidersByUser(service.getById(id)), 
				DummyInvocationUtils.methodOn(this.getClass()).findProvidersByUser(id));
	}
}
