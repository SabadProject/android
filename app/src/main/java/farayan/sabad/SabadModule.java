package farayan.sabad;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import farayan.sabad.core.OnePlace.CategoryGroup.ICategoryGroupRepo;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo;
import farayan.sabad.core.OnePlace.NeedChange.INeedChangeRepo;
import farayan.sabad.core.OnePlace.Store.IStoreRepo;
import farayan.sabad.core.OnePlace.StoreCategory.IStoreCategoryRepo;
import farayan.sabad.core.OnePlace.StoreGroup.IStoreGroupRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.model.product.IProductRepo;
import farayan.sabad.models.CategoryGroup.CategoryGroupRepo;
import farayan.sabad.models.Group.GroupRepo;
import farayan.sabad.models.GroupUnit.GroupUnitRepo;
import farayan.sabad.models.Invoice.InvoiceRepo;
import farayan.sabad.models.NeedChange.NeedChangeRepo;
import farayan.sabad.models.Product.ProductRepo;
import farayan.sabad.models.Store.StoreRepo;
import farayan.sabad.models.StoreCategory.StoreCategoryRepo;
import farayan.sabad.models.StoreGroup.StoreGroupRepo;
import farayan.sabad.models.Unit.UnitRepo;

@Module
@InstallIn(SingletonComponent.class)
public class SabadModule {
    @Provides
    public static IGroupRepo groupRepo() {
        return new GroupRepo();
    }

    @Provides
    public static IInvoiceRepo invoiceRepo() {
        return new InvoiceRepo();
    }

    @Provides
    public static IProductRepo productRepo() {
        return new ProductRepo();
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
