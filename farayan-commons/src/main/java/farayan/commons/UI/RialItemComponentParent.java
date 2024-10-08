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
public abstract class RialItemComponentParent extends FarayanCommonBaseComponent {
	private View rootView = null;

	protected static final String tag = "RialItemComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.rial_item, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating rial_item takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public RialItemComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public RialItemComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public RialItemComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public RialItemComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_NameTextView = null;
	}

	private TextView m_NameTextView;

	protected TextView NameTextView(){
		if(m_NameTextView == null)
			m_NameTextView = (TextView)rootView.findViewById(R.id.NameTextView);
		return m_NameTextView; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
