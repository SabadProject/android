package farayan.sabad.core.OnePlace.InvoiceItem;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.core.OnePlace.product.ProductEntity;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class InvoiceItemParams extends SabadParamsBase<InvoiceItemEntity>
{
	public EntityFilter<ProductEntity> Product;

	public ComparableFilter<Double> Quantity;

	public ComparableFilter<Double> Fee;

	public ComparableFilter<Double> Discount;

	public ComparableFilter<Double> Total;

	public EntityFilter<InvoiceEntity> Invoice;

	public EntityFilter<GroupEntity> Purchasable;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(InvoiceItemSchema.Product, () -> Product),
				new PropertyFilter(InvoiceItemSchema.Quantity, () -> Quantity),
				new PropertyFilter(InvoiceItemSchema.Fee, () -> Fee),
				new PropertyFilter(InvoiceItemSchema.Discount, () -> Discount),
				new PropertyFilter(InvoiceItemSchema.Total, () -> Total),
				new PropertyFilter(InvoiceItemSchema.Invoice, () -> Invoice),
				new PropertyFilter(InvoiceItemSchema.Group, () -> Purchasable)
		);
	}
}
