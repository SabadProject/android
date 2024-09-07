package farayan.sabad.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import farayan.sabad.core.commons.displayable
import farayan.sabad.core.commons.parseBigDecimal
import farayan.sabad.utility.appFont
import farayan.sabad.utility.defaults
import java.math.BigDecimal


@Composable
fun NumberEntry(
    number: BigDecimal?,
    label: String,
    onValueChanged: (BigDecimal?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var value: BigDecimal? = number
    var suffixedWithDecimalPoint by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value.displayable(suffixedWithDecimalPoint).let { TextFieldValue(it, TextRange(it.length)) },
        label = {
            if (label.isNotBlank()) {
                Text(
                    text = label,
                    style = TextStyle(fontFamily = appFont)
                )
            }
        },
        onValueChange = { rawPrice ->
            val rawText = rawPrice.text
            value = parseBigDecimal(rawText)
            suffixedWithDecimalPoint = rawText.isNotBlank() && rawText.indexOfFirst { it == '.' } == rawText.length - 1
            onValueChanged(value)
        },
        modifier = modifier
            .defaults()
            .padding(top = 6.dp),
        textStyle = TextStyle(
            fontFamily = appFont,
            textAlign = TextAlign.Center,
            textDirection = TextDirection.Ltr
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
    )
}
