package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.EmailType;

public interface IEmailTypeDao extends PagingAndSortingRepository<EmailType, Long> {
}
