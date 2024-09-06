package farayan.sabad.contracts

import android.app.Activity
import android.content.Context
import farayan.commons.FarayanUtility
import farayan.commons.UI.Core.IGenericEvent

const val UpdateActivityResultCode: Int = 251478

interface IVendorContract {
    class LatestVersion(val code: Long, val name: String)

    val supportsShare: Boolean get() = false
    val supportsRate: Boolean get() = false
    val supportsUpdate: Boolean get() = false
    val nameResID: Int

    fun checkUpdate(
            context: Context,
            onCheckFinished: IGenericEvent<LatestVersion>,
            onCheckFailed: IGenericEvent<Exception>
    ) {
    }

    enum class UpdateTypes {
        ImmediateUpdate,
        FlexibleUpdate,
    }

    fun installUpdate(
            activity: Activity,
            updateType: UpdateTypes = UpdateTypes.FlexibleUpdate,
            minPriority: VendorUpdatesPriorities = VendorUpdatesPriorities.Medium,
            updateActivityResultCode: Int = UpdateActivityResultCode,
            skipDaysCount: Int = 0,
            onProgress: IGenericEvent<VendorUpdateProgress>? = null,
            onStatus: IGenericEvent<VendorUpdateStates>? = null,
            failed: IGenericEvent<Exception>? = null
    ) {

    }

    fun shareUrl(): String {
        return "market://details?id=${FarayanUtility.GetPackageName()}"
    }
    fun rate(activity: Activity) {

    }

    fun stopUpdateCheck(activity: Activity) {

    }
}