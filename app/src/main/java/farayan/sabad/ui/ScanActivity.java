package farayan.sabad.ui;

import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.FarayanUtility;
import farayan.sabad.SabadUtility;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.core.OnePlace.Product.IProductRepo;
import farayan.sabad.core.OnePlace.Product.ProductEntity;
import farayan.sabad.core.OnePlace.ProductBarcode.CapturedBarcode;
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.SabadConstants;

@AndroidEntryPoint
public class ScanActivity extends ScanActivityParent
{
	private IGroupRepo TheGroupRepo;
	private IGroupUnitRepo TheGroupUnitRepo;
	private IProductRepo TheProductRepo;
	private IProductBarcodeRepo TheProductBarcodeRepo;
	private IInvoiceItemRepo TheInvoiceItemRepo;
	private IUnitRepo TheUnitRepo;
	private boolean dataChanged;

	@Inject
	public void Inject(
			IGroupRepo GroupRepo,
			IGroupUnitRepo GroupUnitRepo,
			IProductRepo productRepo,
			IProductBarcodeRepo productBarcodeRepo,
			IInvoiceItemRepo invoiceItemRepo,
			IUnitRepo unitRepo
	) {
		this.TheGroupRepo = GroupRepo;
		this.TheGroupUnitRepo = GroupUnitRepo;
		this.TheProductRepo = productRepo;
		this.TheProductBarcodeRepo = productBarcodeRepo;
		this.TheInvoiceItemRepo = invoiceItemRepo;
		this.TheUnitRepo = unitRepo;
	}

	BeepManager beepManager;

	private final BarcodeCallback ScanBarcodeView_DecodeContinuous = new BarcodeCallback()
	{
		@Override
		public void barcodeResult(BarcodeResult result) {
			ScanBarcodeView().pause();
			FarayanUtility.ReleaseScreenOn(getWindow());
			CapturedBarcode capturedBarcode = SabadUtility.CapturedBarcodeVersion(result);
			ProductEntity productEntity = TheProductBarcodeRepo.ByBarcode(capturedBarcode);

			beepManager.playBeepSoundAndVibrate();
			InvoiceItemFormDialog dialog = new InvoiceItemFormDialog(
					new InvoiceItemFormDialog.InputArgs(
							null,
							productEntity == null ? null : productEntity.Group,
							productEntity,
							capturedBarcode,
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
							TheProductBarcodeRepo,
							TheInvoiceItemRepo,
							TheUnitRepo,
							new BeepManager(ScanActivity.this)
					),
					ScanActivity.this,
					true,
					dialogInterface -> {
						ScanBarcodeView().resume();
						FarayanUtility.KeepScreenOn(getWindow());
					}
			);
			dialog.show();
		}
	};

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