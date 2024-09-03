package farayan.commons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpgradeBooted extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		UpgradeUtility.RegisterUpgradeAlarm(ctx, ((FarayanBaseApp) ctx.getApplicationContext()).UpgradeAlarmType());
	}
}
