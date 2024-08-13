package farayan.sabad

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import farayan.sabad.db.SabadPersistence
import farayan.sabad.repo.BarcodeRepo
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.InvoiceItemRepo
import farayan.sabad.repo.PriceRepo
import farayan.sabad.repo.ProductPhotoRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo


@Module
@InstallIn(SingletonComponent::class)
class SabadDependencies {
    private lateinit var db: SabadPersistence

    @Provides
    fun persistence(): SabadPersistence {
        if (!this::db.isInitialized)
            db = SabadPersistence(AndroidSqliteDriver(SabadPersistence.Schema, SabadTheApp.getContext(), "sabad.sqdb"))
        return db
    }

    @Provides
    fun categoryRepo(): CategoryRepo = CategoryRepo(persistence().categoryQueries)

    @Provides
    fun invoiceItemRepo(): InvoiceItemRepo = InvoiceItemRepo(persistence().invoiceItemQueries)

    @Provides
    fun productRepo(): ProductRepo = ProductRepo(persistence().productQueries)

    @Provides
    fun barcodeRepo(): BarcodeRepo = BarcodeRepo(persistence().barcodeQueries)

    @Provides
    fun photoRepo(): ProductPhotoRepo = ProductPhotoRepo(persistence().photoQueries)

    @Provides
    fun unitRepo(): UnitRepo = UnitRepo(persistence().unitQueries)

    @Provides
    fun priceRepo(): PriceRepo = PriceRepo(persistence().priceQueries)
}