package com.audiobea.crm.app.controller.customer;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(Constants.URL_BASE + "/admin/customers")
public class CustomerController {

	private final ICustomerService customerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseData<DtoInCustomer>> getCustomers(
			@RequestParam(name = "name", defaultValue = "", required = false) String name,
			@RequestParam(name = "lastName", defaultValue = "", required = false) String lastName,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		log.debug("getCustomers");
		return new ResponseEntity<>(customerService.getCustomers(name, lastName, page, pageSize),
									HttpStatus.OK);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<DtoInCustomer> addCustomer(@RequestBody DtoInCustomer customer) {
		log.debug("addCustomer");
		return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<DtoInCustomer> updateCustomer(@PathVariable(name = "id") String id,
														@RequestBody DtoInCustomer customer) {
		log.debug("updateCustomer");
		return new ResponseEntity<>(customerService.updateCustomer(id, customer), HttpStatus.CREATED);
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
	public ResponseEntity<DtoInCustomer> getCustomerById(
			@PathVariable(name = "id") String id, Principal principal, Authentication authentication) {
		log.debug("getCustomerById");
		return new ResponseEntity<>(customerService.getCustomerById(id, authentication), HttpStatus.CREATED);
	}
}
