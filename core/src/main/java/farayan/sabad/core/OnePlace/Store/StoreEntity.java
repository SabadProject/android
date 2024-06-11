package farayan.sabad.core.OnePlace.Store;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = StoreSchema.Stores)
public class StoreEntity extends SabadEntityBase<StoreEntity> implements IBoxEntity
{
	@DatabaseField(columnName = StoreSchema.DisplayableName)
	public String DisplayableName;

	@DatabaseField(columnName = StoreSchema.QueryableName)
	public String QueryableName;

	@Override
	public boolean NeedsRefresh() {
		return DisplayableName == null;
	}

	@Override
	public String getTitle() {
		return DisplayableName;
	}

	@Override
	public void setTitle(String title) {
		DisplayableName = FarayanUtility.Displayable(title);
		QueryableName = FarayanUtility.Queryable(title);
	}
}
