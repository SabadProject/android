package farayan.sabad.vms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import farayan.commons.queryable
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Group.IGroupRepo
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitEntity
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo
import farayan.sabad.core.OnePlace.ProductBarcode.BarcodeFormats
import farayan.sabad.core.OnePlace.ProductBarcode.BarcodePoint
import farayan.sabad.core.OnePlace.ProductBarcode.CapturedBarcode
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo
import farayan.sabad.core.OnePlace.Unit.IUnitRepo
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.Money
import farayan.sabad.core.model.product.IProductRepo
import farayan.sabad.core.model.product.ProductEntity
import farayan.sabad.utility.hasValue
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class InvoiceItemFormViewModel @Inject constructor(
    private val groupRepo: IGroupRepo,
    private val productRepo: IProductRepo,
    private val productBarcodeRepo: IProductBarcodeRepo,
    private val itemRepo: IInvoiceItemRepo,
    val unitRepo: IUnitRepo,
    private val groupUnitRepo: IGroupUnitRepo,
) : ViewModel() {
    fun barcodeScanned(br: BarcodeResult) {
        val products = productBarcodeRepo.byBarcode(
            CapturedBarcode(
                br.text,
                BarcodeFormats.valueOf(br.barcodeFormat.name),
                br.bitmapScaleFactor,
                br.bitmap,
                br.resultPoints.map { BarcodePoint(it.x, it.y) }.toTypedArray()
            )
        )
        if (products.isEmpty()) {
            productBarcode.value = br.text
            return
        }
        if (product.fixed().hasValue) {
            if (!products.contains(product.fixed())) {
                question.value = Question(
                    Questions.ScannedProductIsNotFixedProduct,
                    "Scanned barcode belongs to another product",
                    Icons.Filled.Star,
                    listOf(
                        QuestionButton("Clear", "clear"),
                        QuestionButton("Add to product barcodes", "add-product-barcode"),
                    ),
                    listOf()
                )
            }
            return
        }
        if (group.fixed().hasValue) {
            val fixedGroupProducts = products.filter { it.Group == group.value }
            if (fixedGroupProducts.isEmpty()) {
                question.value = Question(
                    Questions.NoScannedProductBelongsToFixedGroup,
                    "while there are ${products.size} products with scanned barcode, none of them are in group ${group.fixed()!!.DisplayableName}. Please decide if you want to add new product or scan another product in group ${group.fixed()!!.DisplayableName}",
                    Icons.Filled.Star,
                    listOf(
                        QuestionButton("Clear", "clear"),
                        QuestionButton("Add new product", "add-product"),
                    ),
                    listOf()
                )
                return
            }
            if (fixedGroupProducts.size == 1) {
                val theProduct = fixedGroupProducts.first()
                if (productName.value.isNotBlank() && productName.value.queryable()
                        .contentEquals(theProduct.QueryableName)
                ) {
                    question.value = Question(
                        Questions.ScannedProductWithinFixedGroupDoesNotMatchFilledForm,
                        "One product found in group ${group.fixed()!!.DisplayableName}, but the name does not match what you entered",
                        Icons.Filled.Star,
                        listOf(
                            QuestionButton("Select scanned product", "clear"),
                            QuestionButton("Create new product", "create-product"),
                        ),
                        listOf()
                    )
                    return
                }
            }
            if (fixedGroupProducts.size > 1) {
                if (productName.value.isNotBlank()) {
                    val queryableName = productName.value.queryable()
                    val firstOrNull = fixedGroupProducts.firstOrNull {
                        queryableName.contentEquals(
                            it.QueryableName,
                            true
                        )
                    }
                    if (firstOrNull == null) {
                        question.value = Question(
                            Questions.ScannedProductWithinFixedGroupDoesNotMatchFilledForm,
                            "Multiple products within fixed group found with scanned barcode, but none of them match the filled form.",
                            Icons.Filled.Star,
                            listOf(
                                QuestionButton(
                                    "Continue with selected product from above list",
                                    "clear"
                                ),
                                QuestionButton("Create new product", "create-product"),
                                QuestionButton("Clear barcode", "create-product"),
                            ),
                            fixedGroupProducts.map {
                                QuestionOption(
                                    it.DisplayableName,
                                    it.id.toString()
                                )
                            }
                        )
                        return
                    }
                }
            }
        } else {
            if (products.size == 1) {
                val theProduct = products.first()
                if (productName.value.isNotBlank() && !productName.value.queryable()
                        .contentEquals(theProduct.QueryableName)
                ) {
                    question.value = Question(
                        Questions.ScannedProductExistsWithDifferentName,
                        "One product found with scanned barcode, but its name is different with filled.",
                        Icons.Filled.Star,
                        listOf(
                            QuestionButton("Select product", "clear"),
                            QuestionButton("Create new product", "create-product"),
                            QuestionButton("Clear barcode", "create-product"),
                        ),
                        listOf()
                    )
                    return
                }
            }
            if (products.size > 1) {
                if (productName.value.isNotBlank()) {
                    val queryableName = productName.value.queryable()
                    val firstOrNull =
                        products.firstOrNull { queryableName.contentEquals(it.QueryableName, true) }
                    if (firstOrNull == null) {
                        question.value = Question(
                            Questions.ScannedProductDoesNotMatchFilledForm,
                            "Multiple products with scanned barcode, but none of them match the filled form.",
                            Icons.Filled.Star,
                            listOf(
                                QuestionButton(
                                    "Continue with selected product from above list",
                                    "clear"
                                ),
                                QuestionButton("Create new product", "create-product"),
                                QuestionButton("Clear barcode", "create-product"),
                            ),
                            products.map { QuestionOption(it.DisplayableName, it.id.toString()) }
                        )
                        return
                    }
                }
            }
        }
    }

    fun init(
        selectedGroup: GroupEntity? = null,
        fixedSelectedGroup: Boolean = false,
        selectedProduct: ProductEntity? = null,
        fixedSelectedProduct: Boolean = false,
    ) {
        group.value = Fixable(selectedGroup, fixedSelectedGroup)
        product.value = Fixable(selectedProduct, fixedSelectedProduct)
    }

    fun barcodeChangedManually(barcodeValue: String) {
        productBarcode.value = barcodeValue
    }

    fun units(): List<GroupUnitEntity> {
        return if (group.fixed().hasValue)
            groupUnitRepo.groupUnits(group.fixed()!!)
        else
            listOf()
    }

    fun photoTaken(it: File) {
        productPhotos.value += it.toString()
    }

    fun photoRemoved(photo: String) {
        productPhotos.value -= photo
    }

    val groups = MutableStateFlow(groupRepo.All(groupRepo.NewParams()))
    val pickedItems = MutableStateFlow(itemRepo.pickings())
    val group = MutableStateFlow(Fixable<GroupEntity>())
    val product = MutableStateFlow(Fixable<ProductEntity>())
    val productBarcode = MutableStateFlow("")
    val productName = MutableStateFlow("")
    val productPrice = MutableStateFlow<Money?>(Money(BigDecimal.ZERO, Currency.Rial))
    val productPhotos = MutableStateFlow(listOf<String>())
    val itemQuantity = MutableStateFlow(0.0)
    val itemUnit = MutableStateFlow("")
    val itemPriceAmount = MutableStateFlow(BigDecimal.ZERO)
    val itemPriceCurrency = MutableStateFlow("")

    val question = MutableStateFlow<Question?>(null)
}

data class Fixable<T>(val value: T? = null, val fixed: Boolean = false)

enum class Questions {
    ScannedProductIsNotFixedProduct,
    NoScannedProductBelongsToFixedGroup,
    ScannedProductWithinFixedGroupDoesNotMatchFilledForm,
    ScannedProductExistsWithDifferentName,
    ScannedProductDoesNotMatchFilledForm
}

data class Question(
    val question: Questions,
    val message: String,
    val icon: Any,
    val buttons: List<QuestionButton>,
    val options: List<QuestionOption>
)

data class QuestionButton(val label: String, val tag: String)
data class QuestionOption(val label: String, val tag: String)

fun <T> MutableStateFlow<Fixable<T>>.fixed(): T? =
    if (this.value.fixed && this.value.hasValue) this.value.value else null