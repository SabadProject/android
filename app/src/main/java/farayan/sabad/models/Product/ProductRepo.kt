package farayan.sabad.models.Product

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.product.ProductEntity
import farayan.sabad.core.OnePlace.product.ProductParams
import farayan.sabad.core.model.product.IProductRepo

class ProductRepo : IProductRepo {
    override fun byBarcode(barcode: String): List<ProductEntity> {
        val dao = DAO()
        val qb = dao.queryBuilder()
        val where = qb.where()
        where.raw("barcode = '$barcode'")
        qb.setWhere(where)
        return qb.query()
    }

    override fun DAO(): RuntimeExceptionDao<ProductEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(ProductEntity::class.java)
    }

    override fun NewParams(): BaseParams<ProductEntity> {
        return ProductParams()
    }
}