package farayan.sabad.core.OnePlace.Group;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = GroupSchema.Groups)
public class GroupEntity extends SabadEntityBase<GroupEntity> implements IBoxEntity
{
	@DatabaseField(columnName = GroupSchema.Category, foreign = true)
	public CategoryEntity Category;

	@DatabaseField(columnName = GroupSchema.UniqueKey)
	public String UniqueKey;

	@DatabaseField(columnName = GroupSchema.DisplayableName)
	public String DisplayableName;

	@DatabaseField(columnName = GroupSchema.QueryableName)
	public String QueryableName;

	@DatabaseField(columnName = GroupSchema.PhotoLocation)
	public String PhotoLocation;

	@DatabaseField(columnName = GroupSchema.DisplayableDescription)
	public String DisplayableDescription;

	@DatabaseField(columnName = GroupSchema.QueryableDescription)
	public String QueryableDescription;

	@DatabaseField(columnName = GroupSchema.Needed)
	public boolean Needed;

	@DatabaseField(columnName = GroupSchema.PurchasedCount)
	public int PurchasedCount;

	@DatabaseField(columnName = GroupSchema.Importance)
	public int Importance;

	@DatabaseField(columnName = GroupSchema.LastPurchase, foreign = true)
	public InvoiceItemEntity LastPurchase;

	@DatabaseField(columnName = GroupSchema.LastPurchased)
	public long LastPurchased;

	@DatabaseField(columnName = GroupSchema.Deleted)
	public boolean Deleted;

	@DatabaseField(columnName = GroupSchema.Item, foreign = true)
	public InvoiceItemEntity Item;

	@DatabaseField(columnName = GroupSchema.UnitPrefix)
	public String UnitPrefix;

	@DatabaseField(columnName = GroupSchema.Picked)
	public boolean Picked;

	@Override
	public String getTitle() {
		return DisplayableName;
	}

	@Override
	public void setTitle(String title) {
		DisplayableName = FarayanUtility.Displayable(title);
		QueryableName = FarayanUtility.Queryable(title);
	}

	@Override
	public boolean NeedsRefresh() {
		return DisplayableName == null;
	}

	public GroupEntity() {

	}


	public GroupEntity(String uniqueKey, String name, int importance, String unitPrefix) {
		UniqueKey = uniqueKey;
		Importance = importance;
		UnitPrefix = unitPrefix;
		setTitle(name);
	}

	@Override
	public String toString() {
		return "GroupEntity#" + ID + "$" + DisplayableName;
	}
}
