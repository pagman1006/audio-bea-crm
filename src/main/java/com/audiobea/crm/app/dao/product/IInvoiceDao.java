package com.audiobea.crm.app.dao.product;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.product.model.Invoice;

public interface IInvoiceDao extends PagingAndSortingRepository<Invoice, Long> {
}
