package farayan.sabad.ui;
import android.content.*;
import android.widget.*;
import android.view.*;
import android.util.*;
import androidx.annotation.*;
import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class UnitBoxItemComponentParent extends SabadBaseComponent {
	private View rootView = null;

	protected static final String tag = "UnitBoxItemComponentParent";

	private void LoadLayout(Context context) {
		Ctx = context;
		NullLoadedViews();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		long start = System.currentTimeMillis();
		rootView = inflater.inflate(R.layout.unit_box_item, this);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating unit_box_item takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(rootView,getDefaultFont());
	}


	
	Context Ctx;

	public UnitBoxItemComponentParent(Context context) {
		super(context);
		LoadLayout(context);
		InitializeLayout();
	}

	public UnitBoxItemComponentParent(Context context, AttributeSet attrs) {
		super(context,attrs);
		LoadLayout(context);
		InitializeLayout(attrs);
	}

	public UnitBoxItemComponentParent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LoadLayout(context);
		InitializeLayout(attrs, defStyle);
	}

	public UnitBoxItemComponentParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
