package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.product.ProductEntity;

public class ProductBoxItemComponent extends ProductBoxItemComponentParent implements IEntityView<ProductEntity>
{
	private ProductEntity TheEntity;

	public ProductBoxItemComponent(Context context) {
		super(context);
	}

	public ProductBoxItemComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProductBoxItemComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ProductBoxItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void DisplayEntity(ProductEntity entity) {
		TheEntity = entity;
		NameTextView().setText(TheEntity.DisplayableName);
	}
}
