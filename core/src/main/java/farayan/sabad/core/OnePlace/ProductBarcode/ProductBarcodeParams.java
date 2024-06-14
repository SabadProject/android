package farayan.sabad.core.OnePlace.ProductBarcode;


import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.EnumFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.sabad.core.OnePlace.product.ProductEntity;
import farayan.sabad.core.base.SabadParamsBase;

public class ProductBarcodeParams extends SabadParamsBase<ProductBarcodeEntity>
{
	public TextFilter Text;

	public EnumFilter<BarcodeFormats> Format;

	public EntityFilter<ProductEntity> Product;

	@Override
	public PropertyFilter[] Filters() {
		return ArrayUtils.addAll(
				super.Filters(),
				new PropertyFilter(ProductBarcodeSchema.Text, () -> Text),
				new PropertyFilter(ProductBarcodeSchema.Format, () -> Format),
				new PropertyFilter(ProductBarcodeSchema.Product, () -> Product)
		);
	}
}
