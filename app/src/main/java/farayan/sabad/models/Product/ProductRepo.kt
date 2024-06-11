package farayan.sabad.models.Product

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Product.IProductRepo
import farayan.sabad.core.OnePlace.Product.ProductEntity
import farayan.sabad.core.OnePlace.Product.ProductParams

class ProductRepo : IProductRepo {
    override fun DAO(): RuntimeExceptionDao<ProductEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(ProductEntity::class.java)
    }

    override fun NewParams(): BaseParams<ProductEntity> {
        return ProductParams();
    }
}