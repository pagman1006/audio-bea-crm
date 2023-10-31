package com.audiobea.crm.app.dao.invoice;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.dao.invoice.model.Invoice;

public interface IInvoiceDao extends PagingAndSortingRepository<Invoice, Long> {
}
