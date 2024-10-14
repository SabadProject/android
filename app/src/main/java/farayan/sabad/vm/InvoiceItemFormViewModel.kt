package farayan.sabad.vm

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeResult
import farayan.sabad.R
import farayan.sabad.di.SabadDeps
import farayan.sabad.commons.ExtractedBarcode
import farayan.sabad.commons.Fixable
import farayan.sabad.commons.Text
import farayan.sabad.commons.anyValue
import farayan.sabad.commons.barcode
import farayan.sabad.commons.hasAnyValue
import farayan.sabad.commons.hasFixedValue
import farayan.sabad.commons.theValue
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.core.commons.currency
import farayan.sabad.core.commons.localize
import farayan.sabad.db.Barcode
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Photo
import farayan.sabad.db.Product
import farayan.sabad.db.Unit
import farayan.sabad.repo.BarcodeRepo
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.ItemRepo
import farayan.sabad.repo.PriceRepo
import farayan.sabad.repo.PhotoRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo
import farayan.sabad.core.commons.hasValue
import farayan.sabad.utility.isUsable
import farayan.sabad.utility.queryable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val photoRepo: PhotoRepo,
    private val barcodeRepo: BarcodeRepo,
    private val priceRepo: PriceRepo,
) : ViewModel() {

    val categories = MutableStateFlow(categoryRepo.all())
    val pickedItems = itemRepo.pickingsFlow()

    val category = MutableStateFlow(Fixable<Category>())
    val product = MutableStateFlow(Fixable<Product>())
    val formScannedExtractedBarcode = MutableStateFlow<ExtractedBarcode?>(null)
    private val formNameMutable = MutableStateFlow("")
    val formNameReadonly = formNameMutable.asStateFlow()
    val formPhotos = MutableStateFlow(listOf<ProductPhotoItem>())
    val formQuantityValue = MutableStateFlow<BigDecimal?>(null)
    val formQuantityUnit = MutableStateFlow<PersistenceUnit?>(null)
    val formPackageUnit = MutableStateFlow<UnitVariations?>(null)
    val formPackageWorth = MutableStateFlow<BigDecimal?>(null)
    val formPriceAmount = MutableStateFlow<BigDecimal?>(null)
    private val formPriceCurrencyMutable = MutableStateFlow(Fixable<Currency>())
    val formPriceCurrencyReadOnly = formPriceCurrencyMutable.asStateFlow()
    val question = MutableStateFlow<Question?>(null)
    val errorMessage = MutableStateFlow<String?>(null)
    var item: Item? = null

    fun barcodeScanned(br: BarcodeResult, ctx: Context) {
        val tag = "invoice-item-form-dialog:barcode-scanned"
        Log.d(tag, "barcode scanned: $br")
        val extractedBarcode = br.barcode()
        val barcodes = barcodeRepo.byBarcode(extractedBarcode)
        if (barcodes.isEmpty()) {
            formScannedExtractedBarcode.value = extractedBarcode
            Log.d(tag, "No barcode found with barcode: $extractedBarcode")
            return
        }
        Log.d(tag, "${barcodes.size} barcodes found with barcode: $extractedBarcode")
        val products = productRepo.byIds(barcodes.map { it.productId })
        Log.d(tag, "${products.size} products found with barcode: $extractedBarcode")
        if (product.hasFixedValue) {
            Log.d(
                tag,
                "Product is fixed, so start barcodeScannedWithFixedProduct. barcode: $extractedBarcode"
            )
            barcodeScannedWithFixedProduct(products)
            return
        }
        if (category.hasFixedValue) {
            Log.d(
                tag,
                "Category is fixed, so start barcodeScannedWithFixedCategory. barcode: $extractedBarcode"
            )
            barcodeScannedWithFixedCategory(extractedBarcode, products, barcodes, ctx)
        } else {
            if (products.size == 1) {
                Log.d(
                    tag,
                    "Category is NOT fixed and only one product is found so start barcodeScannedMatched1RelatedProduct. barcode: $extractedBarcode"
                )
                barcodeScannedMatched1RelatedProduct(products)
                return
            }
            if (products.size > 1) {
                Log.d(
                    tag,
                    "Category is NOT fixed, and multiple products found so start barcodeScannedMatchedMultipleProduct. barcode: $extractedBarcode"
                )
                barcodeScannedMatchedMultipleProduct(products)
            }
        }
    }

    private fun barcodeScannedMatchedMultipleProduct(products: List<Product>) {
        val tag = "barcodeScannedMatchedMultipleProduct"
        if (formNameMutable.value.isNotBlank()) {
            Log.d(tag, "form is usable, checking if any related product can be found")
            val queryableName = formNameMutable.value.queryable()
            val firstOrNull =
                products.firstOrNull { queryableName.contentEquals(it.queryableName, true) }
            if (firstOrNull == null) {
                Log.d(tag, "No product matched with filled form, so asking from user what to do")
                question.value = Question(
                    Questions.ScannedProductDoesNotMatchFilledForm,
                    "",
                    "Multiple products with scanned barcode, but none of them match the filled form.",
                    Icons.Filled.Star,
                    listOf(
                        QuestionButton("Continue with selected product from above list") { /*"clear"*/ },
                        QuestionButton("Create new product") { /*"create-product"*/ },
                        QuestionButton("Clear barcode") { /*"create-product"*/ },
                    ),
                    products.map { QuestionOption(it.displayableName, it.id.toString()) }
                )
                return
            } else {
                Log.d(tag, "One product matched with filled form, so filling rest of form with it")
                fillProduct(firstOrNull)
            }
        }
    }

    private fun barcodeScannedMatched1RelatedProduct(products: List<Product>) {
        val theProduct = products.first()
        if (formNameMutable.value.isNotBlank() && !formNameMutable.value.queryable().contentEquals(theProduct.queryableName)) {
            question.value = Question(
                Questions.ScannedProductExistsWithDifferentName,
                "",
                "One product found with scanned barcode, but its name is different with filled.",
                Icons.Filled.Star,
                listOf(
                    QuestionButton("Select product") { /*"clear"*/ },
                    QuestionButton("Create new product") { /*"create-product"*/ },
                    QuestionButton("Clear barcode") { /*"create-product"*/ },
                ),
                listOf()
            )
            return
        } else {
            fillProduct(theProduct)
        }
    }

    private fun barcodeScannedWithFixedCategory(extractedBarcode: ExtractedBarcode, products: List<Product>, barcodes: List<Barcode>, ctx: Context) {
        val theFixedCategoryProducts = products.filter { it.categoryId == category.value.value!!.id }
        if (theFixedCategoryProducts.isEmpty()) {
            barcodeScannedWithFixedCategoryAndNoRelatedScannedProduct(extractedBarcode, barcodes, ctx)
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

    private fun barcodeScannedWithFixedCategoryAndMultipleRelatedScannedProduct(
        theFixedCategoryProducts: List<Product>
    ) {
        if (formNameMutable.value.isUsable) {
            val queryableName = formNameMutable.value.queryable()
            val firstOrNull = theFixedCategoryProducts.firstOrNull {
                queryableName.contentEquals(
                    it.queryableName,
                    true
                )
            }
            if (firstOrNull == null) {
                question.value = Question(
                    Questions.ScannedProductWithinFixedGroupDoesNotMatchFilledForm,
                    "",
                    "Multiple products within fixed group found with scanned barcode, but none of them match the filled form.",
                    Icons.Filled.Star,
                    listOf(
                        QuestionButton("Continue with selected product from above list") { /*"clear"*/ },
                        QuestionButton("Create new product") { /*"create-product"*/ },
                        QuestionButton("Clear barcode") {/*"create-product"*/ },
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

    private fun barcodeScannedWithFixedCategoryAndSingleRelatedScannedProduct(
        theFixedCategoryProducts: List<Product>
    ) {
        val theProduct = theFixedCategoryProducts.first()
        if (formNameMutable.value.isNotBlank() && !formNameMutable.value.queryable().contentEquals(theProduct.queryableName)
        ) {
            question.value = Question(
                Questions.ScannedProductWithinFixedGroupDoesNotMatchFilledForm,
                "",
                "One product found in group ${category.theValue.displayableName}, but the name does not match what you entered",
                Icons.Filled.Star,
                listOf(
                    QuestionButton("Select scanned product") { "clear" },
                    QuestionButton("Create new product") { "create-product" },
                ),
                listOf()
            )
        } else {
            fillProduct(theProduct)
        }
    }

    private fun barcodeScannedWithFixedCategoryAndNoRelatedScannedProduct(extractedBarcode: ExtractedBarcode, barcodes: List<Barcode>, ctx: Context) {
        question.value = Question(
            Questions.NoScannedProductBelongsToFixedGroup,
            ctx.getString(R.string.item_form_barcode_registered_for_other_category_title),
            ctx.getString(
                R.string.item_form_barcode_registered_for_other_category_message_template,
                barcodes.size.toString().localize(),
                extractedBarcode.toString(),
                category.theValue.displayableName
            ),
            Icons.Filled.Star,
            listOf(
                QuestionButton(ctx.getString(R.string.item_form_barcode_registered_for_other_category_skip_barcode_label)) { question.value = null },
                QuestionButton(
                    ctx.getString(R.string.item_form_barcode_registered_for_other_category_create_product_label)
                ) {
                    question.value = null
                    formScannedExtractedBarcode.value = extractedBarcode
                },
            ),
            listOf()
        )
    }

    private fun barcodeScannedWithFixedProduct(products: List<Product>) {
        val id = product.theValue.id
        if (!products.any { it.id == id }) {
            question.value = Question(
                Questions.ScannedProductIsNotFixedProduct,
                "",
                "Scanned barcode belongs to another product",
                Icons.Filled.Star,
                listOf(
                    QuestionButton("Clear") { "clear" },
                    QuestionButton("Add to product barcodes") { "add-product-barcode" },
                ),
                listOf()
            )
        }
    }

    private fun fillProduct(selected: Product) {
        if (product.anyValue != selected) {
            product.value = Fixable(selected, false)
        }
        if (formNameMutable.value != selected.displayableName) {
            formNameMutable.value = selected.displayableName
        }
        val productBarcodes = barcodeRepo.byProduct(selected)
        if (productBarcodes.any()) {
            val barcode: ExtractedBarcode
            if (formScannedExtractedBarcode.value.hasValue) {
                val scannedBarcode = productBarcodes.firstOrNull {
                    it.textual == formScannedExtractedBarcode.value!!.text && BarcodeFormat.valueOf(it.format) == formScannedExtractedBarcode.value!!.format
                }
                barcode = scannedBarcode?.extracted() ?: productBarcodes.first().extracted()
            } else {
                barcode = productBarcodes.first().extracted()
            }

            formScannedExtractedBarcode.value = barcode
        }

        val price = priceRepo.last(selected)
        val incompletePrice =
            formQuantityUnit.value == null || formPriceAmount.value == null || formPriceAmount.value!! <= BigDecimal.ZERO || formPriceCurrencyMutable.hasAnyValue
        if (price.hasValue && incompletePrice) {
            val currency = price!!.currency.currency()
            if ((!formPriceCurrencyMutable.hasFixedValue || formPriceCurrencyMutable.theValue == currency)) {
                formPriceAmount.value = BigDecimal(price.amount)
                formPriceCurrencyMutable.value = Fixable(currency)
                formQuantityUnit.value = unitRepo.byId(price.packagingUnitId)
            }
        }

        formPhotos.value = photoRepo.byProduct(selected).map { ProductPhotoItem(it.path, it) }

        val item = itemRepo.pendingItemByProduct(selected)
        if (item.hasValue) {
            formQuantityValue.value = BigDecimal(item!!.quantity)
            formQuantityUnit.value = item.unitId?.let { unitRepo.byId(it) }
            formPackageWorth.value = item.packageWorth?.let { BigDecimal(item.packageWorth) }
            formPackageUnit.value = item.packageUnit?.let { UnitVariations.valueOf(item.packageUnit) }
            val currency = item.currency?.currency()
            @Suppress("SimplifyBooleanWithConstants")
            if ((formPriceCurrencyMutable.hasFixedValue == false || formPriceCurrencyMutable.theValue == currency)) {
                formPriceAmount.value = item.fee?.let { BigDecimal(item.fee) }
                formPriceCurrencyMutable.value = Fixable(currency, true)
            }
        }
    }

    private fun reset() {
        categories.value = listOf()
        //TODO:pickedItems.value = listOf()
        formScannedExtractedBarcode.value = null
        formNameMutable.value = ""
        formPhotos.value = listOf()
        formQuantityValue.value = null
        formQuantityUnit.value = null
        formPriceAmount.value = null
        formPriceCurrencyMutable.value = Fixable()
        question.value = null
        errorMessage.value = null
    }

    fun init(selectedCategory: Category): InvoiceItemFormViewModel {
        reset()
        category.value = Fixable(selectedCategory, true)
        product.value = Fixable(null, false)
        itemRepo.currentCurrency()?.apply { formPriceCurrencyMutable.value = Fixable(this, true) }
        return this
    }

    fun init(item: Item): InvoiceItemFormViewModel {
        reset()
        category.value = Fixable(categoryRepo.byId(item.categoryId), true)
        product.value = Fixable(productRepo.byId(item.productId), true)
        fillProduct(product.anyValue!!)
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

    fun persistInvoiceItem(context: Context): Boolean {
        var isMinInfoProvided = formScannedExtractedBarcode.value.hasValue
        isMinInfoProvided = isMinInfoProvided || formNameMutable.value.isUsable
        isMinInfoProvided = isMinInfoProvided || (formPriceAmount.value.hasValue && formPriceCurrencyMutable.value.hasValue)
        if (!isMinInfoProvided) {
            errorMessage.value = context.getString(R.string.item_form_persist_error)
            return false
        }
        val itemSummaryCurrency = itemRepo.itemSummaryItem()?.currency
        val formCurrency = formPriceCurrencyMutable.anyValue?.name
        if(itemSummaryCurrency.hasValue && !itemSummaryCurrency.equals(formCurrency)){
            errorMessage.value = context.getString(R.string.invoice_item_form_multiple_currency_error)
            return false
        }
        var product = product.anyValue
        if (product == null) {
            product = productRepo.ensure(
                category.anyValue ?: throw RuntimeException("category is null"), formNameMutable.value
            )
        }
        val name = Text(formNameMutable.value)
        if (!name.equals(product.displayableName, product.queryableName)) {
            productRepo.updateName(product, name)
        }
        for (photo in formPhotos.value) {
            photoRepo.ensure(product, photo.path)
        }
        if (formScannedExtractedBarcode.value.hasValue) {
            barcodeRepo.ensure(product, formScannedExtractedBarcode.value!!)
        }
        itemRepo.ensure(
            product,
            formPriceAmount.value,
            formPriceCurrencyMutable.anyValue,
            formQuantityValue.value ?: BigDecimal.ONE,
            formQuantityUnit.value,
            formPackageWorth.value,
            formPackageUnit.value
        )
        return true
    }

    fun changeCurrency(currency: Currency?) {
        if (formPriceCurrencyMutable.hasFixedValue)
            return
        formPriceCurrencyMutable.value = Fixable(currency)
    }

    fun clearQuestion() {
        question.value = null
    }

    fun formNameChanged(name: String) {
        formNameMutable.value = name
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                return InvoiceItemFormViewModel(
                    SabadDeps.unitRepo(),
                    SabadDeps.categoryRepo(),
                    SabadDeps.itemRepo(),
                    SabadDeps.productRepo(),
                    SabadDeps.photoRepo(),
                    SabadDeps.barcodeRepo(),
                    SabadDeps.priceRepo()
                ) as T
            }
        }
    }
}

private fun Barcode.extracted(): ExtractedBarcode {
    return ExtractedBarcode(textual, BarcodeFormat.valueOf(format))
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
    val title: String,
    val message: String,
    val icon: Any,
    val buttons: List<QuestionButton>,
    val options: List<QuestionOption>
)

data class QuestionButton(val label: String, val onClicked: () -> kotlin.Unit)
data class QuestionOption(val label: String, val tag: String)


data class ProductPhotoItem(val path: String, val record: Photo?)