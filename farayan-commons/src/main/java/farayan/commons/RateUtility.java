package farayan.commons;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class RateUtility {

	enum RatePreferenceKeys {
		Rated
	}

	public static boolean DisplayRate(final Activity activity)
	{
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean rated = settings.getBoolean(RatePreferenceKeys.Rated.name(), false);
		if (!rated) {
			final Dialog rateDialog = new Dialog(activity);
			rateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			rateDialog.setContentView(R.layout.dialog_rate);

			PackageInfo pi = FarayanUtility.GetPackageInfo(activity);
			TextView rateCommentTextView = (TextView) rateDialog.findViewById(R.id.RateCommentTextView);
			String rateText = activity.getResources().getString(R.string.rate_text);
			rateText = String.format(rateText, activity.getPackageManager().getApplicationLabel(pi.applicationInfo));
			rateCommentTextView.setText(rateText);
			rateCommentTextView.setTypeface(FarayanBaseApp.getFont());

			ImageButton rateButton = (ImageButton) rateDialog.findViewById(R.id.RateImageButton);
			rateButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Editor editor = settings.edit();
					editor.putBoolean(RatePreferenceKeys.Rated.name(), true);
					editor.apply();
					GoRate(activity);
				}
			});

			ImageButton notNowButton = (ImageButton) rateDialog.findViewById(R.id.NotNowImageButton);
			notNowButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						activity.finish();
					} catch (Exception e) {
						activity.finish();
					}
				}
			});

			rateDialog.show();
			return true;
		} else {
			return false;
		}
	}

	public static void GoRate(Activity activity) {
		String action = Intent.ACTION_VIEW;
		// if cafebazaar is the only and you want to comment directly
		// String action = Intent.ACTION_EDIT;
		Intent commentIntent = new Intent(action);
		String data = "market://details?id=" + FarayanUtility.GetPackageName(activity);
		Uri uri = Uri.parse(data);
		commentIntent.setData(uri);
		if (commentIntent.resolveActivity(activity.getPackageManager()) != null) {
			activity.startActivity(Intent.createChooser(commentIntent, "فروشگاه را انتخاب کنید"));
		} else {
			Intent detailsIntent = new Intent(Intent.ACTION_VIEW);
			String url = String.format("http://play.google.com/store/apps/details?id=%s", FarayanUtility.GetPackageName(activity));
			detailsIntent.setData(Uri.parse(url));
			activity.startActivity(detailsIntent);
		}
	}
}
