package farayan.sabad.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.Rial;
import farayan.commons.FarayanUtility;
import farayan.sabad.R;
import farayan.sabad.SabadConstants;
import farayan.sabad.SabadUtility;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;

@AndroidEntryPoint
public class PurchaseSummaryComponent extends PurchaseSummaryComponentParent {
    private IGroupRepo TheGroupRepo;

    public PurchaseSummaryComponent(Context context) {
        super(context);
    }

    public PurchaseSummaryComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PurchaseSummaryComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PurchaseSummaryComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Inject
    public void Inject(
            IGroupRepo groupRepo
    ) {
        TheGroupRepo = groupRepo;
        ReloadSummary(SabadConstants.TheCoefficient);
    }

    @Override
    protected void InitializeComponents() {
        ContainerLayout().setOnClickListener(view -> ReloadSummary(SabadConstants.TheCoefficient));
    }

    public void ReloadSummary(Rial.Coefficients coefficient) {

        SabadUtility.PurchaseSummary purchaseSummary = SabadUtility.PurchaseSummary(TheGroupRepo);

        PickedItemsTextView().setText(getContext().getString(
                R.string.PurchaseSummaryPickedItems,
                FarayanUtility.MoneyFormatted(purchaseSummary.ItemedCount, true, false),
                FarayanUtility.MoneyFormatted(purchaseSummary.QuantitiesSum, true, false)
        ));

        RemainedItemsTextView().setText(getContext().getString(
                R.string.PurchaseSummaryRemainedItems,
                FarayanUtility.MoneyFormatted(purchaseSummary.RemainedCount, true, false)
        ));

        SubtotalTextView().setText(getContext().getString(
                R.string.SubtotalTextView,
                new Rial(coefficient, purchaseSummary.PricesSum).Textual(getResources())
        ));
    }
}
