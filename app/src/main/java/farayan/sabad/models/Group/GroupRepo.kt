package farayan.sabad.models.Group

import com.j256.ormlite.dao.RuntimeExceptionDao
import farayan.commons.FarayanUtility
import farayan.commons.QueryBuilderCore.*
import farayan.sabad.SabadTheApp
import farayan.sabad.core.OnePlace.Group.*
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitParams
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemParams
import farayan.sabad.core.OnePlace.Product.IProductRepo
import farayan.sabad.core.OnePlace.Product.ProductParams

class GroupRepo : IGroupRepo {
    override fun New(name: String, description: String?, icon: String?, undelete: Boolean): GroupEntity {
        if (FarayanUtility.IsNullOrEmpty(name)) throw NewGroupNameNeededException()
        val groupEntity = FirstByName(name)
        if (groupEntity != null && groupEntity.Deleted && undelete) {
            groupEntity.Deleted = false
            Update(groupEntity)
            return groupEntity
        }
        val params = GroupParams()
        params.QueryableName = TextFilter(FarayanUtility.Queryable(name), TextMatchModes.Exactly)
        val count = Count(params)
        if (count > 0) {
            throw GroupUniqueNameNeededException()
        }
        val category = GroupEntity()
        category.DisplayableDescription = FarayanUtility.Displayable(description)
        category.DisplayableName = FarayanUtility.Displayable(name)
        category.QueryableDescription = FarayanUtility.Queryable(description)
        category.QueryableName = FarayanUtility.Queryable(name)
        category.Needed = true
        Save(category)
        return category
    }

    override fun Rename(Group: GroupEntity, name: String) {
        if (FarayanUtility.IsNullOrEmpty(name)) throw EmptyNameRexception()
        val groupParams = GroupParams()
        groupParams.QueryableName = TextFilter(FarayanUtility.Queryable(name), TextMatchModes.Exactly)
        groupParams.ID = ComparableFilter(Group.ID, RelationalOperators.NotEqual)
        groupParams.Deleted = ComparableFilter(false)
        val count = Count(groupParams)
        if (count > 0) throw DuplicatedNameRexception()
        Group.title = name
        Update(Group)
    }

    override fun Remove(invoiceItemRepo: IInvoiceItemRepo, productRepo: IProductRepo, GroupUnitRepo: IGroupUnitRepo, Group: GroupEntity) {
        val invoiceItemParams = InvoiceItemParams()
        invoiceItemParams.Purchasable = EntityFilter(Group)
        if (invoiceItemRepo.Count(invoiceItemParams) > 0) {
            Hide(Group)
            return
        }
        val productParams = ProductParams()
        productParams.Purchasable = EntityFilter(Group)
        if (productRepo.Count(productParams) > 0) {
            Hide(Group)
            return
        }
        val groupUnitParams = GroupUnitParams()
        groupUnitParams.Group = EntityFilter(Group)
        GroupUnitRepo.Delete(groupUnitParams)
        Delete(Group)
    }

    override fun FirstByName(name: String): GroupEntity? {
        var queryableName: String? = name
        queryableName = FarayanUtility.Queryable(queryableName)
        if (FarayanUtility.IsNullOrEmpty(queryableName)) return null
        val groupParams = GroupParams()
        groupParams.QueryableName = TextFilter(queryableName, TextMatchModes.Exactly)
        return First(groupParams)
    }

    override fun EmptyParamReturning(): EmptyParamReturns {
        return EmptyParamReturns.Everything
    }

    override fun DAO(): RuntimeExceptionDao<GroupEntity, Int> {
        return SabadTheApp.DB().getRuntimeExceptionDao(GroupEntity::class.java)
    }

    override fun NewParams(): BaseParams<GroupEntity> {
        return GroupParams();
    }

    private fun Hide(Group: GroupEntity) {
        Group.Deleted = true
        Update(Group)
    }
}