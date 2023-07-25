package com.audiobea.crm.app.dao.customer;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.customer.model.PhoneType;

public interface IPhoneTypeDao extends PagingAndSortingRepository<PhoneType, Long> {
}
