package farayan.sabad.vms

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import farayan.sabad.core.OnePlace.Group.IGroupRepo
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class InvoiceItemFormViewModel @Inject constructor(
    private val groupRepo: IGroupRepo,
    private val itemRepo: IInvoiceItemRepo,
) : ViewModel() {
    var groups = MutableStateFlow(groupRepo.All(groupRepo.NewParams()))
    var pickedItems = MutableStateFlow(itemRepo.pickings())
}