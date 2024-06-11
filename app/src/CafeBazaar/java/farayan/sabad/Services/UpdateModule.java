package farayan.sabad.services;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import farayan.sabad.contracts.IVendorContract;
import farayan.sabad.services.CafeBazaarVendor;

@InstallIn(ApplicationComponent.class)
@Module
public class UpdateModule
{
	@Provides
	public IVendorContract updateCheckerContract(){
		return new CafeBazaarVendor();
	}
}
