package farayan.sabad.core.OnePlace.ProductBarcode;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import farayan.sabad.core.OnePlace.product.ProductEntity;
import farayan.sabad.core.base.SabadEntityBase;

@DatabaseTable(tableName = ProductBarcodeSchema.ProductBarcodes)
public class ProductBarcodeEntity extends SabadEntityBase<ProductBarcodeEntity>
{

	@DatabaseField(columnName = ProductBarcodeSchema.BitmapResultPoints)
	public String BitmapResultPoints;

	@DatabaseField(columnName = ProductBarcodeSchema.BitmapScaleFactor)
	public int BitmapScaleFactor;

	@DatabaseField(columnName = ProductBarcodeSchema.BitmapFile)
	public String BitmapFile;

	@DatabaseField(columnName = ProductBarcodeSchema.Text)
	public String Text;

	@DatabaseField(columnName = ProductBarcodeSchema.Format)
	public BarcodeFormats Format;

	@DatabaseField(columnName = ProductBarcodeSchema.Product, foreign = true)
	public ProductEntity Product;

	public ProductBarcodeEntity() {

	}


	public ProductBarcodeEntity(String barcode, ProductEntity product) {
		Text = barcode;
		Product = product;
	}

	@Override
	public boolean NeedsRefresh() {
		return Text == null;
	}
}
