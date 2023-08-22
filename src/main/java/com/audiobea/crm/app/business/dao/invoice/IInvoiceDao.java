package com.audiobea.crm.app.business.dao.invoice;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.audiobea.crm.app.business.dao.invoice.model.Invoice;

public interface IInvoiceDao extends PagingAndSortingRepository<Invoice, Long> {
}
