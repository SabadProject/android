package farayan.commons.UI.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.content.ContextCompat;
import farayan.commons.R;
import farayan.commons.SimpleEnumBoxTextViewComponent;
import farayan.commons.UI.Core.GeneralCommands;
import farayan.commons.UI.Core.IGeneralEvent;
import farayan.commons.UI.Core.IGeneralEventProvider;

import static android.R.color.black;
import static android.R.color.darker_gray;

public abstract class EnumSpinner<
		EnumType extends Enum<EnumType> & IBoxEnum/*,
		SelectableType extends View & IEnumBoxTextView,
		SelectedType extends View & IEnumBoxTextView*/
		>
		extends Spinner
		implements IEnumBoxTextViewGenerator, IGeneralEventProvider<Void, GeneralCommands, EnumType>
{
	String Hint;
	private EnumAdapter adapter;
	private IGeneralEvent<Void, GeneralCommands, EnumType> TheEventHandler;
	private int HintColor;
	private int TextColor;
	private boolean reloading;

	public EnumSpinner(Context context) {
		super(context);
		InitializeLayout();
	}

	public EnumSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		LoadAttributes(context, attrs);
		InitializeLayout();
	}

	public EnumSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadAttributes(context, attrs);
		InitializeLayout();
	}

	private void LoadAttributes(Context context, AttributeSet attrs) {
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.EnumSpinner, 0, 0);
		try {
			setHint(attributes.getString(R.styleable.EnumSpinner_hintValue));
			HintColor = attributes.getColor(R.styleable.EnumSpinner_hintColor, ContextCompat.getColor(context, darker_gray));
			TextColor = attributes.getColor(R.styleable.EnumSpinner_textColor, ContextCompat.getColor(context, black));
		} finally {
			attributes.recycle();
		}
	}

	protected void Reload() {
		reloading = true;
		List<EnumType> values = new ArrayList<>(Arrays.asList(EnumValues()));
		adapter = new EnumAdapter<>(getContext(), values, Hint, this);
		setAdapter(adapter);
		reloading = false;
	}

	public String getHint() {
		return Hint;
	}

	public void setHint(String hint) {
		Hint = hint;
	}

	protected abstract EnumType[] EnumValues();

	protected void InitializeLayout() {
		setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (reloading)
					return;
				if (TheEventHandler == null)
					return;
				TheEventHandler.OnEvent(GeneralCommands.Picked, getSelectedEnum());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (reloading)
					return;
				if (TheEventHandler == null)
					return;
				TheEventHandler.OnEvent(GeneralCommands.Picked, getSelectedEnum());
			}
		});
		Reload();
	}

	public EnumType getSelectedEnum() {
		if (adapter == null)
			return null;

		int selectedID = (int) getSelectedItemId();
		Object selectedValue = adapter.getItem(selectedID);
		if (selectedValue == null)
			return null;
		EnumType selectedEnum = (EnumType) selectedValue;
		return selectedEnum;
	}

	public void setSelectedEnum(EnumType value) {
		if (adapter == null)
			return;

		int position = adapter.ItemPosition(value);
		setSelection(position);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public IEnumBoxTextView NewSelectableView(Context ctx) {
		SimpleEnumBoxTextViewComponent textView = new SimpleEnumBoxTextViewComponent(ctx);
		ConfigSelectableView(textView);
		return textView;
	}

	protected void ConfigSelectableView(IEnumBoxTextView iEnumBoxTextView) {
		TextView textView = (TextView) iEnumBoxTextView;
		textView.setTextColor(TextColor);
		textView.setHintTextColor(HintColor);
	}

	@Override
	public IEnumBoxTextView NewSelectedView(Context ctx) {
		SimpleEnumBoxTextViewComponent textView = new SimpleEnumBoxTextViewComponent(ctx);
		ConfigSelectedView(textView);
		return textView;
	}

	protected void ConfigSelectedView(IEnumBoxTextView iEnumBoxTextView) {
		TextView textView = (TextView) iEnumBoxTextView;
		textView.setTextColor(TextColor);
		textView.setHintTextColor(HintColor);
	}

	@Override
	public void SetEventHandler(IGeneralEvent<Void, GeneralCommands, EnumType> iEvent) {
		TheEventHandler = iEvent;
	}
}
