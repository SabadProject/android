package farayan.sabad.ui

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import farayan.commons.FarayanUtility
import farayan.commons.QueryBuilderCore.ensured
import farayan.sabad.R
import farayan.sabad.SabadConfigs
import farayan.sabad.SabadConstants
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity
import farayan.sabad.core.commons.Currency
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.core.model.unit.UnitEntity
import farayan.sabad.isUsable
import farayan.sabad.referencePrice
import farayan.sabad.ui.components.CameraCapture
import farayan.sabad.ui.components.CurrenciesDropdownMenuBox
import farayan.sabad.ui.components.GroupsDropdownMenuBox
import farayan.sabad.ui.components.NumberEntry
import farayan.sabad.ui.components.UnitVariationDropdownBox
import farayan.sabad.ui.components.UnitsDropdownMenuBox
import farayan.sabad.ui.components.displayable
import farayan.sabad.utility.hasValue
import farayan.sabad.vms.InvoiceItemFormViewModel
import java.math.BigDecimal

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
class InvoiceItemFormDialog(
    private val inputArgs: InvoiceItemFormInputArgs,
    context: AppCompatActivity,
    cancelable: Boolean,
    cancelListener: DialogInterface.OnCancelListener?,
    private val viewModel: InvoiceItemFormViewModel
) : Dialog(context, cancelable, cancelListener) {
    init {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window!!.setDecorFitsSystemWindows(false)
            //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        } else {
            @Suppress("DEPRECATION")
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        setContentView(ComposeView(context).apply {
            setOnApplyWindowInsetsListener { _, windowInsets ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                    setPadding(0, 0, 0, imeHeight)
                }
                windowInsets
            }
            setViewTreeLifecycleOwner(context)
            setViewTreeSavedStateRegistryOwner(context)
            setViewTreeViewModelStoreOwner(context)
            setContent {
                val groups = viewModel.groups.collectAsState()
                val groupValue = viewModel.group.collectAsState()
                val product = viewModel.product.collectAsState()
                val question = viewModel.question.collectAsState()

                val barcodeValue = viewModel.productBarcode.collectAsState()
                var barcodeScan by remember { mutableStateOf(false) }
                var productPhotography by remember { mutableStateOf(false) }
                val cameraPermissionState = rememberPermissionState(
                    Manifest.permission.CAMERA
                ) {
                    Log.i("Permission", "resulted")
                }
                var nameValue by remember { mutableStateOf("") }
                var quantityValue: BigDecimal? by remember { mutableStateOf(BigDecimal.ONE) }
                var quantityUnit: UnitEntity? by remember { mutableStateOf<UnitEntity?>(null) }
                var packageMeasurementUnit: UnitVariations? by remember {
                    mutableStateOf<UnitVariations?>(
                        null
                    )
                }
                var packageMeasurementValue: BigDecimal? by remember {
                    mutableStateOf<BigDecimal?>(
                        null
                    )
                }
                var priceAmount: BigDecimal? by remember { mutableStateOf<BigDecimal?>(null) }
                var priceCurrency: Currency? by remember { mutableStateOf<Currency?>(null) }
                val photos = viewModel.productPhotos.collectAsState()

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = stringResource(id = R.string.invoice_item_form_dialog_title),
                            style = TextStyle(color = Color.White, fontFamily = appFont),
                            modifier = Modifier
                                .background(Color.Black)
                                .fillMaxWidth()
                                .defaults()
                        )
                        OutlinedTextField(
                            value = barcodeValue.value,
                            onValueChange = { viewModel.barcodeChangedManually(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.Ltr
                            ),
                            label = {
                                Text(
                                    text = stringResource(id = R.string.invoice_item_form_dialog_scan_barcode_hint),
                                    style = TextStyle(fontFamily = appFont)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        barcodeScan = true
                                        if (!cameraPermissionState.status.isGranted) {
                                            if (!cameraPermissionState.status.shouldShowRationale) {
                                                cameraPermissionState.launchPermissionRequest()
                                            }
                                        }
                                    },
                                ) {
                                    Image(
                                        painterResource(R.drawable.ic_noun_scan),
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height(24.dp),
                                        colorFilter = ColorFilter.tint(Color.Black),
                                        contentDescription = "scan"
                                    )
                                }
                            }
                        )
                        if (barcodeScan) {
                            if (cameraPermissionState.status.isGranted) {
                                AndroidView(
                                    modifier = Modifier
                                        .defaults()
                                        .fillMaxWidth()
                                        .height(120.dp),
                                    factory = { ctx ->
                                        BarcodeView(ctx).apply {
                                            resume()
                                            setDecoderFactory(
                                                DefaultDecoderFactory(
                                                    SabadConstants.SupportedBarcodeFormats
                                                )
                                            )
                                            decodeContinuous {
                                                pause()
                                                FarayanUtility.ReleaseScreenOn(window)
                                                SabadConfigs.Notify(inputArgs.beepManager)
                                                viewModel.barcodeScanned(it)
                                            }
                                        }
                                    }
                                )
                            } else {
                                Text(
                                    text = stringResource(id = R.string.invoice_item_form_dialog_camera_permission_needed),
                                    style = TextStyle(fontFamily = appFont),
                                    modifier = Modifier.defaults()
                                )
                            }
                        }
                        GroupsDropdownMenuBox(
                            groupValue.value.value?.DisplayableName ?: "",
                            stringResource(id = R.string.invoice_item_form_dialog_group_label),
                            groups.value!!.map {
                                GroupInvoiceItemForm(
                                    it.id,
                                    it.DisplayableName,
                                    GroupPickState.resolveStatus(it, viewModel.pickedItems.value)
                                )
                            }.sortedBy { it.status.position },
                            readonly = groupValue.value.fixed,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = nameValue,
                            label = {
                                Text(
                                    text = stringResource(id = R.string.invoice_item_form_dialog_name_placeholder),
                                    style = TextStyle(fontFamily = appFont)
                                )
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.invoice_item_form_dialog_name_placeholder),
                                    style = TextStyle(fontFamily = appFont)
                                )
                            },
                            onValueChange = { nameValue = it },
                            modifier = Modifier
                                .defaults()
                                .fillMaxWidth(),
                            textStyle = TextStyle(fontFamily = appFont),
                            readOnly = product.value.fixed,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        )

                        Row(verticalAlignment = Alignment.Bottom) {
                            NumberEntry(
                                number = quantityValue,
                                label = stringResource(id = R.string.invoice_item_form_dialog_quantity_placeholder),
                                onValueChanged = { quantityValue = it },
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(0.5f, true)
                            )
                            UnitsDropdownMenuBox(
                                selected = "",
                                label = "واحد",
                                units = viewModel.units().map { it.Unit.ensured(viewModel.unitRepo) },
                                onValueChanged = { quantityUnit = it },
                                modifier = Modifier
                                    .defaults()
                                    .padding(top = 6.dp)
                                    .weight(0.5f, true),
                            )
                        }
                        if (quantityUnit.hasValue && quantityUnit!!.variation == null) {
                            Row {
                                NumberEntry(
                                    number = packageMeasurementValue,
                                    label = stringResource(id = R.string.invoice_item_form_dialog_reference_price_measurable_value, quantityUnit?.displayableName ?: ""),
                                    onValueChanged = { packageMeasurementValue = it },
                                    modifier = Modifier.weight(0.6f)
                                )
                                UnitVariationDropdownBox(
                                    selected = packageMeasurementUnit,
                                    onValueChanged = { packageMeasurementUnit = it },
                                    modifier = Modifier
                                        .defaults()
                                        .padding(top = 6.dp)
                                        .weight(0.4f)
                                )
                            }
                        }

                        val unitVariation = quantityUnit?.variation ?: packageMeasurementUnit
                        val unitAmount = if (quantityUnit?.variation.hasValue) quantityValue else packageMeasurementValue
                        val priceLabel: String = if (quantityUnit?.displayableName.isUsable()) stringResource(
                            id = R.string.invoice_item_form_dialog_price_amount_by_unit_label,
                            quantityUnit!!.displayableName!!
                        ) else stringResource(id = R.string.invoice_item_form_dialog_price_amount_no_unit_label)

                        Row(verticalAlignment = Alignment.Bottom) {
                            NumberEntry(
                                number = priceAmount,
                                label = priceLabel,
                                onValueChanged = { priceAmount = it },
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(0.6f, true)
                            )
                            CurrenciesDropdownMenuBox(
                                selected = priceCurrency,
                                context = LocalContext.current,
                                onValueChanged = { priceCurrency = it },
                                modifier = Modifier
                                    .defaults()
                                    .padding(top = 0.dp)
                                    .weight(0.4f, true),
                            )
                        }

                        if (unitVariation.hasValue && unitAmount.hasValue && priceAmount.hasValue) {
                            val mainUnitName = stringResource(id = unitVariation!!.mainUnitNameResId)
                            val message: String
                            if (unitAmount!! <= BigDecimal.ZERO) {
                                message = if (quantityUnit?.variation.hasValue) {
                                    stringResource(id = R.string.invoice_item_form_dialog_reference_quantity_unit_unacceptable)
                                } else {
                                    stringResource(id = R.string.invoice_item_form_dialog_reference_package_unit_unacceptable)
                                }
                            } else {
                                val equivalentText = referencePrice(priceAmount!!, unitAmount, unitVariation.coefficient)
                                    .displayable(false)
                                val currency = priceCurrency?.resourceId?.let { stringResource(id = it) } ?: ""
                                message = stringResource(id = R.string.invoice_item_form_dialog_reference_price_template, mainUnitName, equivalentText, currency)
                            }
                            Text(
                                modifier = Modifier
                                    .defaults()
                                    .fillMaxWidth(),
                                text = message,
                                style = TextStyle(fontFamily = appFont),
                                textAlign = TextAlign.Center
                            )
                        }

                        if (cameraPermissionState.status.isGranted) {
                            CameraCapture(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .defaults(),
                                onImageFile = { viewModel.photoTaken(it) }
                            )
                        } else {
                            if (!productPhotography) {
                                Image(
                                    painter = painterResource(id = R.drawable.camera_placeholder),
                                    contentDescription = "camera",
                                    modifier = Modifier
                                        .clickable { productPhotography = true }
                                        .height(192.dp)
                                        .fillMaxWidth()
                                        .defaults()
                                )
                            } else {
                                if (cameraPermissionState.status.shouldShowRationale) {
                                    Text(text = "Camera permission is required for taking photo from product")
                                    Button(onClick = { }) {
                                        Text(text = "request for permission")
                                    }
                                } else {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                        }

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            items(photos.value) { photo ->
                                InvoiceItemProduct(photo, onRemove = { viewModel.photoRemoved(it) })
                            }
                        }

                        if (question.value.hasValue) {
                            androidx.compose.material3.BasicAlertDialog(
                                onDismissRequest = {
                                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                                    // button. If you want to disable that functionality, simply use an empty
                                    // onDismissRequest.
                                    //question = null
                                }
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .wrapContentHeight(),
                                    shape = MaterialTheme.shapes.large
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {

                                        //... AlertDialog content
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun dialogFullScreen(): Boolean {
        return true
    }

    fun maximize() {
        val window = window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        window.attributes = wlp
        getWindow()!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}

fun Modifier.defaults(): Modifier {
    return this.padding(5.dp)
}

val appFont = FontFamily(
    Font(R.font.vazir)
)

enum class GroupPickState(
    val iconColor: Color,
    val position: Int,
    val icon: ImageVector,
    val style: TextStyle
) {
    NeededButNotPicked(
        Color.Black, 0, Icons.Outlined.CheckCircle,
        TextStyle(
            fontFamily = appFont,
            color = Color.Black,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        )
    ),
    NeededAndPicked(
        Color.Green, 1, Icons.Filled.CheckCircle,
        TextStyle(
            fontFamily = appFont,
            color = Color.hsl(196F, 0.67F, 0.45F),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal
        )
    ),
    PickedWithoutNeeded(
        Color.Blue, 2, Icons.Filled.Warning,
        TextStyle(
            fontFamily = appFont,
            color = Color.hsl(26F, 0.73F, 0.57F),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal
        )
    ),
    NotNeededNotPicked(
        Color.Gray, 3, Icons.Outlined.Add,
        TextStyle(
            fontFamily = appFont,
            color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal
        )
    ),
    ;

    companion object {
        fun resolveStatus(
            group: GroupEntity,
            pickedItems: List<InvoiceItemEntity>
        ): GroupPickState {
            return when {
                group.Needed && group.Picked -> NeededAndPicked
                group.Needed && !group.Picked -> NeededButNotPicked
                !group.Needed && group.Picked -> PickedWithoutNeeded
                !group.Needed && !group.Picked -> NotNeededNotPicked
                else -> throw RuntimeException()
            }
        }
    }
}

data class GroupInvoiceItemForm(val id: Int, val text: String, val status: GroupPickState)

