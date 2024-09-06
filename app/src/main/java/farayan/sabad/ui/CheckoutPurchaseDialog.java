package farayan.sabad.ui;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import farayan.commons.Commons.Rial;
import farayan.commons.FarayanUtility;
import farayan.commons.PersianCalendar;
import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyValueConditionModes;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.sabad.R;
import farayan.sabad.SabadUtility;
import farayan.sabad.core.OnePlace.Category.CategoryEntity;
import farayan.sabad.core.OnePlace.CategoryGroup.ICategoryGroupRepo;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Group.GroupParams;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo;
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity;
import farayan.sabad.core.OnePlace.Store.IStoreRepo;
import farayan.sabad.core.OnePlace.StoreCategory.IStoreCategoryRepo;
import farayan.sabad.core.OnePlace.StoreGroup.IStoreGroupRepo;

public class CheckoutPurchaseDialog extends CheckoutPurchaseDialogParent {
    private final Input Args;

    public CheckoutPurchaseDialog(@NonNull Input args, @NonNull Activity context) {
        super(context);
        Args = args;
        init();
    }

    public CheckoutPurchaseDialog(@NonNull Input args, @NonNull Activity context, int themeResId) {
        super(context, themeResId);
        Args = args;
        init();
    }

    public CheckoutPurchaseDialog(@NonNull Input args, @NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        Args = args;
        init();
    }

    public void init() {
        CheckoutButton().setOnClickListener(CheckoutButtonOnClickListener());
        Reload();
    }

    private void Reload() {
        SabadUtility.PurchaseSummary purchaseSummary = SabadUtility.PurchaseSummary(Args.GroupRepo);

        PurchasableCountEditText().setText(getString(R.string.CheckoutPurchaseNeededGroupsCount, purchaseSummary.NeededCount));
        PickedCountEditText().setText(getString(R.string.CheckoutPurchasePickedGroupsCount, purchaseSummary.PickedCount));
        RemainedCountEditText().setText(
                purchaseSummary.RemainedCount == 0
                        ? getContext().getResources().getString(R.string.CheckoutPurchaseNoGroupsRemained)
                        : getString(R.string.CheckoutPurchaseRemainedGroupsCount, purchaseSummary.RemainedCount)
        );
        PayableEditText().setText(new Rial(purchaseSummary.PricesSum).Textual(getContext().getResources()));
        PurchasedDatePicker().setValue(new PersianCalendar());
    }

    private String getString(int resID, int value) {
        return getContext().getString(resID, FarayanUtility.MoneyFormatted(value, true, false));
    }

    private View.OnClickListener CheckoutButtonOnClickListener() {
        return view -> {
            GroupParams pickedGroupsParams = new GroupParams();
            pickedGroupsParams.Picked = new ComparableFilter<>(true);
            long pickedGroupsCount = Args.GroupRepo.Count(pickedGroupsParams);
            if (pickedGroupsCount == 0) {
                return;
            }

            InvoiceEntity invoiceEntity = new InvoiceEntity();
            invoiceEntity.Instant = PurchasedDatePicker().getValue().getEpoch();
            invoiceEntity.Seller = StorePicker().SelectedEntity(true);
            Args.StoreRepo.SaveOnNeed(invoiceEntity.Seller);
            Args.InvoiceRepo.Save(invoiceEntity);

            List<GroupEntity> pickedGroups = Args.GroupRepo.All(pickedGroupsParams);

            for (GroupEntity groupEntity : pickedGroups) {
                if (groupEntity.Item != null) {
                    if (invoiceEntity.Seller != null) {
                        Args.StoreGroupRepo.EnsureRelated(groupEntity, invoiceEntity.Seller);
                        List<CategoryEntity> categories = Args.CategoryGroupRepo.CategoriesOf(groupEntity);
                        if (FarayanUtility.IsUsable(categories)) {
                            categories.forEach(x -> Args.StoreCategoryRepo.EnsureRelated(invoiceEntity.Seller, x));
                        }
                    }

                    groupEntity.Item.Invoice = invoiceEntity;
                    //Args.InvoiceItemRepo.Update(groupEntity.Item);
                    invoiceEntity.Total += groupEntity.Item.Total.doubleValue();
                    invoiceEntity.ItemsQuantitySum += groupEntity.Item.Quantity.intValue();
                    invoiceEntity.ItemsPriceSum += groupEntity.Item.Total.intValue();
                    groupEntity.LastPurchase = groupEntity.Item;
                    groupEntity.Item = null;
                }

                invoiceEntity.ItemsCountSum++;

                groupEntity.Needed = false;
                groupEntity.Picked = false;
                groupEntity.LastPurchased = System.currentTimeMillis();
                groupEntity.PurchasedCount++;
                Args.GroupRepo.Update(groupEntity);
            }

            GroupParams groupParams = new GroupParams();
            groupParams.Item = new EntityFilter<>(PropertyValueConditionModes.ProvidedValueOrHavingValue);
            List<GroupEntity> GroupEntities = Args.GroupRepo.All(groupParams);
            for (GroupEntity groupEntity : GroupEntities) {
                groupEntity.LastPurchase = groupEntity.Item;
                groupEntity.Item = null;
                groupEntity.Needed = false;
                groupEntity.LastPurchased = System.currentTimeMillis();
                groupEntity.PurchasedCount++;
                Args.GroupRepo.Update(groupEntity);
            }

            Args.InvoiceRepo.Update(invoiceEntity);

            //Args.InvoiceItemRepo.DeleteAllItemsWithoutInvoice();

            FarayanUtility.ShowToast(TheActivity, "صورتحساب صادر و کالاهای برداشته‌شده، از فهرست خرید خارج شدند");
            IGenericEvent.Exec(Args.OnCheckedOut, invoiceEntity);
            dismiss();
        };
    }

    static class Input {
        public final IStoreCategoryRepo StoreCategoryRepo;
        public final IStoreGroupRepo StoreGroupRepo;
        public final ICategoryGroupRepo CategoryGroupRepo;
        public final IGroupRepo GroupRepo;
        public final IStoreRepo StoreRepo;
        public final IInvoiceRepo InvoiceRepo;
        public final IGenericEvent<InvoiceEntity> OnCheckedOut;

        Input(
                IStoreCategoryRepo storeCategoryRepo,
                IStoreGroupRepo storeGroupRepo,
                ICategoryGroupRepo categoryGroupRepo,
                IGroupRepo groupRepo,
                IStoreRepo storeRepo,
                IInvoiceRepo invoiceRepo,
                IGenericEvent<InvoiceEntity> onCheckedOut
        ) {
            StoreCategoryRepo = storeCategoryRepo;
            StoreGroupRepo = storeGroupRepo;
            CategoryGroupRepo = categoryGroupRepo;
            GroupRepo = groupRepo;
            StoreRepo = storeRepo;
            InvoiceRepo = invoiceRepo;
            OnCheckedOut = onCheckedOut;
        }
    }
}
