package farayan.sabad.vms;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;

public class CategoriesViewModel extends ViewModel
{
	private IGroupRepo TheRepo;

	@ViewModelInject
	public  CategoriesViewModel(IGroupRepo iGroupRepo){
		TheRepo= iGroupRepo;
	}
}
