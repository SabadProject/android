package farayan.sabad.ui;
import android.os.Bundle;
import android.widget.*;
import android.util.*;

import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.ui.Core.*;
public abstract class AboutActivityParent extends SabadBaseActivity {
	protected static final String tag = "AboutActivityParent";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NullLoadedViews();
		long start = System.currentTimeMillis();
		setContentView(R.layout.activity_about);
		long finish = System.currentTimeMillis();
		Log.i("Performance", String.format("inflating activity_about takes %s milliseconds", FarayanUtility.MoneyFormatted(finish - start, false)));
		FarayanUtility.OverrideFonts(findViewById(android.R.id.content) ,getDefaultFont());
		InitializeLayout();	}

	private void NullLoadedViews() {
		m_ReleaseTextView = null;
		m_AppIdTextView = null;
		m_UpdatableTextView = null;
		m_CheckFailedTextView = null;
		m_StatusTextView = null;
		m_UpdateProgressBar = null;
		m_CheckProgressBar = null;
		m_CheckButton = null;
		m_UpdateButton = null;
		m_AboutTextView = null;
		m_ShareButton = null;
		m_RateAndReviewButton = null;
		m_RateAndReviewCommentTextView = null;
		m_FeedbackButton = null;
		m_FeedbackLayout = null;
		m_TelegramSupportIcon = null;
		m_TelegramSubscribeChannelIcon = null;
		m_TelegramBrowseChannelIcon = null;
		m_TwitterIcon = null;
		m_FacebookIcon = null;
		m_InstagramIcon = null;
		m_WhatsAppSupportIcon = null;
		m_WebsiteIcon = null;
		m_EmailIcon = null;
		m_ParticipateButton = null;
	}

	private TextView m_ReleaseTextView;
	protected TextView ReleaseTextView(){
		if(m_ReleaseTextView == null)
			m_ReleaseTextView = (TextView)findViewById(R.id.ReleaseTextView);
		return m_ReleaseTextView; 
	}

	private TextView m_AppIdTextView;
	protected TextView AppIdTextView(){
		if(m_AppIdTextView == null)
			m_AppIdTextView = (TextView)findViewById(R.id.AppIdTextView);
		return m_AppIdTextView; 
	}

	private TextView m_UpdatableTextView;
	protected TextView UpdatableTextView(){
		if(m_UpdatableTextView == null)
			m_UpdatableTextView = (TextView)findViewById(R.id.UpdatableTextView);
		return m_UpdatableTextView; 
	}

	private TextView m_CheckFailedTextView;
	protected TextView CheckFailedTextView(){
		if(m_CheckFailedTextView == null)
			m_CheckFailedTextView = (TextView)findViewById(R.id.CheckFailedTextView);
		return m_CheckFailedTextView; 
	}

	private TextView m_StatusTextView;
	protected TextView StatusTextView(){
		if(m_StatusTextView == null)
			m_StatusTextView = (TextView)findViewById(R.id.StatusTextView);
		return m_StatusTextView; 
	}

	private com.beardedhen.androidbootstrap.BootstrapProgressBar m_UpdateProgressBar;
	protected com.beardedhen.androidbootstrap.BootstrapProgressBar UpdateProgressBar(){
		if(m_UpdateProgressBar == null)
			m_UpdateProgressBar = (com.beardedhen.androidbootstrap.BootstrapProgressBar)findViewById(R.id.UpdateProgressBar);
		return m_UpdateProgressBar; 
	}

	private ProgressBar m_CheckProgressBar;
	protected ProgressBar CheckProgressBar(){
		if(m_CheckProgressBar == null)
			m_CheckProgressBar = (ProgressBar)findViewById(R.id.CheckProgressBar);
		return m_CheckProgressBar; 
	}

	private com.google.android.material.button.MaterialButton m_CheckButton;
	protected com.google.android.material.button.MaterialButton CheckButton(){
		if(m_CheckButton == null)
			m_CheckButton = (com.google.android.material.button.MaterialButton)findViewById(R.id.CheckButton);
		return m_CheckButton; 
	}

	private com.google.android.material.button.MaterialButton m_UpdateButton;
	protected com.google.android.material.button.MaterialButton UpdateButton(){
		if(m_UpdateButton == null)
			m_UpdateButton = (com.google.android.material.button.MaterialButton)findViewById(R.id.UpdateButton);
		return m_UpdateButton; 
	}

	private TextView m_AboutTextView;
	protected TextView AboutTextView(){
		if(m_AboutTextView == null)
			m_AboutTextView = (TextView)findViewById(R.id.AboutTextView);
		return m_AboutTextView; 
	}

	private com.google.android.material.button.MaterialButton m_ShareButton;
	protected com.google.android.material.button.MaterialButton ShareButton(){
		if(m_ShareButton == null)
			m_ShareButton = (com.google.android.material.button.MaterialButton)findViewById(R.id.ShareButton);
		return m_ShareButton; 
	}

	private com.google.android.material.button.MaterialButton m_RateAndReviewButton;
	protected com.google.android.material.button.MaterialButton RateAndReviewButton(){
		if(m_RateAndReviewButton == null)
			m_RateAndReviewButton = (com.google.android.material.button.MaterialButton)findViewById(R.id.RateAndReviewButton);
		return m_RateAndReviewButton; 
	}

	private TextView m_RateAndReviewCommentTextView;
	protected TextView RateAndReviewCommentTextView(){
		if(m_RateAndReviewCommentTextView == null)
			m_RateAndReviewCommentTextView = (TextView)findViewById(R.id.RateAndReviewCommentTextView);
		return m_RateAndReviewCommentTextView; 
	}

	private com.google.android.material.button.MaterialButton m_FeedbackButton;
	protected com.google.android.material.button.MaterialButton FeedbackButton(){
		if(m_FeedbackButton == null)
			m_FeedbackButton = (com.google.android.material.button.MaterialButton)findViewById(R.id.FeedbackButton);
		return m_FeedbackButton; 
	}

	private LinearLayout m_FeedbackLayout;
	protected LinearLayout FeedbackLayout(){
		if(m_FeedbackLayout == null)
			m_FeedbackLayout = (LinearLayout)findViewById(R.id.FeedbackLayout);
		return m_FeedbackLayout; 
	}

	private farayan.commons.components.FontAwesomeBrandIcon m_TelegramSupportIcon;
	protected farayan.commons.components.FontAwesomeBrandIcon TelegramSupportIcon(){
		if(m_TelegramSupportIcon == null)
			m_TelegramSupportIcon = (farayan.commons.components.FontAwesomeBrandIcon)findViewById(R.id.TelegramSupportIcon);
		return m_TelegramSupportIcon; 
	}

	private farayan.commons.components.FontAwesomeBrandIcon m_TelegramSubscribeChannelIcon;
	protected farayan.commons.components.FontAwesomeBrandIcon TelegramSubscribeChannelIcon(){
		if(m_TelegramSubscribeChannelIcon == null)
			m_TelegramSubscribeChannelIcon = (farayan.commons.components.FontAwesomeBrandIcon)findViewById(R.id.TelegramSubscribeChannelIcon);
		return m_TelegramSubscribeChannelIcon; 
	}

	private farayan.commons.components.FontAwesomeBrandIcon m_TelegramBrowseChannelIcon;
	protected farayan.commons.components.FontAwesomeBrandIcon TelegramBrowseChannelIcon(){
		if(m_TelegramBrowseChannelIcon == null)
			m_TelegramBrowseChannelIcon = (farayan.commons.components.FontAwesomeBrandIcon)findViewById(R.id.TelegramBrowseChannelIcon);
		return m_TelegramBrowseChannelIcon; 
	}

	private farayan.commons.components.FontAwesomeBrandIcon m_TwitterIcon;
	protected farayan.commons.components.FontAwesomeBrandIcon TwitterIcon(){
		if(m_TwitterIcon == null)
			m_TwitterIcon = (farayan.commons.components.FontAwesomeBrandIcon)findViewById(R.id.TwitterIcon);
		return m_TwitterIcon; 
	}

	private farayan.commons.components.FontAwesomeBrandIcon m_FacebookIcon;
	protected farayan.commons.components.FontAwesomeBrandIcon FacebookIcon(){
		if(m_FacebookIcon == null)
			m_FacebookIcon = (farayan.commons.components.FontAwesomeBrandIcon)findViewById(R.id.FacebookIcon);
		return m_FacebookIcon; 
	}

	private farayan.commons.components.FontAwesomeBrandIcon m_InstagramIcon;
	protected farayan.commons.components.FontAwesomeBrandIcon InstagramIcon(){
		if(m_InstagramIcon == null)
			m_InstagramIcon = (farayan.commons.components.FontAwesomeBrandIcon)findViewById(R.id.InstagramIcon);
		return m_InstagramIcon; 
	}

	private farayan.commons.components.FontAwesomeBrandIcon m_WhatsAppSupportIcon;
	protected farayan.commons.components.FontAwesomeBrandIcon WhatsAppSupportIcon(){
		if(m_WhatsAppSupportIcon == null)
			m_WhatsAppSupportIcon = (farayan.commons.components.FontAwesomeBrandIcon)findViewById(R.id.WhatsAppSupportIcon);
		return m_WhatsAppSupportIcon; 
	}

	private farayan.commons.components.FontAwesomeSolidIcon m_WebsiteIcon;
	protected farayan.commons.components.FontAwesomeSolidIcon WebsiteIcon(){
		if(m_WebsiteIcon == null)
			m_WebsiteIcon = (farayan.commons.components.FontAwesomeSolidIcon)findViewById(R.id.WebsiteIcon);
		return m_WebsiteIcon; 
	}

	private farayan.commons.components.FontAwesomeRegularIcon m_EmailIcon;
	protected farayan.commons.components.FontAwesomeRegularIcon EmailIcon(){
		if(m_EmailIcon == null)
			m_EmailIcon = (farayan.commons.components.FontAwesomeRegularIcon)findViewById(R.id.EmailIcon);
		return m_EmailIcon; 
	}

	private com.google.android.material.button.MaterialButton m_ParticipateButton;
	protected com.google.android.material.button.MaterialButton ParticipateButton(){
		if(m_ParticipateButton == null)
			m_ParticipateButton = (com.google.android.material.button.MaterialButton)findViewById(R.id.ParticipateButton);
		return m_ParticipateButton; 
	}

	protected void InitializeLayout(){}
}
