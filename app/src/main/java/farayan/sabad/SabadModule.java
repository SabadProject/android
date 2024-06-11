package farayan.sabad;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import farayan.sabad.models.Category.CategoryRepo;
import farayan.sabad.models.CategoryGroup.CategoryGroupRepo;
import farayan.sabad.models.Invoice.InvoiceRepo;
import farayan.sabad.models.InvoiceItem.InvoiceItemRepo;
import farayan.sabad.models.NeedChange.NeedChangeRepo;
import farayan.sabad.models.Product.ProductRepo;
import farayan.sabad.models.ProductBarcode.ProductBarcodeRepo;
import farayan.sabad.models.Store.StoreRepo;
import farayan.sabad.models.StoreCategory.StoreCategoryRepo;
import farayan.sabad.models.StoreGroup.StoreGroupRepo;
import farayan.sabad.models.Group.GroupRepo;
import farayan.sabad.models.GroupUnit.GroupUnitRepo;
import farayan.sabad.models.Unit.UnitRepo;
import farayan.sabad.core.OnePlace.Category.ICategoryRepo;
import farayan.sabad.core.OnePlace.CategoryGroup.ICategoryGroupRepo;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.core.OnePlace.NeedChange.INeedChangeRepo;
import farayan.sabad.core.OnePlace.Product.IProductRepo;
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo;
import farayan.sabad.core.OnePlace.Store.IStoreRepo;
import farayan.sabad.core.OnePlace.StoreCategory.IStoreCategoryRepo;
import farayan.sabad.core.OnePlace.StoreGroup.IStoreGroupRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;

@Module
@InstallIn(SingletonComponent.class)
public class SabadModule
{
	@Provides
	public static IGroupRepo groupRepo() {
		return new GroupRepo();
	}

	@Provides
	public static IInvoiceRepo invoiceRepo() {
		return new InvoiceRepo();
	}

	@Provides
	public static IInvoiceItemRepo invoiceItemRepo() {
		return new InvoiceItemRepo();
	}

	@Provides
	public static IProductRepo productRepo() {
		return new ProductRepo();
	}

	@Provides
	public static IProductBarcodeRepo productBarcodeRepo() {
		return new ProductBarcodeRepo();
	}

	@Provides
	public static IUnitRepo unitRepo() {
		return new UnitRepo();
	}

	@Provides
	public static IGroupUnitRepo GroupUnitRepo() {
		return new GroupUnitRepo();
	}

	@Provides
	public static IStoreRepo storeRepo() {
		return new StoreRepo();
	}

	@Provides
	public static ICategoryRepo categoryRepo() {
		return new CategoryRepo();
	}

	@Provides
	public static INeedChangeRepo needChangeRepo() {
		return new NeedChangeRepo();
	}

	@Provides
	public static IStoreGroupRepo storeGroupRepo() {
		return new StoreGroupRepo();
	}

	@Provides
	public static IStoreCategoryRepo storeCategoryRepo() {
		return new StoreCategoryRepo();
	}

	@Provides
	public static ICategoryGroupRepo categoryGroupRepo() {
		return new CategoryGroupRepo();
	}
}
