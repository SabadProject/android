package farayan.sabad.contracts

class VendorUpdateProgress(
        val bytesDownloaded: Long,
        val totalBytesToDownload: Long,
        val state: VendorUpdateStates,
        val terminated: Boolean,
        val error: Int
)