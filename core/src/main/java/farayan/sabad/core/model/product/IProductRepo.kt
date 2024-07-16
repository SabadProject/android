package farayan.sabad.core.model.product

import farayan.commons.QueryBuilderCore.IRepo

interface IProductRepo : IRepo<ProductEntity> {
    fun byBarcode(barcode: String): List<ProductEntity>
}