package farayan.sabad.commons

import farayan.sabad.core.commons.Money

data class InvoiceSummary(val pickedItemsCount: Long, val remainedItemsCount: Long, val pickedQuantitiesSum: Long, val price: Money?)