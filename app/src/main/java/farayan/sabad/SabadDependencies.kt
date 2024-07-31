package farayan.sabad

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import farayan.sabad.db.ProductBarcodeQueries
import farayan.sabad.db.SabadPersistence


@Module
@InstallIn(SingletonComponent::class)
class SabadDependencies {
    @Provides
    fun persistence(): SabadPersistence {
        return SabadPersistence(
            AndroidSqliteDriver(SabadPersistence.Schema, SabadTheApp.getContext(), "sabad.sqdb"),
        )
    }

    @Provides
    fun productQueries(db: SabadPersistence): ProductBarcodeQueries {
        return db.productBarcodeQueries
    }
}