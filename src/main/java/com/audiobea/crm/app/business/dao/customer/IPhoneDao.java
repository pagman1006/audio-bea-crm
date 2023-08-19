package com.audiobea.crm.app.business.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.customer.model.Phone;

public interface IPhoneDao extends PagingAndSortingRepository<Phone, Long> {
}
