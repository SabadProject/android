package farayan.commons.UI.Core;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import farayan.commons.QueryBuilderCore.IEntity;

public class EntityPlusRecyclerValueHolder<TEntity extends IEntity, TPlus>
		extends RecyclerView.ViewHolder
		implements IEntityPlusView<TEntity, TPlus>
{
	IEntityPlusView<TEntity, TPlus> displayer;

	public EntityPlusRecyclerValueHolder(IEntityPlusView<TEntity, TPlus> itemView, EntityRecyclerValueHolderFull full) {
		super((View) itemView);
		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
				full == EntityRecyclerValueHolderFull.Width ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT,
				full == EntityRecyclerValueHolderFull.Height ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT
		);
		((View) itemView).setLayoutParams(lp);
		setDisplayer(itemView);
	}

	public EntityPlusRecyclerValueHolder(IEntityPlusView<TEntity, TPlus> itemView) {
		this(itemView, EntityRecyclerValueHolderFull.Width);
	}

	public IEntityPlusView<TEntity, TPlus> getDisplayer() {
		return displayer;
	}

	public void setDisplayer(IEntityPlusView<TEntity, TPlus> displayer) {
		this.displayer = displayer;
	}

	@Override
	public void DisplayEntity(TEntity entity, TPlus plus) {
		getDisplayer().DisplayEntity(entity, plus);
	}
}
