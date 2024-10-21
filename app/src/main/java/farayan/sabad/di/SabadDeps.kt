package farayan.sabad.di

import android.util.Log
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import farayan.sabad.SabadTheApp
import farayan.sabad.db.SabadPersistence
import farayan.sabad.repo.BarcodeRepo
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.repo.InvoiceRepo
import farayan.sabad.repo.ItemRepo
import farayan.sabad.repo.PriceRepo
import farayan.sabad.repo.PhotoRepo
import farayan.sabad.repo.ProductRepo
import farayan.sabad.repo.UnitRepo
import farayan.sabad.utility.displayable
import farayan.sabad.utility.queryable

object SabadDeps {
    private lateinit var db: SabadPersistence

    private fun persistence(): SabadPersistence {
        if (!this::db.isInitialized) {
            val driver = AndroidSqliteDriver(YourDatabase.Schema, context, "sabad.sqdb")
            db = SabadPersistence(driver)
            ensureCategories(db)
        }
        return db
    }

    private fun ensureCategories(db: SabadPersistence) {
        createCategory("آب هندوانه")
        createCategory("پودر لبنه‌ی قهوه")
        createCategory("خوشبوکننده")
        createCategory("آبلیمو")
        createCategory("نمک")
        createCategory("خیارشور")
        createCategory("مایع ظرفشویی")
        createCategory("مایع دستشویی")
        createCategory("روغن")
        createCategory("کره")
        createCategory("قارچ")
        createCategory("سیب‌زمینی")
        createCategory("پیاز")
        createCategory("خیار")
        createCategory("گوجه‌فرنگی")
        createCategory("چای")
        createCategory("قهوه")
        createCategory("نسکافه")
        createCategory("دستمال کاغذی")
        createCategory("دستمال توالت")
        createCategory("قند")
        createCategory("شکر")
        createCategory("نبات")
        createCategory("آبنبات/شکرپنیر")
        createCategory("سس مایونز")
        createCategory("سس فرانسوی")
        createCategory("سس هزارجزیره")
        createCategory("رب گوجه‌فرنگی")
        createCategory("رب انار")
        createCategory("سیب")
        createCategory("هلو")
        createCategory("انار")
        createCategory("انگور")
        createCategory("پرتغال")
        createCategory("لیمو")
        createCategory("لیمو امانی")
        createCategory("لوبیاقرمز")
        createCategory("لوبیاچیتی")
        createCategory("لپه")
        createCategory("نخود")
        createCategory("گوشت چرخ‌کرده گاو")
        createCategory("گوشت خورشتی گاو")
        createCategory("مرغ")
        createCategory("ران مرغ")
        createCategory("سینه‌ی مرغ")
        createCategory("نوشابه")
        createCategory("نوشابه‌ی رژیمی")
        createCategory("دلستر لیمویی")
        createCategory("دلستر هلو")
        createCategory("فلفل سیاه")
        createCategory("فلفل قرمز")
        createCategory("فلفل دلمه‌ای")
        createCategory("ماست")
        createCategory("شیر کم‌چرب")
        createCategory("شیر پرچرب")
        createCategory("شیر ماندگار")
        createCategory("پودر کیک")
        createCategory("آرد")
        createCategory("پنیر پیتزا")
        createCategory("شکلات")
    }

    private fun createCategory(name: String) {
        db.categoryQueries.byNameIncludingDeleted(name.queryable()).executeAsOneOrNull() ?: db.categoryQueries.create(name.displayable(), name.queryable(), "", "", false, index++)
    }

    fun categoryRepo(): CategoryRepo = CategoryRepo(persistence().categoryQueries)

    private val invoiceRepoValue by lazy { InvoiceRepo(persistence().invoiceQueries) }

    fun invoiceRepo(): InvoiceRepo = invoiceRepoValue

    private val itemRepoValue by lazy { ItemRepo(persistence().itemQueries) }

    fun itemRepo(): ItemRepo = itemRepoValue

    fun productRepo(): ProductRepo = ProductRepo(persistence().productQueries)

    fun barcodeRepo(): BarcodeRepo = BarcodeRepo(persistence().barcodeQueries)

    fun photoRepo(): PhotoRepo = PhotoRepo(persistence().photoQueries)

    fun unitRepo(): UnitRepo = UnitRepo(persistence().unitQueries)

    fun priceRepo(): PriceRepo = PriceRepo(persistence().priceQueries)

    private var index = 0L
}