package com.audiobea.crm.app.business.impl;

import com.audiobea.crm.app.business.ICustomerService;
import com.audiobea.crm.app.commons.I18Constants;
import com.audiobea.crm.app.commons.ResponseData;
import com.audiobea.crm.app.commons.dto.DtoInCustomer;
import com.audiobea.crm.app.commons.mapper.CustomerMapper;
import com.audiobea.crm.app.core.exception.NoSuchElementFoundException;
import com.audiobea.crm.app.dao.customer.ICustomerDao;
import com.audiobea.crm.app.dao.customer.model.Customer;
import com.audiobea.crm.app.utils.Constants;
import com.audiobea.crm.app.utils.Utils;
import com.audiobea.crm.app.utils.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private CustomerMapper customerMapper;

    @Override
    public ResponseData<DtoInCustomer> getCustomers(String firstName, String firstLastName, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Constants.FIRST_LAST_NAME).and(Sort.by(Constants.FIRST_NAME)));
        Page<Customer> pageCustomer;

        if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(firstLastName)) {
            pageCustomer = customerDao.findByFirstNameContainsAndFirstLastNameContains(firstName, firstLastName, pageable);
        } else if (StringUtils.isNotBlank(firstName)) {
            pageCustomer = customerDao.findByFirstNameContains(firstName, pageable);
        }else if (StringUtils.isNotBlank(firstLastName)) {
            pageCustomer = customerDao.findByFirstLastNameContains(firstLastName, pageable);
        } else {
            pageCustomer = customerDao.findAll(pageable);
        }
        Validator.validatePage(pageCustomer, messageSource);
        List<DtoInCustomer> listCustomer = pageCustomer.getContent().stream().map(customer -> customerMapper.customerToDtoInCustomer(customer)).collect(Collectors.toList());
        return new ResponseData<>(listCustomer, pageCustomer);
    }

    @Transactional
    @Override
    public DtoInCustomer saveCustomer(DtoInCustomer customer) {
        return customerMapper.customerToDtoInCustomer(customerDao.save(customerMapper.customerDtoInToCustomer(customer)));
    }

    @Transactional
    @Override
    public DtoInCustomer updateCustomer(Long customerId, DtoInCustomer customer) {
        Customer customerUpdate = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(customerId))));
        customerUpdate.setFirstName(customer.getFirstName());
        customerUpdate.setSecondName(customer.getSecondName());
        customerUpdate.setFirstLastName(customer.getFirstLastName());
        customerUpdate.setSecondLastName(customer.getSecondLastName());
        customerUpdate.setBirthday(customer.getBirthday());
        customerUpdate.setEnabled(customer.isEnabled());
        return customerMapper.customerToDtoInCustomer(customerDao.save(customerUpdate));
    }

    @Transactional
    @Override
    public boolean deleteCustomer(Long customerId) {
        customerDao.deleteById(customerId);
        return true;
    }

    @Override
    public DtoInCustomer getCustomerById(Long customerId, Authentication auth) {
        Customer customer = customerDao.findById(customerId).orElseThrow(() -> new NoSuchElementFoundException(
                Utils.getLocalMessage(messageSource, I18Constants.NO_ITEM_FOUND.getKey(), String.valueOf(customerId))));
        log.debug(auth.getName());
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
            Validator.validateUserInformationOwner(customer.getUser(), auth.getName(), messageSource);
        }
        return customerMapper.customerToDtoInCustomer(customer);
    }

}
