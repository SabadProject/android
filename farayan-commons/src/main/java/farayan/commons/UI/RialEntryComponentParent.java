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
public abstract class RialEntryComponentParent extends FarayanCommonBaseComponent {
	private View rootView = null;

	protected static final String tag = "RialEntryComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.rial_entry, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating rial_entry takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public RialEntryComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public RialEntryComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public RialEntryComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public RialEntryComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_DisplayableNumberEntry = null;
		m_CoefficientBox = null;
	}

	private farayan.commons.components.NumberEntry m_DisplayableNumberEntry;

	protected farayan.commons.components.NumberEntry DisplayableNumberEntry(){
		if(m_DisplayableNumberEntry == null)
			m_DisplayableNumberEntry = (farayan.commons.components.NumberEntry)rootView.findViewById(R.id.DisplayableNumberEntry);
		return m_DisplayableNumberEntry; 
	}

	private farayan.commons.UI.RialCoefficientBox m_CoefficientBox;

	protected farayan.commons.UI.RialCoefficientBox CoefficientBox(){
		if(m_CoefficientBox == null)
			m_CoefficientBox = (farayan.commons.UI.RialCoefficientBox)rootView.findViewById(R.id.CoefficientBox);
		return m_CoefficientBox; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
