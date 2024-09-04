package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.Group.GroupEntity;

public class GroupBoxItemComponent extends GroupBoxItemComponentParent implements IEntityView<GroupEntity>
{
	private GroupEntity TheEntity;

	public GroupBoxItemComponent(Context context) {
		super(context);
	}

	public GroupBoxItemComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GroupBoxItemComponent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GroupBoxItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void DisplayEntity(GroupEntity entity) {
		TheEntity = entity;
		NameTextView().setText(entity.DisplayableName);
	}
}
