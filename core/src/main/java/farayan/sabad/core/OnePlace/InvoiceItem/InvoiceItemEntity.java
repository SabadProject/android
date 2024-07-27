package farayan.sabad.core.OnePlace.InvoiceItem;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.core.base.SabadEntityBase;
import farayan.sabad.core.model.product.ProductEntity;
import farayan.sabad.core.model.unit.UnitEntity;


@DatabaseTable(tableName = InvoiceItemSchema.InvoiceItems)
public class InvoiceItemEntity extends SabadEntityBase<InvoiceItemEntity> {
    @DatabaseField(columnName = InvoiceItemSchema.Product, foreign = true)
    public ProductEntity Product;

    @DatabaseField(columnName = InvoiceItemSchema.Quantity)
    public BigDecimal Quantity;

    @DatabaseField(columnName = InvoiceItemSchema.Fee)
    public BigDecimal Fee;

    @DatabaseField(columnName = InvoiceItemSchema.Discount)
    public BigDecimal Discount;

    @DatabaseField(columnName = InvoiceItemSchema.Total)
    public BigDecimal Total;

    @DatabaseField(columnName = InvoiceItemSchema.Invoice, foreign = true)
    public InvoiceEntity Invoice;

    @DatabaseField(columnName = InvoiceItemSchema.Group, foreign = true)
    public GroupEntity Group;

    @DatabaseField(columnName = InvoiceItemSchema.Unit, foreign = true)
    public UnitEntity Unit;

    public InvoiceItemEntity() {

    }

    public InvoiceItemEntity(
            GroupEntity groupEntity,
            ProductEntity productEntity,
            BigDecimal quantity,
            BigDecimal fee,
            UnitEntity unitEntity
    ) {
        this.Group = groupEntity;
        this.Product = productEntity;
        this.Quantity = quantity;
        this.Fee = fee;
        this.Total = quantity.multiply(fee);
        this.Unit = unitEntity;
    }

    @Override
    public boolean NeedsRefresh() {
        return Group == null;
    }
}
