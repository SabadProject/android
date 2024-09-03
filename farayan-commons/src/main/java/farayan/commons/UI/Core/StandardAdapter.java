package farayan.commons.UI.Core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * این آداپتور، آداپتور پایه است که برای نمایش موجودیت‌های مختلف با کمترین تغییر و کد می‌توان از آن استفاده کرد
 *
 * @param <TValue> نوع موجودیت که باید IEntity را پیاده‌سازی کرده باشد
 */
public abstract class StandardAdapter<TValue> extends BaseAdapter
{
	final List<TValue> allItems = new ArrayList<>();
	final List<TValue> filteredItems = new ArrayList<>();
	protected final Context Ctx;

	public StandardAdapter(Context ctx, Collection<? extends TValue> items) {
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

	public TValue getValue(int position) {
		return filteredItems.get(position);
	}

	protected boolean emptyItemSupported() {
		return false;
	}

	@Override
	public long getItemId(int position) {
		TValue value = filteredItems.get(position);
		return getValueID(value);
	}

	protected abstract long getValueID(TValue value);

	public TValue getItemByID(int id) {
		for (TValue item : allItems) {
			if (getValueID(item) == id)
				return item;
		}
		return null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = (View) NewView(Ctx);
		IDisplay<TValue> display = (IDisplay<TValue>) convertView;
		display.Display(filteredItems.get(position));
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = (View) NewDropDownView(Ctx);
		IDisplay<TValue> display = (IDisplay<TValue>) convertView;
		display.Display(filteredItems.get(position));
		return convertView;
	}


	protected abstract IDisplay<TValue> NewView(Context ctx);

	protected IDisplay<TValue> NewDropDownView(Context ctx) {
		return NewView(ctx);
	}

	public void addItem(TValue item) {
		if (item == null)
			return;
		allItems.add(item);
		filteredItems.add(item);
	}

	public int getItemPositionByID(int id) {
		for (int index = 0; index < getCount(); index++) {
			if (getValue(index) != null && getValueID(getValue(index)) == id)
				return index;
		}
		return -1;
	}
}