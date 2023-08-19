package com.audiobea.crm.app.business.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.customer.model.Email;

public interface IEmailDao extends PagingAndSortingRepository<Email, Long> {
}
