package farayan.commons.UI;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.util.*;
import androidx.annotation.*;
import android.util.*;
import farayan.commons.FarayanUtility;
import farayan.commons.*;
import farayan.commons.UI.Core.*;
public abstract class TimePickerComponentParent extends FarayanCommonBaseComponent {
	private View rootView = null;

	protected static final String tag = "TimePickerComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.time_picker, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating time_picker takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public TimePickerComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public TimePickerComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public TimePickerComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public TimePickerComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_hourNumberPicker = null;
		m_minuteNumberPicker = null;
		m_secondNumberPicker = null;
	}

	private farayan.commons.UI.CustomNumberPicker m_hourNumberPicker;

	protected farayan.commons.UI.CustomNumberPicker hourNumberPicker(){
		if(m_hourNumberPicker == null)
			m_hourNumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.hourNumberPicker);
		return m_hourNumberPicker; 
	}

	private farayan.commons.UI.CustomNumberPicker m_minuteNumberPicker;

	protected farayan.commons.UI.CustomNumberPicker minuteNumberPicker(){
		if(m_minuteNumberPicker == null)
			m_minuteNumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.minuteNumberPicker);
		return m_minuteNumberPicker; 
	}

	private farayan.commons.UI.CustomNumberPicker m_secondNumberPicker;

	protected farayan.commons.UI.CustomNumberPicker secondNumberPicker(){
		if(m_secondNumberPicker == null)
			m_secondNumberPicker = (farayan.commons.UI.CustomNumberPicker)rootView.findViewById(R.id.secondNumberPicker);
		return m_secondNumberPicker; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
