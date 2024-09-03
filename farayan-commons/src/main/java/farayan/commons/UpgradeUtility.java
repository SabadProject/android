package farayan.commons;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.jaredrummler.android.device.DeviceName;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import farayan.commons.UI.QuestionDialog;

/**
 * در این کلاس تمامی ابزارهای لازم برای به‌روزرسانی گنجانده شده است
 */
public class UpgradeUtility {
	final static int UpdateNotificationID = 3546846;
	final static String ServerUrl = "http://support.farayan.net";
	private static final String TAG = "UpgradeUtility";

	enum UpgradePreferenceKeys {
		/**
		 * آخرین زمانی که برای نسخه جدید بررسی شده است
		 */
		LastChecked,
		/**
		 * نشانی فایل نسخه جدید
		 */
		Downloadable,
		/**
		 * جایی که فایل نسخه جدید ذخیره شده است
		 */
		DownloadedInstallerLocation,

		/**
		 * شناسه دانلود در سرویس دانلود
		 */
		DownloadID,

		/**
		 * زمانی که دالنود تمام می‌شود؟
		 */
		DownloadedTime,

		/**
		 * اولین باری که امکان بررسی نسخه جدید وجود نداشته
		 * این فیلد زمانی به کار می‌رود که بیشتر از دو هفته برنامه نتواند وجود نسخه جدید را بررسی کند
		 */
		FirstCheckFailedMillis,

		/**
		 * آخرین باری که وجود نسخه جدید بررسی شده است
		 */
		LastCheckTry,

		/**
		 * متنی که سرور به عنوان اطلاعات به‌روزرسانی ارسال کرده است
		 */
		ResultText;
	}

	enum UpgradeTypes {
		Skip(1), Optional(2), Mandatory(3);

		public final int ServerCode;

		UpgradeTypes(int code) {
			ServerCode = code;
		}

		public static UpgradeTypes FromServerCode(Integer serverCode) {
			if (serverCode == null)
				return Skip;
			for (UpgradeTypes type : values()) {
				if (type.ServerCode == serverCode)
					return type;
			}
			return null;
		}
	}

	static SharedPreferences m_settings = null;

	static SharedPreferences Settings(Context ctx) {
		if (m_settings == null)
			m_settings = PreferenceManager.getDefaultSharedPreferences(ctx);
		return m_settings;
	}

	/**
	 * تایمری را تنطیم می‌کند که هروز ساعت ۱۱ برای نسخه جدید بررسی کند
	 * کلاس نوع آلارم باید در برنامه مرتبط از کلاس تهیه شده در همین کدخانه مشتق بشود
	 *
	 * @param ctx
	 * @param alarmType
	 */
	public static void RegisterUpgradeAlarm(Context ctx, Class<?> alarmType) {
		if (alarmType == null)
			return;
		AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 11);

		Intent i = new Intent(ctx, alarmType);
		PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, i, FLAG_IMMUTABLE);
		am.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
	}

	/**
	 * در صورتی که برای دالنود از سرویس دانلود اندروید استفاده کنیم، این تابع زمان تمام شدن دانلود فراخوانی می‌شود
	 */
	private static class DownloadCompletedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent i) {
			SharedPreferences settings = Settings(context);
			DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			long completedDownloadID = i.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
			long requestedDownloadID = settings.getLong(UpgradePreferenceKeys.DownloadID.name(), -1);
			if (requestedDownloadID != completedDownloadID)
				return;
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(requestedDownloadID);
			Cursor downloadsCursor = dm.query(query);
			if (!downloadsCursor.moveToFirst())
				return;
			int downloadStatus = downloadsCursor.getInt(downloadsCursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
			if (downloadStatus != DownloadManager.STATUS_SUCCESSFUL)
				return;
			String downloadLocalFile = downloadsCursor.getString(downloadsCursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
			if (downloadLocalFile.startsWith("file://"))
				downloadLocalFile = downloadLocalFile.substring("file://".length());

			Editor editor = settings.edit();
			editor.putString(UpgradePreferenceKeys.DownloadedInstallerLocation.name(), downloadLocalFile);
			editor.remove(UpgradePreferenceKeys.DownloadID.name());
			editor.putLong(UpgradePreferenceKeys.DownloadedTime.name(), System.currentTimeMillis());
			editor.apply();

			DisplayUpgradeAvailableNotification(context);
		}
	}

	/**
	 * در صورتی که براساس اطلاعات دریافتی، نسخه جدیدتر موجود و دانلود شده باشد، به صورت اعلان نمایش داده می‌شود
	 * با کلیک برروی اعلان مستقیما وارد نصب نسخه جدید می‌شود
	 *
	 * @param ctx
	 */
	protected static void DisplayUpgradeAvailableNotification(Context ctx) {

		if (!IsUpgradeAvailable(ctx))
			return;

		SharedPreferences settings = Settings(ctx);
		UpgradeResult result = LoadUpgradeResult();
		if (result == null)
			return;
		UpgradeTypes type = UpgradeTypes.FromServerCode(result.getType());
		File installerFile = new File(settings.getString(UpgradePreferenceKeys.DownloadedInstallerLocation.name(), ""));

		PackageInfo pi = FarayanUtility.GetPackageInfo(ctx);

		Intent intent = UpgradeIntent(installerFile, ctx);
		PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, FLAG_IMMUTABLE);

		Bitmap icon = ((BitmapDrawable) pi.applicationInfo.loadIcon(ctx.getPackageManager())).getBitmap();
		NotificationCompat.Builder nb = new NotificationCompat.Builder(ctx)
				.setContentTitle(result.getTitle())
				.setContentInfo(result.getVersion() + "")
				.setContentText(result.getComment())
				.setContentIntent(pendingIntent)
				.setAutoCancel(true)
				.setLargeIcon(icon)
				.setOngoing(type == UpgradeTypes.Mandatory)
				.setSmallIcon(pi.applicationInfo.icon)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setTicker(result.getTitle());

		NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(UpdateNotificationID, nb.build());
	}

	/**
	 * آخرین متن دریافی از کارگزار به‌روزرسانی را در صورت وجود تبدیل به کلاس UpgradeResult می‌کند
	 *
	 * @return
	 */
	private static UpgradeResult LoadUpgradeResult() {
		String resultText = Settings(FarayanBaseApp.getContext()).getString(UpgradePreferenceKeys.ResultText.name(), "");
		if (FarayanUtility.IsNullOrEmpty(resultText))
			return null;
		return new Gson().fromJson(resultText, UpgradeResult.class);
	}

	/**
	 * وجود نسخه جدید را بررسی می‌کند
	 * در صورتی که فایل نسخه جدید قابل دانلود باشد، آن را دریافت می‌کند
	 */
	public static class UpdaterAsyncTask extends AsyncTask<Void, Integer, UpgradeResult> {
		final Context ctx;
		private NotificationManager notificationManager;
		private Notification.Builder notificationBuilder;
		static boolean DoNotExecute = false;

		public UpdaterAsyncTask(Context context) {
			this.ctx = context;
		}

		@Override
		protected UpgradeResult doInBackground(Void... arg0) {
			if (DoNotExecute)
				return null;
			DoNotExecute = true;
			UpgradeResult result = Do();
			DoNotExecute = false;
			return result;
		}

		@Override
		protected void onPreExecute() {
			notificationManager = (NotificationManager) FarayanUtility.GlobalContext.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationBuilder = new Notification.Builder(FarayanUtility.GlobalContext);
			notificationBuilder.setContentInfo("دانلود نسخه جدید");
			notificationBuilder.setSmallIcon(FarayanUtility.GetPackageInfo(FarayanUtility.GlobalContext).applicationInfo.icon);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			notificationBuilder.setProgress(100, values[0], false);
			notificationManager.notify(UpdateNotificationID, notificationBuilder.build());
		}

		private UpgradeResult Do() {
			Log.i(TAG, "Check for upgrades");
			String url = ServerUrl + "/product/last-version.ashx?code=%s&platform=2&version=%s&client-version-code=%s&client-version-name=%s&client-platform=%s&client-device=%s";
			PackageInfo pi = FarayanUtility.GetPackageInfo(ctx);
			String deviceName = "";
			try {
				deviceName = URLEncoder.encode(DeviceName.getDeviceName(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			url = String.format(url, pi.packageName, pi.versionCode, pi.versionCode, pi.versionName, 2, deviceName);
			UpgradeResult result = FarayanUtility.GetGson(false, url, null, UpgradeResult.class);

			if (result == null) {
				PersistUpgradeFailure();
				return null;
			}

			RemoveUpgradeFailure();

			if (FarayanUtility.IsUsable(result.getErrorMessage())) {
				return null;
			}

			if (!IsUpgradeNewerFromInstaller(result))
				return null;

			UpgradeTypes Type = UpgradeTypes.FromServerCode(result.getType());
			if (Type == UpgradeTypes.Skip) {
				return null;
			}
			if (FarayanUtility.IsUsable(result.getDownloadUrl())) {
				String installerLocation = DownloadInstaller(result);
				if (FarayanUtility.IsUsable(installerLocation)) {
					PersistUpgradeSuccess(result, installerLocation);
					DisplayUpgradeAvailableNotification(ctx);
				} else {
					FarayanUtility.Log(false, true, true, "File %s not downloaded", result.getDownloadUrl());
				}
			} else {
				PersistUpgradeSuccess(result, null);
				if (Type == UpgradeTypes.Mandatory)
					DisplayUpgradeAvailableNotification(ctx);
				if (Type == UpgradeTypes.Optional)
					DisplayUpgradeAvailableNotification(ctx);
			}

			return result;
		}

		/**
		 * در صورتی که نسخه موجود از نسخه نصب شده کوچکتر باشد مقدار درست وگزنه مقدار نادرست را برمی‌گرداند
		 *
		 * @param loadedResult
		 * @return
		 */
		private boolean IsUpgradeNewerFromInstaller(UpgradeResult loadedResult) {
			UpgradeResult persistedResult = LoadUpgradeResult();
			if (persistedResult == null)
				return true;
			if (loadedResult.getVersion() > persistedResult.getVersion())
				return true;
			return false;
		}

		/**
		 * دسترسی موفق به کارگزار را به همراه جواب دریافتی از کارگزار و نشانی به ‌روزرسانی ذخیره می‌کند
		 *
		 * @param result
		 * @param installerLocation
		 */
		private void PersistUpgradeSuccess(UpgradeResult result, String installerLocation) {
			String resultText = new Gson().toJson(result);
			SharedPreferences settings = Settings(ctx);
			Editor editor = settings.edit();
			editor.putString(UpgradePreferenceKeys.ResultText.name(), resultText);
			editor.putString(UpgradePreferenceKeys.DownloadedInstallerLocation.name(), installerLocation);
			editor.remove(UpgradePreferenceKeys.FirstCheckFailedMillis.name());
			editor.apply();
		}

		/**
		 * تلاش می‌کند فایل نسخه جدید را دانلود کند
		 *
		 * @param result
		 * @return * در صورت دانلود موفق، نشانی فایل دانلود شده و در فیر این‌صورت مقدا خالی را برمی‌گرداند
		 */
		private String DownloadInstaller(UpgradeResult result) {
			FarayanUtility.Log(false, true, true, "Download installer");
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(result.getDownloadUrl());
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					FarayanUtility.Log(false, true, true, "Download installer failed. response code is %s", connection.getResponseCode());
					return null;// "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
				}

				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();
				File folder = ctx.getExternalFilesDir("upgrades");
				File file = new File(folder.getAbsolutePath() + "/" + FilenameUtils.getName(result.getDownloadUrl()));
				FarayanUtility.Log(true, true, true, "Download installer to %s", file.getAbsolutePath());
				output = new FileOutputStream(file.getAbsolutePath());

				byte data[] = new byte[4096];
				long total = 0;
				int count;

				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					// publishing the progress....
					//if (fileLength > 0) // only if total length is known
					//	publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
				FarayanUtility.Log(false, true, true, "Download installer completes at %s", file.getAbsolutePath());
				return file.getAbsolutePath();
			} catch (Exception e) {
				e.printStackTrace();
				return null;// e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
		}

		@Override
		protected void onPostExecute(UpgradeResult result) {

		}
	}

	/**
	 * اگر در ۵ دقیقه گذشته نسخه جدید بررسی نشده باشد
	 * یا در یک روز گذشته یک بررسی موفق وجود نداشته باشد
	 * وجود نسخه جدید را بررسی می‌کند
	 *
	 * @param ctx
	 */
	public static void TryCheck(Context ctx) {
		Log.i(TAG, "TryCheck");

		SharedPreferences settings = Settings(ctx);
		long nowLong = System.currentTimeMillis();
		long lastUpdateCheckTryMillis = settings.getLong(UpgradePreferenceKeys.LastCheckTry.name(), 0);
		long checkExpiration = nowLong - 5 * 60 * 1000;
		if (lastUpdateCheckTryMillis > 0 && lastUpdateCheckTryMillis > checkExpiration) {
			FarayanUtility.Log(false, true, true, "Skip Check for upgrades because previously check tried in less than 5 minutes");
			return;
		}

		Editor editor = settings.edit();
		editor.putLong(UpgradePreferenceKeys.LastCheckTry.name(), nowLong);
		editor.commit();

		long lastUpdateCheckedMillis = settings.getLong(UpgradePreferenceKeys.LastChecked.name(), 0);
		if (lastUpdateCheckedMillis > 0) {
			PersianCalendar lastUpdateChecked = new PersianCalendar(lastUpdateCheckedMillis);
			PersianCalendar now = new PersianCalendar();
			PersianPeriod period = new PersianPeriod(lastUpdateChecked, now);
			if (period.TotalDays() < 1) {
				FarayanUtility.Log(false, true, true, "Skip Check for upgrades because previously checked in less than 1 day");
				return;
			}
		}
		Check();
	}

	/**
	 * تسک بررسی نسخه جدید را اجرا می‌کند
	 */
	public static void Check() {
		new UpdaterAsyncTask(FarayanBaseApp.getContext()).execute();
	}

	/**
	 * در صورتی که امکان دسترسی به کارگزار به‌روزرسانی باشد، اطلاعات اولین مشکل در دسترسی حذف می‌شود
	 */
	public static void RemoveUpgradeFailure() {
		SharedPreferences settings = Settings(FarayanBaseApp.getContext());

		Editor editor = settings.edit();
		editor.remove(UpgradePreferenceKeys.FirstCheckFailedMillis.name());
		editor.apply();
	}

	/**
	 * فراخوانی این تابع به معنای مشکل در بررسی نسخه جدید هست
	 * اگر بیشتر از ۱۴ روز برنامه نتواند وجود نسخه جدید را بررسی کند اعلان می‌دهد
	 * اگر این اولین فراخوانی تابع باشد، زمان فعلی به عنوان اولین زمان اشکال در دریافت اطلاعات به‌روزرسانی ثبت می‌شود تا در صورتی که تا ۱۴ روز بعد امکان به‌روزرسانی نباشد، اعلان بدهد
	 */
	public static void PersistUpgradeFailure() {
		SharedPreferences settings = Settings(FarayanBaseApp.getContext());

		long lastSuccessfulCheck = settings.getLong(UpgradePreferenceKeys.LastChecked.name(), 0);
		long firstFailedCheck = settings.getLong(UpgradePreferenceKeys.FirstCheckFailedMillis.name(), 0);
		long startPeriod = Math.max(lastSuccessfulCheck, firstFailedCheck);

		// if (lastSuccessfulCheck > 0 || firstFailedCheck > 0) {
		if (startPeriod > 0) {
			PersianCalendar now = new PersianCalendar();
			PersianPeriod period = new PersianPeriod(new PersianCalendar(startPeriod), now);
			if (period.TotalDays() > 14) {
				DisplayUpdateFailedNotification(FarayanBaseApp.getContext(), period.TotalDays() > 21);
			}
		}

		// if firstCheckFailedMillis is not persisted before, persist it now
		if (firstFailedCheck <= 0) {
			Editor editor = settings.edit();
			editor.putLong(UpgradePreferenceKeys.FirstCheckFailedMillis.name(), System.currentTimeMillis());
			editor.apply();
		}
	}

	/**
	 * توسط تابع PersistUpgradeFailure در صورتی که بیشتر از ۱۴ روز امکان بررسی نسخه جدید وجود نداشته باشد فراخوانی می‌شود
	 * با کلیک، کاربر به صفحه برنامه در مارکت هدایت می‌شود
	 *
	 * @param ctx
	 * @param permanent
	 */
	public static void DisplayUpdateFailedNotification(Context ctx, boolean permanent) {
		PackageInfo pi = FarayanUtility.GetPackageInfo(ctx);
		String appName = ctx.getString(ctx.getApplicationInfo().labelRes);

		Intent intent = UpgradeIntent(null, ctx);
		if (intent == null)
			return;
		PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, FLAG_IMMUTABLE);

		//Bitmap icon = ((BitmapDrawable) pi.applicationInfo.loadIcon(ctx.getPackageManager())).getBitmap();
		//Drawable icon = pi.applicationInfo.loadIcon(ctx.getPackageManager());
		//Drawable icon = pi.applicationInfo.icon.loadIcon(ctx.getPackageManager());

		String title = ctx.getResources().getString(R.string.Upgrade_UpdateFailedNotificationTitle);
		title = String.format(title, appName);

		String comment = ctx.getResources().getString(R.string.Upgrade_UpdateFailedNotificationComment);
		comment = String.format(comment, appName);

		String ticker = ctx.getResources().getString(R.string.Upgrade_UpdateFailedNotificationTicker);
		ticker = String.format(ticker, appName);

		NotificationCompat.Builder nb = new NotificationCompat.Builder(ctx)
				.setContentTitle(title)
				.setContentText(comment)
				.setContentIntent(pendingIntent)
				.setAutoCancel(true)
				.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),pi.applicationInfo.icon))
				.setOngoing(permanent)
				.setSmallIcon(pi.applicationInfo.icon)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setTicker(ticker);

		NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
		String bigText = ctx.getString(R.string.Upgrade_UpdateFailedNotificationBigText);
		bigText = String.format(bigText, appName, appName);
		style.bigText(bigText);
		nb.setStyle(style);

		NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(UpdateNotificationID, nb.build());
	}

	/**
	 * به کاربر یک پنجره برای نصب نسخه جدید نشان می‌دهد
	 * در صورتی که نسخه فعلی برابر یا بیشتر از به‌روزرسانی موجود باشد
	 * اطلاعات به‌روزرسانی را حذف می‌کند تا نسخه جدید به‌روزرسانی اعمال شود
	 * در غیر اینصورت تابع پنجره‌ای به کاربر نشان می‌دهد و می‌خواهد نسخه جدید را نصب کند
	 * در صورتی که نسخه جدید موجود نباشد ولی برنامه منقضی شده باشد باز هم پنجره نمایش داده می‌شود
	 * این تابع باید در آغاز اجرای اکتیویتی یا فرگمنت فراخوانی شود
	 *
	 * @param activity
	 * @param expiration
	 */
	public static void FragmentResumed(AppCompatActivity activity, PersianCalendar expiration) {
		if (!FarayanBaseApp.Instance().CheckUpdate())
			return;
		TryCheck(activity);
		boolean display = false;
		File installerFile = null;
		SharedPreferences settings = Settings(activity);
		String title, comment;
		UpgradeResult result = LoadUpgradeResult();
		if (result != null && result.getVersion() != null) {
			int installedVersion = FarayanUtility.GetPackageInfo(activity).versionCode;
			UpgradeTypes type = UpgradeTypes.FromServerCode(result.getType()); // UpdateTypes.valueOf(settings.getString(UpgradePreferenceKeys.Type.name(), UpdateTypes.Skip.name()));
			String installerLocation = settings.getString(UpgradePreferenceKeys.DownloadedInstallerLocation.name(), "");
			installerFile = new File(installerLocation);
			if (result.getVersion() <= installedVersion) {
				Editor editor = settings.edit();
				editor.remove(UpgradePreferenceKeys.ResultText.name());
				editor.commit();
				FarayanUtility.Log(false, true, true, "UpdateVersion (%s) is less or equal to installed version (%s), so removed", result.getVersion(), installedVersion);
				return;
			}
			display = type == UpgradeTypes.Mandatory && result.getVersion() > installedVersion;
			title = result.getTitle();
			comment = result.getComment();
		} else {
			title = "نسخه جدید";
			comment = "نسخه فعلی برنامه منقضی شده است. لطفا نسخه جدید را نصب کنید";
		}

		boolean downloadingInstaller = settings.contains(UpgradePreferenceKeys.DownloadID.name());
		boolean expired = expiration != null && expiration.before(Calendar.getInstance());

		if (!downloadingInstaller && (display || expired))
			DisplayUpdateAlert(activity, title, comment, installerFile);
	}

	/**
	 * پنجره به ‌روزرسانی را در برنامه به کاربر نشان می‌دهد
	 *
	 * @param activity
	 * @param title
	 * @param comment
	 * @param installerFile
	 */
	private static void DisplayUpdateAlert(final AppCompatActivity activity, String title, String comment, final File installerFile) {
		QuestionDialog q = new QuestionDialog(activity);
		String oldVersionTitle = activity.getResources().getString(farayan.commons.R.string.Upgrade_OldVersionTitle);
		q.setTitle(FarayanUtility.GetValueOrDefault(title, oldVersionTitle));
		String oldVersionComment = activity.getResources().getString(farayan.commons.R.string.Upgrade_OldVersionComment);
		q.setMessage(FarayanUtility.GetValueOrDefault(comment, oldVersionComment));
		q.setCancelable(false);

		String oldVersionButton = activity.getResources().getString(farayan.commons.R.string.Upgrade_OldVersionButton);

		q.setMiddleButton(oldVersionButton, new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = UpgradeIntent(installerFile, activity);
				if (intent == null) {
					FarayanUtility.ShowToast("گوگل‌پلی یا کافه‌بازار برای نصب نسخه جدید الزامی است");
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cafebazaar.ir"));
				}
				if (activity.getPackageManager().resolveActivity(intent, 0) != null)
					activity.startActivity(intent);
				else
					FarayanUtility.ShowToast("گوگل‌پلی یا کافه‌بازار برای نصب نسخه جدید الزامی است");
			}
		});
		q.show();
	}

	private static Intent UpgradeIntent(File installerFile, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (installerFile != null && installerFile.exists()) {
			intent.setDataAndType(Uri.fromFile(installerFile), "application/vnd.android.package-archive");
		} else {
			intent.setData(Uri.parse("market://details?id=" + FarayanUtility.GetPackageName(context)));
			if (context.getPackageManager().resolveActivity(intent, 0) == null)
				return null;
		}
		return intent;
	}

	/**
	 * در صورتی که جواب کارگزار به‌روزرسانی وجود داشته باشد، و نسخه فعلی از نسخه جدید کمتر باشد، نوع به‌ورزرسانی اختیاری یا اجباری باشد مقدار صحیح برمی‌گرداند وگرنه مقدار نادرست
	 * به عبارت دیگر بررسی می‌کند آیا براساس اطلاعات دریافتی، نسخه جدیدتر وجود دارد یا نه؟
	 *
	 * @param ctx
	 * @return
	 */
	private static boolean IsUpgradeAvailable(Context ctx) {
		// SharedPreferences settings = Settings(ctx);

		UpgradeResult result = LoadUpgradeResult();
		if (result == null) {
			return false;
		}

		int installedVersion = FarayanUtility.GetPackageInfo(ctx).versionCode;
		Integer upgradeVersion = result.getVersion();
		UpgradeTypes type = UpgradeTypes.FromServerCode(result.getType());

		return upgradeVersion != null &&
				(type == UpgradeTypes.Optional || type == UpgradeTypes.Mandatory) &&
				upgradeVersion > installedVersion;
	}
}
