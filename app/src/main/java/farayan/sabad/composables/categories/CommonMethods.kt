package farayan.sabad.composables.categories

import android.app.Activity
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.ui.InvoiceItemFormDialog
import farayan.sabad.vms.InvoiceItemFormViewModel
import farayan.sabad.vms.InvoiceItemFormViewModel.Companion.Factory

fun displayItemDialog(item: Item, activity: Activity) {
    val dialogViewModel = Factory.create(InvoiceItemFormViewModel::class.java)
    val dialog = InvoiceItemFormDialog(
        activity,
        true,
        null,
        dialogViewModel
    )
    dialog.show()
    dialog.maximize()
    dialogViewModel.init(item)
}

fun displayCategoryDialog(category: Category, activity: Activity) {
    val dialogViewModel = Factory.create(InvoiceItemFormViewModel::class.java)
    val dialog = InvoiceItemFormDialog(
        activity,
        true,
        null,
        dialogViewModel
    )
    dialog.show()
    dialog.maximize()
    dialogViewModel.init(category)
}