package farayan.sabad.vms

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeResult
import farayan.sabad.SabadDependencies
import farayan.sabad.commons.ExtractedBarcode
import farayan.sabad.commons.barcode
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.db.Barcode
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Photo
import farayan.sabad.db.Product
import farayan.sabad.db.Unit
import farayan.sabad.isUsable
import farayan.sabad.queryable
import farayan.sabad.repo.BarcodeRepo
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.ItemRepo
import farayan.sabad.repo.PriceRepo
import farayan.sabad.repo.ProductPhotoRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo
import farayan.sabad.utility.hasValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject
import farayan.sabad.db.Unit as PersistenceUnit

//@HiltViewModel
class InvoiceItemFormViewModel @Inject constructor(
    private val unitRepo: UnitRepo,
    private val categoryRepo: CategoryRepo,
    private val itemRepo: ItemRepo,
    private val productRepo: ProductRepo,
    private val productPhotoRepo: ProductPhotoRepo,
    private val barcodeRepo: BarcodeRepo,
    private val priceRepo: PriceRepo,
) : ViewModel() {
    fun barcodeScanned(br: BarcodeResult) {
        formScannedExtractedBarcode.value = br.barcode()
        val productBarcodes = barcodeRepo.byBarcode(br.barcode())
        if (productBarcodes.isEmpty()) {
            return
        }
        val products = productRepo.byIds(productBarcodes.map { it.productId })
        if (product.hasFixedValue) {
            barcodeScannedWithFixedProduct(products)
            return
        }
        if (category.hasFixedValue) {
            barcodeScannedWithFixedCategory(products, productBarcodes)
        } else {
            if (products.size == 1) {
                barcodeScannedMatched1RelatedProduct(products)
                return
            }
            if (products.size > 1) {
                barcodeScannedMatchedMultipleProduct(products)
            }
        }
    }

    private fun barcodeScannedMatchedMultipleProduct(products: List<Product>) {
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
            } else {
                fillProduct(firstOrNull)
            }
        }
    }

    private fun barcodeScannedMatched1RelatedProduct(products: List<Product>) {
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
        } else {
            fillProduct(theProduct)
        }
    }

    private fun barcodeScannedWithFixedCategory(products: List<Product>, barcodes: List<Barcode>) {
        val theFixedCategoryProducts = products.filter { it.categoryId == category.value.value!!.id }
        if (theFixedCategoryProducts.isEmpty()) {
            barcodeScannedWithFixedCategoryAndNoRelatedScannedProduct(barcodes)
            return
        }
        if (theFixedCategoryProducts.size == 1) {
            barcodeScannedWithFixedCategoryAndSingleRelatedScannedProduct(theFixedCategoryProducts)
            return
        }
        @Suppress("KotlinConstantConditions")
        if (theFixedCategoryProducts.size > 1) {
            barcodeScannedWithFixedCategoryAndMultipleRelatedScannedProduct(theFixedCategoryProducts)
        }
    }

    private fun barcodeScannedWithFixedCategoryAndMultipleRelatedScannedProduct(theFixedCategoryProducts: List<Product>) {
        if (formName.value.isUsable) {
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
            } else {
                fillProduct(firstOrNull)
            }
        }
    }

    private fun barcodeScannedWithFixedCategoryAndSingleRelatedScannedProduct(theFixedCategoryProducts: List<Product>) {
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
        } else {
            fillProduct(theProduct)
        }
    }

    private fun barcodeScannedWithFixedCategoryAndNoRelatedScannedProduct(barcodes: List<Barcode>) {
        question.value = Question(
            Questions.NoScannedProductBelongsToFixedGroup,
            "while there are ${barcodes.size} products with scanned barcode, none of them are in group ${category.theFixedValue.displayableName}. Please decide if you want to add new product or scan another product in group ${category.theFixedValue.displayableName}",
            Icons.Filled.Star,
            listOf(
                QuestionButton("Clear", "clear"),
                QuestionButton("Add new product", "add-product"),
            ),
            listOf()
        )
    }

    private fun barcodeScannedWithFixedProduct(products: List<Product>) {
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
    }

    private fun fillProduct(selected: Product) {
        if (product.stateValue != selected) {
            product.value = Fixable(selected, false)
        }
        if (formName.value != selected.displayableName) {
            formName.value = selected.displayableName
        }
        val productBarcodes = barcodeRepo.byProduct(selected)
        if (productBarcodes.any() && formScannedExtractedBarcode.value.hasValue) {
            val scannedBarcode =
                productBarcodes.firstOrNull { it.textual == formScannedExtractedBarcode.value!!.textual && BarcodeFormat.valueOf(it.format) == formScannedExtractedBarcode.value!!.format }
            if (scannedBarcode == null) {
                val firstBarcode = productBarcodes.first()
                formScannedExtractedBarcode.value = ExtractedBarcode(firstBarcode.textual, BarcodeFormat.valueOf(firstBarcode.format))
            }
        }

        val price = priceRepo.last(selected)
        val incompletePrice = formQuantityUnit.value == null || formPriceAmount.value == null || formPriceAmount.value!! <= BigDecimal.ZERO || formPriceCurrency.value == null
        if (price.hasValue && incompletePrice) {
            formPriceAmount.value = BigDecimal(price!!.amount)
            formPriceCurrency.value = Currency.valueOf(price.currency)
            formQuantityUnit.value = unitRepo.byId(price.packagingUnitId)
        }

        formPhotos.value = productPhotoRepo.byProduct(selected).map { ProductPhotoItem(it.path, it) }

        val item = itemRepo.pendingItemByProduct(selected)
        if (item.hasValue) {
            formQuantityValue.value = BigDecimal(item!!.quantity)
            formQuantityUnit.value = item.unitId?.let { unitRepo.byId(it) }
            formPackageWorth.value = item.packageWorth?.let { BigDecimal(item.packageWorth) }
            formPackageUnit.value = item.packageUnit?.let { UnitVariations.valueOf(item.packageUnit) }
            formPriceAmount.value = BigDecimal(item.fee)
            formPriceCurrency.value = Currency.valueOf(item.currency)
        }
    }

    private fun reset() {
        categories.value = listOf()
        //TODO:pickedItems.value = listOf()
        formScannedExtractedBarcode.value = null
        formName.value = ""
        formPhotos.value = listOf()
        formQuantityValue.value = null
        formQuantityUnit.value = null
        formPriceAmount.value = null
        formPriceCurrency.value = null
        question.value = null
        errorMessage.value = null
    }

    fun init(selectedCategory: Category): InvoiceItemFormViewModel {
        reset()
        category.value = Fixable(selectedCategory, true)
        product.value = Fixable(null, false)
        return this
    }

    fun init(item: Item): InvoiceItemFormViewModel {
        reset()
        category.value = Fixable(categoryRepo.byId(item.categoryId), true)
        product.value = Fixable(productRepo.byId(item.productId), true)
        fillProduct(product.stateValue!!)
        return this
    }

    fun photoTaken(it: File) {
        formPhotos.value += ProductPhotoItem(it.toString(), null)
    }

    fun photoRemoved(photo: String) {
        formPhotos.value -= ProductPhotoItem(photo, null)
    }

    fun units(): List<Unit> {
        if (category.hasAnyValue)
            return unitRepo.all()
        return listOf()
    }

    fun persistInvoiceItem(): Boolean {
        var p = product.stateValue
        if (p == null) {
            p = productRepo.ensure(category.stateValue ?: throw RuntimeException("category is null"), formName.value)
        }
        for (photo in formPhotos.value) {
            productPhotoRepo.ensure(p, photo.path)
        }
        if (formScannedExtractedBarcode.value.hasValue) {
            barcodeRepo.ensure(p, formScannedExtractedBarcode.value!!)
        }
        if (formPriceAmount.value.hasValue && formPriceAmount.value!! > BigDecimal.ZERO && formPriceCurrency.value.hasValue && formQuantityValue.value.hasValue && formQuantityUnit.value.hasValue)
            itemRepo.ensure(
                p,
                formPriceAmount.value!!,
                formPriceCurrency.value!!,
                formQuantityValue.value!!,
                formQuantityUnit.value!!,
                formPackageWorth.value,
                formPackageUnit.value
            )
        return true
    }

    val categories = MutableStateFlow(categoryRepo.all())
    val pickedItems = itemRepo.pickings().onEach { items ->
        Log.d("flow", "Collected items in InvoiceItemFormViewModel: ${items.size}")
    }
    val category = MutableStateFlow(Fixable<Category>())
    val product = MutableStateFlow(Fixable<Product>())
    val formScannedExtractedBarcode = MutableStateFlow<ExtractedBarcode?>(null)
    val formName = MutableStateFlow("")
    val formPhotos = MutableStateFlow(listOf<ProductPhotoItem>())
    val formQuantityValue = MutableStateFlow<BigDecimal?>(null)
    val formQuantityUnit = MutableStateFlow<PersistenceUnit?>(null)
    val formPackageUnit = MutableStateFlow<UnitVariations?>(null)
    val formPackageWorth = MutableStateFlow<BigDecimal?>(null)
    val formPriceAmount = MutableStateFlow<BigDecimal?>(null)
    val formPriceCurrency = MutableStateFlow<Currency?>(null)
    val question = MutableStateFlow<Question?>(null)
    val errorMessage = MutableStateFlow<String?>(null)
    var item: Item? = null

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
                    sd.itemRepo(),
                    sd.productRepo(),
                    sd.photoRepo(),
                    sd.barcodeRepo(),
                    sd.priceRepo()
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

data class ProductPhotoItem(val path: String, val record: Photo?)