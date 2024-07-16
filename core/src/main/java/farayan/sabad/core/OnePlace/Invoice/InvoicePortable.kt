package farayan.sabad.core.OnePlace.Invoice

import farayan.commons.QueryBuilderCore.IRepo
import farayan.commons.QueryBuilderCore.ensured
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemPortable
import farayan.sabad.core.OnePlace.Store.StoreEntity
import farayan.sabad.core.OnePlace.Unit.UnitEntity
import farayan.sabad.core.base.SabadPortableBase
import farayan.sabad.core.model.product.ProductEntity
import org.jetbrains.annotations.NotNull

class InvoicePortable(
    invoice: InvoiceEntity,
    val Deleted: Boolean,
    val Instant: Long,
    val Discount: Double,
    val Total: Double,
    val Seller: SabadPortableBase<StoreEntity>?,
    val ItemsCountSum: Int,
    val ItemsQuantitySum: Int,
    val ItemsPriceSum: Int,
    val Items: Array<InvoiceItemPortable>
) : SabadPortableBase<InvoiceEntity>(invoice) {
    constructor(
        invoice: @NotNull InvoiceEntity,
        items: MutableList<InvoiceItemEntity>,
        storeRepo: @NotNull IRepo<StoreEntity>,
        productRepo: @NotNull IRepo<ProductEntity>,
        invoiceRepo: @NotNull IRepo<InvoiceEntity>,
        groupRepo: @NotNull IRepo<GroupEntity>,
        unitRepo: @NotNull IRepo<UnitEntity>
    ) : this(
        invoice,
        invoice.Deleted,
        invoice.Instant,
        invoice.Discount,
        invoice.Total,
        invoice.Seller?.let { SabadPortableBase(invoice.Seller.ensured(storeRepo)) },
        invoice.ItemsCountSum,
        invoice.ItemsQuantitySum,
        invoice.ItemsPriceSum,
        items.map { x -> InvoiceItemPortable(x, productRepo, invoiceRepo, groupRepo, unitRepo) }
            .toTypedArray()
    )
}