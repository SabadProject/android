package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.SabadBaseComponent;
public abstract class PurchaseSummaryComponentParent extends SabadBaseComponent {
	private View rootView = null;

	protected static final String tag = "PurchaseSummaryComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.purchase_summary, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating purchase_summary takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public PurchaseSummaryComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public PurchaseSummaryComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public PurchaseSummaryComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public PurchaseSummaryComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_ContainerLayout = null;
		m_PickedItemsTextView = null;
		m_RemainedItemsTextView = null;
		m_SubtotalTextView = null;
	}

	private LinearLayout m_ContainerLayout;

	protected LinearLayout ContainerLayout(){
		if(m_ContainerLayout == null)
			m_ContainerLayout = (LinearLayout)rootView.findViewById(R.id.ContainerLayout);
		return m_ContainerLayout; 
	}

	private TextView m_PickedItemsTextView;

	protected TextView PickedItemsTextView(){
		if(m_PickedItemsTextView == null)
			m_PickedItemsTextView = (TextView)rootView.findViewById(R.id.PickedItemsTextView);
		return m_PickedItemsTextView; 
	}

	private TextView m_RemainedItemsTextView;

	protected TextView RemainedItemsTextView(){
		if(m_RemainedItemsTextView == null)
			m_RemainedItemsTextView = (TextView)rootView.findViewById(R.id.RemainedItemsTextView);
		return m_RemainedItemsTextView; 
	}

	private TextView m_SubtotalTextView;

	protected TextView SubtotalTextView(){
		if(m_SubtotalTextView == null)
			m_SubtotalTextView = (TextView)rootView.findViewById(R.id.SubtotalTextView);
		return m_SubtotalTextView; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
