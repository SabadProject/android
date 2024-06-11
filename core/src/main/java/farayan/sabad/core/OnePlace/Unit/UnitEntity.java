package farayan.sabad.core.OnePlace.Unit;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = UnitSchema.Units)
public class UnitEntity extends SabadEntityBase<UnitEntity> implements IBoxEntity
{
	@DatabaseField(columnName = UnitSchema.DisplayableName)
	public String DisplayableName;

	@DatabaseField(columnName = UnitSchema.QueryableName)
	public String QueryableName;

	@DatabaseField(columnName = UnitSchema.Category)
	public String Category;

	@DatabaseField(columnName = UnitSchema.Coefficient)
	public double Coefficient;

	public UnitEntity() {
	}

	public UnitEntity(String name, String category, double coefficient) {
		DisplayableName = FarayanUtility.Displayable(name);
		QueryableName = FarayanUtility.Queryable(name);
		Category = category;
		Coefficient = coefficient;
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
		QueryableName = FarayanUtility.Queryable(title);
		DisplayableName = FarayanUtility.Displayable(title);
	}
}
