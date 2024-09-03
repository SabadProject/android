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
public abstract class DateTimeDialogComponentParent extends FarayanCommonBaseComponent {
	private View rootView = null;

	protected static final String tag = "DateTimeDialogComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.date_time_dialog, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating date_time_dialog takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public DateTimeDialogComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public DateTimeDialogComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public DateTimeDialogComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public DateTimeDialogComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_TopContainer = null;
		m_HeaderTextView = null;
		m_Picker = null;
		m_SelectButton = null;
	}

	private LinearLayout m_TopContainer;

	protected LinearLayout TopContainer(){
		if(m_TopContainer == null)
			m_TopContainer = (LinearLayout)rootView.findViewById(R.id.TopContainer);
		return m_TopContainer; 
	}

	private TextView m_HeaderTextView;

	protected TextView HeaderTextView(){
		if(m_HeaderTextView == null)
			m_HeaderTextView = (TextView)rootView.findViewById(R.id.HeaderTextView);
		return m_HeaderTextView; 
	}

	private farayan.commons.UI.DateTimePickerComponent m_Picker;

	protected farayan.commons.UI.DateTimePickerComponent Picker(){
		if(m_Picker == null)
			m_Picker = (farayan.commons.UI.DateTimePickerComponent)rootView.findViewById(R.id.Picker);
		return m_Picker; 
	}

	private Button m_SelectButton;

	protected Button SelectButton(){
		if(m_SelectButton == null)
			m_SelectButton = (Button)rootView.findViewById(R.id.SelectButton);
		return m_SelectButton; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
