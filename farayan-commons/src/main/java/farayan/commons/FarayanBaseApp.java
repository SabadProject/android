package farayan.commons;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;


public class FarayanBaseApp extends Application {
	private static Context ctx;
	private static Typeface defaultFont;

	private UncaughtExceptionHandler LogUnhandledExceptions = new UncaughtExceptionHandler() {

		@Override
		public void uncaughtException(Thread arg0, Throwable arg1) {
			FarayanUtility.LogException(arg1);
            arg1.printStackTrace();
			System.exit(1);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		ctx = getApplicationContext();
		FarayanUtility.GlobalContext = getApplicationContext();
		UpgradeUtility.RegisterUpgradeAlarm(ctx, UpgradeAlarmType());
		if (FarayanUtility.IsUsable(FontFileName()))
			defaultFont = Typeface.createFromAsset(getAssets(), FontFileName());
		Thread.setDefaultUncaughtExceptionHandler(LogUnhandledExceptions);
		/*if (GoogleAnalyticsTracker() == null)
			Log.i("GoogleAnalytics", "GoogleAnalytics is null");*/
	}

	protected Class<? extends UpgradeAlarm> UpgradeAlarmType() {
		return UpgradeAlarm.class;
	}

	public static Context getContext() {
		return ctx;
	}

	protected String FontFileName() {
		return "";
	}

	public static Typeface getFont() {
		return defaultFont;
	}

	protected String GoogleAnalyticsTrackerID() {
		return "";
	}

	// TODO: 30/12/2017 uncomment for google analytics
	/*static Tracker tracker = null;*/
	/*public Tracker GoogleAnalyticsTracker() {
		if (tracker == null && FarayanUtility.IsUsable(GoogleAnalyticsTrackerID())) {
			tracker = GoogleAnalytics.getInstance(getContext()).newTracker(GoogleAnalyticsTrackerID());
			tracker.enableAdvertisingIdCollection(true);
			tracker.enableExceptionReporting(true);
			tracker.enableAutoActivityTracking(true);
		}

		return tracker;
	}*/

	private static FarayanBaseApp m_instance;

	public static FarayanBaseApp Instance() {
		return m_instance;
	}

	public FarayanBaseApp() {
		m_instance = this;
	}

	public PersianCalendar Expiration() {
		return null;
	}

	protected boolean CheckUpdate() {
		return true;
	}
}
