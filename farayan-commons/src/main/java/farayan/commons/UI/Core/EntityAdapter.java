package farayan.commons.UI.Core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import farayan.commons.QueryBuilderCore.IEntity;
import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.IRepo;
import kotlin.Function;


/**
 * این آداپتور، آداپتور پایه است که برای نمایش موجودیت‌های مختلف با کمترین تغییر و کد می‌توان از آن استفاده کرد
 *
 * @param <EntityType> نوع موجودیت که باید IEntity را پیاده‌سازی کرده باشد
 */
public abstract class EntityAdapter<EntityType extends IEntity> extends BaseAdapter
{
	final List<EntityType> allItems = new ArrayList<>();
	final List<EntityType> filteredItems = new ArrayList<>();
	protected final Context Ctx;

	public EntityAdapter(Context ctx, Collection<? extends EntityType> items) {
		this.Ctx = ctx;
		if (items == null)
			throw new RuntimeException("items is null");
		allItems.addAll(items);
		filteredItems.addAll(allItems);
	}

	@Override
	public int getCount() {
		return filteredItems.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredItems.get(position);
	}

	public EntityType getIEntity(int position) {
		return filteredItems.get(position);
	}

	protected boolean emptyItemSupported() {
		return false;
	}

	@Override
	public long getItemId(int position) {
		EntityType entity = filteredItems.get(position);
		return entity == null ? -1 : entity.getID();
	}

	public EntityType getItemByID(int id) {
		for (EntityType item : allItems) {
			if (item.getID() == id)
				return item;
		}
		return null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = (View) NewView(Ctx);
		IEntityView<IEntity> iEntityView = (IEntityView<IEntity>) convertView;
		iEntityView.DisplayEntity(filteredItems.get(position));
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = (View) NewDropDownView(Ctx);
		IEntityView<IEntity> iEntityView = (IEntityView<IEntity>) convertView;
		iEntityView.DisplayEntity(filteredItems.get(position));
		return convertView;
	}


	protected abstract IEntityView<EntityType> NewView(Context ctx);

	protected IEntityView<EntityType> NewDropDownView(Context ctx) {
		return NewView(ctx);
	}

	public void addItem(EntityType item) {
		if (item == null)
			return;
		allItems.add(item);
		filteredItems.add(item);
	}

	public int getItemPositionByID(int id) {
		for (int i = 0; i < getCount(); i++) {
			if (getIEntity(i) != null && getIEntity(i).getID() == id)
				return i;
		}
		return -1;
	}
}