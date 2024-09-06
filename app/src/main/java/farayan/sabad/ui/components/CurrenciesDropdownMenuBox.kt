package farayan.sabad.ui.components

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import farayan.sabad.R
import farayan.sabad.core.commons.Currency
import farayan.sabad.utility.appFont

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrenciesDropdownMenuBox(
    selected: Currency?,
    context: Context,
    onValueChanged: (Currency?) -> Unit,
    modifier: Modifier = Modifier,
    readonly: Boolean = false,
) {

    fun Currency.textual(): String {
        return context.getString(this.resourceId)
    }

    var expanded by remember { mutableStateOf(false) }
    var text = TextFieldValue(selected?.textual() ?: "")
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
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = if (readonly) Color.LightGray else Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        )
        ExposedDropdownMenu(
            expanded = editable && expanded,
            onDismissRequest = { expanded = false },
        ) {
            Currency.values().forEach { currency ->
                DropdownMenuItem(
                    onClick = {
                        text = TextFieldValue(
                            currency.textual(),
                            selection = TextRange(currency.textual().length)
                        )
                        onValueChanged(currency)
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