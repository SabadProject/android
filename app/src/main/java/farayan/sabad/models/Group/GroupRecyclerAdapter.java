package farayan.sabad.models.Group;

import android.content.Context;
import android.view.ViewGroup;

import java.util.Collection;

import farayan.commons.Commons.Rial;
import farayan.commons.UI.Core.EntityRecyclerViewAdapter;
import farayan.commons.UI.Core.IEntityView;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.ui.GroupHomeItemComponent;

public class GroupRecyclerAdapter extends EntityRecyclerViewAdapter<GroupEntity>
{
	private final IGenericEvent<GroupEntity> OnNeededChanged;
	private final IGenericEvent<GroupEntity> OnPickedChanged;
	private final IGenericEvent<GroupHomeItemComponent> OnPicked;
	private final IGenericEvent<GroupEntity> OnRemoved;
	private final Rial.Coefficients FixedCoefficient;

	public GroupRecyclerAdapter(
			Context ctx,
			Collection<? extends GroupEntity> items,
			IGenericEvent<GroupEntity> onNeededChanged,
			IGenericEvent<GroupEntity> onPickedChanged,
			IGenericEvent<GroupHomeItemComponent> onPicked,
			IGenericEvent<GroupEntity> onRemoved,
			Rial.Coefficients fixedCoefficient) {
		super(ctx, items);
		OnNeededChanged = onNeededChanged;
		OnPickedChanged = onPickedChanged;
		OnPicked = onPicked;
		OnRemoved = onRemoved;
		FixedCoefficient = fixedCoefficient;
	}

	@Override
	protected IEntityView<GroupEntity> NewView(ViewGroup parent, int viewGroup) {
		return new GroupHomeItemComponent(
				Ctx,
				OnNeededChanged,
				OnPickedChanged,
				OnPicked,
				OnRemoved,
				FixedCoefficient
		);
	}
}
