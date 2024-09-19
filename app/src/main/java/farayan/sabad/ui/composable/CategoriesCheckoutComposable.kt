package farayan.sabad.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farayan.sabad.R
import farayan.sabad.commons.ConditionalErrorMessage
import farayan.sabad.utility.appFont
import farayan.sabad.utility.isUsable

@Composable
fun CategoriesCheckoutComposable(
    onConfirmed: (String) -> Unit,
    onCancelled: () -> Unit
) {
    var shop by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCancelled,
        title = {
            Text(text = stringResource(id = R.string.categories_checkout_title), fontFamily = appFont)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = shop,
                    onValueChange = { shop = it; },
                    label = { Text(text = stringResource(id = R.string.categories_checkout_shop_label), fontFamily = appFont) },
                    textStyle = TextStyle(fontFamily = appFont),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onConfirmed(shop)
                    })
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = {
                        onConfirmed(shop)
                    }
                ) {
                    Text(stringResource(id = R.string.categories_checkout_checkout_command), fontFamily = appFont)
                }
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = onCancelled
                ) {
                    Text(stringResource(id = R.string.categories_checkout_cancel_command), fontFamily = appFont)
                }
            }
        }
    )
}