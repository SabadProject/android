package farayan.sabad.contracts

enum class VendorUpdateStates(val CommentResID: Int) {
    Unknown(0),
    Pending(0),
    Installing(0),
    Installed(0),
    Failed(0),
    Downloading(0),
    Downloaded(0),
    Canceled(0);
}