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
public abstract class EnumItemComponentParent extends FarayanCommonBaseComponent {
	private View rootView = null;

	protected static final String tag = "EnumItemComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.enum_item, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating enum_item takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public EnumItemComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public EnumItemComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public EnumItemComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public EnumItemComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_ValueTextView = null;
	}

	private TextView m_ValueTextView;

	protected TextView ValueTextView(){
		if(m_ValueTextView == null)
			m_ValueTextView = (TextView)rootView.findViewById(R.id.ValueTextView);
		return m_ValueTextView; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
