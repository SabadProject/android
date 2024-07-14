package farayan.sabad.ui

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import farayan.sabad.core.OnePlace.Unit.UnitEntity
import farayan.sabad.core.commons.Currency
import farayan.sabad.ui.Core.SabadBaseDialog
import farayan.sabad.ui.components.CameraCapture
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
) : SabadBaseDialog(context, cancelable, cancelListener) {
    init {
        if (dialogFullScreen()) {
            val window = window
            val wlp = window!!.attributes
            wlp.gravity = Gravity.CENTER
            window.attributes = wlp
            getWindow()!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(ComposeView(context).apply {
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
                val nameValue by remember { mutableStateOf("") }
                var quantityValue: BigDecimal? by remember { mutableStateOf(BigDecimal.ONE) }
                var priceAmount: BigDecimal? by remember { mutableStateOf<BigDecimal?>(null) }
                val priceCurrency: Currency? by remember { mutableStateOf<Currency?>(null) }
                val photos = viewModel.productPhotos.collectAsState()

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
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
                            modifier = Modifier.padding(5.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
                            readonly = groupValue.value.fixed
                        )
                        OutlinedTextField(
                            value = nameValue,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.invoice_item_form_dialog_name_placeholder),
                                    style = TextStyle(fontFamily = appFont)
                                )
                            },
                            onValueChange = {},
                            modifier = Modifier.defaults(),
                            textStyle = TextStyle(fontFamily = appFont),
                            readOnly = product.value.fixed,
                        )

                        Row(verticalAlignment = Alignment.Bottom) {
                            OutlinedTextField(
                                value = quantityValue?.toPlainString() ?: "",
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.invoice_item_form_dialog_quantity_placeholder),
                                        style = TextStyle(fontFamily = appFont)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = stringResource(id = R.string.invoice_item_form_dialog_quantity_placeholder),
                                        style = TextStyle(fontFamily = appFont)
                                    )
                                },
                                onValueChange = { quantityValue = parseBigDecimal(it) },
                                modifier = Modifier
                                    .defaults()
                                    .align(Alignment.CenterVertically)
                                    .padding(top = 6.dp)
                                    .weight(0.6f, true),
                                textStyle = TextStyle(
                                    fontFamily = appFont,
                                    textAlign = TextAlign.Center
                                ),
                            )
                            UnitsDropdownMenuBox(
                                selected = "",
                                label = "واحد",
                                modifier = Modifier
                                    .defaults()
                                    .padding(top = 0.dp)
                                    .weight(0.4f, true),
                                units = viewModel.units()
                                    .map { it.Unit.ensured(viewModel.unitRepo) }
                            )
                        }

                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(photos.value) { photo ->
                                InvoiceItemProduct(photo, onRemove = { viewModel.photoRemoved(it) })
                            }
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
                                        .fillMaxWidth()
                                        .defaults()
                                )
                            } else {
                                if (cameraPermissionState.status.shouldShowRationale) {
                                    Text(text = "Camera permission is required for taking photo from product")
                                    Button(onClick = { /*TODO*/ }) {
                                        Text(text = "request for permission")
                                    }
                                } else {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.Bottom) {
                            NumberEntry(
                                priceAmount, priceCurrency ?: Currency.Rial,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(0.6f, true)
                            ) {

                            }
                            CurrenciesDropdownMenuBox(
                                selected = priceCurrency ?: Currency.Rial,
                                context = LocalContext.current,
                                modifier = Modifier
                                    .defaults()
                                    .padding(top = 0.dp)
                                    .weight(0.4f, true),
                            )
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupsDropdownMenuBox(
    selected: String,
    label: String,
    groups: List<GroupInvoiceItemForm>,
    modifier: Modifier = Modifier,
    readonly: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue(selected)) }
    val editable = !readonly

    ExposedDropdownMenuBox(
        expanded = editable && expanded,
        modifier = modifier.padding(5.dp),
        onExpandedChange = { expanded = editable && it },
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; expanded = true },
            readOnly = readonly,
            singleLine = true,
            label = { Text(label, fontFamily = appFont) },
            textStyle = TextStyle(fontFamily = appFont),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = editable && expanded) },
            modifier = Modifier.onFocusEvent { expanded = editable && it.hasFocus },
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = if (readonly) Color.LightGray else Color.Transparent)
        )
        ExposedDropdownMenu(
            expanded = editable && expanded,
            onDismissRequest = { expanded = false },
        ) {
            groups.filter { text.text.isEmpty() || it.text.contains(text.text) }.forEach { group ->
                DropdownMenuItem(
                    onClick = {
                        text = TextFieldValue(group.text, selection = TextRange(group.text.length))
                        expanded = false
                    }
                ) {
                    val displayable = buildAnnotatedString {
                        appendInlineContent("prefix", "[prefix]")
                        append(" ")
                        append(group.text)
                    }
                    val ic = mapOf(
                        Pair(
                            "prefix",
                            InlineTextContent(
                                Placeholder(
                                    width = 16.sp,
                                    height = 16.sp,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            )
                            {
                                Icon(group.status.icon, "", tint = group.status.iconColor)
                            }
                        )
                    )
                    Text(
                        displayable,
                        inlineContent = ic,
                        style = group.status.style
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UnitsDropdownMenuBox(
    selected: String,
    label: String,
    units: List<UnitEntity>,
    modifier: Modifier = Modifier,
    readonly: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue(selected)) }
    val editable = !readonly

    ExposedDropdownMenuBox(
        expanded = editable && expanded,
        modifier = modifier,
        onExpandedChange = { expanded = editable && it },
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; expanded = true },
            readOnly = readonly,
            singleLine = true,
            label = {
                Text(
                    label, fontFamily = appFont, modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            },
            textStyle = TextStyle(fontFamily = appFont),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = editable && expanded) },
            modifier = Modifier.onFocusEvent { expanded = editable && it.hasFocus },
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = if (readonly) Color.LightGray else Color.Transparent)
        )
        ExposedDropdownMenu(
            expanded = editable && expanded,
            onDismissRequest = { expanded = false },
        ) {
            units.filter { text.text.isEmpty() || it.QueryableName.contains(text.text) }
                .forEach { groupUnit ->
                    DropdownMenuItem(
                        onClick = {
                            text = TextFieldValue(
                                groupUnit.DisplayableName,
                                selection = TextRange(groupUnit.DisplayableName.length)
                            )
                            expanded = false
                        }
                    ) {
                        Text(
                            groupUnit.DisplayableName,
                            style = TextStyle(fontFamily = appFont)
                        )
                    }
                }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrenciesDropdownMenuBox(
    selected: Currency,
    context: Context,
    modifier: Modifier = Modifier,
    readonly: Boolean = false,
) {

    fun Currency.textual(): String {
        return context.getString(this.resourceId)
    }

    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue(selected.textual())) }
    val editable = !readonly

    ExposedDropdownMenuBox(
        expanded = editable && expanded,
        modifier = modifier,
        onExpandedChange = { expanded = editable && it },
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; expanded = true },
            readOnly = readonly,
            singleLine = true,
            label = {
                Text(
                    stringResource(id = R.string.invoice_item_form_dialog_price_currency_label),
                    fontFamily = appFont,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            },
            textStyle = TextStyle(fontFamily = appFont),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = editable && expanded) },
            modifier = Modifier.onFocusEvent { expanded = editable && it.hasFocus },
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = if (readonly) Color.LightGray else Color.Transparent)
        )
        ExposedDropdownMenu(
            expanded = editable && expanded,
            onDismissRequest = { expanded = false },
        ) {
            Currency.values().filter { text.text.isEmpty() || it.textual().contains(text.text) }
                .forEach { currency ->
                    DropdownMenuItem(
                        onClick = {
                            text = TextFieldValue(
                                currency.textual(),
                                selection = TextRange(currency.textual().length)
                            )
                            expanded = false
                        }
                    ) {
                        Text(
                            currency.textual(),
                            style = TextStyle(fontFamily = appFont)
                        )
                    }
                }
        }
    }
}

@Composable
fun NumberEntry(
    number: BigDecimal?,
    currency: Currency,
    modifier: Modifier = Modifier,
    onValueChanged: (BigDecimal?) -> Unit = { }
) {
    var value: BigDecimal? by remember {
        mutableStateOf<BigDecimal?>(number)
    }
    var suffixedWithDecimalPoint by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value?.displayable(currency, suffixedWithDecimalPoint)
            ?: TextFieldValue(),
        label = {
            Text(
                text = stringResource(id = R.string.invoice_item_form_dialog_price_amount_label),
                style = TextStyle(fontFamily = appFont)
            )
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.invoice_item_form_dialog_price_amount_placeholder),
                style = TextStyle(fontFamily = appFont)
            )
        },
        onValueChange = { rawPrice ->
            val rawText = rawPrice.text
            value = parseBigDecimal(rawText)
            suffixedWithDecimalPoint = rawText.indexOfFirst { it == '.' } == rawText.length - 1
            Log.i(
                "number",
                "$rawText is ${if (suffixedWithDecimalPoint) "" else "NOT"} suffixedWithDecimalPoint "
            )
        },
        modifier = modifier
            .defaults()
            .padding(top = 6.dp),
        textStyle = TextStyle(
            fontFamily = appFont,
            textAlign = TextAlign.Center,
            textDirection = TextDirection.Ltr
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}


private fun parseBigDecimal(value: String): BigDecimal? {
    Log.i("number", "value: $value")
    return try {
        val sb = StringBuilder()
        var decimalPointAdded = false
        for (c in value) {
            if (c in '0'..'9') {
                sb.append(c)
                continue
            }
            if (c == '.' && !decimalPointAdded) {
                sb.append('.')
                decimalPointAdded = true
                continue
            }
        }
        val bigDecimal = BigDecimal(sb.toString())
        Log.i("number", "parsed value of $value is $bigDecimal")
        bigDecimal
    } catch (e: Exception) {
        Log.i("number", "unable to parse value of $value")
        null
    }
}


fun decimalPointPosition(value: String): Int {
    return value.indexOfFirst { c -> c == '.' }
}

fun BigDecimal.displayable(
    currency: Currency,
    suffixedWithDecimalPoint: Boolean,
): TextFieldValue {
    val toString = this.toString()
    Log.i("number", "toString: $toString")
    val decimalPointPos = decimalPointPosition(toString)
    Log.i("number", "decimalPointPos: $decimalPointPos")
    val precision = if (decimalPointPos >= 0) toString.length - decimalPointPos - 1 else 0
    Log.i("number", "precision: $precision")
    val format = "%,.${precision}f"
    Log.i("number", "format: $format")
    val text = String.format(format, this).let { if (suffixedWithDecimalPoint) "$it." else it }
    Log.i("number", "text: $text")
    return TextFieldValue(text, selection = TextRange(text.length))
}

private fun String.countFromEnd(c: Char): Int {
    var count = 0
    for (index in length - 1 downTo 0) {
        if (this[index] == c)
            count++
        else
            return count
    }
    return count
}