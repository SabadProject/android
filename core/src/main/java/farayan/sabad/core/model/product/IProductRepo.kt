package farayan.sabad.core.model.product

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.product.ProductEntity

interface IProductRepo : IRepo<ProductEntity>{
    fun byBarcode(barcode: String): List<ProductEntity>
}