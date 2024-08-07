package farayan.sabad.ui

import com.google.zxing.client.android.BeepManager
import farayan.commons.UI.Core.IGenericEvent
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Group.IGroupRepo
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity
import farayan.sabad.core.OnePlace.ProductBarcode.CapturedBarcode
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo
import farayan.sabad.core.OnePlace.Unit.IUnitRepo
import farayan.sabad.core.model.product.IProductRepo
import farayan.sabad.core.model.product.ProductEntity

@Suppress("unused")
class InvoiceItemFormInputArgs(
    val invoiceItem: InvoiceItemEntity?,
    val group: GroupEntity?,
    val product: ProductEntity?,
    var barcode: CapturedBarcode?,
    val onRegistered: IGenericEvent<InvoiceItemEntity>?,
    val onRemoved: IGenericEvent<InvoiceItemEntity>?,
    val groupRepo: IGroupRepo,
    val groupUnitRepo: IGroupUnitRepo,
    val productRepo: IProductRepo,
    val productBarcodeRepo: IProductBarcodeRepo,
    val invoiceItemRepo: IInvoiceItemRepo,
    val unitRepo: IUnitRepo,
    val beepManager: BeepManager
)
