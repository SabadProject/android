package farayan.sabad.core.OnePlace.Invoice

import farayan.commons.QueryBuilderCore.IRepo

interface IInvoiceRepo : IRepo<InvoiceEntity> {
    fun Hide(invoiceEntity: InvoiceEntity)
}