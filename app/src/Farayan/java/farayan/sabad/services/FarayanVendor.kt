package farayan.sabad.services

import farayan.sabad.contracts.IVendorContract
import farayan.sabad.R
class FarayanVendor : IVendorContract {
    override val nameResID: Int
        get() = R.string.farayanVendorName
}