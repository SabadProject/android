package farayan.sabad.model.product_barcode

import android.content.Context
import android.graphics.Bitmap
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.journeyapps.barcodescanner.BarcodeResult
import farayan.commons.Exceptions.Rexception
import farayan.commons.PersianCalendar
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.commons.QueryBuilderCore.EntityFilter
import farayan.commons.QueryBuilderCore.EnumFilter
import farayan.commons.QueryBuilderCore.PropertyValueConditionModes
import farayan.commons.QueryBuilderCore.RelationalOperators
import farayan.commons.QueryBuilderCore.TextFilter
import farayan.commons.QueryBuilderCore.TextMatchModes
import farayan.sabad.SabadTheApp
import farayan.sabad.core.model.product.ProductEntity
import farayan.sabad.utility.hasValue
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Arrays
import java.util.stream.Collectors

class ProductBarcodeRepo : IProductBarcodeRepo {
    override fun DAO(): RuntimeExceptionDao<ProductBarcodeEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(ProductBarcodeEntity::class.java)
    }

    override fun NewParams(): BaseParams<ProductBarcodeEntity> {
        return ProductBarcodeParams()
    }

    override fun ByProduct(product: ProductEntity): ProductBarcodeEntity? {
        val params = ProductBarcodeParams()
        params.Product = EntityFilter(product)
        return First(params)
    }

    override fun EnsureBarcodeRegistered(
        context: Context,
        barcodeResult: BarcodeResult,
        productEntity: ProductEntity
    ): ProductBarcodeEntity {
        val params = ProductBarcodeParams()
        params.Product = EntityFilter(productEntity)
        params.Text = TextFilter(barcodeResult.text, TextMatchModes.Exactly)
        params.Format = EnumFilter(barcodeResult.barcodeFormat)
        var entity = First(params)
        if (entity == null) {
            entity = New(context, barcodeResult, productEntity)
            Save(entity)
        }
        return entity
    }

    private fun New(
        context: Context,
        barcodeResult: BarcodeResult,
        product: ProductEntity
    ): ProductBarcodeEntity {
        val entity = ProductBarcodeEntity()
        entity.value = barcodeResult.text
        entity.format = barcodeResult.barcodeFormat
        entity.product = product
        val now = PersianCalendar(System.currentTimeMillis())
        val path = String.format(
            "/Barcodes/%s/%s/%s/",
            now.persianYear,
            now.persianMonthIndexFrom1,
            now.persianDay
        )
        val folder = context.getExternalFilesDir(path)
        val fileName = String.format(
            "%s-%s",
            barcodeResult.barcodeFormat,
            barcodeResult.text
        )
        return try {
            if (!folder!!.exists()) folder.mkdirs()
            var file = File("$folder$fileName.jpg")
            if (file.exists()) {
                file = File(
                    "$folder$fileName-" + PersianCalendar().getPersianDateTimeConcatenated(
                        ""
                    ) + ".jpg"
                )
            }
            file.createNewFile()
            val fileOutputStream = FileOutputStream(file)
            val compress =
                barcodeResult.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            entity.bitmapFile = folder.toString() + fileName
            entity.bitmapScaleFactor = barcodeResult.bitmapScaleFactor
            entity.bitmapResultPoints = Arrays
                .stream(barcodeResult.resultPoints)
                .map { p -> "${p.x}:${p.y}" }
                .collect(Collectors.joining(","))
            entity
        } catch (e: IOException) {
            throw Rexception(e, "Unable to persist image of barcode")
        }
    }

    override fun EnsureBarcodeRegistered(
        barcode: String,
        productEntity: ProductEntity
    ): ProductBarcodeEntity {
        val params = ProductBarcodeParams()
        params.Product = EntityFilter(productEntity)
        params.Text = TextFilter(barcode, TextMatchModes.Exactly)
        params.Format = EnumFilter(
            null,
            PropertyValueConditionModes.ProvidedValueOrNull,
            RelationalOperators.IsNull
        )
        var entity = First(params)
        if (entity == null) {
            entity = ProductBarcodeEntity().apply {
                value = barcode
                product = productEntity
            }
            Save(entity)
        }
        return entity
    }

    override fun ByBarcode(barcodeResult: QueryableBarcode): ProductEntity? {
        val params = ProductBarcodeParams()
        params.Text = TextFilter(barcodeResult.value, TextMatchModes.Exactly)
        params.Format = EnumFilter(barcodeResult.format)
        val entity = First(params)
        return entity?.product
    }

    override fun byBarcode(barcodeResult: QueryableBarcode): List<ProductEntity> {
        val params = ProductBarcodeParams()
        params.Text = TextFilter(barcodeResult.value, TextMatchModes.Exactly)
        params.Format = EnumFilter(barcodeResult.format)
        val entities = All(params)
        return entities?.filter { it.hasValue }?.map { it.product!! } ?: listOf()
    }
}