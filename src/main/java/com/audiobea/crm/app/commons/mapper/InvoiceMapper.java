package com.audiobea.crm.app.commons.mapper;

import com.audiobea.crm.app.commons.dto.DtoInInvoice;
import com.audiobea.crm.app.dao.invoice.model.Invoice;
import com.audiobea.crm.app.utils.Constants;
import org.mapstruct.Mapper;

@Mapper(componentModel = Constants.SPRING)
public interface InvoiceMapper {

    DtoInInvoice invoiceToDtoInInvoice(Invoice invoice);

    Invoice invoiceDtoInToInvoice(DtoInInvoice dtoInInvoice);
}
