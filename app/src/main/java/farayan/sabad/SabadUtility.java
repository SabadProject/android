package farayan.sabad;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import farayan.commons.Action;
import farayan.commons.FarayanBaseCoreActivity;
import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.PropertyValueConditionModes;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Group.GroupParams;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;

public class SabadUtility {

    private static boolean DisplayGuideAllowed(
            Context context,
            SabadPreferenceKeys countKey,
            int displayMaxCount,
            SabadPreferenceKeys lastKey,
            long displayIntervalMin
    ) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int displayedCount = preferences.getInt(countKey.name(), 0);
        boolean countAllowed = displayedCount < displayMaxCount;
        long displayedLast = preferences.getLong(lastKey.name(), 0);
        boolean intervalAllowed = displayIntervalMin < System.currentTimeMillis() - displayedLast;
        boolean allowed = countAllowed && intervalAllowed;
        if (allowed) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(countKey.name(), ++displayedCount);
            editor.putLong(lastKey.name(), System.currentTimeMillis());
            editor.apply();
        }
        return allowed;
    }

    public static boolean NewGroupGuideNeeded(Context context) {
        return DisplayGuideAllowed(
                context,
                SabadPreferenceKeys.NewGroupGuideDisplayedCount,
                SabadConstants.NewGroupGuideDisplayMaxCount,
                SabadPreferenceKeys.LastNewGroupGuideDisplayed,
                SabadConstants.NewGroupGuideDisplayMinInterval
        );
    }

    public static boolean NeedChangeGuideNeeded(Context context) {
        return DisplayGuideAllowed(
                context,
                SabadPreferenceKeys.NeedChangeGuideDisplayedCount,
                SabadConstants.NeedChangeGuideDisplayMaxCount,
                SabadPreferenceKeys.LastNeedChangeGuideDisplayed,
                SabadConstants.NeedChangeGuideDisplayMinInterval
        );
    }

    public static boolean PickChangeGuideNeeded(Context context) {
        return DisplayGuideAllowed(
                context,
                SabadPreferenceKeys.PickChangeGuideDisplayedCount,
                SabadConstants.PickChangeGuideDisplayMaxCount,
                SabadPreferenceKeys.LastPickChangeGuideDisplayed,
                SabadConstants.PickChangeGuideDisplayMinInterval
        );
    }

    public static boolean GroupNameClickGuideNeeded(Context context) {
        return DisplayGuideAllowed(
                context,
                SabadPreferenceKeys.GroupNewClickedGuideDisplayedCount,
                SabadConstants.GroupNewClickedGuideDisplayMaxCount,
                SabadPreferenceKeys.LastGroupNewClickedGuideDisplayed,
                SabadConstants.GroupNewClickedGuideDisplayMinInterval
        );
    }

    public static boolean InvoiceItemRegisteredGuideNeeded(Context context) {
        return DisplayGuideAllowed(
                context,
                SabadPreferenceKeys.InvoiceItemRegisteredGuideDisplayedCount,
                SabadConstants.InvoiceItemRegisteredGuideDisplayMaxCount,
                SabadPreferenceKeys.LastInvoiceItemRegisteredGuideDisplayed,
                SabadConstants.InvoiceItemRegisteredGuideDisplayMinInterval
        );
    }

    public static boolean CheckoutGuideNeeded(Context context) {
        return DisplayGuideAllowed(
                context,
                SabadPreferenceKeys.CheckoutGuideDisplayedCount,
                SabadConstants.CheckoutGuideDisplayMaxCount,
                SabadPreferenceKeys.LastCheckoutGuideDisplayed,
                SabadConstants.CheckoutGuideDisplayMinInterval
        );
    }

    public static boolean DisplayCheckoutDialogAutomatically(Context context) {
        return DisplayGuideAllowed(
                context,
                SabadPreferenceKeys.CheckoutDialogDisplayedCount,
                SabadConstants.CheckoutDialogDisplayMaxCount,
                SabadPreferenceKeys.LastCheckoutDialogDisplayed,
                SabadConstants.CheckoutDialogDisplayMinInterval
        );
    }

    public static void BarcodeScanCameraPermission(
            FarayanBaseCoreActivity activity,
            Action onSuccess,
            boolean commented
    ) {
        FarayanUtility.RequestCameraPermission(
                activity,
                onSuccess,
                commented,
                "«سبد» برای اسکن بارکد کالاها نیاز به محوز دوربین دارد",
                "اعطای مجوز",
                "اسکن بارکد لازم نیست",
                "شما قبلا مجوز دوربین را به «سبد» نداده یا گرفته‌اید، آیا می‌خواهید برای اسکن بارکد، به «سبد» مجوز استفاده از دوربین را بدهید؟",
                "تنظیمات اندروید",
                "خب"
        );
    }

    public static PurchaseSummary PurchaseSummary(IGroupRepo groupRepo) {
        // آنهایی که مورد نیازند
        GroupParams neededGroupParams = new GroupParams();
        neededGroupParams.Needed = new ComparableFilter<>(true);
        neededGroupParams.Deleted = new ComparableFilter<>(false);
        int neededCount = (int) groupRepo.Count(neededGroupParams);

        // آنهایی که مورد نیازند ولی برداشته نشده‌اند
        GroupParams remainedGroupParams = new GroupParams();
        remainedGroupParams.Needed = new ComparableFilter<>(true);
        remainedGroupParams.Picked = new ComparableFilter<>(false);
        neededGroupParams.Deleted = new ComparableFilter<>(false);
        int remainedCount = (int) groupRepo.Count(remainedGroupParams);

        // آنهایی که مورد نیازند و برداشته شده‌اند
        GroupParams pickedGroupsParams = new GroupParams();
        pickedGroupsParams.Needed = new ComparableFilter<>(true);
        pickedGroupsParams.Picked = new ComparableFilter<>(true);
        pickedGroupsParams.Deleted = new ComparableFilter<>(false);
        int pickedGroupsCount = (int) groupRepo.Count(pickedGroupsParams);

        // آنهایی که مورد نیازند، برداشته شده‌اند و کالایشان هم مشخص است
        GroupParams itemedGroupsParams = new GroupParams();
        itemedGroupsParams.Item = new EntityFilter<>(PropertyValueConditionModes.ProvidedValueOrHavingValue);
        itemedGroupsParams.Needed = new ComparableFilter<>(true);
        itemedGroupsParams.Picked = new ComparableFilter<>(true);
        itemedGroupsParams.Deleted = new ComparableFilter<>(false);

        List<GroupEntity> itemedGroupes = groupRepo.All(itemedGroupsParams);
        assert itemedGroupes != null;
        int itemedCount = itemedGroupes.size();
        double quantitiesSum = itemedGroupes.stream().mapToDouble(x -> x.Item.Quantity.doubleValue()).sum();
        double pricesSum = itemedGroupes.stream().mapToDouble(x -> x.Item.Total.doubleValue()).sum();

        GroupParams checkedGroupsParams = new GroupParams();
        checkedGroupsParams.Item = new EntityFilter<>(PropertyValueConditionModes.ProvidedValueOrNull);
        checkedGroupsParams.Picked = new ComparableFilter<>(true);
        remainedGroupParams.Needed = new ComparableFilter<>(true);
        int checkedGroupsCount = (int) groupRepo.Count(checkedGroupsParams);

        return new PurchaseSummary(
                neededCount,
                remainedCount,
                pickedGroupsCount,
                checkedGroupsCount,
                itemedCount,
                quantitiesSum,
                pricesSum
        );
    }

    public static class PurchaseSummary {
        /**
         * تعداد گروه‌های مورد نیاز
         */
        public final int NeededCount;

        /**
         * تعداد گروه‌های مورد نیاز که کالایشان برداشته نشده
         */
        public final int RemainedCount;

        /**
         * تعداد گروه‌هایی که اقلام مرتبط‌شان برداشته شده
         */
        public final int PickedCount;

        /**
         * تعداد گروه‌هایی که کالایی ندارند، ولی کاربر تیک زده
         */
        public final int CheckedCount;

        /**
         * تعداد گروه‌هایی که کاربر برایشان کالا هم برداشته
         */
        public final int ItemedCount;

        /**
         * جمع تعداد کالاهای برداشته شده
         */
        public final double QuantitiesSum;

        /**
         * جمع مبلغ کالاهای برداشته
         */
        public final double PricesSum;

        PurchaseSummary(
                int neededCount,
                int remainedCount,
                int pickedCount,
                int checkedCount,
                int itemedCount,
                double quantitiesSum,
                double pricesSum
        ) {
            NeededCount = neededCount;
            RemainedCount = remainedCount;
            PickedCount = pickedCount;
            CheckedCount = checkedCount;
            ItemedCount = itemedCount;
            QuantitiesSum = quantitiesSum;
            PricesSum = pricesSum;
        }
    }
}
