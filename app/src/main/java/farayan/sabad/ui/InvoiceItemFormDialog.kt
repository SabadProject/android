package farayan.sabad.ui

import android.Manifest
import android.content.DialogInterface
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
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
import farayan.sabad.R
import farayan.sabad.SabadConfigs
import farayan.sabad.SabadConstants
import farayan.sabad.core.OnePlace.Group.GroupEntity
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity
import farayan.sabad.ui.Core.SabadBaseDialog
import farayan.sabad.utility.hasValue
import farayan.sabad.vms.InvoiceItemFormViewModel

@OptIn(ExperimentalPermissionsApi::class)
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
                var groupValue = viewModel.group.collectAsState()
                val groupFixed = viewModel.groupFixed.collectAsState()

                val productFixed = viewModel.productFixed.collectAsState()

                var barcodeValue = viewModel.productBarcode.collectAsState()
                var barcodeScannerState by remember { mutableStateOf(false) }
                val cameraPermissionState = rememberPermissionState(
                    Manifest.permission.CAMERA
                ) {
                    Log.i("Permission", "resulted")
                }
                var nameValue by remember { mutableStateOf("") }
                var photoValue by remember { mutableStateOf("") }

                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Column(modifier = Modifier.fillMaxSize()) {
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
                                        barcodeScannerState = true
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
                        if (barcodeScannerState) {
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
                            groupValue.value?.DisplayableName ?: "",
                            stringResource(id = R.string.invoice_item_form_dialog_group_label),
                            groups.value!!.map {
                                GroupInvoiceItemForm(
                                    it.id,
                                    it.DisplayableName,
                                    GroupPickState.resolveStatus(it, viewModel.pickedItems.value)
                                )
                            }.sortedBy { it.status.position },
                            readonly = groupFixed.value
                        )
                        OutlinedTextField(
                            value = nameValue,
                            onValueChange = {},
                            modifier = Modifier.defaults(),
                            textStyle = TextStyle(fontFamily = appFont),
                            readOnly = productFixed.value,
                        )
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