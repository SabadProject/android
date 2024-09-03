package farayan.commons;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpgradeAlarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Log.i("alarm", "alarm executed @ " + Calendar.getInstance().getTimeInMillis());
		UpgradeUtility.TryCheck(ctx);
		UpgradeUtility.DisplayUpgradeAvailableNotification(ctx);
	}
}
