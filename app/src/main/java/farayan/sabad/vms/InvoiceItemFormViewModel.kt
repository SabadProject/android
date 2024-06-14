package farayan.sabad.vms

import androidx.lifecycle.ViewModel
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import farayan.commons.FarayanUtility
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Group.IGroupRepo
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo
import farayan.sabad.core.OnePlace.ProductBarcode.BarcodeFormats
import farayan.sabad.core.OnePlace.ProductBarcode.BarcodePoint
import farayan.sabad.core.OnePlace.ProductBarcode.CapturedBarcode
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo
import farayan.sabad.core.OnePlace.product.ProductEntity
import farayan.sabad.core.model.product.IProductRepo
import farayan.sabad.utility.hasValue
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class InvoiceItemFormViewModel @Inject constructor(
    private val groupRepo: IGroupRepo,
    private val productRepo: IProductRepo,
    private val productBarcodeRepo: IProductBarcodeRepo,
    private val itemRepo: IInvoiceItemRepo,
) : ViewModel() {
    fun barcodeScanned(br: BarcodeResult) {
        productBarcode.value = br.text
        val products = productBarcodeRepo.byBarcode(
            CapturedBarcode(
                br.text,
                BarcodeFormats.valueOf(br.barcodeFormat.name),
                br.bitmapScaleFactor,
                br.bitmap,
                br.resultPoints.map { BarcodePoint(it.x, it.y) }.toTypedArray()
            )
        )
        if (products.isEmpty())
            return
        if (productName.value.isEmpty())
            return
        val queryableProductName = FarayanUtility.Queryable(productName.value)
        val foundProduct = products.firstOrNull() { it.QueryableName.equals(queryableProductName) }
        if (foundProduct.hasValue)
            return
    }

    fun init(
        selectedGroup: GroupEntity? = null,
        fixedSelectedGroup: Boolean = false,
        selectedProduct: ProductEntity? = null,
        fixedSelectedProduct: Boolean = false,
    ) {
        group.value = selectedGroup
        groupFixed.value = fixedSelectedGroup
        product.value = selectedProduct
        productFixed.value = fixedSelectedProduct
    }

    fun barcodeChangedManually(barcodeValue: String) {
        productBarcode.value = barcodeValue
    }

    val groups = MutableStateFlow(groupRepo.All(groupRepo.NewParams()))
    val pickedItems = MutableStateFlow(itemRepo.pickings())
    val group = MutableStateFlow<GroupEntity?>(null)
    val groupFixed = MutableStateFlow<Boolean>(false)
    val product = MutableStateFlow<ProductEntity?>(null)
    val productFixed = MutableStateFlow(false)
    val productBarcode = MutableStateFlow("")
    val productName = MutableStateFlow("")
    val productPhoto = MutableStateFlow("")
    val itemQuantity = MutableStateFlow(0.0)
    val itemUnit = MutableStateFlow("")
    val itemPriceAmount = MutableStateFlow(BigDecimal.ZERO)
    val itemPriceCurrency = MutableStateFlow("")
}