package farayan.sabad.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import farayan.commons.Commons.Rial;
import farayan.commons.Exceptions.Rexception;
import farayan.commons.FarayanBaseCoreActivity;
import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.EntityFilter;
import farayan.commons.QueryBuilderCore.EnumFilter;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.commons.QueryBuilderCore.TextMatchModes;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.commons.UI.QuestionDialog;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.GroupUnit.GroupUnitParams;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.OnePlace.Product.IProductRepo;
import farayan.sabad.core.OnePlace.Product.ProductEntity;
import farayan.sabad.core.OnePlace.Product.ProductParams;
import farayan.sabad.core.OnePlace.ProductBarcode.CapturedBarcode;
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo;
import farayan.sabad.core.OnePlace.ProductBarcode.ProductBarcodeEntity;
import farayan.sabad.core.OnePlace.ProductBarcode.ProductBarcodeParams;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.OnePlace.Unit.UnitEntity;
import farayan.sabad.R;
import farayan.sabad.SabadConfigs;
import farayan.sabad.SabadConstants;
import farayan.sabad.SabadUtility;
import farayan.sabad.core.OnePlace.Group.GroupEntity;

public class InvoiceItemFormDialog extends InvoiceItemFormDialogParent
{
	private final InputArgs Args;

	public static class InputArgs
	{
		private final InvoiceItemEntity InvoiceItem;
		private final IGroupRepo GroupRepo;
		private final IGroupUnitRepo GroupUnitRepo;
		private final IProductRepo ProductRepo;
		private final IProductBarcodeRepo ProductBarcodeRepo;
		private final IInvoiceItemRepo InvoiceItemRepo;
		private final IUnitRepo UnitRepo;
		private final BeepManager BeepManager;
		private final GroupEntity Group;
		private final ProductEntity Product;
		private CapturedBarcode Barcode;
		private final IGenericEvent<InvoiceItemEntity> OnRegistered;
		private final IGenericEvent<InvoiceItemEntity> OnRemoved;

		public InputArgs(
				InvoiceItemEntity invoiceItem,
				GroupEntity group,
				ProductEntity product,
				CapturedBarcode barcode,
				IGenericEvent<InvoiceItemEntity> onRegistered,
				IGenericEvent<InvoiceItemEntity> onRemoved,
				IGroupRepo groupRepo,
				IGroupUnitRepo groupUnitRepo,
				IProductRepo productRepo,
				IProductBarcodeRepo productBarcodeRepo,
				IInvoiceItemRepo invoiceItemRepo,
				IUnitRepo unitRepo,
				BeepManager beepManager
		) {
			InvoiceItem = invoiceItem;
			GroupRepo = groupRepo;
			GroupUnitRepo = groupUnitRepo;
			ProductRepo = productRepo;
			ProductBarcodeRepo = productBarcodeRepo;
			InvoiceItemRepo = invoiceItemRepo;
			UnitRepo = unitRepo;
			BeepManager = beepManager;
			Group = group;
			Product = product;
			Barcode = barcode;
			OnRegistered = onRegistered;
			OnRemoved = onRemoved;
		}
	}

	public InvoiceItemFormDialog(
			InputArgs args,
			@NonNull Activity context
	) {
		super(context);
		Args = args;
		init();
	}

	public InvoiceItemFormDialog(
			InputArgs args,
			@NonNull Activity context,
			int themeResId
	) {
		super(context, themeResId);
		Args = args;
		init();
	}

	public InvoiceItemFormDialog(
			InputArgs args,
			@NonNull Activity context,
			boolean cancelable,
			@Nullable OnCancelListener cancelListener
	) {
		super(context, cancelable, cancelListener);
		Args = args;
		init();
	}

	@SuppressLint("SetTextI18n")
	private void init() {
		ScanBarcodeView().setDecoderFactory(new DefaultDecoderFactory(SabadConstants.SupportedBarcodeFormats));
		ScanBarcodeView().decodeContinuous(ScanBarcodeViewDecodeContinuousProvider());

		if (Args.Group == null) {
			GroupPicker().requestFocus();
		} else if (Args.Product == null) {
			ProductPicker().requestFocus();
		} else {
			QuantityNumberEntry().requestFocus();
		}

		if (Args.Barcode != null && Args.Product != null) {
			ProductBarcodeParams productBarcodeParams = new ProductBarcodeParams();
			productBarcodeParams.Text = new TextFilter(Args.Barcode.getText(), TextMatchModes.Exactly);
			productBarcodeParams.Format = new EnumFilter<>(Args.Barcode.getFormat());
			ProductBarcodeEntity productBarcodeEntity = Args.ProductBarcodeRepo.First(productBarcodeParams);

			if (productBarcodeEntity != null && productBarcodeEntity.Product.ID != Args.Product.ID) {
				throw new Rexception(
						null,
						"While both barcode and product (%s) provided, barcode (%s) is registered for another product (%s)",
						Args.Product.getID(),
						Args.Barcode,
						Args.Product.ID
				);
			}
		}

		if (Args.Group != null && Args.Product != null && Args.Product.Group.getID() != Args.Group.getID()) {
			throw new Rexception(
					null,
					"While both Group and product provided, product(%s)'s Group (%s) is not equal to provided Group (%s)",
					Args.Product.getID(),
					Args.Product.Group.getID(),
					Args.Group.getID()
			);
		}

		GroupPicker().setOnEntityChangedEvent(this::GroupChanged);
		ProductPicker().setOnEntityChangedEvent(this::ProductChanged);
		GroupPicker().setValue(Args.GroupRepo.RefreshedNullables(Args.Group, false));
		ProductPicker().setValue(Args.ProductRepo.RefreshedNullables(Args.Product, false));
		BarcodeEditText().setText(FarayanUtility.CatchException(() -> Args.Barcode.getText(), x -> ""));

		GroupPicker().setEnabled(Args.InvoiceItem == null);
		if (Args.InvoiceItem != null) {
			ProductPicker().setValue(Args.InvoiceItem.Product);
			ProductBarcodeEntity productBarcodeEntity = Args.InvoiceItem.Product == null ? null : Args.ProductBarcodeRepo.ByProduct(Args.InvoiceItem.Product);
			BarcodeEditText().setText(productBarcodeEntity == null ? "" : productBarcodeEntity.Text);
			QuantityNumberEntry().setDoubleValue(Args.InvoiceItem.Quantity);
			FeeRialEntry().setValue(new Rial(Args.InvoiceItem.Fee));
			ProductPicker().setValue(Args.InvoiceItem.Product);
		} else {
			if (Args.Group != null && Args.Group.LastPurchase != null) {
				QuantityNumberEntry().setDoubleValue(Args.Group.LastPurchase.Refreshed(Args.InvoiceItemRepo).Quantity);
				if (Args.Group.LastPurchase.Unit != null)
					QuantityUnitBox().setValue(Args.Group.LastPurchase.Unit.Refreshed(Args.UnitRepo));
				FeeRialEntry().setValue(new Rial(Args.Group.LastPurchase.Fee));
			}
		}

		StartScanButton().setOnClickListener(StartScanButtonOnClickListener());

		StopScanButton().setOnClickListener(view -> StopScan());

		RemoveButton().setOnClickListener(RemoveButtonOnClickListener());

		RegisterButton().setOnClickListener(RegisterButtonOnClickListener());
	}

	private View.OnClickListener RegisterButtonOnClickListener() {
		return view -> {

			GroupEntity groupEntity = GroupPicker().SelectedEntity(true);
			if (groupEntity == null) {
				FarayanUtility.ShowToast(TheActivity, "نوع را انتخاب کنید");
				return;
			}
			if (groupEntity.getID() == 0) {
				groupEntity.Needed = true;
				Args.GroupRepo.Save(groupEntity);
			}
			if (!groupEntity.Needed) {
				groupEntity.Needed = true;
				Args.GroupRepo.Update(groupEntity);
			}
			ProductEntity productEntity = ProductPicker().SelectedEntity(true);
			if (productEntity == null) {
				productEntity = new ProductEntity();
				Args.ProductRepo.Save(productEntity);
			}

			productEntity.Group = groupEntity;
			if (Args.Barcode != null) {
				Args.ProductBarcodeRepo.EnsureBarcodeRegistered(getContext(), Args.Barcode, productEntity);
			} else if (FarayanUtility.IsUsable(BarcodeEditText().getText())) {
				Args.ProductBarcodeRepo.EnsureBarcodeRegistered(Objects.requireNonNull(BarcodeEditText().getText()).toString(), productEntity);
			}
			Args.ProductRepo.Update(productEntity);

			double quantity = QuantityNumberEntry().getDoubleValue(1);
			double fee = FeeRialEntry().getValue().TheAmount;
			UnitEntity unitEntity = QuantityUnitBox().SelectedEntity(true);
			if (unitEntity != null) {
				if (unitEntity.getID() == 0) {
					Args.UnitRepo.Save(unitEntity);
				}
				Args.GroupUnitRepo.EnsureRelated(groupEntity, unitEntity);
			}
			if (Args.InvoiceItem == null) {
				InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity(
						groupEntity,
						productEntity,
						quantity,
						fee,
						unitEntity
				);
				Args.InvoiceItemRepo.Save(invoiceItemEntity);
				groupEntity.Item = invoiceItemEntity;
				groupEntity.Picked = true;
				Args.GroupRepo.Update(groupEntity);

				if (SabadUtility.InvoiceItemRegisteredGuideNeeded(getContext())) {
					FarayanUtility.ShowToastFormatted(getContext(), false, "راهنما* کالای انتخابی به سبد افزوده شد، برای پایان خرید برروی آیکن صندوق بالای صفحه کلیک کنید");
				}

				IGenericEvent.Exec(Args.OnRegistered, invoiceItemEntity);
			} else {
				//todo: if user changed Group?
				Args.InvoiceItem.Group = groupEntity;
				Args.InvoiceItem.Product = productEntity;
				Args.InvoiceItem.Quantity = quantity;
				Args.InvoiceItem.Fee = fee;
				Args.InvoiceItem.Total = Math.round(quantity * fee);
				Args.InvoiceItem.Unit = unitEntity;
				Args.InvoiceItemRepo.Update(Args.InvoiceItem);

				IGenericEvent.Exec(Args.OnRegistered, Args.InvoiceItem);
			}
			dismiss();
		};
	}

	private View.OnClickListener RemoveButtonOnClickListener() {
		return view -> {
			GroupEntity groupEntity = GroupPicker().SelectedEntity(false);
			if (groupEntity == null)
				throw new Rexception(null, "");
			if (groupEntity.Item != null) {
				if (groupEntity.Item.getID() > 0)
					Args.InvoiceItemRepo.Delete(groupEntity.Item);
				groupEntity.Item = null;
				groupEntity.Picked = false;
				Args.GroupRepo.Update(groupEntity);

				if (Args.OnRemoved != null)
					Args.OnRemoved.Fire(groupEntity.Item);
			}
			dismiss();
		};
	}

	private View.OnClickListener StartScanButtonOnClickListener() {
		return view -> SabadUtility.BarcodeScanCameraPermission(
				(FarayanBaseCoreActivity) TheActivity,
				InvoiceItemFormDialog.this::StartScan,
				false
		);
	}

	private BarcodeCallback ScanBarcodeViewDecodeContinuousProvider() {
		return barcodeResult -> {

			ScanBarcodeView().pause();
			FarayanUtility.ReleaseScreenOn(getWindow());
			SabadConfigs.Notify(Args.BeepManager);

			CapturedBarcode capturedBarcode = SabadUtility.CapturedBarcodeVersion(barcodeResult);
			ProductEntity scannedProduct = Args.ProductBarcodeRepo.ByBarcode(capturedBarcode);
			if (scannedProduct == null) {
				Args.Barcode = capturedBarcode;
				BarcodeEditText().setText(barcodeResult.getText());
				StopScan();
				return;
			}

			String groupEntered = ProductPicker().getText().toString();
			ProductEntity pickedProduct = ProductPicker().SelectedEntity(false);
			if (FarayanUtility.IsUsable(groupEntered)) {
				if (pickedProduct == null) {
					if (scannedProduct.Group.Refreshed(Args.GroupRepo).getID() == GroupPicker().SelectedEntity(false).getID()) {
						AskFillByScannedProduct(scannedProduct, barcodeResult.getText());
					} else {
						if (scannedProduct.Group.Needed) {
							AskChangeToNeededGroupAndProduct(scannedProduct, barcodeResult.getText());
						} else {
							AskChangeGroupAndNeededAndProduct(scannedProduct, barcodeResult.getText());
						}
					}
				} else {
					if (pickedProduct.getID() == scannedProduct.getID()) {
						FillUnfilled(pickedProduct, barcodeResult.getText());
						BarcodeEditText().setText(barcodeResult.getText());
						StopScan();
					}
				}
			} else {
				FillUnfilled(scannedProduct, barcodeResult.getText());
				StopScan();
			}
		};
	}

	private void StartScan() {
		HideKeyboard();
		ScanBarcodeView().setVisibility(View.VISIBLE);
		ScanBarcodeView().resume();
		FarayanUtility.KeepScreenOn(getWindow());
		StartScanButton().setVisibility(View.GONE);
		StopScanButton().setVisibility(View.VISIBLE);
	}

	private void HideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);
	}

	private void StopScan() {
		ScanBarcodeView().setVisibility(View.GONE);
		ScanBarcodeView().pause();
		FarayanUtility.ReleaseScreenOn(getWindow());
		StartScanButton().setVisibility(View.VISIBLE);
		StopScanButton().setVisibility(View.GONE);
	}

	private void ProductChanged(ProductEntity productEntity) {

	}

	private void AskChangeGroupAndNeededAndProduct(ProductEntity scannedProduct, String barcodeValue) {
		AskChangeGroupAndProduct(
				scannedProduct,
				TheActivity.getString(R.string.ChangeGroupAndNeededAndProductQuestion, scannedProduct.Group.DisplayableName),
				barcodeValue
		);
	}

	private void ClearAndFill(ProductEntity scannedProduct, String barcodeValue) {
		ProductPicker().setValue(scannedProduct);
		BarcodeEditText().setText(barcodeValue);
	}

	private void AskChangeToNeededGroupAndProduct(ProductEntity scannedProduct, String barcodeValue) {
		AskChangeGroupAndProduct(
				scannedProduct,
				"کالای اسکن‌شده در فهرست خرید است ولی از نوع انتخابی نیست، آیا ادامه می‌دهید؟",
				barcodeValue
		);
	}

	private void AskChangeGroupAndProduct(ProductEntity scannedProduct, String message, String barcodeValue) {
		QuestionDialog question = new QuestionDialog(TheActivity);
		question.setMessage(message);
		question.setFirstButton("خیر، دوباره اسکن می‌کنم", view -> {
			BarcodeEditText().setText("");
			StartScan();
		});
		question.setLastButton("بله", view -> {
			StopScan();
			ClearAndFill(scannedProduct, barcodeValue);
		});
		question.show();
	}

	private void AskFillByScannedProduct(ProductEntity scannedProduct, String barcodeValue) {
		QuestionDialog question = new QuestionDialog(TheActivity);
		question.setMessage("نام کالای اسکن‌شده با نام درج‌شده متفاوت است، پرسشنامه با اطلاعات کالای بارکدخوانده پر شود؟");
		question.setFirstButton("خیر", view -> {
			BarcodeEditText().setText("");
			StartScan();
		});
		question.setMiddleButton("ویرایش کالای اسکن‌شده", new View.OnClickListener()
		{
			@Override
			public void onClick(View view) {
				scannedProduct.setTitle(ProductPicker().getText().toString());
				Args.ProductRepo.Update(scannedProduct);
				StopScan();
				GroupChanged(scannedProduct.Group);
			}
		});
		question.setLastButton("بله", view -> {
			StopScan();
			ClearAndFill(scannedProduct, barcodeValue);
		});
		question.show();
	}

	private void GroupChanged(GroupEntity groupEntity) {
		if (groupEntity == null) {
			ProductPicker().Reload(Collections.emptyList());
			QuantityUnitBox().Reload(Collections.emptyList());
		} else {
			ProductParams productParams = new ProductParams();
			productParams.Purchasable = new EntityFilter<>(groupEntity);
			productParams.QueryableName = new TextFilter(null, TextMatchModes.NotNull);
			ProductPicker().Reload(productParams);

			QuantityNumberEntry().setHint(FarayanUtility.Or(groupEntity.UnitPrefix, "وزن، حجم، تعداد یا ..."));
			GroupUnitParams GroupUnitParams = new GroupUnitParams();
			GroupUnitParams.Group = new EntityFilter<>(groupEntity);
			List<UnitEntity> units = Args.GroupUnitRepo.All(GroupUnitParams).stream().map(x -> x.Unit.Refreshed(Args.UnitRepo)).collect(Collectors.toList());

			QuantityUnitBox().Reload(units);
		}
	}

	private void FillUnfilled(ProductEntity product, String barcodeValue) {
		if (ProductPicker().SelectedEntity(false) == null)
			ProductPicker().setValue(product);

		if (FarayanUtility.IsNullOrEmpty(BarcodeEditText().getText()))
			BarcodeEditText().setText(barcodeValue);

		if (QuantityUnitBox().SelectedEntity(false) == null && product.LastPurchase != null)
			QuantityUnitBox().setValue(product.LastPurchase.Unit);

		if (FeeRialEntry().getValue().TheAmount == 0 && product.LastPurchase != null)
			FeeRialEntry().setValue(new Rial(product.LastPurchase.Fee));
	}
}
