package farayan.sabad.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import farayan.sabad.R
import farayan.sabad.utility.appFont
import farayan.sabad.utility.isUsable

@Composable
fun CategoryQuickQueryComposable(
    addable: Boolean,
    pickable: Boolean,
    needable: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,
    onAdd: () -> Unit,
    onNeed: () -> Unit,
    onPick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            textStyle = TextStyle(fontFamily = appFont),
            label = { Text(text = stringResource(id = R.string.category_quick_query_label), fontFamily = appFont) },
            modifier = Modifier.weight(1.0f),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = calcImeAction(value, addable, needable, pickable)),
            keyboardActions = KeyboardActions(onDone = { onAdd() }, onSend = { onNeed() }, onGo = { onPick() }),
            trailingIcon = {
                if (value.isUsable) {
                    if (addable) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add", modifier = Modifier.clickable { onAdd() })
                    }
                    if (needable) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "search", modifier = Modifier.clickable { onNeed() })
                    }
                    if (pickable) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "pick", modifier = Modifier.clickable { onPick() })
                    }
                }
            }
        )
    }
}

fun calcImeAction(value: String, addable: Boolean, needable: Boolean, pickable: Boolean): ImeAction {
    return if (value.isUsable) {
        when {
            addable -> ImeAction.Done
            needable -> ImeAction.Send
            pickable -> ImeAction.Go
            else -> ImeAction.Unspecified
        }
    } else {
        ImeAction.Search
    }
}
