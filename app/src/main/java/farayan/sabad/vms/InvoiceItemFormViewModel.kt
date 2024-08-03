package farayan.sabad.vms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.journeyapps.barcodescanner.BarcodeResult
import farayan.sabad.SabadDependencies
import farayan.sabad.core.commons.Currency
import farayan.sabad.db.Category
import farayan.sabad.db.Product
import farayan.sabad.db.Unit
import farayan.sabad.queryable
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.InvoiceItemRepo
import farayan.sabad.repo.ProductBarcodeRepo
import farayan.sabad.repo.ProductPhotoRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo
import farayan.sabad.utility.hasValue
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject

//@HiltViewModel
class InvoiceItemFormViewModel @Inject constructor(
    private val unitRepo: UnitRepo,
    private val categoryRepo: CategoryRepo,
    private val invoiceItemRepo: InvoiceItemRepo,
    private val productRepo: ProductRepo,
    private val productPhotoRepo: ProductPhotoRepo,
    private val productBarcodeRepo: ProductBarcodeRepo,
) : ViewModel() {
    fun barcodeScanned(br: BarcodeResult) {
        val productBarcodes = productBarcodeRepo.byBarcode(br)
        if (productBarcodes.isEmpty()) {
            formScannedBarcode.value = br
            return
        }
        val products = productRepo.byIds(productBarcodes.map { it.productId })
        if (product.hasFixedValue) {
            val id = product.theFixedValue.id
            if (!products.any { it.id == id }) {
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
        if (category.hasFixedValue) {
            val theFixedCategoryProducts = products.filter { it.categoryId == category.value.value!!.id }
            if (theFixedCategoryProducts.isEmpty()) {
                question.value = Question(
                    Questions.NoScannedProductBelongsToFixedGroup,
                    "while there are ${productBarcodes.size} products with scanned barcode, none of them are in group ${category.theFixedValue.displayableName}. Please decide if you want to add new product or scan another product in group ${category.theFixedValue.displayableName}",
                    Icons.Filled.Star,
                    listOf(
                        QuestionButton("Clear", "clear"),
                        QuestionButton("Add new product", "add-product"),
                    ),
                    listOf()
                )
                return
            }
            if (theFixedCategoryProducts.size == 1) {
                val theProduct = theFixedCategoryProducts.first()
                if (formName.value.isNotBlank() && !formName.value.queryable().contentEquals(theProduct.queryableName)) {
                    question.value = Question(
                        Questions.ScannedProductWithinFixedGroupDoesNotMatchFilledForm,
                        "One product found in group ${category.theFixedValue.displayableName}, but the name does not match what you entered",
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
            if (theFixedCategoryProducts.size > 1) {
                if (formName.value.isNotBlank()) {
                    val queryableName = formName.value.queryable()
                    val firstOrNull = theFixedCategoryProducts.firstOrNull {
                        queryableName.contentEquals(
                            it.queryableName,
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
                            theFixedCategoryProducts.map {
                                QuestionOption(
                                    it.queryableName,
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
                if (formName.value.isNotBlank() && !formName.value.queryable().contentEquals(theProduct.queryableName)) {
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
                if (formName.value.isNotBlank()) {
                    val queryableName = formName.value.queryable()
                    val firstOrNull = products.firstOrNull { queryableName.contentEquals(it.queryableName, true) }
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
                            products.map { QuestionOption(it.displayableName, it.id.toString()) }
                        )
                        return
                    }
                }
            }
        }
    }

    fun init(
        selectedCategory: Category? = null,
        fixedSelectedGroup: Boolean = false,
        selectedProduct: Product? = null,
        fixedSelectedProduct: Boolean = false,
    ) {
        category.value = Fixable(selectedCategory, fixedSelectedGroup)
        product.value = Fixable(selectedProduct, fixedSelectedProduct)
    }

    fun photoTaken(it: File) {
        formPhotos.value += it.toString()
    }

    fun photoRemoved(photo: String) {
        formPhotos.value -= photo
    }

    fun units(): List<Unit> {
        if (category.hasAnyValue)
            return unitRepo.all()
        return listOf()
    }

    fun persistInvoiceItem(): Boolean {
        var p = product.stateValue
        if (p == null) {
            p = productRepo.create(formName.value, category.stateValue ?: throw RuntimeException("category is null"))
        }
        for (photo in formPhotos.value) {
            productPhotoRepo.ensure(p, photo)
        }
        if (formScannedBarcode.value.hasValue) {
            productBarcodeRepo.ensure(p, formScannedBarcode.value!!)
        }
        invoiceItemRepo.ensure(p, formPriceAmount.value, Currency.valueOf(formPriceCurrency.value), formQuantity.value)
        return true
    }

    val categories = MutableStateFlow(categoryRepo.all())
    val pickedItems = MutableStateFlow(invoiceItemRepo.pickedItems())
    val category = MutableStateFlow(Fixable<Category>())
    val product = MutableStateFlow(Fixable<Product>())
    val formScannedBarcode = MutableStateFlow<BarcodeResult?>(null)
    val formName = MutableStateFlow("")
    val formPhotos = MutableStateFlow(listOf<String>())
    val formQuantity = MutableStateFlow<BigDecimal>(BigDecimal.ZERO)
    val formUnit = MutableStateFlow("")
    val formPriceAmount = MutableStateFlow(BigDecimal.ZERO)
    val formPriceCurrency = MutableStateFlow("")
    val question = MutableStateFlow<Question?>(null)
    val errorMessage = MutableStateFlow<String?>(null)

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                val sd = SabadDependencies()

                return InvoiceItemFormViewModel(
                    sd.unitRepo(),
                    sd.categoryRepo(),
                    sd.invoiceItemRepo(),
                    sd.productRepo(),
                    sd.productPhotoRepo(),
                    sd.productBarcodeRepo(),
                ) as T
            }
        }
    }
}

class Fixable<T>(val value: T? = null, val fixed: Boolean = false) {
    operator fun invoke() = value
}

fun <T> MutableStateFlow<T>.invoke(): T {
    return this.value
}

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

val <T> MutableStateFlow<Fixable<T>>.hasFixedValue: Boolean
    get() {
        return this.value.value.hasValue && this.value.fixed
    }

val <T> MutableStateFlow<Fixable<T>>.hasAnyValue: Boolean
    get() {
        return this.value.value.hasValue
    }

val <T> MutableStateFlow<Fixable<T>>.theFixedValue: T
    get() {
        return this.value.value ?: throw RuntimeException("value is null")
    }

val <T> MutableStateFlow<Fixable<T>>.stateValue: T?
    get() {
        return this.value.value
    }
