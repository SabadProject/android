package farayan.sabad.ui;
import android.content.*;
import android.widget.*;
import android.view.*;
import android.util.*;
import androidx.annotation.*;
import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class InvoicesItemComponentParent extends SabadBaseComponent {
	private View rootView = null;

	protected static final String tag = "InvoicesItemComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.invoices_item, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating invoices_item takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public InvoicesItemComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public InvoicesItemComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public InvoicesItemComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public InvoicesItemComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		LoadLayout(context);
		InitializeLayout(attrs, defStyleAttr, defStyleRes);
	}

	private void NullLoadedViews() {
		m_Container = null;
		m_Line1TextView = null;
		m_Line2TextView = null;
		m_DeleteImageButton = null;
	}

	private LinearLayout m_Container;

	protected LinearLayout Container(){
		if(m_Container == null)
			m_Container = (LinearLayout)rootView.findViewById(R.id.Container);
		return m_Container; 
	}

	private TextView m_Line1TextView;

	protected TextView Line1TextView(){
		if(m_Line1TextView == null)
			m_Line1TextView = (TextView)rootView.findViewById(R.id.Line1TextView);
		return m_Line1TextView; 
	}

	private TextView m_Line2TextView;

	protected TextView Line2TextView(){
		if(m_Line2TextView == null)
			m_Line2TextView = (TextView)rootView.findViewById(R.id.Line2TextView);
		return m_Line2TextView; 
	}

	private ImageButton m_DeleteImageButton;

	protected ImageButton DeleteImageButton(){
		if(m_DeleteImageButton == null)
			m_DeleteImageButton = (ImageButton)rootView.findViewById(R.id.DeleteImageButton);
		return m_DeleteImageButton; 
	}

	protected void InitializeComponents(){}
	protected void InitializeLayout(){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs){InitializeComponents();}
	protected void InitializeLayout(AttributeSet attrs, int defStyleAttr){InitializeComponents();}
	protected void InitializeLayout(@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){InitializeComponents();}
}
