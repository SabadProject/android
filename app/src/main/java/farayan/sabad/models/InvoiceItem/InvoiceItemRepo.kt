package farayan.sabad.models.InvoiceItem

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.commons.QueryBuilderCore.EntityFilter
import farayan.commons.QueryBuilderCore.PropertyValueConditionModes
import farayan.commons.QueryBuilderCore.RelationalOperators
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemParams

class InvoiceItemRepo : IInvoiceItemRepo {
    override fun DAO(): RuntimeExceptionDao<InvoiceItemEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(InvoiceItemEntity::class.java)
    }

    override fun NewParams(): BaseParams<InvoiceItemEntity> {
        return InvoiceItemParams()
    }

    override fun DeleteAllItemsWithoutInvoice() {
        val itemsWithoutInvoiceParams = InvoiceItemParams()
        itemsWithoutInvoiceParams.Invoice =
            EntityFilter(PropertyValueConditionModes.ProvidedValueOrNull)
        Delete(itemsWithoutInvoiceParams)
    }

    override fun InvoiceParams(invoice: InvoiceEntity): List<InvoiceItemEntity> {
        val params = InvoiceItemParams()
        params.Invoice = EntityFilter(invoice)
        return All(params)!!
    }

    override fun pickings(): List<InvoiceItemEntity> {
        val params = InvoiceItemParams()
        params.Invoice = EntityFilter(
            null,
            RelationalOperators.IsNull,
            PropertyValueConditionModes.ProvidedValueOrNull
        )
        return All(params)!!
    }
}