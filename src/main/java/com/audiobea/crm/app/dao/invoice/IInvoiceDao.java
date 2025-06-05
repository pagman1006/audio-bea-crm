package com.audiobea.crm.app.dao.invoice;

import com.audiobea.crm.app.dao.invoice.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IInvoiceDao extends MongoRepository<Invoice, String> {
}
