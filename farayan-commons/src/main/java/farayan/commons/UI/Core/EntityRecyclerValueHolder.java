package farayan.commons.UI.Core;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import farayan.commons.QueryBuilderCore.IEntity;

public class EntityRecyclerValueHolder<EntityType extends IEntity> extends RecyclerView.ViewHolder implements IEntityView<EntityType> {
	IEntityView<EntityType> displayer;

	public EntityRecyclerValueHolder(IEntityView<EntityType> itemView, EntityRecyclerValueHolderFull full) {
		super((View) itemView);
		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
				full == EntityRecyclerValueHolderFull.Width ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT,
				full == EntityRecyclerValueHolderFull.Height ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT
		);
		((View) itemView).setLayoutParams(lp);
		setDisplayer(itemView);
	}

	public EntityRecyclerValueHolder(IEntityView<EntityType> itemView) {
		this(itemView, EntityRecyclerValueHolderFull.Width);
	}

	public IEntityView<EntityType> getDisplayer() {
		return displayer;
	}

	public void setDisplayer(IEntityView<EntityType> displayer) {
		this.displayer = displayer;
	}

	public void DisplayEntity(EntityType entity) {
		getDisplayer().DisplayEntity(entity);
	}
}
