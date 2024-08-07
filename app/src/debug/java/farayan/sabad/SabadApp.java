package farayan.sabad;

import com.facebook.stetho.Stetho;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class SabadApp extends SabadTheApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho
                .newInitializerBuilder(getApplicationContext())
                .enableDumpapp(() -> new Stetho
                        .DefaultDumperPluginsBuilder(getApplicationContext())
                        .finish())
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
                .build()
        );
    }
}
