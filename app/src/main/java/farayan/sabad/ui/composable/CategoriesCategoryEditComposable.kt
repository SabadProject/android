package farayan.sabad.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farayan.sabad.R
import farayan.sabad.commons.ConditionalErrorMessage
import farayan.sabad.db.Category
import farayan.sabad.utility.appFont
import farayan.sabad.utility.isUsable

@Composable
fun CategoriesCategoryEditComposable(
    category: Category,
    upstreamError: ConditionalErrorMessage?,
    onConfirmed: (String) -> Unit,
    onCancelled: () -> Unit
) {
    var name by remember { mutableStateOf(category.displayableName) }
    var emptyNameError by remember { mutableStateOf("") }
    val error = upstreamError?.errorMessage(name, LocalContext.current) ?: emptyNameError
    val emptyNameErrorValue = stringResource(id = R.string.category_edit_empty_name_error)
    val colors = LocalContentColor.current
    AlertDialog(
        onDismissRequest = onCancelled,
        title = {
            Text(text = stringResource(id = R.string.category_edit_dialog_title), fontFamily = appFont)
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; emptyNameError = if (name.isEmpty()) emptyNameErrorValue else "" },
                    label = { Text(text = stringResource(id = R.string.category_edit_name_entry_label), fontFamily = appFont) },
                    trailingIcon = { if (error.isUsable) Icon(imageVector = Icons.Filled.Info, contentDescription = "", tint = MaterialTheme.colors.error) },
                    textStyle = TextStyle(fontFamily = appFont),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (error.isEmpty()) {
                            onConfirmed(name)
                        }
                    })
                )
                Text(text = error, color = MaterialTheme.colors.error, fontFamily = appFont, fontSize = 10.sp)
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    enabled = error.isEmpty(),
                    onClick = {
                        if (error.isEmpty()) {
                            onConfirmed(name)
                        }
                    }
                ) {
                    Text(stringResource(id = R.string.category_edit_submit_command), fontFamily = appFont)
                }
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = onCancelled
                ) {
                    Text(stringResource(id = R.string.category_edit_cancel_command), fontFamily = appFont)
                }
            }
        }
    )
}