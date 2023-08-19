package com.audiobea.crm.app.business.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.customer.model.EmailType;

public interface IEmailTypeDao extends PagingAndSortingRepository<EmailType, Long> {
}
