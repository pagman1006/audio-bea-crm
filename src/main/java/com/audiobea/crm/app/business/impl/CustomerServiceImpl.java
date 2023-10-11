package com.audiobea.crm.app.business.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.business.dao.customer.ICustomerDao;
import com.audiobea.crm.app.business.dao.customer.model.Customer;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service("customerService")
@Transactional(readOnly = true)
public class CustomerServiceImpl implements ICustomerService {

	private final MessageSource messageSource;
	@Autowired
	private ICustomerDao customerDao;

	@Override
	public Page<Customer> getCustomers(String firstName, String firstLastName, Integer page, Integer pageSize) {

		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Constants.FIRST_LAST_NAME).and(Sort.by(Constants.FIRST_NAME)));

		if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(firstLastName)) {
			return customerDao.findByFirstNameContainsAndFirstLastNameContains(firstName, firstLastName, pageable);
		}
		if (StringUtils.isNotBlank(firstName)) {
			return customerDao.findByFirstNameContains(firstName, pageable);
		}
		if (StringUtils.isNotBlank(firstLastName)) {
			return customerDao.findByFirstLastNameContains(firstLastName, pageable);
		}
		return customerDao.findAll(pageable);
	}

	@Transactional
	@Override
	public Customer saveCustomer(Customer customer) {
		return customerDao.save(customer);
	}

	@Transactional
	@Override
	public Customer updateCustomer(Long customerId, Customer customer) {
		Customer customerUpdate = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(customerId))));
		customerUpdate.setFirstName(customer.getFirstName());
		customerUpdate.setSecondName(customer.getSecondName());
		customerUpdate.setFirstLastName(customer.getFirstLastName());
		customerUpdate.setSecondLastName(customer.getSecondLastName());
		customerUpdate.setBirthday(customer.getBirthday());
		customerUpdate.setEnabled(customer.isEnabled());
		return customerDao.save(customerUpdate);
	}

	@Transactional
	@Override
	public boolean deleteCustomer(Long customerId) {
		customerDao.deleteById(customerId);
		return true;
	}

	@Override
	public Customer getCustomerById(Long customerId, Authentication auth) {
		Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(customerId))));
		log.debug(auth.getName());
		if (auth.getAuthorities().stream().filter(a -> a.getAuthority().equals("ADMIN")).count() < 1) {
			Validator.validateUserInformationOwner(customer.getUser(), auth.getName(), messageSource);
		}
		return customer;
	}

}
