package farayan.sabad.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.client.android.BeepManager;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.RecyclerViewOrientations;
import farayan.commons.Commons.Rial;
import farayan.commons.FarayanUtility;
import farayan.commons.QueryBuilderCore.ComparableFilter;
import farayan.commons.QueryBuilderCore.SortConfig;
import farayan.commons.QueryBuilderCore.SortDirections;
import farayan.commons.QueryBuilderCore.TextFilter;
import farayan.commons.QueryBuilderCore.TextMatchModes;
import farayan.sabad.R;
import farayan.sabad.SabadConstants;
import farayan.sabad.SabadUtility;
import farayan.sabad.constants.SabadFragmentEvents;
import farayan.sabad.core.OnePlace.CategoryGroup.ICategoryGroupRepo;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Group.GroupParams;
import farayan.sabad.core.OnePlace.Group.GroupSchema;
import farayan.sabad.core.OnePlace.Group.GroupUniqueNameNeededException;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.Group.NewGroupNameNeededException;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo;
import farayan.sabad.core.OnePlace.Store.IStoreRepo;
import farayan.sabad.core.OnePlace.StoreCategory.IStoreCategoryRepo;
import farayan.sabad.core.OnePlace.StoreGroup.IStoreGroupRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.model.product.IProductRepo;
import farayan.sabad.models.Group.GroupRecyclerAdapter;
import farayan.sabad.vms.InvoiceItemFormViewModel;


@AndroidEntryPoint
public class HomeFragment extends HomeFragmentParent
{
	private InvoiceItemFormViewModel invoiceItemFormViewModel ;//= new ViewModelProvider(requireActivity()).get(InvoiceItemFormViewModel.class);
	private final View.OnClickListener EditButtonOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View view) {
			String name = Objects.requireNonNull(QueryEditText().getText()).toString();
			if (FarayanUtility.IsNullOrEmpty(name))
				return;
			GroupParams groupParams = new GroupParams();
			groupParams.QueryableName = new TextFilter(FarayanUtility.Queryable(name));
			GroupEntity groupEntity = TheGroupRepo.First(groupParams);
			if (groupEntity == null) {
				return;
			}
			GroupFormDialog dialog = new GroupFormDialog(
					new GroupFormDialog.Input(
							groupEntity,
							TheGroupRepo,
							TheInvoiceItemRepo,
							TheProductRepo,
							TheGroupUnitRepo,
							x -> Reload(),
							x -> Reload()
					),
					requireActivity()
			);
			dialog.show();
		}
	};
	private boolean reloaded;

	@Inject
	public void Inject(
			IStoreCategoryRepo storeCategoryRepo,
			IStoreGroupRepo storeGroupRepo,
			ICategoryGroupRepo categoryGroupRepo,
			IGroupRepo GroupRepo,
			IStoreRepo storeRepo,
			IInvoiceRepo invoiceRepo,
			IInvoiceItemRepo invoiceItemRepo,
			IGroupUnitRepo GroupUnitRepo,
			IProductRepo productRepo,
			IProductBarcodeRepo productBarcodeRepo,
			IUnitRepo unitRepo
	) {
		TheStoreCategoryRepo = storeCategoryRepo;
		TheStoreGroupRepo = storeGroupRepo;
		TheCategoryGroupRepo = categoryGroupRepo;
		TheGroupRepo = GroupRepo;
		TheStoreRepo = storeRepo;
		TheInvoiceRepo = invoiceRepo;
		TheInvoiceItemRepo = invoiceItemRepo;
		TheGroupUnitRepo = GroupUnitRepo;
		TheProductRepo = productRepo;
		TheProductBarcodeRepo = productBarcodeRepo;
		TheUnitRepo = unitRepo;
		init();
	}

	private IStoreCategoryRepo TheStoreCategoryRepo;
	private IStoreGroupRepo TheStoreGroupRepo;
	private ICategoryGroupRepo TheCategoryGroupRepo;
	private IGroupRepo TheGroupRepo;
	private IStoreRepo TheStoreRepo;
	private IInvoiceRepo TheInvoiceRepo;
	private IInvoiceItemRepo TheInvoiceItemRepo;
	private IGroupUnitRepo TheGroupUnitRepo;
	private IProductRepo TheProductRepo;
	private IProductBarcodeRepo TheProductBarcodeRepo;
	private IUnitRepo TheUnitRepo;

	@Override
	public int TitleID() {
		return R.string.home_title;
	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.ScanMenu:
				StartScan();
				break;
			case R.id.CheckoutMenu:
				TryCheckout();
				break;
			case R.id.DisplayInvoicesMenu:
				HostActivity.OnFragmentCalled(null, SabadFragmentEvents.DisplayInvoices);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public enum Requests
	{
		Scan(1);

		public final int ID;

		Requests(int id) {
			ID = id;
		}
	}

	private void StartScan() {
		SabadUtility.BarcodeScanCameraPermission(
				HostActivity,
				() -> {
					Intent intent = new Intent(getActivity(), ScanActivity.class);
					startActivityForResult(intent, Requests.Scan.ID);
				},
				false
		);
	}

	private void TryCheckout() {

		GroupParams remainedGroupsParams = new GroupParams();
		remainedGroupsParams.Needed = new ComparableFilter<>(true);
		remainedGroupsParams.Picked = new ComparableFilter<>(true);
		remainedGroupsParams.Deleted = new ComparableFilter<>(false);
		long pickedCount = TheGroupRepo.Count(remainedGroupsParams);

		if (pickedCount == 0) {
			FarayanUtility.ShowToastFormatted(getActivity(), "برای ثبت صورتحساب و صندوق، دستکم یکی از نیازمندی‌ها را بردارید");
			return;
		}

		DisplayCheckout();
	}

	private void AutoCheckout() {
		GroupParams neededGroupsParams = new GroupParams();
		neededGroupsParams.Needed = new ComparableFilter<>(true);
		long neededCount = TheGroupRepo.Count(neededGroupsParams);

		GroupParams remainedGroupsParams = new GroupParams();
		remainedGroupsParams.Needed = new ComparableFilter<>(true);
		remainedGroupsParams.Picked = new ComparableFilter<>(false);
		long remainedCount = TheGroupRepo.Count(remainedGroupsParams);

		if (neededCount > 0 && remainedCount == 0) {
			if (SabadUtility.DisplayCheckoutDialogAutomatically(getActivity())) {
				if (SabadUtility.CheckoutGuideNeeded(getActivity()))
					FarayanUtility.ShowToastFormatted(getActivity(), false, "چون تمام کالاهای فهرست خرید (روشن) را برداشنه‌اید، می‌توانید پایان خرید را بزنید");
				handler.postDelayed(this::DisplayCheckout, 5000);
			}
		}
	}

	private void DisplayCheckout() {
		CheckoutPurchaseDialog dialog = new CheckoutPurchaseDialog(
				new CheckoutPurchaseDialog.Input(
						TheStoreCategoryRepo,
						TheStoreGroupRepo,
						TheCategoryGroupRepo,
						TheGroupRepo,
						TheStoreRepo,
						TheInvoiceRepo,
						TheInvoiceItemRepo,
						invoiceEntity -> {
							Reload();
							HostActivity.OnFragmentCalled(null, SabadFragmentEvents.DisplayInvoices);
						}
				),
				HostActivity
		);

		dialog.show();
	}

	private final TextView.OnEditorActionListener QueryEditTextOnEditorActionListener = (v, actionId, event) -> {
		if (FarayanUtility.IsNullOrEmpty(QueryEditText().getText()))
			return false;
		String query = Objects.requireNonNull(QueryEditText().getText()).toString();
		if (actionId != EditorInfo.IME_ACTION_DONE)
			return false;
		if (Persist(false))
			return true;

		GroupEntity groupEntity = TheGroupRepo.FirstByName(query);

		if (QueryEditText().getSelectionStart() != 0 || QueryEditText().getSelectionEnd() != query.length()) {
			String message = groupEntity.Needed ? "«%s» هم‌اکنون در  فهرست خرید است، برای ثبت کالای برداشته‌شده، یکبار دیگر اینتر صفحه‌کلید را بزنید" : "«%s» هم‌اکنون موجود است، برای افزودن به فهرست خرید، یکبار دیگر اینتر صفحه‌کلید را بزنید";
			FarayanUtility.ShowToastFormatted(
					getActivity(),
					message,
					query
			);
			QueryEditText().selectAll();
			return true;
		}
		if (!groupEntity.Needed) {
			FarayanUtility.ShowToastFormatted(
					getActivity(),
					"«%s» به فهرست خرید افزوده شد، برای ثبت کالای برداشته‌شده، یکبار دیگر اینتر صفحه‌کلید را بزنید",
					query
			);

			groupEntity.Needed = true;
			TheGroupRepo.Update(groupEntity);
			Reload();
			return true;
		}

		if (!groupEntity.Picked) {
			InvoiceItemFormDialog dialog = new InvoiceItemFormDialog(
					new InvoiceItemFormInputArgs(
							groupEntity.Item,
							groupEntity,
							null,
							null,
							invoiceItemEntity -> Reload(),
							invoiceItemEntity -> Reload(),
							TheGroupRepo,
							TheGroupUnitRepo,
							TheProductRepo,
							TheProductBarcodeRepo,
							TheInvoiceItemRepo,
							TheUnitRepo,
							new BeepManager(requireActivity())
					),
					(AppCompatActivity) requireActivity(),
					true,
					null,
					invoiceItemFormViewModel
			);
			dialog.show();
			return true;
		}
		return false;
	};

	private final Runnable ReloadCallback = () -> Reload(Objects.requireNonNull(QueryEditText().getText()).toString());
	private final TextWatcher QueryTextChangedListener = new TextWatcher()
	{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			handler.removeCallbacks(ReloadCallback);
			handler.postDelayed(ReloadCallback, 500);
		}

		@Override
		public void afterTextChanged(Editable s) {
			QueryLayout().setError(null);
		}
	};
	private final View.OnClickListener RegisterButtonOnClickListener = v -> Persist(true);

	private boolean Persist(boolean toast) {
		try {
			String name = Objects.requireNonNull(QueryEditText().getText()).toString();
			TheGroupRepo.New(name, null, null, true);
			Reload(name);
			QueryLayout().setError(null);
			if (SabadUtility.NewGroupGuideNeeded(getActivity()))
				FarayanUtility.ShowToastFormatted(getActivity(), false, getString(R.string.NewGroupGuide));
			return true;
		} catch (NewGroupNameNeededException e) {
			QueryLayout().setError(getString(R.string.NameNeeded));
			if (toast)
				FarayanUtility.ShowToast(getActivity(), getString(R.string.NameNeeded));
		} catch (GroupUniqueNameNeededException e) {
			QueryLayout().setError(getString(R.string.CategoryUsedName));
			if (toast)
				FarayanUtility.ShowToast(getActivity(), getString(R.string.CategoryUsedName));
		}
		return false;
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		inflater.inflate(R.menu.home_menu, menu);
	}


	Handler handler = new Handler();

	protected void InitializeLayout() {
		FarayanUtility.prepareRecyclerView(
				GroupsRecyclerView(),
				RecyclerViewOrientations.Vertical,
				null,
				true,
				true
		);
		init();
		QueryEditText().addTextChangedListener(QueryTextChangedListener);
		EditButton().setOnClickListener(EditButtonOnClickListener);
		RegisterButton().setOnClickListener(RegisterButtonOnClickListener);
		QueryEditText().setOnEditorActionListener(QueryEditTextOnEditorActionListener);
	}

	private void Reload(String query) {
		query = FarayanUtility.Queryable(query);
		GroupParams groupParams = new GroupParams();
		groupParams.Deleted = new ComparableFilter<>(false);
		groupParams.QueryableName = new TextFilter(query);
		groupParams.Sorts.add(new SortConfig(GroupSchema.Needed, SortDirections.Descending));
		groupParams.Sorts.add(new SortConfig(GroupSchema.Picked, SortDirections.Ascending));
		groupParams.Sorts.add(new SortConfig(GroupSchema.Category, SortDirections.Ascending));
		groupParams.Sorts.add(new SortConfig(GroupSchema.PurchasedCount, SortDirections.Descending));
		groupParams.Sorts.add(new SortConfig(GroupSchema.Importance, SortDirections.Descending));

		GroupRecyclerAdapter theAdapter = new GroupRecyclerAdapter(
				getActivity(),
				TheGroupRepo.All(groupParams),
				GroupEntity -> {
					PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient);
					AutoCheckout();
				},
				GroupEntity -> {
					PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient);
					AutoCheckout();
				},
				component -> {
					InvoiceItemFormDialog dialog = new InvoiceItemFormDialog(
							new InvoiceItemFormInputArgs(
									component.getEntity().Item,
									component.getEntity(),
									null,
									null,
									invoiceItemEntity -> {
										component.DisplayEntity(component.getEntity().RefreshForced(TheGroupRepo));
										PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient);
										AutoCheckout();
									},
									invoiceItemEntity -> {
										component.DisplayEntity(component.getEntity().RefreshForced(TheGroupRepo));
										PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient);
									},
									TheGroupRepo,
									TheGroupUnitRepo,
									TheProductRepo,
									TheProductBarcodeRepo,
									TheInvoiceItemRepo,
									TheUnitRepo,
									new BeepManager(requireActivity())
							),
							(AppCompatActivity) requireActivity(),
							true,
							null,
							invoiceItemFormViewModel
					);
					dialog.show();
				},
				GroupEntity -> Reload(),
				Rial.Coefficients.Rial
		);
		GroupsRecyclerView().setAdapter(theAdapter);

		groupParams.QueryableName = new TextFilter(query, TextMatchModes.Exactly);
		boolean found = TheGroupRepo.Count(groupParams) == 1;
		RegisterButton().setVisibility(found ? View.GONE : View.VISIBLE);
		EditButton().setVisibility(found ? View.VISIBLE : View.GONE);
		RegisterButton().setEnabled(FarayanUtility.IsUsable(query));
		PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient);
	}

	private void init() {
		if (reloaded)
			return;
		if (TheGroupRepo == null)
			return;
		if (QueryEditText() == null)
			return;
		Reload(Objects.requireNonNull(QueryEditText().getText()).toString());
		reloaded = true;
	}

	private void Reload() {
		Reload(Objects.requireNonNull(QueryEditText().getText()).toString());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		if (requestCode == Requests.Scan.ID && resultCode == SabadConstants.DataChangedResultCode) {
			Reload();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public boolean HandleBackPressed() {
		if (FarayanUtility.IsUsable(QueryEditText().getText())) {
			QueryEditText().setText("");
			return true;
		}
		return super.HandleBackPressed();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		invoiceItemFormViewModel = new ViewModelProvider(requireActivity()).get(InvoiceItemFormViewModel.class);
	}
}
