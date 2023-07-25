package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.Phone;

public interface IPhoneDao extends PagingAndSortingRepository<Phone, Long> {
}
