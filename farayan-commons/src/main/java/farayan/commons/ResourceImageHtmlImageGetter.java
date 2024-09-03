package farayan.commons;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;

/**
 * Created by Homayoun on 23/01/2017.
 */

public class ResourceImageHtmlImageGetter implements Html.ImageGetter {
	android.app.Activity ActivityInstance;

	public ResourceImageHtmlImageGetter(Activity activity) {
		this.ActivityInstance = activity;
	}

	@Override
	public Drawable getDrawable(String s) {
		if(FarayanUtility.IsNullOrEmpty(s))
			return null;
		s = s.toLowerCase();
		if(s.startsWith("@drawable/")) {
			String drawableName = s.substring("@drawable/".length());
			drawableName = FarayanUtility.Trim(drawableName, "/", false, true);
			int drawableResourceId = ActivityInstance.getResources().getIdentifier(drawableName, "drawable", FarayanUtility.GetPackageName(ActivityInstance));
			if(drawableResourceId <= 0)
				return null;
			Drawable drawable = ActivityInstance.getResources().getDrawable(drawableResourceId);

			DisplayMetrics displaymetrics = new DisplayMetrics();
			ActivityInstance.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			double windowHeight = displaymetrics.heightPixels;
			double windowWidth = displaymetrics.widthPixels;

			double imageHeight = drawable.getIntrinsicHeight();
			double imageWidth = drawable.getIntrinsicWidth();

			double heightCoefficient = windowHeight / imageHeight;
			double widthCoefficient = windowWidth / imageWidth;
			double coefficient = Math.min(heightCoefficient, widthCoefficient);

			if(coefficient < 1) {
				imageWidth *= coefficient;
				imageHeight *= coefficient;
			}

			drawable.setBounds(0, 0, (int) imageWidth, (int) imageHeight);
			return drawable;
		}
		return null;
	}
}
