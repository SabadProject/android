@file:Suppress("UnusedImport")

package farayan.sabad.ui

//import androidx.compose.foundation.lazy.items
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.localize
import farayan.sabad.core.model.product.IProductRepo
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.db.Product
import farayan.sabad.isUsable
import farayan.sabad.models.Group.GroupRecyclerAdapter
import farayan.sabad.repo.CategoryRepo
import farayan.sabad.utility.hasValue
import farayan.sabad.vms.HomeViewModel
import farayan.sabad.vms.InvoiceItemFormViewModel
import farayan.sabad.vms.InvoiceItemFormViewModel.Companion.Factory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.math.BigDecimal
import java.util.Objects
import javax.inject.Inject
import kotlin.Unit
import farayan.sabad.db.Unit as PersistenceUnit

@AndroidEntryPoint
class HomeFragment : HomeFragmentParent() {
    var handler: Handler = Handler()
    private var invoiceItemFormViewModel: InvoiceItemFormViewModel? = null //= new ViewModelProvider(requireActivity()).get(InvoiceItemFormViewModel.class);
    private val homeViewModel: HomeViewModel = HomeViewModel.Factory.create(HomeViewModel::class.java)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun InitializeLayout() {

        GroupsRecyclerView().setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides requireActivity()) {
                val categories = homeViewModel.categories.collectAsState()
                val products = homeViewModel.pickedProducts.collectAsState()
                val units = homeViewModel.pickedUnits.collectAsState()
                val items = homeViewModel.items.collectAsStateWithLifecycle(listOf())
                val removingItem = remember { mutableStateOf<ItemRich?>(null) }
                Column {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Text(
                            text = "cats: ${categories.value.size}, prods: ${products.value.size}, items: ${items.value.size}, subs: ${homeViewModel.items.javaClass.simpleName}",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(categories.value, key = { it.id }) { category -> // import androidx.compose.foundation.lazy.items
                                val categoryItems = items.value.filter { it.categoryId == category.id }
                                val picked = categoryItems.isNotEmpty()
                                Column(
                                    modifier = Modifier
                                        .background(categoryBackgroundRes(category.needed, picked), shape = RoundedCornerShape(5.dp))
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp)
                                            .clickable {
                                                if (!category.needed) {
                                                    homeViewModel.changeNeeded(category, true)
                                                } else {
                                                    val dialogViewModel = Factory.create(InvoiceItemFormViewModel::class.java)
                                                    val dialog = InvoiceItemFormDialog(
                                                        requireActivity() as AppCompatActivity,
                                                        true,
                                                        null,
                                                        dialogViewModel
                                                    )
                                                    dialog.show()
                                                    dialog.maximize()
                                                    dialogViewModel.init(category)
                                                }
                                            },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = picked(R.drawable.ic_baseline_check_box_24, R.drawable.ic_baseline_crop_square_24)),
                                            modifier = Modifier.alpha(category.needed(1.0f, 0.0f)),
                                            contentDescription = ""
                                        )
                                        Text(
                                            text = category.displayableName,
                                            style = TextStyle(fontFamily = appFont, fontSize = 14.sp),
                                            modifier = Modifier.weight(1.0f)
                                        )
                                        Text(
                                            text = stringResource(id = statusResId(category.needed, category.picked)),
                                            style = TextStyle(fontFamily = appFont, fontSize = 9.sp, color = Color.DarkGray)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Switch(
                                            checked = category.needed, onCheckedChange = {
                                                homeViewModel.changeNeeded(category, it)
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color(0xff_76FF03),
                                                checkedTrackColor = Color.White,
                                            )
                                        )
                                    }
                                    for (item in categoryItems) {
                                        val product = products.value.first { it.id == item.productId }
                                        val unit = item.unitId?.let { units.value.firstOrNull { it.id == item.unitId } }
                                        pickedItem(
                                            item,
                                            product,
                                            unit,
                                            onEdit = { displayItemDialog(item) },
                                            onRemove = { removingItem.value = ItemRich(item, product, category) },
                                            Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                        }
                        if (removingItem.value.hasValue) {
                            AlertDialog(
                                onDismissRequest = { removingItem.value = null },
                                title = {
                                    Text(text = stringResource(id = R.string.home_item_remove_dialog_title))
                                },
                                text = {
                                    Text(text = stringResource(id = R.string.home_item_remove_dialog_text, removingItem.value!!.product.displayableName))
                                },
                                buttons = {
                                    Row(
                                        modifier = Modifier.padding(all = 8.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        TextButton(
                                            modifier = Modifier.weight(1.0f),
                                            onClick = { homeViewModel.removeItem(removingItem.value!!.item); removingItem.value = null }
                                        ) {
                                            Text(stringResource(id = R.string.home_item_remove_dialog_action_remove_text))
                                        }
                                        TextButton(
                                            modifier = Modifier.weight(1.0f),
                                            onClick = { removingItem.value = null }
                                        ) {
                                            Text(stringResource(id = R.string.home_item_remove_dialog_action_skip_text))
                                        }
                                    }
                                }
                            )
                        }
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

    private fun displayItemDialog(item: Item) {
        val dialogViewModel = Factory.create(InvoiceItemFormViewModel::class.java)
        val dialog = InvoiceItemFormDialog(
            requireActivity() as AppCompatActivity,
            true,
            null,
            dialogViewModel
        )
        dialog.show()
        dialog.maximize()
        dialogViewModel.init(item)
    }

    @Composable
    private fun pickedItem(item: Item, product: Product, unit: PersistenceUnit?, onEdit: (Item) -> Unit, onRemove: (Item) -> Unit, modifier: Modifier = Modifier) {
        Divider(color = Color.White, modifier = Modifier.padding(8.dp, 0.dp))
        Row(modifier = modifier
            .padding(5.dp)
            .clickable { onEdit(item) }) {
            Text(
                text = product.displayableName,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(4.dp, 2.dp),
                style = TextStyle(fontFamily = appFont, color = Color.DarkGray)
            )
            Text(
                text = "${item.quantity.localize()} ${unit?.displayableName}",
                modifier = Modifier
                    .padding(4.dp, 2.dp)
                    .width(48.dp),
                style = TextStyle(fontFamily = appFont, color = Color.DarkGray)
            )
            val currency = item.currency.isUsable({ Currency.valueOf(item.currency) }, { null })
            val price = currency?.let { currency.formatter(BigDecimal(item.fee), LocalContext.current) } ?: item.fee.localize()

            Text(
                text = price,
                modifier = Modifier
                    .padding(4.dp, 2.dp)
                    .width(24.dp),
                style = TextStyle(fontFamily = appFont, color = Color.DarkGray)
            )
            Icon(
                Icons.Filled.Edit, contentDescription = "edit", tint = Color.White, modifier = Modifier
                    .padding(4.dp, 2.dp)
                    .width(16.dp)
                    .height(16.dp)
            )
            Icon(
                Icons.Filled.Delete, contentDescription = "delete", tint = Color.White, modifier = Modifier
                    .padding(4.dp, 2.dp)
                    .width(16.dp)
                    .height(16.dp)
                    .clickable { onRemove(item) }
            )
        }
    }

    private fun statusResId(needed: Boolean, picked: Boolean): Int {
        return if (needed) {
            if (picked) R.string.GroupStatusPicked else R.string.GroupStatusNeeded
        } else {
            R.string.GroupStatusNone
        }
    }

    private fun categoryBackgroundRes(needed: Boolean, picked: Boolean): Color {
        return if (needed) {
            if (picked) Color(0x5b, 0xc0, 0xde) else Color(0x5c, 0xb8, 0x5c)
        } else {
            Color(0xf5, 0xf5, 0xf5)
        }
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

operator fun <T> Boolean.invoke(onTrue: T, onFalse: T): T {
    return if (this) onTrue else onFalse
}

operator fun <T> Boolean.invoke(onTrue: () -> T, onFalse: () -> T): T {
    return if (this) onTrue() else onFalse()
}

data class ItemRich(val item: Item, val product: Product, val category: Category)