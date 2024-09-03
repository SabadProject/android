package farayan.commons.UI.Core;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.IEntity;


/**
 * این آداپتور، آداپتور پایه است که برای نمایش موجودیت‌های مختلف با کمترین تغییر و کد می‌توان از آن استفاده کرد
 *
 * @param <TEntity> نوع موجودیت که باید IEntity را پیاده‌سازی کرده باشد
 */
public abstract class EntityPlusRecyclerViewAdapter<TEntity extends IEntity, TPlus>
        extends RecyclerView.Adapter<EntityPlusRecyclerValueHolder<TEntity, TPlus>> {
    private final List<TEntity> allItems = new ArrayList<>();
    private final List<TEntity> filteredItems = new ArrayList<>();
    private TPlus plus;
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

    public EntityPlusRecyclerViewAdapter(Context ctx, BaseParams<TEntity> baseParams, TPlus plusValue) {
        this.Ctx = ctx;
        if (baseParams == null)
            throw new RuntimeException("baseParams is null");
        allItems.addAll(baseParams.List());
        filteredItems.addAll(allItems);
        plus = plusValue;
    }

    public EntityPlusRecyclerViewAdapter(Context ctx, Collection<? extends TEntity> items, TPlus plusValue) {
        this.Ctx = ctx;
        if (items == null)
            throw new RuntimeException("items is null");
        allItems.addAll(items);
        filteredItems.addAll(allItems);
        plus = plusValue;
    }

    public TEntity getIEntity(int position) {
        return filteredItems.get(position);
    }

    @Override
    public void onBindViewHolder(EntityPlusRecyclerValueHolder<TEntity, TPlus> holder, int position) {
        holder.DisplayEntity(getIEntity(position), plus);
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
     * @param item شی‌ مد نظر
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

    public void Reload(List<TEntity> data, TPlus plus) {
        this.plus = plus;
        allItems.clear();
        allItems.addAll(data);
        filteredItems.clear();
        filteredItems.addAll(data);
        notifyDataSetChanged();
    }
}