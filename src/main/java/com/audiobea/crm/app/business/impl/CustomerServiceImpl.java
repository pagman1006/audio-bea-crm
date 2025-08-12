package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInAddress;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.commons.dto.DtoInEmail;
import com.audiobea.crm.app.commons.dto.DtoInInvoice;
import com.audiobea.crm.app.commons.dto.DtoInPhone;
import com.audiobea.crm.app.commons.mapper.AddressMapper;
import com.audiobea.crm.app.commons.mapper.CustomerMapper;
import com.audiobea.crm.app.commons.mapper.EmailMapper;
import com.audiobea.crm.app.commons.mapper.InvoiceMapper;
import com.audiobea.crm.app.commons.mapper.PhoneMapper;
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

import static com.audiobea.crm.app.utils.ConstantsLog.LOG_CUSTOMER;

@Slf4j
@AllArgsConstructor
@Service("customerService")
@Transactional(readOnly = true)
public class CustomerServiceImpl implements ICustomerService {

    private MessageSource messageSource;

    private ICustomerDao customerDao;
    private IAddressDao addressDao;
    private IPhoneDao phoneDao;
    private IEmailDao emailDao;
    private IInvoiceDao invoiceDao;
    private CustomerMapper customerMapper;
    private AddressMapper addressMapper;
    private PhoneMapper phoneMapper;
    private EmailMapper emailMapper;
    private InvoiceMapper invoiceMapper;

    @Override
    public ResponseData<DtoInCustomer> getCustomers(final String firstName, final String firstLastName,
            final Integer page, final Integer pageSize) {
        final Pageable pageable = PageRequest.of(page, pageSize,
                Sort.by(Constants.FIRST_LAST_NAME).and(Sort.by(Constants.FIRST_NAME)));
        final Page<Customer> pageCustomer;
        if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(firstLastName)) {
            pageCustomer = customerDao.findByNameContainsAndLastNameContains(firstName, firstLastName, pageable);
        } else {
            pageCustomer = customerDao.findAll(pageable);
        }
        Validator.validatePage(pageCustomer, messageSource);
        final List<DtoInCustomer> listCustomer = pageCustomer.getContent().stream()
                .map(customerMapper::customerToDtoInCustomer).collect(Collectors.toList());
        return new ResponseData<>(listCustomer, pageCustomer);
    }

    @Transactional
    @Override
    public DtoInCustomer saveCustomer(final DtoInCustomer customer) {
        log.debug(LOG_CUSTOMER, customer);
        final Customer customerToSave = customerMapper.customerDtoInToCustomer(customer);
        customerToSave.setAddress(setAddressFromCustomer(customer.getAddress()));
        customerToSave.setPhones(setPhonesFromCustomer(customer.getPhones()));
        customerToSave.setEmails(setEmailsFromCustomer(customer.getEmails()));
        customerToSave.setInvoices(setInvoicesFromCustomer(customer.getInvoices()));
        return customerMapper.customerToDtoInCustomer(customerDao.save(customerToSave));
    }

    private List<Address> setAddressFromCustomer(final List<DtoInAddress> address) {
        if (address == null || address.isEmpty()) return Collections.emptyList();
        return addressDao.saveAll(address.stream().map(addressMapper::addressDtoInToAddress).toList());
    }

    private List<Phone> setPhonesFromCustomer(final List<DtoInPhone> phones) {
        if (phones == null || phones.isEmpty()) return Collections.emptyList();
        return phoneDao.saveAll(phones.stream().map(phoneMapper::phoneDtoToPhone).toList());
    }

    private List<Email> setEmailsFromCustomer(final List<DtoInEmail> emails) {
        if (emails == null || emails.isEmpty()) return Collections.emptyList();
        return emailDao.saveAll(emails.stream().map(emailMapper::emailDtoInToEmail).toList());
    }

    private List<Invoice> setInvoicesFromCustomer(final List<DtoInInvoice> invoices) {
        if (invoices == null || invoices.isEmpty()) return Collections.emptyList();
        return invoiceDao.saveAll(invoices.stream().map(invoiceMapper::invoiceDtoInToInvoice).toList());
    }

    @Transactional
    @Override
    public DtoInCustomer updateCustomer(final String customerId, final DtoInCustomer customer) {
        final Customer customerUpdate = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), customerId)));
        customerUpdate.setName(customer.getName());
        customerUpdate.setLastName(customer.getLastName());
        customerUpdate.setBirthday(customer.getBirthday());
        customerUpdate.setEnabled(customer.isEnabled());
        return customerMapper.customerToDtoInCustomer(customerDao.save(customerUpdate));
    }

    @Transactional
    @Override
    public void deleteCustomer(final String customerId) {
        customerDao.deleteById(customerId);
    }

    @Override
    public DtoInCustomer getCustomerById(final String customerId, final Authentication auth) {

        final Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), customerId)));
        log.debug(auth.getName());
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            Validator.validateUserInformationOwner(customer.getUser(), auth.getName(), messageSource);
        }
        return customerMapper.customerToDtoInCustomer(customer);
    }

}
