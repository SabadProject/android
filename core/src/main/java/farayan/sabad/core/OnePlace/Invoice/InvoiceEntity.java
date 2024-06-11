package farayan.sabad.core.OnePlace.Invoice;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = InvoiceSchema.Invoices)
public class InvoiceEntity extends SabadEntityBase<InvoiceEntity>
{
	@DatabaseField(columnName = InvoiceSchema.Deleted)
	public boolean Deleted;

	@DatabaseField(columnName = InvoiceSchema.Instant)
	public long Instant;

	@DatabaseField(columnName = InvoiceSchema.Discount)
	public double Discount;

	@DatabaseField(columnName = InvoiceSchema.Total)
	public double Total;

	@DatabaseField(columnName = InvoiceSchema.Seller, foreign = true)
	public StoreEntity Seller;

	@DatabaseField(columnName = InvoiceSchema.ItemsCountSum)
	public int ItemsCountSum;

	@DatabaseField(columnName = InvoiceSchema.ItemsQuantitySum)
	public int ItemsQuantitySum;

	@DatabaseField(columnName = InvoiceSchema.ItemsPriceSum)
	public int ItemsPriceSum;

	@Override
	public boolean NeedsRefresh() {
		return Instant == 0;
	}
}
