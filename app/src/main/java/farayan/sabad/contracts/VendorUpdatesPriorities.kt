package farayan.sabad.contracts

enum class VendorUpdatesPriorities(val Code: Int) {
    Critical(5),
    High(4),
    Medium(3),
    Low(2),
    None(1);
}