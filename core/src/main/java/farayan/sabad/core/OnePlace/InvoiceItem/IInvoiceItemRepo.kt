package farayan.sabad.core.OnePlace.InvoiceItem

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity

interface IInvoiceItemRepo : IRepo<InvoiceItemEntity> {
    fun DeleteAllItemsWithoutInvoice()
    fun InvoiceParams(invoice: InvoiceEntity): List<InvoiceItemEntity>
}