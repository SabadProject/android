package farayan.sabad.models.Category

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Category.CategoryEntity
import farayan.sabad.core.OnePlace.Category.CategoryParams
import farayan.sabad.core.OnePlace.Category.ICategoryRepo

class CategoryRepo : ICategoryRepo {
    override fun DAO(): RuntimeExceptionDao<CategoryEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(CategoryEntity::class.java)
    }

    override fun NewParams(): BaseParams<CategoryEntity> {
        return CategoryParams();
    }
}