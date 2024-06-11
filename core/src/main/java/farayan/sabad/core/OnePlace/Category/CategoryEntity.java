package farayan.sabad.core.OnePlace.Category;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.sabad.core.base.*;

@DatabaseTable(tableName = CategorySchema.Categories)
public class CategoryEntity extends SabadEntityBase<CategoryEntity> implements IBoxEntity
{
	@DatabaseField(columnName = CategorySchema.DisplayableName)
	public String DisplayableName;

	@DatabaseField(columnName = CategorySchema.QueryableName)
	public String QueryableName;

	public CategoryEntity() {

	}

	public CategoryEntity(String name) {
		setTitle(name);
	}

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
