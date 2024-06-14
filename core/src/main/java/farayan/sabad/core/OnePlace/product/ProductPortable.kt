package farayan.sabad.core.OnePlace.product

import farayan.commons.FarayanUtility
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity
import farayan.sabad.core.base.SabadPortableBase
import org.jetbrains.annotations.NotNull

class ProductPortable(
        product: ProductEntity,
        val DisplayableName: String?,
        val QueryableName: String?,
        val PhotoLocation: String?,
        val Deleted: Boolean,
        val Group: SabadPortableBase<GroupEntity>,
        val LastPurchase: SabadPortableBase<InvoiceItemEntity>?,
) : SabadPortableBase<ProductEntity>(product) {
    constructor(product: @NotNull ProductEntity) : this(
            product,
            FarayanUtility.Or(product.Group.DisplayableName, "") + " " + FarayanUtility.Or(product.DisplayableName, ""),
            FarayanUtility.Or(product.Group.QueryableName, "") + " " + FarayanUtility.Or(product.QueryableName, ""),
            product.PhotoLocation ?: "",
            product.Deleted,
            SabadPortableBase(product.Group),
            product.LastPurchase?.let { SabadPortableBase(product.LastPurchase) },
    )
}