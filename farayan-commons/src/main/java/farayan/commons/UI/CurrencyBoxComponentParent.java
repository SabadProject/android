package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Core.FarayanCommonBaseComponent;
public abstract class CurrencyBoxComponentParent extends FarayanCommonBaseComponent {
	private View rootView = null;

	protected static final String tag = "CurrencyBoxComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.currency_box, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating currency_box takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public CurrencyBoxComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public CurrencyBoxComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public CurrencyBoxComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public CurrencyBoxComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_PickerLayout = null;
		m_Level4NumberPicker = null;
		m_Level4And3SeparatorTextView = null;
		m_Level3NumberPicker = null;
		m_Level3And2SeparatorTextView = null;
		m_Level2NumberPicker = null;
		m_Level2And1SeparatorTextView = null;
		m_Level1NumberPicker = null;
		m_FloatingPointSeparatorTextView = null;
		m_FloatingPointNumberPicker = null;
		m_PrefixTextView = null;
		m_DisplayPickerCheckBox = null;
		m_SignTextView = null;
		m_NaturalNumberEditText = null;
		m_DecimalSeparatorTextView = null;
		m_DecimalPartEditText = null;
		m_SuffixTextView = null;
	}

	private LinearLayout m_PickerLayout;

	protected LinearLayout PickerLayout(){
		if(m_PickerLayout == null)
			m_PickerLayout = (LinearLayout)rootView.findViewById(R.id.PickerLayout);
		return m_PickerLayout; 
	}

	private farayan.commons.UI.CustomNumberPicker m_Level4NumberPicker;

	protected farayan.commons.UI.CustomNumberPicker Level4NumberPicker(){
		if(m_Level4NumberPicker == null)
			m_Level4NumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.Level4NumberPicker);
		return m_Level4NumberPicker; 
	}

	private TextView m_Level4And3SeparatorTextView;

	protected TextView Level4And3SeparatorTextView(){
		if(m_Level4And3SeparatorTextView == null)
			m_Level4And3SeparatorTextView = (TextView)rootView.findViewById(R.id.Level4And3SeparatorTextView);
		return m_Level4And3SeparatorTextView; 
	}

	private farayan.commons.UI.CustomNumberPicker m_Level3NumberPicker;

	protected farayan.commons.UI.CustomNumberPicker Level3NumberPicker(){
		if(m_Level3NumberPicker == null)
			m_Level3NumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.Level3NumberPicker);
		return m_Level3NumberPicker; 
	}

	private TextView m_Level3And2SeparatorTextView;

	protected TextView Level3And2SeparatorTextView(){
		if(m_Level3And2SeparatorTextView == null)
			m_Level3And2SeparatorTextView = (TextView)rootView.findViewById(R.id.Level3And2SeparatorTextView);
		return m_Level3And2SeparatorTextView; 
	}

	private farayan.commons.UI.CustomNumberPicker m_Level2NumberPicker;

	protected farayan.commons.UI.CustomNumberPicker Level2NumberPicker(){
		if(m_Level2NumberPicker == null)
			m_Level2NumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.Level2NumberPicker);
		return m_Level2NumberPicker; 
	}

	private TextView m_Level2And1SeparatorTextView;

	protected TextView Level2And1SeparatorTextView(){
		if(m_Level2And1SeparatorTextView == null)
			m_Level2And1SeparatorTextView = (TextView)rootView.findViewById(R.id.Level2And1SeparatorTextView);
		return m_Level2And1SeparatorTextView; 
	}

	private farayan.commons.UI.CustomNumberPicker m_Level1NumberPicker;

	protected farayan.commons.UI.CustomNumberPicker Level1NumberPicker(){
		if(m_Level1NumberPicker == null)
			m_Level1NumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.Level1NumberPicker);
		return m_Level1NumberPicker; 
	}

	private TextView m_FloatingPointSeparatorTextView;

	protected TextView FloatingPointSeparatorTextView(){
		if(m_FloatingPointSeparatorTextView == null)
			m_FloatingPointSeparatorTextView = (TextView)rootView.findViewById(R.id.FloatingPointSeparatorTextView);
		return m_FloatingPointSeparatorTextView; 
	}

	private farayan.commons.UI.CustomNumberPicker m_FloatingPointNumberPicker;

	protected farayan.commons.UI.CustomNumberPicker FloatingPointNumberPicker(){
		if(m_FloatingPointNumberPicker == null)
			m_FloatingPointNumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.FloatingPointNumberPicker);
		return m_FloatingPointNumberPicker; 
	}

	private TextView m_PrefixTextView;

	protected TextView PrefixTextView(){
		if(m_PrefixTextView == null)
			m_PrefixTextView = (TextView)rootView.findViewById(R.id.PrefixTextView);
		return m_PrefixTextView; 
	}

	private CheckBox m_DisplayPickerCheckBox;

	protected CheckBox DisplayPickerCheckBox(){
		if(m_DisplayPickerCheckBox == null)
			m_DisplayPickerCheckBox = (CheckBox)rootView.findViewById(R.id.DisplayPickerCheckBox);
		return m_DisplayPickerCheckBox; 
	}

	private TextView m_SignTextView;

	protected TextView SignTextView(){
		if(m_SignTextView == null)
			m_SignTextView = (TextView)rootView.findViewById(R.id.SignTextView);
		return m_SignTextView; 
	}

	private EditText m_NaturalNumberEditText;

	protected EditText NaturalNumberEditText(){
		if(m_NaturalNumberEditText == null)
			m_NaturalNumberEditText = (EditText)rootView.findViewById(R.id.NaturalNumberEditText);
		return m_NaturalNumberEditText; 
	}

	private TextView m_DecimalSeparatorTextView;

	protected TextView DecimalSeparatorTextView(){
		if(m_DecimalSeparatorTextView == null)
			m_DecimalSeparatorTextView = (TextView)rootView.findViewById(R.id.DecimalSeparatorTextView);
		return m_DecimalSeparatorTextView; 
	}

	private EditText m_DecimalPartEditText;

	protected EditText DecimalPartEditText(){
		if(m_DecimalPartEditText == null)
			m_DecimalPartEditText = (EditText)rootView.findViewById(R.id.DecimalPartEditText);
		return m_DecimalPartEditText; 
	}

	private TextView m_SuffixTextView;

	protected TextView SuffixTextView(){
		if(m_SuffixTextView == null)
			m_SuffixTextView = (TextView)rootView.findViewById(R.id.SuffixTextView);
		return m_SuffixTextView; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
