@file:Suppress("UnusedImport")

package farayan.sabad.ui

//import androidx.compose.foundation.lazy.items
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import farayan.commons.Commons.Rial
import farayan.commons.FarayanUtility
import farayan.commons.QueryBuilderCore.ComparableFilter
import farayan.commons.QueryBuilderCore.SortConfig
import farayan.commons.QueryBuilderCore.SortDirections
import farayan.commons.QueryBuilderCore.TextFilter
import farayan.commons.QueryBuilderCore.TextMatchModes
import farayan.sabad.R
import farayan.sabad.SabadConstants
import farayan.sabad.SabadUtility
import farayan.sabad.composables.CategoriesCategoryRemoteItemComposable
import farayan.sabad.composables.CategoriesCategoryWithItemsComposable
import farayan.sabad.constants.SabadFragmentEvents
import farayan.sabad.core.OnePlace.CategoryGroup.ICategoryGroupRepo
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.Group.GroupParams
import farayan.sabad.core.OnePlace.Group.GroupSchema
import farayan.sabad.core.OnePlace.Group.GroupUniqueNameNeededException
import farayan.sabad.core.OnePlace.Group.IGroupRepo
import farayan.sabad.core.OnePlace.Group.NewGroupNameNeededException
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo
import farayan.sabad.core.OnePlace.Invoice.IInvoiceRepo
import farayan.sabad.core.OnePlace.Invoice.InvoiceEntity
import farayan.sabad.core.OnePlace.Store.IStoreRepo
import farayan.sabad.core.OnePlace.StoreCategory.IStoreCategoryRepo
import farayan.sabad.core.OnePlace.StoreGroup.IStoreGroupRepo
import farayan.sabad.core.OnePlace.Unit.IUnitRepo
import farayan.sabad.core.model.product.IProductRepo
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Product
import farayan.sabad.models.Group.GroupRecyclerAdapter
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.utility.hasValue
import farayan.sabad.vms.HomeViewModel
import farayan.sabad.vms.InvoiceItemFormViewModel
import farayan.sabad.vms.InvoiceItemFormViewModel.Companion.Factory
import java.util.Objects
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : HomeFragmentParent() {
    var handler: Handler = Handler()
    private var invoiceItemFormViewModel: InvoiceItemFormViewModel? = null //= new ViewModelProvider(requireActivity()).get(InvoiceItemFormViewModel.class);
    private lateinit var homeViewModel: HomeViewModel
    private var reloaded = false
    private var TheStoreCategoryRepo: IStoreCategoryRepo? = null
    private var TheStoreGroupRepo: IStoreGroupRepo? = null
    private var TheCategoryGroupRepo: ICategoryGroupRepo? = null
    private var TheGroupRepo: IGroupRepo? = null
    private var TheStoreRepo: IStoreRepo? = null
    private var TheInvoiceRepo: IInvoiceRepo? = null
    private var TheGroupUnitRepo: IGroupUnitRepo? = null
    private var TheProductRepo: IProductRepo? = null
    private var TheUnitRepo: IUnitRepo? = null
    private var categoryRepo: CategoryRepo? = null

    private val EditButtonOnClickListener = View.OnClickListener {
        val name = Objects.requireNonNull(QueryEditText().text).toString()
        if (FarayanUtility.IsNullOrEmpty(name)) return@OnClickListener
        val groupParams = GroupParams()
        groupParams.QueryableName = TextFilter(FarayanUtility.Queryable(name))
        val groupEntity = TheGroupRepo!!.First(groupParams) ?: return@OnClickListener
        val dialog = GroupFormDialog(
            GroupFormDialog.Input(
                groupEntity,
                TheGroupRepo!!,
                TheProductRepo!!,
                TheGroupUnitRepo!!,
                { x: GroupEntity? -> Reload() },
                { x: GroupEntity? -> Reload() }
            ),
            requireActivity()
        )
        dialog.show()
    }
    private val QueryEditTextOnEditorActionListener = OnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
        if (FarayanUtility.IsNullOrEmpty(QueryEditText().text)) return@OnEditorActionListener false
        val query = Objects.requireNonNull(QueryEditText().text).toString()
        if (actionId != EditorInfo.IME_ACTION_DONE) return@OnEditorActionListener false
        if (Persist(false)) return@OnEditorActionListener true

        val groupEntity = TheGroupRepo!!.FirstByName(query)

        if (QueryEditText().selectionStart != 0 || QueryEditText().selectionEnd != query.length) {
            val message =
                if (groupEntity!!.Needed) "«%s» هم‌اکنون در  فهرست خرید است، برای ثبت کالای برداشته‌شده، یکبار دیگر اینتر صفحه‌کلید را بزنید" else "«%s» هم‌اکنون موجود است، برای افزودن به فهرست خرید، یکبار دیگر اینتر صفحه‌کلید را بزنید"
            FarayanUtility.ShowToastFormatted(
                activity,
                message,
                query
            )
            QueryEditText().selectAll()
            return@OnEditorActionListener true
        }
        if (!groupEntity!!.Needed) {
            FarayanUtility.ShowToastFormatted(
                activity,
                "«%s» به فهرست خرید افزوده شد، برای ثبت کالای برداشته‌شده، یکبار دیگر اینتر صفحه‌کلید را بزنید",
                query
            )

            groupEntity.Needed = true
            TheGroupRepo!!.Update(groupEntity)
            Reload()
            return@OnEditorActionListener true
        }

        if (!groupEntity.Picked) {
            //invoiceItemFormViewModel!!.init(categoryRepo!!.first(), true, null, false)
            val dialog = InvoiceItemFormDialog(
                (requireActivity() as AppCompatActivity),
                true,
                null,
                invoiceItemFormViewModel!!
            )
            dialog.show()
            dialog.maximize()
            return@OnEditorActionListener true
        }
        false
    }
    private val ReloadCallback = Runnable {
        Reload(Objects.requireNonNull(QueryEditText().text).toString())
        //
        homeViewModel.filter(QueryEditText().text.toString())
    }
    private val QueryTextChangedListener: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            handler.removeCallbacks(ReloadCallback)
            handler.postDelayed(ReloadCallback, 500)
        }

        override fun afterTextChanged(s: Editable) {
            QueryLayout().error = null
        }
    }
    private val RegisterButtonOnClickListener = View.OnClickListener { v: View? -> Persist(true) }

    @Inject
    fun Inject(
        storeCategoryRepo: IStoreCategoryRepo?,
        storeGroupRepo: IStoreGroupRepo?,
        categoryGroupRepo: ICategoryGroupRepo?,
        GroupRepo: IGroupRepo?,
        storeRepo: IStoreRepo?,
        invoiceRepo: IInvoiceRepo?,
        GroupUnitRepo: IGroupUnitRepo?,
        productRepo: IProductRepo?,
        unitRepo: IUnitRepo?,
        categoryRepo: CategoryRepo?
    ) {
        TheStoreCategoryRepo = storeCategoryRepo
        TheStoreGroupRepo = storeGroupRepo
        TheCategoryGroupRepo = categoryGroupRepo
        TheGroupRepo = GroupRepo
        TheStoreRepo = storeRepo
        TheInvoiceRepo = invoiceRepo
        TheGroupUnitRepo = GroupUnitRepo
        TheProductRepo = productRepo
        TheUnitRepo = unitRepo
        this.categoryRepo = categoryRepo
        init()
    }

    override fun TitleID(): Int {
        return R.string.home_title
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ScanMenu -> StartScan()
            R.id.CheckoutMenu -> TryCheckout()
            R.id.DisplayInvoicesMenu -> HostActivity.OnFragmentCalled(null, SabadFragmentEvents.DisplayInvoices)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun StartScan() {
        SabadUtility.BarcodeScanCameraPermission(
            HostActivity,
            {
                val intent = Intent(activity, ScanActivity::class.java)
                startActivityForResult(intent, Requests.Scan.ID)
            },
            false
        )
    }

    private fun TryCheckout() {
        val remainedGroupsParams = GroupParams()
        remainedGroupsParams.Needed = ComparableFilter(true)
        remainedGroupsParams.Picked = ComparableFilter(true)
        remainedGroupsParams.Deleted = ComparableFilter(false)
        val pickedCount = TheGroupRepo!!.Count(remainedGroupsParams)

        if (pickedCount == 0L) {
            FarayanUtility.ShowToastFormatted(activity, "برای ثبت صورتحساب و صندوق، دستکم یکی از نیازمندی‌ها را بردارید")
            return
        }

        DisplayCheckout()
    }

    private fun AutoCheckout() {
        val neededGroupsParams = GroupParams()
        neededGroupsParams.Needed = ComparableFilter(true)
        val neededCount = TheGroupRepo!!.Count(neededGroupsParams)

        val remainedGroupsParams = GroupParams()
        remainedGroupsParams.Needed = ComparableFilter(true)
        remainedGroupsParams.Picked = ComparableFilter(false)
        val remainedCount = TheGroupRepo!!.Count(remainedGroupsParams)

        if (neededCount > 0 && remainedCount == 0L) {
            if (SabadUtility.DisplayCheckoutDialogAutomatically(activity)) {
                if (SabadUtility.CheckoutGuideNeeded(activity)) FarayanUtility.ShowToastFormatted(
                    activity,
                    false,
                    "چون تمام کالاهای فهرست خرید (روشن) را برداشنه‌اید، می‌توانید پایان خرید را بزنید"
                )
                handler.postDelayed({ this.DisplayCheckout() }, 5000)
            }
        }
    }

    private fun DisplayCheckout() {
        val dialog = CheckoutPurchaseDialog(
            CheckoutPurchaseDialog.Input(
                TheStoreCategoryRepo,
                TheStoreGroupRepo,
                TheCategoryGroupRepo,
                TheGroupRepo,
                TheStoreRepo,
                TheInvoiceRepo
            ) { invoiceEntity: InvoiceEntity? ->
                Reload()
                HostActivity.OnFragmentCalled(null, SabadFragmentEvents.DisplayInvoices)
            },
            HostActivity
        )

        dialog.show()
    }

    private fun Persist(toast: Boolean): Boolean {
        try {
            val name = Objects.requireNonNull(QueryEditText().text).toString()
            TheGroupRepo!!.New(name, null, null, true)
            Reload(name)
            QueryLayout().error = null
            if (SabadUtility.NewGroupGuideNeeded(activity)) FarayanUtility.ShowToastFormatted(activity, false, getString(R.string.NewGroupGuide))
            return true
        } catch (e: NewGroupNameNeededException) {
            QueryLayout().error = getString(R.string.NameNeeded)
            if (toast) FarayanUtility.ShowToast(activity, getString(R.string.NameNeeded))
        } catch (e: GroupUniqueNameNeededException) {
            QueryLayout().error = getString(R.string.CategoryUsedName)
            if (toast) FarayanUtility.ShowToast(activity, getString(R.string.CategoryUsedName))
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun InitializeLayout() {
        Log.i("flow", "HomeFragment: InitializeLayout")
        homeViewModel = HomeViewModel.Factory.create(HomeViewModel::class.java)
        GroupsRecyclerView().setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides requireActivity(), LocalLayoutDirection provides LayoutDirection.Rtl) {
                val categories = homeViewModel.categories.collectAsState()
                val products = homeViewModel.pickedProducts.collectAsState()
                val units = homeViewModel.pickedUnits.collectAsState()
                val items = homeViewModel.items.collectAsStateWithLifecycle(listOf())
                val removingItem = remember { mutableStateOf<ItemRich?>(null) }
                Column {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(categories.value, key = { it.id }) { category -> // import androidx.compose.foundation.lazy.items
                            CategoriesCategoryWithItemsComposable(
                                category,
                                items.value,
                                { c, n -> homeViewModel.changeNeeded(c, n) },
                                products.value,
                                units.value,
                                { removingItem.value = it }
                            )
                        }
                    }
                    if (removingItem.value.hasValue) {
                        CategoriesCategoryRemoteItemComposable(
                            removingItem = removingItem.value!!,
                            onConfirmed = { homeViewModel.removeItem(removingItem.value!!.item); removingItem.value = null },
                            onCancelled = { removingItem.value = null }
                        )
                    }
                }
            }
        }
        init()
        QueryEditText().addTextChangedListener(QueryTextChangedListener)
        EditButton().setOnClickListener(EditButtonOnClickListener)
        RegisterButton().setOnClickListener(RegisterButtonOnClickListener)
        QueryEditText().setOnEditorActionListener(QueryEditTextOnEditorActionListener)
    }


    private fun Reload(query: String = Objects.requireNonNull(QueryEditText().text).toString()) {
        var query: String? = query
        query = FarayanUtility.Queryable(query)
        val groupParams = GroupParams()
        groupParams.Deleted = ComparableFilter(false)
        groupParams.QueryableName = TextFilter(query)
        groupParams.Sorts.add(SortConfig(GroupSchema.Needed, SortDirections.Descending))
        groupParams.Sorts.add(SortConfig(GroupSchema.Picked, SortDirections.Ascending))
        groupParams.Sorts.add(SortConfig(GroupSchema.Category, SortDirections.Ascending))
        groupParams.Sorts.add(SortConfig(GroupSchema.PurchasedCount, SortDirections.Descending))
        groupParams.Sorts.add(SortConfig(GroupSchema.Importance, SortDirections.Descending))

        val theAdapter = GroupRecyclerAdapter(
            activity,
            TheGroupRepo!!.All(groupParams),
            { GroupEntity: GroupEntity? ->
                PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient)
                AutoCheckout()
            },
            { GroupEntity: GroupEntity? ->
                PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient)
                AutoCheckout()
            },
            { component: GroupHomeItemComponent ->
                //invoiceItemFormViewModel!!.init(categoryRepo!!.first(), true, null, false)
                val dialog = InvoiceItemFormDialog(
                    (requireActivity() as AppCompatActivity),
                    true,
                    null,
                    invoiceItemFormViewModel!!
                )
                dialog.show()
                dialog.maximize()
            },
            { GroupEntity: GroupEntity? -> Reload() },
            Rial.Coefficients.Rial
        )
        //todo:GroupsRecyclerView().setAdapter(theAdapter)

        groupParams.QueryableName = TextFilter(query, TextMatchModes.Exactly)
        val found = TheGroupRepo!!.Count(groupParams) == 1L
        RegisterButton().visibility = if (found) View.GONE else View.VISIBLE
        EditButton().visibility = if (found) View.VISIBLE else View.GONE
        RegisterButton().isEnabled = FarayanUtility.IsUsable(query)
        PurchaseSummary().ReloadSummary(SabadConstants.TheCoefficient)
    }

    private fun init() {
        if (reloaded) return
        if (TheGroupRepo == null) return
        if (QueryEditText() == null) return
        Reload(Objects.requireNonNull(QueryEditText().text).toString())
        reloaded = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Requests.Scan.ID && resultCode == SabadConstants.DataChangedResultCode) {
            Reload()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun HandleBackPressed(): Boolean {
        if (FarayanUtility.IsUsable(QueryEditText().text)) {
            QueryEditText().setText("")
            return true
        }
        return super.HandleBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //invoiceItemFormViewModel = new ViewModelProvider(requireActivity()).get(InvoiceItemFormViewModel.class);
        invoiceItemFormViewModel = Factory.create(InvoiceItemFormViewModel::class.java)
    }

    enum class Requests(val ID: Int) {
        Scan(1)
    }

}

data class ItemRich(val item: Item, val product: Product, val category: Category)