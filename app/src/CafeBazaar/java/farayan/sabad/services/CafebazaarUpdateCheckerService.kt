package farayan.sabad.services

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.farsitel.bazaar.IUpdateCheckService
import farayan.commons.FarayanUtility
import farayan.commons.UI.Core.IGenericEvent
import farayan.sabad.contracts.IVendorContract


class UpdateCheckerService(
    private val onCheckFinished: IGenericEvent<IVendorContract.LatestVersion>,
    private val onCheckFailed: IGenericEvent<java.lang.Exception>
) : ServiceConnection {
    private var cafeBazaarUpdateService: IUpdateCheckService? = null

    override fun onServiceConnected(name: ComponentName?, boundService: IBinder?) {
        cafeBazaarUpdateService = IUpdateCheckService.Stub.asInterface(boundService)
        if (cafeBazaarUpdateService == null)
            return
        try {
            val availableVersionCode: Long = cafeBazaarUpdateService!!.getVersionCode(FarayanUtility.GetPackageName())
            val installedVersionCode: Long = FarayanUtility.GetPackageVersion()

            if (installedVersionCode < availableVersionCode) {
                onCheckFinished.Fire(IVendorContract.LatestVersion(availableVersionCode, ""))
            } else {
                onCheckFinished.Fire(null)
            }
        } catch (e: Exception) {
            onCheckFailed.Fire(e)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        cafeBazaarUpdateService = null
    }

    companion object {
        lateinit var connection: UpdateCheckerService
        fun destroyUpdateCheck(context: Context) {
            context.unbindService(connection)
        }
    }
}