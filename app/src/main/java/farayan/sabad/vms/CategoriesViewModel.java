package farayan.sabad.vms;

import androidx.lifecycle.ViewModel;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;

public class CategoriesViewModel extends ViewModel
{
	private IGroupRepo TheRepo;

	public  CategoriesViewModel(IGroupRepo iGroupRepo){
		TheRepo= iGroupRepo;
	}
}
