package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.CashOrder;

public interface ICashOrderDao extends PagingAndSortingRepository<CashOrder, Long> {
}
