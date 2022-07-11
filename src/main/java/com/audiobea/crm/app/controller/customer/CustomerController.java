package com.audiobea.crm.app.controller.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.audiobea.crm.app.dao.model.customer.Customer;

@RestController
@RequestMapping("/cliente")
public class CustomerController {

	@GetMapping
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public List<Customer> getCustomers() {
		List<Customer> listCustomer = new ArrayList<>();
		return listCustomer;
	}
}
