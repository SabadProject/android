package farayan.sabad.model.product_barcode;


import com.google.zxing.BarcodeFormat;

import org.apache.commons.lang3.ArrayUtils;

import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.EnumFilter;
import farayan.commons.QueryBuilderCore.PropertyFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.sabad.core.base.SabadParamsBase;
import farayan.sabad.core.model.product.ProductEntity;

public class ProductBarcodeParams extends SabadParamsBase<ProductBarcodeEntity> {
    public TextFilter Text;

    public EnumFilter<BarcodeFormat> Format;

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
