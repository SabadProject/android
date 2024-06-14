package farayan.sabad.core.OnePlace.product;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class ProductParams extends SabadParamsBase<ProductEntity>
{
	public TextFilter DisplayableName;

	public TextFilter QueryableName;

	public TextFilter PhotoLocation;

	public ComparableFilter<Boolean> Deleted;

	public EntityFilter<GroupEntity> Purchasable;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(ProductSchema.DisplayableName, () -> DisplayableName),
				new PropertyFilter(ProductSchema.QueryableName, () -> QueryableName),
				new PropertyFilter(ProductSchema.PhotoLocation, () -> PhotoLocation),
				new PropertyFilter(ProductSchema.Group, () -> Purchasable)
		);
	}
}
