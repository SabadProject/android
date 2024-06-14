package farayan.sabad.services

import farayan.sabad.R
import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.*
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import farayan.commons.Exceptions.Rexception
import farayan.commons.FarayanUtility
import farayan.commons.UI.Core.IGenericEvent
import farayan.sabad.contracts.IVendorContract
import farayan.sabad.contracts.VendorUpdateProgress
import farayan.sabad.contracts.VendorUpdateStates
import farayan.sabad.contracts.VendorUpdatesPriorities
import java.lang.Exception

class GooglePlayVendorService : IVendorContract {
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var availableVersion: AppUpdateInfo

    override fun checkUpdate(context: Context, onCheckFinished: IGenericEvent<IVendorContract.LatestVersion>, onCheckFailed: IGenericEvent<Exception>) {
        appUpdateManager = AppUpdateManagerFactory.create(context)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnFailureListener { exception -> onCheckFailed.Fire(exception) }
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            availableVersion = appUpdateInfo
            val versionCode = appUpdateInfo.availableVersionCode().toLong()
            val versionName = appUpdateInfo.availableVersionCode().toString()
            val latest = IVendorContract.LatestVersion(versionCode, versionName)
            onCheckFinished.Fire(latest)
        }
    }

    override fun installUpdate(
        activity: Activity,
        updateType: IVendorContract.UpdateTypes,
        minPriority: VendorUpdatesPriorities,
        updateActivityResultCode: Int,
        skipDaysCount: Int,
        onProgress: IGenericEvent<VendorUpdateProgress>?,
        onStatus: IGenericEvent<VendorUpdateStates>?,
        failed: IGenericEvent<Exception>?
    ) {
        val updated = availableVersion.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
        if (!updated)
            return
        val updateTypeValue = getUpdateTypeValue(updateType)
        if (!availableVersion.isUpdateTypeAllowed(updateTypeValue))
            return
        val skippedDaysCount = this.availableVersion.clientVersionStalenessDays() ?: 0
        if (skippedDaysCount < skipDaysCount)
            return
        if (availableVersion.updatePriority() < minPriority.Code)
            return
        appUpdateManager.startUpdateFlow(
                availableVersion,
                activity,
                AppUpdateOptions.newBuilder(updateTypeValue).build()
        )
        appUpdateManager.registerListener { state ->
            when (state.installStatus) {
                InstallStatus.DOWNLOADING ->
                    onProgress?.Fire(VendorUpdateProgress(
                            state.bytesDownloaded,
                            state.totalBytesToDownload,
                            translateStatus(state.installStatus),
                            state.hasTerminalStatus,
                            state.installErrorCode
                    ))
                InstallStatus.CANCELED,
                InstallStatus.DOWNLOADED,
                InstallStatus.FAILED,
                InstallStatus.INSTALLED,
                InstallStatus.PENDING,
                InstallStatus.UNKNOWN,
                InstallStatus.INSTALLING ->
                    onStatus?.Fire(translateStatus(state.installStatus()))
            }
        }
    }

    override val nameResID: Int
        get() = R.string.googlePlayVendor

    override fun shareUrl(): String {
        return "https://play.google.com/store/apps/details?id=${FarayanUtility.GetPackageName()}"
    }

    private fun translateStatus(installStatus: Int): VendorUpdateStates {
        return when (installStatus) {
            InstallStatus.UNKNOWN -> VendorUpdateStates.Unknown
            InstallStatus.PENDING -> VendorUpdateStates.Pending
            InstallStatus.INSTALLING -> VendorUpdateStates.Installing
            InstallStatus.INSTALLED -> VendorUpdateStates.Installed
            InstallStatus.FAILED -> VendorUpdateStates.Failed
            InstallStatus.DOWNLOADING -> VendorUpdateStates.Downloading
            InstallStatus.DOWNLOADED -> VendorUpdateStates.Downloaded
            InstallStatus.CANCELED -> VendorUpdateStates.Canceled
            else -> VendorUpdateStates.Unknown
        }
    }

    private fun getUpdateTypeValue(updateType: IVendorContract.UpdateTypes): Int {
        when (updateType) {
            IVendorContract.UpdateTypes.FlexibleUpdate -> AppUpdateType.FLEXIBLE
            IVendorContract.UpdateTypes.ImmediateUpdate -> AppUpdateType.IMMEDIATE
        }
        throw Rexception(null, "")
    }

    override fun rate(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { }
            }
        }
    }
}