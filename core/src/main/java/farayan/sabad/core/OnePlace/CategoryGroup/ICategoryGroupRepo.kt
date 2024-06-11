package farayan.sabad.core.OnePlace.CategoryGroup

import farayan.commons.QueryBuilderCore.IRepo
import farayan.sabad.core.OnePlace.Category.CategoryEntity
import farayan.sabad.core.OnePlace.Group.GroupEntity

interface ICategoryGroupRepo : IRepo<CategoryGroupEntity> {
    fun EnsureRelated(Group: GroupEntity, category: CategoryEntity): CategoryGroupEntity
    fun CategoriesOf(Group: GroupEntity): List<CategoryEntity>
}