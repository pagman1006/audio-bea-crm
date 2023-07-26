package com.audiobea.crm.app.controller.customer;

import java.util.List;

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
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.controller.mapper.CustomerMapper;
import com.audiobea.crm.app.controller.mapper.ListCustomerMapper;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.exception.NoSuchElementsFoundException;
import com.audiobea.crm.app.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final MessageSource messageSource;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ListCustomerMapper listCustomerMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<ResponseData<DtoInCustomer>> getCustomers(
            @RequestParam(name = "firstName", defaultValue = "", required = false) String firstName,
            @RequestParam(name = "secondName", defaultValue = "", required = false) String secondName,
            @RequestParam(name = "firstLastName", defaultValue = "", required = false) String firstLastName,
            @RequestParam(name = "secondLastName", defaultValue = "", required = false) String secondLastName,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        Page<Customer> pageable = customerService.getCustomers(firstName, secondName, firstLastName, secondLastName,
                page, pageSize);
        if (pageable == null || pageable.getContent().isEmpty()) {
            throw new NoSuchElementsFoundException(
                    Utils.getLocalMessage(messageSource, I18Constants.NO_ITEMS_FOUND.getKey()));
        }
        List<DtoInCustomer> listCustomers = listCustomerMapper.listCustomersToListDtoInCustomers(pageable.getContent());
        ResponseData<DtoInCustomer> response = new ResponseData<>(listCustomers, pageable.getNumber(),
                pageable.getSize(), pageable.getTotalElements(), pageable.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DtoInCustomer> saveCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerMapper.customerToDtoInCustomer(customerService.saveCustomer(customer)),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DtoInCustomer> updateCustomer(@PathVariable(name = "id") Long id,
                                                        @RequestBody Customer customer) {
        return new ResponseEntity<>(customerMapper.customerToDtoInCustomer(customerService.updateCustomer(id, customer)), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoInCustomer> getCustomer(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(customerMapper.customerToDtoInCustomer(customerService.getCustomerById(id)), HttpStatus.CREATED);
    }
}
