package farayan.commons.Commons;

import android.app.Activity;
import android.content.Intent;

public interface IActivityResultCallback {
	/**
	 * تابع زمانی فراخوانده می‌شود که جواب اکتیوتی دریافت شده باشد
	 *
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return مثبت در صورتی که تقاضا مرتبط باشد و منفی در صورتی که تقاضا نامرتبط باشد
	 */
	boolean OnActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
}
