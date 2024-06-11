package farayan.sabad.ui;
import android.content.*;
import android.widget.*;
import android.view.*;
import android.util.*;
import androidx.annotation.*;
import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class GroupHomeItemComponentParent extends SabadBaseComponent {
	private View rootView = null;

	protected static final String tag = "GroupHomeItemComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.group_home_item, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating group_home_item takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public GroupHomeItemComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public GroupHomeItemComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public GroupHomeItemComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public GroupHomeItemComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_Container = null;
		m_PickImageView = null;
		m_NameTextView = null;
		m_StatusTextView = null;
		m_NeededSwitch = null;
		m_InvoiceItemSummaryTextView = null;
	}

	private LinearLayout m_Container;

	protected LinearLayout Container(){
		if(m_Container == null)
			m_Container = (LinearLayout)rootView.findViewById(R.id.Container);
		return m_Container; 
	}

	private ImageView m_PickImageView;

	protected ImageView PickImageView(){
		if(m_PickImageView == null)
			m_PickImageView = (ImageView)rootView.findViewById(R.id.PickImageView);
		return m_PickImageView; 
	}

	private TextView m_NameTextView;

	protected TextView NameTextView(){
		if(m_NameTextView == null)
			m_NameTextView = (TextView)rootView.findViewById(R.id.NameTextView);
		return m_NameTextView; 
	}

	private TextView m_StatusTextView;

	protected TextView StatusTextView(){
		if(m_StatusTextView == null)
			m_StatusTextView = (TextView)rootView.findViewById(R.id.StatusTextView);
		return m_StatusTextView; 
	}

	private com.google.android.material.switchmaterial.SwitchMaterial m_NeededSwitch;

	protected com.google.android.material.switchmaterial.SwitchMaterial NeededSwitch(){
		if(m_NeededSwitch == null)
			m_NeededSwitch = (com.google.android.material.switchmaterial.SwitchMaterial)rootView.findViewById(R.id.NeededSwitch);
		return m_NeededSwitch; 
	}

	private TextView m_InvoiceItemSummaryTextView;

	protected TextView InvoiceItemSummaryTextView(){
		if(m_InvoiceItemSummaryTextView == null)
			m_InvoiceItemSummaryTextView = (TextView)rootView.findViewById(R.id.InvoiceItemSummaryTextView);
		return m_InvoiceItemSummaryTextView; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
