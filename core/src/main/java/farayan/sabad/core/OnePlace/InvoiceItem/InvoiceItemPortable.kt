package farayan.sabad.core.OnePlace.InvoiceItem

import farayan.commons.QueryBuilderCore.IRepo
import farayan.commons.QueryBuilderCore.ensured
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity
import farayan.sabad.core.base.SabadPortableBase
import farayan.sabad.core.model.product.ProductEntity
import farayan.sabad.core.model.unit.UnitEntity
import java.math.BigDecimal

class InvoiceItemPortable(
    entity: InvoiceItemEntity,
    val Product: SabadPortableBase<ProductEntity>,
    val Quantity: BigDecimal,
    val Fee: BigDecimal,
    val Discount: BigDecimal,
    val Total: BigDecimal,
    val Invoice: SabadPortableBase<InvoiceEntity>,
    val Group: SabadPortableBase<GroupEntity>,
    val Unit: SabadPortableBase<UnitEntity>?,
) : SabadPortableBase<InvoiceItemEntity>(entity) {
    constructor(
        item: InvoiceItemEntity,
        productRepo: IRepo<ProductEntity>,
        invoiceRepo: IRepo<InvoiceEntity>,
        groupRepo: IRepo<GroupEntity>,
        unitRepo: IRepo<UnitEntity>,
    ) : this(
        item,
        SabadPortableBase(item.Product.ensured(productRepo)),
        item.Quantity,
        item.Fee,
        item.Discount,
        item.Total,
        SabadPortableBase(item.Invoice.ensured(invoiceRepo)),
        SabadPortableBase(item.Group.ensured(groupRepo)),
        item.Unit.let { SabadPortableBase(item.Unit.ensured(unitRepo)) },
    )
}