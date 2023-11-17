package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.*;
import com.audiobea.crm.app.commons.mapper.*;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.customer.ICustomerDao;
import com.audiobea.crm.app.dao.customer.IEmailDao;
import com.audiobea.crm.app.dao.customer.IPhoneDao;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.dao.customer.model.Email;
import com.audiobea.crm.app.dao.customer.model.Phone;
import com.audiobea.crm.app.dao.demographic.IAddressDao;
import com.audiobea.crm.app.dao.demographic.model.Address;
import com.audiobea.crm.app.dao.invoice.IInvoiceDao;
import com.audiobea.crm.app.dao.invoice.model.Invoice;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service("customerService")
@Transactional(readOnly = true)
public class CustomerServiceImpl implements ICustomerService {

	private final MessageSource messageSource;

	@Autowired
	private ICustomerDao customerDao;
	@Autowired
	private IAddressDao addressDao;
	@Autowired
	private IPhoneDao phoneDao;
	@Autowired
	private IEmailDao emailDao;
	@Autowired
	private IInvoiceDao invoiceDao;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private PhoneMapper phoneMapper;
	@Autowired
	private EmailMapper emailMapper;
	@Autowired
	private InvoiceMapper invoiceMapper;

	@Override
	public ResponseData<DtoInCustomer> getCustomers(String firstName, String firstLastName, Integer page, Integer pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize,
										   Sort.by(Constants.FIRST_LAST_NAME).and(Sort.by(Constants.FIRST_NAME)));
		Page<Customer> pageCustomer;
		if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(firstLastName)) {
			pageCustomer = customerDao.findByNameContainsAndLastNameContains(firstName, firstLastName, pageable);
		} else {
			pageCustomer = customerDao.findAll(pageable);
		}
		Validator.validatePage(pageCustomer, messageSource);
		List<DtoInCustomer> listCustomer = pageCustomer.getContent().stream()
													   .map(customer -> customerMapper.customerToDtoInCustomer(
															   customer)).collect(Collectors.toList());
		return new ResponseData<>(listCustomer, pageCustomer);
	}

	@Transactional
	@Override
	public DtoInCustomer saveCustomer(DtoInCustomer customer) {
		log.debug("Customer: {}", customer);
		Customer customerToSave = customerMapper.customerDtoInToCustomer(customer);
		customerToSave.setAddress(setAddressFromCustomer(customer.getAddress()));
		customerToSave.setPhones(setPhonesFromCustomer(customer.getPhones()));
		customerToSave.setEmails(setEmailsFromCustomer(customer.getEmails()));
		customerToSave.setInvoices(setInvoicesFromCustomer(customer.getInvoices()));
		return customerMapper.customerToDtoInCustomer(customerDao.save(customerToSave));
	}

	private List<Address> setAddressFromCustomer(List<DtoInAddress> address) {
		if (address == null || address.isEmpty()) return Collections.emptyList();
		return addressDao.saveAll(address.stream().map(a -> addressMapper.addressDtoInToAddress(a)).toList());
	}

	private List<Phone> setPhonesFromCustomer(List<DtoInPhone> phones) {
		if (phones == null || phones.isEmpty()) return Collections.emptyList();
		return phoneDao.saveAll(phones.stream().map(p -> phoneMapper.phoneDtoToPhone(p)).toList());
	}

	private List<Email> setEmailsFromCustomer(List<DtoInEmail> emails) {
		if (emails == null || emails.isEmpty()) return Collections.emptyList();
		return emailDao.saveAll(emails.stream().map(e -> emailMapper.emailDtoInToEmail(e)).toList());
	}

	private List<Invoice> setInvoicesFromCustomer(List<DtoInInvoice> invoices) {
		if (invoices == null || invoices.isEmpty()) return Collections.emptyList();
		return invoiceDao.saveAll(invoices.stream().map(i -> invoiceMapper.invoiceDtoInToInvoice(i)).toList());
	}

	@Transactional
	@Override
	public DtoInCustomer updateCustomer(String customerId, DtoInCustomer customer) {
		Customer customerUpdate = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
				Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), customerId)));
		customerUpdate.setName(customer.getName());
		customerUpdate.setLastName(customer.getLastName());
		customerUpdate.setBirthday(customer.getBirthday());
		customerUpdate.setEnabled(customer.isEnabled());
		return customerMapper.customerToDtoInCustomer(customerDao.save(customerUpdate));
	}

	@Transactional
	@Override
	public void deleteCustomer(String customerId) {
		customerDao.deleteById(customerId);
	}

	@Override
	public DtoInCustomer getCustomerById(String customerId, Authentication auth) {

        Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), customerId)));
        log.debug(auth.getName());
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            Validator.validateUserInformationOwner(customer.getUser(), auth.getName(), messageSource);
        }
        return customerMapper.customerToDtoInCustomer(customer);
	}

}
