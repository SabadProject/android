package farayan.sabad.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.lightColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import farayan.commons.FarayanUtility
import farayan.sabad.R
import farayan.sabad.SabadConstants
import farayan.sabad.commons.anyValue
import farayan.sabad.commons.hasFixedValue
import farayan.sabad.core.commons.UnitVariations
import farayan.sabad.core.commons.displayable
import farayan.sabad.ui.components.CameraCapture
import farayan.sabad.ui.components.CurrenciesDropdownMenuBox
import farayan.sabad.ui.components.GroupInvoiceItemForm
import farayan.sabad.ui.components.GroupPickState
import farayan.sabad.ui.components.GroupsDropdownMenuBox
import farayan.sabad.ui.components.NumberEntry
import farayan.sabad.ui.components.UnitVariationDropdownBox
import farayan.sabad.ui.components.UnitsDropdownMenuBox
import farayan.sabad.ui.composable.InvoiceItemProduct
import farayan.sabad.utility.appFont
import farayan.sabad.utility.defaults
import farayan.sabad.utility.errorBorder
import farayan.sabad.core.commons.hasValue
import farayan.sabad.utility.isUsable
import farayan.sabad.utility.referencePrice
import farayan.sabad.vm.InvoiceItemFormViewModel
import java.math.BigDecimal
import farayan.sabad.db.Unit as PersistenceUnit

@OptIn(
    ExperimentalPermissionsApi::class
)
class InvoiceItemFormDialog(
    context: Activity,
    cancelable: Boolean,
    cancelListener: DialogInterface.OnCancelListener?,
    private val viewModel: InvoiceItemFormViewModel
) : Dialog(context, cancelable, cancelListener) {

    private val beepManager = BeepManager(context)

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window!!.setDecorFitsSystemWindows(false)
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
            setViewTreeLifecycleOwner(context as LifecycleOwner)
            setViewTreeSavedStateRegistryOwner(context as SavedStateRegistryOwner)
            setViewTreeViewModelStoreOwner(context as ViewModelStoreOwner)
            setContent {
                MaterialTheme(colors = lightColors()) {
                    val groups = viewModel.categories.collectAsState()
                    val groupValue = viewModel.category.collectAsState()
                    val product = viewModel.product.collectAsState()
                    val question by viewModel.question.collectAsState()
                    val barcodeValue = viewModel.formScannedExtractedBarcode.collectAsState()
                    val nameValue = viewModel.formNameReadonly.collectAsState()
                    val quantityValue = viewModel.formQuantityValue.collectAsState()
                    val quantityUnit: State<PersistenceUnit?> = viewModel.formQuantityUnit.collectAsState()
                    val packageMeasurementUnit = viewModel.formPackageUnit.collectAsState()
                    val packageMeasurementValue = viewModel.formPackageWorth.collectAsState()
                    val priceAmount = viewModel.formPriceAmount.collectAsState()
                    val priceCurrency by viewModel.formPriceCurrencyReadOnly.collectAsState()
                    val photos = viewModel.formPhotos.collectAsState()
                    val items = viewModel.pickedItems.collectAsState(listOf())
                    val persistErrorMessage by viewModel.errorMessage.collectAsState()

                    var cameraUsage by remember { mutableStateOf(CameraUsage.None) }
                    val cameraPermissionState = rememberPermissionState(
                        Manifest.permission.CAMERA
                    ) {
                        Log.i("Permission", "resulted")
                    }

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
                            if (persistErrorMessage.isUsable) {
                                Text(
                                    text = persistErrorMessage!!,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier
                                        .padding(4.dp, 4.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            OutlinedTextField(
                                value = barcodeValue.value?.text ?: "",
                                onValueChange = { },
                                enabled = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .errorBorder(persistErrorMessage.isUsable)
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
                                            cameraUsage = CameraUsage.Barcode
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
                                },
                            )
                            if (cameraUsage == CameraUsage.Barcode) {
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
                                                    Log.i("barcode", "Paused")
                                                    pause()
                                                    cameraUsage = CameraUsage.None
                                                    FarayanUtility.ReleaseScreenOn(window)
                                                    beepManager.playBeepSound()
                                                    viewModel.barcodeScanned(it, ctx)
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
                                groupValue.value.value?.displayableName ?: "",
                                stringResource(id = R.string.invoice_item_form_dialog_group_label),
                                groups.value.map {
                                    GroupInvoiceItemForm(
                                        it.id,
                                        it.displayableName,
                                        GroupPickState.resolveStatus(it, items.value)
                                    )
                                }.sortedBy { it.status.position },
                                readonly = groupValue.value.fixed,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = nameValue.value,
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
                                onValueChange = { viewModel.formNameChanged(it) },
                                modifier = Modifier
                                    .defaults()
                                    .errorBorder(persistErrorMessage.isUsable)
                                    .fillMaxWidth(),
                                textStyle = TextStyle(fontFamily = appFont),
                                //readOnly = product.value.fixed,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                                singleLine = true
                            )

                            Row(verticalAlignment = Alignment.Bottom) {
                                NumberEntry(
                                    number = quantityValue.value,
                                    label = stringResource(id = R.string.invoice_item_form_dialog_quantity_placeholder),
                                    onValueChanged = { viewModel.formQuantityValue.value = it },
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .weight(0.5f, true)
                                )
                                UnitsDropdownMenuBox(
                                    selected = quantityUnit.value,
                                    label = "واحد",
                                    units = viewModel.units(),
                                    onValueChanged = { viewModel.formQuantityUnit.value = it },
                                    modifier = Modifier
                                        .defaults()
                                        .padding(top = 6.dp)
                                        .weight(0.5f, true),
                                )
                            }
                            if (quantityUnit.value.hasValue && quantityUnit.value?.variation == null) {
                                Row {
                                    NumberEntry(
                                        number = packageMeasurementValue.value,
                                        label = stringResource(id = R.string.invoice_item_form_dialog_reference_price_measurable_value, quantityUnit.value?.displayableName ?: ""),
                                        onValueChanged = { viewModel.formPackageWorth.value = it },
                                        modifier = Modifier.weight(0.6f)
                                    )
                                    UnitVariationDropdownBox(
                                        selected = packageMeasurementUnit.value,
                                        onValueChanged = { viewModel.formPackageUnit.value = it },
                                        modifier = Modifier
                                            .defaults()
                                            .padding(top = 6.dp)
                                            .weight(0.4f)
                                    )
                                }
                            }

                            val unitVariation = quantityUnit.value?.variation?.let { UnitVariations.valueOf(it) } ?: packageMeasurementUnit.value
                            val unitAmount = if (quantityUnit.value?.variation.hasValue) quantityValue else packageMeasurementValue
                            val priceLabel: String = if (quantityUnit.value?.displayableName.isUsable) stringResource(
                                id = R.string.invoice_item_form_dialog_price_amount_by_unit_label,
                                quantityUnit.value!!.displayableName
                            ) else stringResource(id = R.string.invoice_item_form_dialog_price_amount_no_unit_label)

                            Row(verticalAlignment = Alignment.Bottom) {
                                NumberEntry(
                                    number = priceAmount.value,
                                    label = priceLabel,
                                    onValueChanged = { viewModel.formPriceAmount.value = it },
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .errorBorder(persistErrorMessage.isUsable)
                                        .weight(0.6f, true)
                                )
                                CurrenciesDropdownMenuBox(
                                    selected = priceCurrency.anyValue,
                                    readonly = priceCurrency.hasFixedValue,
                                    context = LocalContext.current,
                                    onValueChanged = { viewModel.changeCurrency(it) },
                                    modifier = Modifier
                                        .defaults()
                                        .padding(top = 0.dp)
                                        .weight(0.4f, true),
                                )
                            }

                            if (unitVariation.hasValue && unitAmount.hasValue && priceAmount.value.hasValue) {
                                val mainUnitName = stringResource(id = unitVariation!!.mainUnitNameResId)
                                val message: String
                                if (unitAmount.value!! <= BigDecimal.ZERO) {
                                    message = if (quantityUnit.value?.variation.hasValue) {
                                        stringResource(id = R.string.invoice_item_form_dialog_reference_quantity_unit_unacceptable)
                                    } else {
                                        stringResource(id = R.string.invoice_item_form_dialog_reference_package_unit_unacceptable)
                                    }
                                } else {
                                    val equivalentText = referencePrice(priceAmount.value!!, unitAmount.value!!, unitVariation.coefficient)
                                        .displayable(false)
                                    val currency = priceCurrency.anyValue?.resourceId?.let { stringResource(id = it) } ?: ""
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

                            if (cameraPermissionState.status.isGranted && cameraUsage == CameraUsage.Photo) {
                                CameraCapture(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp)
                                        .defaults(),
                                    onImageFile = { viewModel.photoTaken(it) }
                                )
                            } else {
                                if (cameraUsage != CameraUsage.Photo) {
                                    Image(
                                        painter = painterResource(id = R.drawable.camera_placeholder),
                                        contentDescription = "camera",
                                        modifier = Modifier
                                            .clickable { cameraUsage = CameraUsage.Photo }
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
                                    InvoiceItemProduct(photo.path, onRemove = { viewModel.photoRemoved(it) })
                                }
                            }

                            if (question.hasValue) {
                                val theQuestion = question!!
                                AlertDialog(
                                    onDismissRequest = { viewModel.clearQuestion() },
                                    title = {
                                        Text(text = theQuestion.title, fontFamily = appFont)
                                    },
                                    text = {
                                        Text(text = theQuestion.message, fontFamily = appFont, fontSize = 11.sp)
                                    },
                                    buttons = {
                                        Row(
                                            modifier = Modifier.padding(all = 8.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            for (button in theQuestion.buttons) {
                                                TextButton(
                                                    modifier = Modifier.weight(1.0f),
                                                    onClick = { button.onClicked() }
                                                ) {
                                                    Text(button.label, fontFamily = appFont)
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                            Button(
                                onClick = {
                                    if (viewModel.persistInvoiceItem(context))
                                        this@InvoiceItemFormDialog.dismiss()
                                },
                                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.invoice_item_form_dialog_submit),
                                    style = TextStyle.Default.copy(fontFamily = appFont, color = Color.White),
                                )
                            }
                        }
                    }
                }
            }
        })
    }
}

enum class CameraUsage {
    None,
    Barcode,
    Photo
}
