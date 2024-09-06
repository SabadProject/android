package farayan.sabad.ui;

import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.FarayanUtility;
import farayan.sabad.SabadConstants;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.model.product.IProductRepo;
import farayan.sabad.core.model.product.ProductEntity;
import farayan.sabad.vms.InvoiceItemFormViewModel;

@AndroidEntryPoint
public class ScanActivity extends ScanActivityParent {
    private final InvoiceItemFormViewModel invoiceItemFormViewModel = new ViewModelProvider(this).get(InvoiceItemFormViewModel.class);
    BeepManager beepManager;
    private final BarcodeCallback ScanBarcodeView_DecodeContinuous = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            ScanBarcodeView().pause();
            FarayanUtility.ReleaseScreenOn(getWindow());
            ProductEntity productEntity = new ProductEntity(); //TheProductBarcodeRepo.ByBarcode(new QueryableBarcode(result.getText(), result.getBarcodeFormat()));

            beepManager.playBeepSoundAndVibrate();
            InvoiceItemFormDialog dialog = new InvoiceItemFormDialog(
                    /*new InvoiceItemFormInputArgs(
                            null,
                            productEntity == null ? null : productEntity.Group,
                            productEntity,
                            result,
                            invoiceItemEntity -> {
                                dataChanged = true;
                                PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient);
                                ScanBarcodeView().resume();
                                FarayanUtility.KeepScreenOn(getWindow());
                            },
                            invoiceItemEntity -> {
                                dataChanged = true;
                                PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient);
                                ScanBarcodeView().resume();
                                FarayanUtility.KeepScreenOn(getWindow());
                            },
                            TheGroupRepo,
                            TheGroupUnitRepo,
                            TheProductRepo,
                            TheUnitRepo,
                            new BeepManager(ScanActivity.this)
                    ),*/
                    ScanActivity.this,
                    true,
                    dialogInterface -> {
                        ScanBarcodeView().resume();
                        FarayanUtility.KeepScreenOn(getWindow());
                    },
                    invoiceItemFormViewModel
            );
            dialog.show();
        }
    };
    private IGroupRepo TheGroupRepo;
    private IGroupUnitRepo TheGroupUnitRepo;
    private IProductRepo TheProductRepo;
    private IUnitRepo TheUnitRepo;
    private boolean dataChanged;

    @Inject
    public void Inject(
            IGroupRepo GroupRepo,
            IGroupUnitRepo GroupUnitRepo,
            IProductRepo productRepo,
            IUnitRepo unitRepo
    ) {
        this.TheGroupRepo = GroupRepo;
        this.TheGroupUnitRepo = GroupUnitRepo;
        this.TheProductRepo = productRepo;
        this.TheUnitRepo = unitRepo;
    }

    @Override
    protected void InitializeLayout() {
        ScanBarcodeView().setDecoderFactory(new DefaultDecoderFactory(SabadConstants.SupportedBarcodeFormats));
        ScanBarcodeView().decodeContinuous(ScanBarcodeView_DecodeContinuous);
        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanBarcodeView().resume();
        FarayanUtility.KeepScreenOn(getWindow());
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanBarcodeView().pause();
        FarayanUtility.ReleaseScreenOn(getWindow());
    }

    @Override
    public void onBackPressed() {
        if (dataChanged) {
            setResult(SabadConstants.DataChangedResultCode);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}