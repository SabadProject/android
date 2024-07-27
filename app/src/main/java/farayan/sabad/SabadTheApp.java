package farayan.sabad;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import farayan.commons.FarayanBaseApp;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public abstract class SabadTheApp extends FarayanBaseApp {
    private static SabadDatabase db;

    public static SabadDatabase DB() {
        if (db == null)
            db = OpenHelperManager.getHelper(getContext(), SabadDatabase.class);
        return db;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        DB().getWritableDatabase();
        //SabadSQLDelight database = new SabadSQLDelight(AndroidSqliteDriver(SabadSQLDelight.Schema, context, "Database.db"));

        InitFonts();
    }

    private void InitFonts() {
        ViewPump.init(ViewPump
                .builder()
                .addInterceptor(
                        new CalligraphyInterceptor(
                                new CalligraphyConfig
                                        .Builder()
                                        .setDefaultFontPath("fonts/vazir.ttf")
                                        .setFontAttrId(R.attr.fontPath)
                                        .build()
                        )
                )
                .build()
        );
    }
}
