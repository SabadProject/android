package farayan.sabad.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import farayan.sabad.di.SabadDeps
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.InvoiceRepo
import farayan.sabad.repo.ItemRepo
import farayan.sabad.repo.PhotoRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo

class InvoicesViewModel(
    private val invoiceRepo: InvoiceRepo,
    private val itemRepo: ItemRepo,
    private val categoryRepo: CategoryRepo,
    private val photoRepo: PhotoRepo,
    private val productRepo: ProductRepo,
    private val unitRepo: UnitRepo,
) : ViewModel() {
    val invoices = invoiceRepo.allFlow
    val items = itemRepo.allFlow
    val categories = categoryRepo.allFlow
    val products = productRepo.allFlow
    val photos = photoRepo.allFlow
    val units = unitRepo.allFlow

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                return InvoicesViewModel(
                    SabadDeps.invoiceRepo(),
                    SabadDeps.itemRepo(),
                    SabadDeps.categoryRepo(),
                    SabadDeps.photoRepo(),
                    SabadDeps.productRepo(),
                    SabadDeps.unitRepo(),
                ) as T
            }
        }
    }
}
