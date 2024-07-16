package farayan.sabad.services;


import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import farayan.sabad.contracts.IVendorContract;

@Module
@InstallIn(SingletonComponent.class)
public class ProviderModule
{
	@Provides
	public static IVendorContract vendorContract() {
		return new FarayanVendor();
	}
}
