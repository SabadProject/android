package farayan.sabad.models.CategoryGroup

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.QueryBuilderCore.BaseParams
import farayan.commons.QueryBuilderCore.EntityFilter
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Category.CategoryEntity
import farayan.sabad.core.OnePlace.CategoryGroup.CategoryGroupEntity
import farayan.sabad.core.OnePlace.CategoryGroup.CategoryGroupParams
import farayan.sabad.core.OnePlace.CategoryGroup.ICategoryGroupRepo
import farayan.sabad.core.OnePlace.Group.GroupEntity
import java.util.stream.Collectors

class CategoryGroupRepo : ICategoryGroupRepo {
    override fun DAO(): RuntimeExceptionDao<CategoryGroupEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(CategoryGroupEntity::class.java)
    }

    override fun NewParams(): BaseParams<CategoryGroupEntity> {
        return CategoryGroupParams()
    }

    override fun EnsureRelated(Group: GroupEntity, category: CategoryEntity): CategoryGroupEntity {
        val params = CategoryGroupParams()
        params.Group = EntityFilter(Group)
        params.Category = EntityFilter(category)
        var entity = First(params)
        if (entity != null) return entity
        entity = CategoryGroupEntity(Group, category)
        Save(entity)
        return entity
    }

    override fun CategoriesOf(Group: GroupEntity): List<CategoryEntity> {
        val params = CategoryGroupParams()
        params.Group = EntityFilter(Group)
        return All(params)!!.stream().map { x: CategoryGroupEntity -> x.Category }.collect(Collectors.toList())
    }
}