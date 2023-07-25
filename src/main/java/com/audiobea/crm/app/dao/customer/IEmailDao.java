package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.Email;

public interface IEmailDao extends PagingAndSortingRepository<Email, Long> {
}
