package com.audiobea.crm.app.business.dao.invoice;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.invoice.model.CashOrder;

public interface ICashOrderDao extends PagingAndSortingRepository<CashOrder, Long> {
}
