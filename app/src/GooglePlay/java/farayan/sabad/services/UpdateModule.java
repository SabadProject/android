package farayan.sabad.services;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import farayan.sabad.contracts.IVendorContract;
import farayan.sabad.services.GooglePlayVendorService;

@InstallIn(SingletonComponent.class)
@Module
public class UpdateModule
{
	@Provides
	public IVendorContract updateCheckerContract(){
		return new GooglePlayVendorService();
	}
}
