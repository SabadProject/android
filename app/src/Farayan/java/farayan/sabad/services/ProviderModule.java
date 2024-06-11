package farayan.sabad.services;


import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import farayan.sabad.contracts.IVendorContract;

@InstallIn(ApplicationComponent.class)
@Module
public class ProviderModule
{
	@Provides
	public static IVendorContract vendorContract() {
		return new FarayanVendor();
	}
}
