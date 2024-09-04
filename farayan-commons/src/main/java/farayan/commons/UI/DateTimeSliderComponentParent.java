package farayan.commons.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.commons.R;
import farayan.commons.UI.Core.FarayanCommonBaseComponent;
public abstract class DateTimeSliderComponentParent extends FarayanCommonBaseComponent {
	private View rootView = null;

	protected static final String tag = "DateTimeSliderComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.date_time_slider, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating date_time_slider takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public DateTimeSliderComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public DateTimeSliderComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public DateTimeSliderComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public DateTimeSliderComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_yearTextView = null;
		m_YearSlider = null;
	}

	private TextView m_yearTextView;

	protected TextView yearTextView(){
		if(m_yearTextView == null)
			m_yearTextView = (TextView)rootView.findViewById(R.id.yearTextView);
		return m_yearTextView; 
	}

	private com.google.android.material.slider.Slider m_YearSlider;

	protected com.google.android.material.slider.Slider YearSlider(){
		if(m_YearSlider == null)
			m_YearSlider = (com.google.android.material.slider.Slider)rootView.findViewById(R.id.YearSlider);
		return m_YearSlider; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
