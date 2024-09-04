package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.Store.StoreEntity;

@AndroidEntryPoint
public class StoreBoxItemComponent extends StoreBoxItemComponentParent implements IEntityView<StoreEntity>
{

	private StoreEntity TheEntity;

	public StoreBoxItemComponent(Context context) {
		super(context);
	}

	public StoreBoxItemComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StoreBoxItemComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public StoreBoxItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void DisplayEntity(StoreEntity entity) {
		TheEntity = entity;
		NameTextView().setText(entity.DisplayableName);
	}
}
