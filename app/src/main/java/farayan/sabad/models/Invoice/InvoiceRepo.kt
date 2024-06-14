package farayan.sabad.models.Invoice

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity
import farayan.sabad.core.OnePlace.Invoice.InvoiceParams

class InvoiceRepo : IInvoiceRepo {
    override fun DAO(): RuntimeExceptionDao<InvoiceEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(InvoiceEntity::class.java)
    }

    override fun NewParams(): BaseParams<InvoiceEntity> {
        return InvoiceParams()
    }

    override fun Hide(invoiceEntity: InvoiceEntity) {
        invoiceEntity.Deleted = true
        Update(invoiceEntity)
    }
}