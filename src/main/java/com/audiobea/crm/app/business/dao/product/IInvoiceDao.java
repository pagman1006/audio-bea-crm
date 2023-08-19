package com.audiobea.crm.app.business.dao.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.product.model.Invoice;

public interface IInvoiceDao extends PagingAndSortingRepository<Invoice, Long> {
}
