package farayan.commons.UI.Core;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import farayan.commons.FarayanUtility;

public abstract class StandardFilterableAdapter<TValue> extends StandardAdapter<TValue> implements Filterable
{
	public StandardFilterableAdapter(Context ctx, Collection<? extends TValue> items) {
		super(ctx, items);
	}

	@Override
	public Filter getFilter() {
		return new Filter()
		{
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				List<TValue> dataFiltered = new ArrayList<>();
				if (FarayanUtility.IsNullOrEmpty(constraint)) {
					results.values = allItems;
					results.count = allItems.size();
				} else {
					for (TValue item : allItems) {
						if (item == null)
							continue;
						if (isFilterMatched(item, constraint)) {
							dataFiltered.add((TValue) item);
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
				filteredItems.addAll(results == null || results.values == null ? Collections.emptyList() : (List<TValue>) results.values);
				notifyDataSetChanged();
			}
		};
	}

	protected abstract boolean isFilterMatched(TValue item, CharSequence constraint);
}
