package farayan.sabad.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import farayan.commons.FarayanUtility
import farayan.commons.UI.Core.IGenericEvent
import farayan.sabad.R
import farayan.sabad.contracts.IVendorContract
import farayan.sabad.contracts.VendorUpdateProgress
import farayan.sabad.contracts.VendorUpdateStates
import farayan.sabad.contracts.VendorUpdatesPriorities

class CafeBazaarVendor : IVendorContract {
    override val nameResID: Int get() = R.string.cafeBazaar

    override val supportsUpdate: Boolean
        get() = true

    override fun checkUpdate(
            context: Context,
            onCheckFinished: IGenericEvent<IVendorContract.LatestVersion>,
            onCheckFailed: IGenericEvent<Exception>
    ) {
        UpdateCheckerService.connection = UpdateCheckerService(onCheckFinished, onCheckFailed)
        val intent = Intent("com.farsitel.bazaar.service.UpdateCheckService.BIND")
        intent.setPackage("com.farsitel.bazaar")
        context.bindService(intent, UpdateCheckerService.connection, Context.BIND_AUTO_CREATE)
    }

    override fun stopUpdateCheck(activity: Activity) {
        UpdateCheckerService.destroyUpdateCheck(activity)
    }

    override fun installUpdate(activity: Activity, updateType: IVendorContract.UpdateTypes, minPriority: VendorUpdatesPriorities, updateActivityResultCode: Int, skipDaysCount: Int, onProgress: IGenericEvent<VendorUpdateProgress>?, onStatus: IGenericEvent<VendorUpdateStates>?, failed: IGenericEvent<Exception>?) {
        startBazaarIntent(activity, Intent.ACTION_VIEW)
    }

    private fun startBazaarIntent(activity: Activity, action: String) {
        val intent = Intent(action)
        val bazaarUrl = "bazaar://details?id=" + FarayanUtility.GetPackageName()
        intent.data = Uri.parse(bazaarUrl)
        intent.`package` = "com.farsitel.bazaar"
        activity.startActivity(intent)
    }

    override val supportsShare: Boolean
        get() = true

    override fun shareUrl(): String {
        return "https://cafebazaar.ir/app/${FarayanUtility.GetPackageName()}"
    }

    override val supportsRate: Boolean
        get() = true

    override fun rate(activity: Activity) {
        startBazaarIntent(activity, Intent.ACTION_EDIT)
    }
}