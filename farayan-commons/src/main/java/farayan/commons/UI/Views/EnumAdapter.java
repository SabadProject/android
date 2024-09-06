package farayan.commons.UI.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import farayan.commons.FarayanUtility;

public class EnumAdapter<
		EnumType extends Enum<EnumType> & IBoxEnum> extends BaseAdapter implements Filterable
{

	private final List<EnumType> allValues = new ArrayList<>();
	private final List<EnumType> filteredValues = new ArrayList<>();
	private final Context Ctx;
	private final IEnumBoxTextViewGenerator TextViewGenerator;
	private final String Hint;

	EnumAdapter(
			Context ctx,
			Collection<EnumType> values,
			String hint,
			IEnumBoxTextViewGenerator/*<SelectedType, SelectableType> */textViewGenerator
	) {
		this.Ctx = ctx;
		TextViewGenerator = textViewGenerator;
		this.allValues.addAll(values);
		this.Hint = hint;
		if (FarayanUtility.IsUsable(hint))
			this.filteredValues.add(0, null);
		this.filteredValues.addAll(allValues);
	}

	@Override
	public int getCount() {
		return filteredValues.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredValues.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = (View) TextViewGenerator.NewSelectedView(this.Ctx);
		IEnumBoxTextView selectedView = (IEnumBoxTextView) convertView;
		selectedView.setText(getItem(position) == null ? Hint : ((EnumType) getItem(position)).getText());
		//selectedView.setTextColor(getItem(position) == null ? HintColor : TextColor);
		return convertView;
	}

	public int ItemPosition(EnumType value) {
		for (int index = 0; index < getCount(); index++) {
			if (getItem(index) == value) {
				return index;
			}
		}
		throw new RuntimeException(String.format("Enum %s not found", value));
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = (View) TextViewGenerator.NewSelectableView(this.Ctx);
		IEnumBoxTextView selectableType = (IEnumBoxTextView) convertView;
		selectableType.setText(getItem(position) == null ? Hint : ((EnumType) getItem(position)).getText());
		//selectableType.setTextColor(getItem(position) == null ? HintColor : TextColor);
		return convertView;
	}

	@Override
	public Filter getFilter() {
		return new Filter()
		{
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				if (FarayanUtility.IsNullOrEmpty(constraint)) {
					results.values = allValues;
					results.count = allValues.size();
				} else {
					List<EnumType> found = new ArrayList<>();
					for (EnumType iBoxEnum : allValues) {
						if (iBoxEnum.getText().contains(constraint))
							found.add(iBoxEnum);
					}
					results.values = found;
					results.count = found.size();
				}
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				filteredValues.clear();
				filteredValues.addAll((List<EnumType>) results.values);
				notifyDataSetChanged();
			}
		};
	}
}