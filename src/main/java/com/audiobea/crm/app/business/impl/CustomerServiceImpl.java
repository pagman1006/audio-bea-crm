package com.audiobea.crm.app.business.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.dao.customer.ICustomerDao;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.utils.Utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service("customerService")
@Transactional(readOnly = true)
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerDao customerDao;
	
	private final MessageSource messageSource;

	@Override
	public Page<Customer> getCustomers(String firstName, String secondName, String firstLastName, String secondLastName,
			Integer page, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("firstLastName").and(Sort.by("firstName")));
		if (StringUtils.isBlank(firstName) && StringUtils.isBlank(secondName) && StringUtils.isBlank(firstLastName) && StringUtils.isBlank(secondLastName)) {
			return customerDao.findAll(pageable);
		}
		return customerDao
				.findByFirstNameContainsAndSecondNameContainsAndFirstLastNameContainsAndSecondLastNameContains(
						firstName, secondName, firstLastName, secondLastName, pageable);
	}

	@Transactional(readOnly = false)
	@Override
	public Customer saveCustomer(Customer customer) {
		return customerDao.save(customer);
	}

	@Transactional(readOnly = false)
	@Override
	public Customer updateCustomer(Long customerId, Customer customer) {
		Customer customerUpdate = customerDao.findById(customerId).orElseThrow(() ->
		new NoSuchElementFoundException(Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(customerId))));
		customerUpdate.setFirstName(customer.getFirstName());
		customerUpdate.setSecondName(customer.getSecondName());
		customerUpdate.setFirstLastName(customer.getFirstLastName());
		customerUpdate.setSecondLastName(customer.getSecondLastName());
		customerUpdate.setBirthday(customer.getBirthday());
		customerUpdate.setEnabled(customer.isEnabled());
		return customerDao.save(customerUpdate);
	}

	@Override
	public boolean deleteCustomer(Long customerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Customer getCustomerById(Long customerId) {
		return customerDao.findById(customerId).orElse(null);
	}

}
