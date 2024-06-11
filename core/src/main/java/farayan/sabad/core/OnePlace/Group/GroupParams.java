package farayan.sabad.core.OnePlace.Group;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.base.SabadParamsBase;


public class GroupParams extends SabadParamsBase<GroupEntity>
{
	public TextFilter DisplayableName;

	public TextFilter QueryableName;

	public TextFilter PhotoLocation;

	public TextFilter DisplayableDescription;

	public TextFilter QueryableDescription;

	public ComparableFilter<Boolean> Needed;

	public ComparableFilter<Long> LastPurchase;

	public ComparableFilter<Boolean> Deleted = new ComparableFilter<>(false);

	public EntityFilter<InvoiceItemEntity> Item;

	public ComparableFilter<Boolean> Picked;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(GroupSchema.DisplayableName, () -> DisplayableName),
				new PropertyFilter(GroupSchema.QueryableName, () -> QueryableName),
				new PropertyFilter(GroupSchema.PhotoLocation, () -> PhotoLocation),
				new PropertyFilter(GroupSchema.DisplayableDescription, () -> DisplayableDescription),
				new PropertyFilter(GroupSchema.QueryableDescription, () -> QueryableDescription),
				new PropertyFilter(GroupSchema.Needed, () -> Needed),
				new PropertyFilter(GroupSchema.LastPurchase, () -> LastPurchase),
				new PropertyFilter(GroupSchema.Deleted, () -> Deleted),
				new PropertyFilter(GroupSchema.Item, () -> Item),
				new PropertyFilter(GroupSchema.Picked, () -> Picked)
		);
	}
}
