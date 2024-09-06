package farayan.sabad.models.Group;

import android.content.Context;

import java.util.Collection;

import farayan.commons.UI.Core.FilterableEntityAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.ui.GroupBoxItemComponent;


public class GroupBoxAdapter extends FilterableEntityAdapter<GroupEntity>
{

	public GroupBoxAdapter(Context ctx, Collection<? extends GroupEntity> items) {
		super(ctx, items);
	}

	@Override
	protected IEntityView<GroupEntity> NewView(Context ctx) {
		return new GroupBoxItemComponent(ctx);
	}
}
