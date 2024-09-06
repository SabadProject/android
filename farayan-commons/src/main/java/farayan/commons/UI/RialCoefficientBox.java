package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.stream.Collectors;

import farayan.commons.Commons.Rial;
import farayan.commons.UI.Core.IDisplay;
import farayan.commons.UI.Core.StandardFilterableAdapter;

public class RialCoefficientBox extends androidx.appcompat.widget.AppCompatSpinner
{
	private RialCoefficientAdapter adapter;

	public RialCoefficientBox(Context context) {
		super(context);
		InitializeLayout();
	}

	public RialCoefficientBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitializeLayout();
	}

	public RialCoefficientBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		InitializeLayout();
	}

	protected void Reload(int itemGravity) {
		adapter = new RialCoefficientAdapter(getContext(), new RialItemComponent.Config(itemGravity));
		setAdapter(adapter);
		//setText(Rial.Coefficients.HezarTooman.NameResID);
		setSelection(Rial.Coefficients.HezarTooman.ordinal());
	}

	protected CharSequence textValue(Object selectedItem) {
		if (selectedItem instanceof Rial.Coefficients)
			return getContext().getResources().getString(((Rial.Coefficients) selectedItem).NameResID);
		return "NOT Rial.Coefficients";
	}

	protected void InitializeLayout() {
	}

	public Rial.Coefficients getValue() {
		return Rial.Coefficients.values()[getSelectedItemPosition()];
		/*if (getText() == null)
			return null;
		String typed = getText().toString();
		return Arrays
				.stream(Rial.Coefficients.values())
				.filter(x -> getContext().getResources().getString(x.NameResID).equalsIgnoreCase(typed))
				.findFirst()
				.orElse(null)
				;*/
	}

	class RialCoefficientAdapter extends StandardFilterableAdapter<Rial.Coefficients>
	{

		private final RialItemComponent.Config itemConfig;

		public RialCoefficientAdapter(Context ctx, RialItemComponent.Config itemConfig) {
			super(ctx, Arrays.stream(Rial.Coefficients.values()).sorted(Rial.Coefficients::Compared).collect(Collectors.toList()));
			this.itemConfig = itemConfig;
		}

		@Override
		protected long getValueID(Rial.Coefficients coefficient) {
			return coefficient.ordinal();
		}

		@Override
		protected IDisplay<Rial.Coefficients> NewView(Context ctx) {
			return new RialItemComponent(ctx, itemConfig);
		}

		@Override
		protected boolean isFilterMatched(Rial.Coefficients item, CharSequence constraint) {
			String name = getResources().getResourceName(item.NameResID);
			boolean result = name.contains(constraint);
			return result;
		}
	}
}
