package farayan.sabad.ui

import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeResult
import farayan.commons.UI.Core.IGenericEvent
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Group.IGroupRepo
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity
import farayan.sabad.core.OnePlace.Unit.IUnitRepo
import farayan.sabad.core.model.product.IProductRepo
import farayan.sabad.core.model.product.ProductEntity
import farayan.sabad.model.product_barcode.IProductBarcodeRepo

@Suppress("unused")
class InvoiceItemFormInputArgs(
    val invoiceItem: InvoiceItemEntity?,
    val group: GroupEntity?,
    val product: ProductEntity?,
    var barcode: BarcodeResult?,
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
