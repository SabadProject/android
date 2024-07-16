
package farayan.sabad;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.sanid.lib.debugghost.DebugGhostBridge;

import dagger.hilt.android.HiltAndroidApp;
import farayan.commons.FarayanBaseApp;
import farayan.sabad.SabadTheApp;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

@HiltAndroidApp
public class SabadApp extends SabadTheApp
{

	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initialize(Stetho
				.newInitializerBuilder(getApplicationContext())
				.enableDumpapp(() -> new Stetho
						.DefaultDumperPluginsBuilder(getApplicationContext())
						//.provide(new MyDumperPlugin())
						.finish())
				.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
				.build()
		);
	}
}
