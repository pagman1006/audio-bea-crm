package com.audiobea.crm.app.business.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.customer.model.PhoneType;

public interface IPhoneTypeDao extends PagingAndSortingRepository<PhoneType, Long> {
}
