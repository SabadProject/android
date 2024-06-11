package farayan.sabad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import farayan.commons.QueryBuilderCore.BaseEntity;
import farayan.commons.QueryBuilderCore.BaseParams;
import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.IRepo;
import farayan.commons.QueryBuilderCore.RelationalOperators;
import farayan.commons.Utilities.DatabaseUtility;
import farayan.sabad.models.Category.CategoryRepo;
import farayan.sabad.models.CategoryGroup.CategoryGroupRepo;
import farayan.sabad.models.Group.GroupRepo;
import farayan.sabad.models.GroupUnit.GroupUnitRepo;
import farayan.sabad.models.Invoice.InvoiceRepo;
import farayan.sabad.models.InvoiceItem.InvoiceItemRepo;
import farayan.sabad.models.NeedChange.NeedChangeRepo;
import farayan.sabad.models.Product.ProductRepo;
import farayan.sabad.models.ProductBarcode.ProductBarcodeRepo;
import farayan.sabad.models.Store.StoreRepo;
import farayan.sabad.models.StoreCategory.StoreCategoryRepo;
import farayan.sabad.models.StoreGroup.StoreGroupRepo;
import farayan.sabad.models.Unit.UnitRepo;
import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.CategoryGroup.CategoryGroupEntity;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitEntity;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.OnePlace.NeedChange.NeedChangeEntity;
import farayan.sabad.core.OnePlace.Product.ProductEntity;
import farayan.sabad.core.OnePlace.ProductBarcode.ProductBarcodeEntity;
import farayan.sabad.core.OnePlace.Store.StoreEntity;
import farayan.sabad.core.OnePlace.StoreCategory.StoreCategoryEntity;
import farayan.sabad.core.OnePlace.StoreGroup.StoreGroupEntity;
import farayan.sabad.core.OnePlace.Unit.UnitEntity;


public class SabadDatabase extends OrmLiteSqliteOpenHelper
{
	public static final String Name = "Sabad";

	public SabadDatabase(Context context) {
		super(context, Name, null, Version);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource cs) {
		try {
			TableUtils.createTable(cs, GroupEntity.class);
			TableUtils.createTable(cs, InvoiceEntity.class);
			TableUtils.createTable(cs, InvoiceItemEntity.class);
			TableUtils.createTable(cs, ProductEntity.class);
			TableUtils.createTable(cs, ProductBarcodeEntity.class);
			TableUtils.createTable(cs, GroupUnitEntity.class);
			TableUtils.createTable(cs, UnitEntity.class);
			TableUtils.createTable(cs, StoreEntity.class);
			TableUtils.createTable(cs, StoreGroupEntity.class);
			TableUtils.createTable(cs, StoreCategoryEntity.class);
			TableUtils.createTable(cs, CategoryEntity.class);
			TableUtils.createTable(cs, NeedChangeEntity.class);
			TableUtils.createTable(cs, CategoryGroupEntity.class);

			DAOs daOs = new DAOs(
					getRuntimeExceptionDao(GroupEntity.class),
					getRuntimeExceptionDao(GroupUnitEntity.class),
					getRuntimeExceptionDao(CategoryGroupEntity.class),
					getRuntimeExceptionDao(ProductBarcodeEntity.class),
					getRuntimeExceptionDao(UnitEntity.class)
			);

			UnitEntity bottle = new UnitEntity("بطری", "", 1);
			UnitEntity kg = new UnitEntity("کیلوگرم", "", 1000);
			UnitEntity g = new UnitEntity("گرم", "", 1);
			UnitEntity litre = new UnitEntity("لیتر", "", 1000);
			UnitEntity can = new UnitEntity("قوطی", "", 1);
			UnitEntity pack = new UnitEntity("بسته", "", 1);

			CategoryEntity grocery = new CategoryEntity("خواروبار");
			CategoryEntity cucumbers = new CategoryEntity("صیفی‌جات");
			CategoryEntity vegetables = new CategoryEntity("سبزیجات");
			CategoryEntity drinks = new CategoryEntity("نوشیدنی");
			CategoryEntity dairy = new CategoryEntity("لبنیات");
			CategoryEntity fruits = new CategoryEntity("میوه‌جات");
			CategoryEntity sanitary = new CategoryEntity("بهداشتی");
			CategoryEntity food = new CategoryEntity("خوراکی");
			CategoryEntity edible = new CategoryEntity("خوردنی");
			CategoryEntity culinary = new CategoryEntity("پختنی");
			CategoryEntity victual = new CategoryEntity("آذوقه");
			CategoryEntity dressing = new CategoryEntity("چاشنی");
			CategoryEntity spices = new CategoryEntity("ادویه");
			daOs.UnitDAO.create(Arrays.asList(bottle, kg, g, litre, can, pack));

			init(
					daOs,
					new GroupEntity("KEY", "آبلیمو", 1, "تعداد"),
					Arrays.asList(bottle),
					Arrays.asList(spices, dressing, victual, culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "نمک", 1, "حجم یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(spices, culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "خیارشور", 1, "وزن یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(spices)
			);
			init(
					daOs,
					new GroupEntity("KEY", "مایع ظرفشویی", 1, "تعداد"),
					Arrays.asList(can, kg, g),
					Arrays.asList(sanitary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "مایع دستشویی", 1, "تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(sanitary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "روغن", 10, "حجم یا تعداد"),
					Arrays.asList(can, kg, g),
					Arrays.asList(culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "کره", 20, "حجم یا تعداد"),
					Arrays.asList(can, kg, g),
					Arrays.asList(dairy)
			);
			init(
					daOs,
					new GroupEntity("KEY", "قارچ", 15, "حجم یا تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(sanitary, vegetables, cucumbers)
			);
			init(
					daOs,
					new GroupEntity("KEY", "سیب‌زمینی", 20, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(cucumbers, vegetables)
			);
			init(
					daOs,
					new GroupEntity("KEY", "پیاز", 20, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(cucumbers, vegetables)
			);
			init(
					daOs,
					new GroupEntity("KEY", "خیار", 20, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(cucumbers, vegetables)
			);
			init(
					daOs,
					new GroupEntity("KEY", "گوجه‌فرنگی", 20, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(cucumbers, vegetables)
			);
			init(
					daOs,
					new GroupEntity("KEY", "چای", 10, "حجم یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(drinks)
			);
			init(
					daOs,
					new GroupEntity("KEY", "قهوه", 10, "حجم یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(drinks)
			);
			init(
					daOs,
					new GroupEntity("KEY", "نسکافه", 10, "نعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(drinks)
			);
			init(
					daOs,
					new GroupEntity("KEY", "دستمال کاغذی", 15, "تعداد"),
					Arrays.asList(pack),
					Arrays.asList(sanitary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "دستمال توالت", 15, "تعداد"),
					Arrays.asList(pack),
					Arrays.asList(sanitary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "قند", 10, "تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(drinks)
			);
			init(
					daOs,
					new GroupEntity("KEY", "شکر", 10, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(drinks, culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "نبات", 5, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(drinks)
			);
			init(
					daOs,
					new GroupEntity("KEY", "آبنبات/شکرپنیر", 5, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(drinks)
			);
			init(
					daOs,
					new GroupEntity("KEY", "سس مایونز", 15, "تعداد"),
					Arrays.asList(can),
					Arrays.asList(dressing)
			);
			init(
					daOs,
					new GroupEntity("KEY", "سس فرانسوی", 15, "تعداد"),
					Arrays.asList(can),
					Arrays.asList(dressing)
			);
			init(
					daOs,
					new GroupEntity("KEY", "سس هزارجزیره", 15, "تعداد"),
					Arrays.asList(can),
					Arrays.asList(dressing)
			);
			init(
					daOs,
					new GroupEntity("KEY", "رب گوجه‌فرنگی", 10, "تعداد"),
					Arrays.asList(can),
					Arrays.asList(culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "رب انار", 5, "تعداد"),
					Arrays.asList(can),
					Arrays.asList(dressing, culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "سیب", 15, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(fruits)
			);
			init(
					daOs,
					new GroupEntity("KEY", "هلو", 15, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(fruits)
			);
			init(
					daOs,
					new GroupEntity("KEY", "انار", 15, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(fruits)
			);
			init(
					daOs,
					new GroupEntity("KEY", "انگور", 15, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(fruits)
			);
			init(
					daOs,
					new GroupEntity("KEY", "پرتغال", 15, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(fruits)
			);
			init(
					daOs,
					new GroupEntity("KEY", "لیمو", 15, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(fruits)
			);
			init(
					daOs,
					new GroupEntity("KEY", "لیمو امانی", 10, "وزن یا تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(spices, culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "لوبیاقرمز", 10, "وزن یا تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "لوبیاچیتی", 10, "وزن یا تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "لپه", 10, "وزن یا تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "نخود", 10, "وزن یا تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "گوشت چرخ‌کرده گاو", 15, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "گوشت خورشتی گاو", 15, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "مرغ", 15, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "ران مرغ", 15, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "سینه‌ی مرغ", 15, "وزن"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "نوشابه", 20, "تعداد"),
					Arrays.asList(bottle, litre),
					Arrays.asList(drinks, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "نوشابه‌ی رژیمی", 20, "تعداد"),
					Arrays.asList(bottle, litre),
					Arrays.asList(drinks, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "دلستر لیمویی", 15, "تعداد"),
					Arrays.asList(bottle, litre),
					Arrays.asList(drinks, grocery)
			);
			init(
					daOs,
					new GroupEntity("KEY", "دلستر هلو", 15, "تعداد"),
					Arrays.asList(bottle),
					Arrays.asList(drinks)
			);
			init(
					daOs,
					new GroupEntity("KEY", "فلفل سیاه", 15, "وزن یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(spices)
			);
			init(
					daOs,
					new GroupEntity("KEY", "فلفل قرمز", 5, "وزن یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(spices)
			);
			init(
					daOs,
					new GroupEntity("KEY", "فلفل دلمه‌ای", 10, "وزن"),
					Arrays.asList(kg, g),
					Arrays.asList(spices)
			);
			init(
					daOs,
					new GroupEntity("KEY", "ماست", 20, "تعداد"),
					Arrays.asList(can, kg, g, litre),
					Arrays.asList(dairy)
			);
			init(
					daOs,
					new GroupEntity("KEY", "شیر کم‌چرب", 20, "وزن یا تعداد"),
					Arrays.asList(can, kg, g, litre),
					Arrays.asList(dairy)
			);
			init(
					daOs,
					new GroupEntity("KEY", "شیر پرچرب", 20, "وزن یا تعداد"),
					Arrays.asList(can, kg, g, litre),
					Arrays.asList(dairy)
			);
			init(
					daOs,
					new GroupEntity("KEY", "شیر ماندگار", 15, "وزن یا تعداد"),
					Arrays.asList(can, kg, g, litre),
					Arrays.asList(dairy)
			);
			init(
					daOs,
					new GroupEntity("KEY", "پودر کیک", 10, "تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "آرد", 5, "وزن یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "پنیر پیتزا", 10, "وزن یا تعداد"),
					Arrays.asList(pack, kg, g),
					Arrays.asList(culinary)
			);
			init(
					daOs,
					new GroupEntity("KEY", "شکلات", 25, "وزن یا تعداد"),
					Arrays.asList(pack, can, kg, g),
					Arrays.asList(dairy, culinary)
			);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	class DAOs
	{
		final RuntimeExceptionDao<GroupEntity, Integer> GroupDAD;
		final RuntimeExceptionDao<GroupUnitEntity, Integer> GroupUnitDAD;
		final RuntimeExceptionDao<CategoryGroupEntity, Integer> CategoryGroupDAO;
		final RuntimeExceptionDao<ProductBarcodeEntity, Integer> ProductBarcodeDAO;
		final RuntimeExceptionDao<UnitEntity, Integer> UnitDAO;

		DAOs(
				RuntimeExceptionDao<GroupEntity, Integer> GroupDAD,
				RuntimeExceptionDao<GroupUnitEntity, Integer> GroupUnitDAD,
				RuntimeExceptionDao<CategoryGroupEntity, Integer> categoryGroupDAO,
				RuntimeExceptionDao<ProductBarcodeEntity, Integer> productBarcodeDAO,
				RuntimeExceptionDao<UnitEntity, Integer> unitDAO
		) {
			this.GroupDAD = GroupDAD;
			this.GroupUnitDAD = GroupUnitDAD;
			this.CategoryGroupDAO = categoryGroupDAO;
			this.ProductBarcodeDAO = productBarcodeDAO;
			this.UnitDAO = unitDAO;
		}
	}

	class Barcode
	{
		final String Text;
		final BarcodeFormat Format;

		Barcode(String text, BarcodeFormat format) {
			Text = text;
			Format = format;
		}
	}

	private GroupEntity init(
			DAOs daos,
			GroupEntity groupEntity,
			List<UnitEntity> units,
			List<CategoryEntity> cats
	) {
		daos.GroupDAD.create(groupEntity);
		for (UnitEntity unitEntity : units) {
			daos.GroupUnitDAD.create(new GroupUnitEntity(groupEntity, unitEntity));
		}
		for (CategoryEntity cat : cats) {
			daos.CategoryGroupDAO.create(new CategoryGroupEntity(groupEntity, cat));
		}
		return groupEntity;
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource cs, int oldVersion, int newVersion) {
		try {
			upgrade(database, cs, oldVersion, newVersion);
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new RuntimeException(exception);
		}
	}

	private void upgrade(SQLiteDatabase database, ConnectionSource cs, int oldVersion, int newVersion) throws SQLException {
		if (oldVersion == 1) {
			DatabaseUtility.addColumn(database, "Invoices", "Deleted");
			DatabaseUtility.addColumn(database, "Invoices", "Instant");
			DatabaseUtility.addColumn(database, "Invoices", "ItemsCountSum");
			DatabaseUtility.addColumn(database, "Invoices", "ItemsPriceSum");
			DatabaseUtility.addColumn(database, "Invoices", "ItemsQuantitySum");
			DatabaseUtility.addColumn(database, "Invoices", "Seller");

			DatabaseUtility.addColumn(database, "Groups", "UniqueKey");
			DatabaseUtility.addColumn(database, "Groups", "Category");

			TableUtils.createTable(cs, CategoryEntity.class);
			TableUtils.createTable(cs, NeedChangeEntity.class);
			TableUtils.createTable(cs, StoreCategoryEntity.class);
			TableUtils.createTable(cs, StoreGroupEntity.class);
			TableUtils.createTable(cs, StoreEntity.class);
			TableUtils.createTable(cs, CategoryGroupEntity.class);
			oldVersion++;
		}

		if (oldVersion == 2) {
			String[] cols = new String[]{"AlwaysID", "GlobalID", "PendingSync", "LastSynced"};
			DatabaseUtility.addColumns(database, "Categories", cols);
			DatabaseUtility.addColumns(database, "CategoryGroups", cols);
			DatabaseUtility.addColumns(database, "Groups", cols);
			DatabaseUtility.addColumns(database, "GroupUnits", cols);
			DatabaseUtility.addColumns(database, "Invoices", cols);
			DatabaseUtility.addColumns(database, "InvoiceItems", cols);
			DatabaseUtility.addColumns(database, "NeedChanges", cols);
			DatabaseUtility.addColumns(database, "Products", cols);
			DatabaseUtility.addColumns(database, "ProductBarcodes", cols);
			DatabaseUtility.addColumns(database, "Stores", cols);
			DatabaseUtility.addColumns(database, "StoreCategories", cols);
			DatabaseUtility.addColumns(database, "Units", cols);
			oldVersion++;
		}

		if (oldVersion == 3) {
			String[] cols = new String[]{"AlwaysID", "GlobalID", "PendingSync", "LastSynced"};
			DatabaseUtility.addColumns(database, "StoreGroups", cols);
			oldVersion++;
		}

		if (oldVersion == 4) {
			ensureAlwaysIDs(new CategoryRepo());
			ensureAlwaysIDs(new CategoryGroupRepo());
			ensureAlwaysIDs(new GroupRepo());
			ensureAlwaysIDs(new GroupUnitRepo());
			ensureAlwaysIDs(new InvoiceRepo());
			ensureAlwaysIDs(new InvoiceItemRepo());
			ensureAlwaysIDs(new NeedChangeRepo());
			ensureAlwaysIDs(new ProductRepo());
			ensureAlwaysIDs(new ProductBarcodeRepo());
			ensureAlwaysIDs(new StoreRepo());
			ensureAlwaysIDs(new StoreCategoryRepo());
			ensureAlwaysIDs(new StoreGroupRepo());
			ensureAlwaysIDs(new UnitRepo());

			oldVersion++;
		}

		Log.i("Database", String.format("Upgraded from %s to %s", oldVersion, newVersion));
	}

	private <TEntity extends BaseEntity<TEntity>> void ensureAlwaysIDs(IRepo<TEntity> repo) {
		BaseParams<TEntity> params = repo.NewParams();
		params.AlwaysID = new ComparableFilter<>(RelationalOperators.IsNull);
		List<TEntity> items = repo.All(params);
		if (items == null)
			return;
		for (TEntity item : items) {
			item.AlwaysID = UUID.randomUUID();
		}
		repo.UpdateAll(items);
	}

	public static final int Version = 5;
}
