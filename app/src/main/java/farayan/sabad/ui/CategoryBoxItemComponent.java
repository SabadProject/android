package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.Category.CategoryEntity;

public class CategoryBoxItemComponent extends CategoryBoxItemComponentParent implements IEntityView<CategoryEntity>
{
	private CategoryEntity TheEntity;

	public CategoryBoxItemComponent(Context context) {
		super(context);
	}

	public CategoryBoxItemComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CategoryBoxItemComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CategoryBoxItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void DisplayEntity(CategoryEntity entity) {
		TheEntity = entity;
		NameTextView().setText(entity.DisplayableName);
	}
}
