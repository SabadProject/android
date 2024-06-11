package farayan.sabad.core.OnePlace.Product;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import androidx.annotation.NonNull;
import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IBoxEntity;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadEntityBase;


@DatabaseTable(tableName = ProductSchema.Products)
public class ProductEntity extends SabadEntityBase<ProductEntity> implements IBoxEntity
{
	@DatabaseField(columnName = ProductSchema.DisplayableName)
	public String DisplayableName;

	@DatabaseField(columnName = ProductSchema.QueryableName)
	public String QueryableName;

	@DatabaseField(columnName = ProductSchema.PhotoLocation)
	public String PhotoLocation;

	@DatabaseField(columnName = ProductSchema.Deleted)
	public boolean Deleted;

	@DatabaseField(columnName = ProductSchema.Group, foreign = true)
	public GroupEntity Group;

	@DatabaseField(columnName = ProductSchema.LastPurchase, foreign = true)
	public InvoiceItemEntity LastPurchase;

	public ProductEntity() {

	}

	public ProductEntity(@NonNull String name, @NonNull GroupEntity groupEntity) {
		setTitle(name);
		Group = groupEntity;
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
