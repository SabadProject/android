package farayan.commons.UI.Core;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.IEntity;

public abstract class FilterableEntityAdapter<TEntity extends IEntity & IBoxEntity> extends EntityAdapter<TEntity> implements Filterable
{
	public FilterableEntityAdapter(Context ctx, Collection<? extends TEntity> items) {
		super(ctx, items);
	}

	@Override
	public Filter getFilter() {
		return new Filter()
		{
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<TEntity> dataFiltered = new ArrayList<>();
				if (FarayanUtility.IsNullOrEmpty(constraint)) {
					results.values = allItems;
					results.count = allItems.size();
				} else {
					for (IBoxEntity item : allItems) {
						if (item == null)
							continue;
						if (item.isFilterMatched(constraint)) {
							dataFiltered.add((TEntity) item);
						}
					}
					results.values = dataFiltered;
					results.count = dataFiltered.size();
				}
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				filteredItems.clear();
				filteredItems.addAll(results == null || results.values == null ? Collections.emptyList() : (List<TEntity>) results.values);
				notifyDataSetChanged();
			}
		};
	}
}
