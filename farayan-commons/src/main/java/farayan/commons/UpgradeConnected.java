package farayan.commons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class UpgradeConnected extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();

		Log.i("NETWORK", "UpgradeConnected");
		if (network != null)
			Log.i("NETWORK", String.format("network isConnected: %s, isAvailable: %s, state: %s", network.isConnected(), network.isAvailable(), network.getDetailedState().name()));

		if (network != null && network.isAvailable())
			UpgradeUtility.TryCheck(ctx);
	}
}
