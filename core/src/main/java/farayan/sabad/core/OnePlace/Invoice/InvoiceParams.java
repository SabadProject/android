package farayan.sabad.core.OnePlace.Invoice;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class InvoiceParams extends SabadParamsBase<InvoiceEntity>
{
	public ComparableFilter<Boolean> Deleted;
	public ComparableFilter<Long> Instant;
	public ComparableFilter<Double> Discount;
	public ComparableFilter<Double> Total;
	public EntityFilter<StoreEntity> Seller;
	public ComparableFilter<Integer> ItemsCountSum;
	public ComparableFilter<Integer> ItemsQuantitySum;
	public ComparableFilter<Integer> ItemsPriceSum;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(InvoiceSchema.Deleted, () -> Deleted),
				new PropertyFilter(InvoiceSchema.Instant, () -> Instant),
				new PropertyFilter(InvoiceSchema.Discount, () -> Discount),
				new PropertyFilter(InvoiceSchema.Total, () -> Total),
				new PropertyFilter(InvoiceSchema.Seller, () -> Seller),
				new PropertyFilter(InvoiceSchema.ItemsCountSum, () -> ItemsCountSum),
				new PropertyFilter(InvoiceSchema.ItemsQuantitySum, () -> ItemsQuantitySum),
				new PropertyFilter(InvoiceSchema.ItemsPriceSum, () -> ItemsPriceSum)
		);
	}
}
