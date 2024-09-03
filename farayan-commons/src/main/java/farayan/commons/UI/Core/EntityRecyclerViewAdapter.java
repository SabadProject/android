package farayan.commons.UI.Core;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import farayan.commons.Exceptions.Rexception;
import farayan.commons.QueryBuilderCore.IEntity;
import farayan.commons.QueryBuilderCore.BaseParams;


/**
 * این آداپتور، آداپتور پایه است که برای نمایش موجودیت‌های مختلف با کمترین تغییر و کد می‌توان از آن استفاده کرد
 *
 * @param <TEntity> نوع موجودیت که باید IEntity را پیاده‌سازی کرده باشد
 */
public abstract class EntityRecyclerViewAdapter<TEntity extends IEntity>
		extends RecyclerView.Adapter<EntityRecyclerValueHolder<TEntity>>
{
	private final List<TEntity> allItems = new ArrayList<>();
	private final List<TEntity> filteredItems = new ArrayList<>();
	protected final Context Ctx;
	private RecyclerView theRecyclerView;

	protected RecyclerView getRecyclerView() {
		return theRecyclerView;
	}

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		theRecyclerView = recyclerView;
		super.onAttachedToRecyclerView(recyclerView);
	}

	@Deprecated
	public EntityRecyclerViewAdapter(Context ctx, BaseParams<TEntity> baseParams) {
		this.Ctx = ctx;
		if (baseParams == null)
			throw new RuntimeException("baseParams is null");

		if (specificRowsCount() > 0)
			allItems.addAll(baseParams.Paged(specificRowsStart(), specificRowsCount()));
		else
			allItems.addAll(baseParams.List());
		filteredItems.addAll(allItems);
	}

	protected int specificRowsCount() {
		return 0;
	}

	protected int specificRowsStart() {
		return 0;
	}

	public EntityRecyclerViewAdapter(Context ctx, Collection<? extends TEntity> items) {
		this.Ctx = ctx;
		if (items == null)
			throw new RuntimeException("items is null");
		allItems.addAll(items);
		filteredItems.addAll(allItems);
	}

	public TEntity getIEntity(int position) {
		return filteredItems.get(position);
	}

	@NonNull
	@Override
	public EntityRecyclerValueHolder<TEntity> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new EntityRecyclerValueHolder<>(NewView(parent, viewType));
	}

	protected abstract IEntityView<TEntity> NewView(ViewGroup parent, int viewType);

	@Override
	public void onBindViewHolder(EntityRecyclerValueHolder<TEntity> holder, int position) {
		holder.DisplayEntity(getIEntity(position));
	}

	@Override
	public long getItemId(int position) {
		return filteredItems.get(position).getID();
	}

	@Override
	public int getItemCount() {
		return filteredItems.size();
	}

	public TEntity getItemByID(int id) {
		for (TEntity item : allItems) {
			if (item.getID() == id)
				return item;
		}
		return null;
	}

	public void addItem(TEntity item) {
		if (item == null)
			return;
		allItems.add(item);
		filteredItems.add(item);
	}

	public int getItemPositionByID(int id) {
		for (int index = 0; index < getItemCount(); index++) {
			if (getIEntity(index) != null && getIEntity(index).getID() == id)
				return index;
		}
		return -1;
	}

	/**
	 * در صورتی که در فهرست اشیا، شی ارسالی وجود داشته باشد، مثبت، وگرنه، منفی را برمی‌گرداند
	 *
	 * @param id شی‌ مد نظر
	 * @return مثبت در صورت وجود شی، مثبت و منفی در صورت عدم وجود شی ارسالی
	 */
	public boolean has(TEntity item) {
		return has(item.getID());
	}

	/**
	 * در صورتی که در فهرست اشیا، شی‌ای با شناسه‌ی ارسالی وجود داشته باشد، مثبت، وگرنه، منفی را برمی‌گرداند
	 *
	 * @param id شناسه‌ی شی‌ مد نظر
	 * @return مثبت در صورت وجود شی‌ای با شناسه‌ی مد نظر و منفی در صورت عدم وجود شی‌ای با شناسه‌ی ارسالی
	 */
	public boolean has(int id) {
		for (int index = 0; index < getItemCount(); index++) {
			if (getIEntity(index) != null && getIEntity(index).getID() == id)
				return true;
		}
		return false;
	}

	public void Reload(List<TEntity> data) {
		allItems.clear();
		allItems.addAll(data);
		filteredItems.clear();
		filteredItems.addAll(data);
		notifyDataSetChanged();
	}
}