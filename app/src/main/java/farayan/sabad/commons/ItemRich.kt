package farayan.sabad.commons

import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Product

data class ItemRich(val item: Item, val product: Product, val category: Category)