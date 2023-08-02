package com.audiobea.crm.app.controller.customer;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.controller.mapper.CustomerMapper;
import com.audiobea.crm.app.controller.mapper.ListCustomerMapper;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/audio-bea/customers")
public class CustomerController {

	private final MessageSource messageSource;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private ListCustomerMapper listCustomerMapper;
	@Autowired
	private CustomerMapper customerMapper;

	@GetMapping
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<ResponseData<DtoInCustomer>> getCustomers(
			@RequestParam(name = "firstName", defaultValue = "", required = false) String firstName,
			@RequestParam(name = "firstLastName", defaultValue = "", required = false) String firstLastName,
			@RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

		Page<Customer> pageable = customerService.getCustomers(firstName, firstLastName, page, pageSize);
		Validator.validatePage(pageable, messageSource);
		List<DtoInCustomer> listCustomers = listCustomerMapper.listCustomersToListDtoInCustomers(pageable.getContent());
		ResponseData<DtoInCustomer> response = new ResponseData<>(listCustomers, pageable.getNumber(),
				pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInCustomer> saveCustomer(@RequestBody DtoInCustomer customer) {
		return new ResponseEntity<>(
				customerMapper.customerToDtoInCustomer(
						customerService.saveCustomer(customerMapper.customerDtoInToCustomer(customer))),
				HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInCustomer> updateCustomer(@PathVariable(name = "id") Long id,
			@RequestBody DtoInCustomer customer) {
		return new ResponseEntity<>(
				customerMapper.customerToDtoInCustomer(
						customerService.updateCustomer(id, customerMapper.customerDtoInToCustomer(customer))),
				HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<DtoInCustomer> getCustomer(@PathVariable(name = "id") Long id) {
		return new ResponseEntity<>(customerMapper.customerToDtoInCustomer(customerService.getCustomerById(id)),
				HttpStatus.CREATED);
	}
}
