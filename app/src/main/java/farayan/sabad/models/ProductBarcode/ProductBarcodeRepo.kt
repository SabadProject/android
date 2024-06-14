package farayan.sabad.models.ProductBarcode

import android.content.Context
import android.graphics.Bitmap
import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.Exceptions.Rexception
import farayan.commons.PersianCalendar
import farayan.commons.QueryBuilderCore.*
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.product.ProductEntity
import farayan.sabad.core.OnePlace.ProductBarcode.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
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

    override fun EnsureBarcodeRegistered(context: Context, barcodeResult: CapturedBarcode, productEntity: ProductEntity): ProductBarcodeEntity {
        val params = ProductBarcodeParams()
        params.Product = EntityFilter(productEntity)
        params.Text = TextFilter(barcodeResult.text, TextMatchModes.Exactly)
        params.Format = EnumFilter(barcodeResult.format)
        var entity = First(params)
        if (entity == null) {
            entity = New(context, barcodeResult, productEntity)
            Save(entity)
        }
        return entity
    }

    private fun New(context: Context, barcodeResult: CapturedBarcode, product: ProductEntity): ProductBarcodeEntity {
        val entity = ProductBarcodeEntity()
        entity.Text = barcodeResult.text
        entity.Format = barcodeResult.format
        entity.Product = product
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
                barcodeResult.format,
                barcodeResult.text
        )
        return try {
            if (!folder!!.exists()) folder.mkdirs()
            var file = File("$folder$fileName.jpg")
            if (file.exists()) {
                file = File(folder.toString() + fileName + "-" + PersianCalendar().getPersianDateTimeConcatenated("") + ".jpg")
            }
            file.createNewFile()
            val fileOutputStream = FileOutputStream(file)
            val compress = barcodeResult.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            entity.BitmapFile = folder.toString() + fileName
            entity.BitmapScaleFactor = barcodeResult.bitmapScaleFactor
            entity.BitmapResultPoints = Arrays
                    .stream(barcodeResult.resultPoints)
                    .map { point: BarcodePoint -> point.x.toString() + ":" + point.y.toString() }
                    .collect(Collectors.joining(","))
            entity
        } catch (e: IOException) {
            throw Rexception(e, "Unable to persist image of barcode")
        }
    }

    override fun EnsureBarcodeRegistered(barcode: String, productEntity: ProductEntity): ProductBarcodeEntity {
        val params = ProductBarcodeParams()
        params.Product = EntityFilter(productEntity)
        params.Text = TextFilter(barcode, TextMatchModes.Exactly)
        params.Format = EnumFilter(null, PropertyValueConditionModes.ProvidedValueOrNull, RelationalOperators.IsNull)
        var entity = First(params)
        if (entity == null) {
            entity = ProductBarcodeEntity(barcode, productEntity)
            Save(entity)
        }
        return entity
    }

    override fun ByBarcode(barcodeResult: CapturedBarcode): ProductEntity? {
        val params = ProductBarcodeParams()
        params.Text = TextFilter(barcodeResult.text, TextMatchModes.Exactly)
        params.Format = EnumFilter(barcodeResult.format)
        val entity = First(params)
        return entity?.Product
    }

    override fun byBarcode(barcodeResult: CapturedBarcode): List<ProductEntity> {
        val params = ProductBarcodeParams()
        params.Text = TextFilter(barcodeResult.text, TextMatchModes.Exactly)
        params.Format = EnumFilter(barcodeResult.format)
        val entities = All(params)
        return entities?.map { it.Product } ?: listOf()
    }
}