package farayan.commons.UI.Views;

import android.content.Context;

public interface IEnumBoxTextViewGenerator/*<SelectableType, SelectedType> */{
	IEnumBoxTextView NewSelectableView(Context ctx);
	IEnumBoxTextView NewSelectedView(Context ctx);
}
