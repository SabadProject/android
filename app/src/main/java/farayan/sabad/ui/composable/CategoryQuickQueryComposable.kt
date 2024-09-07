package farayan.sabad.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import farayan.sabad.R
import farayan.sabad.utility.appFont

@Composable
fun CategoryQuickQueryComposable(
    addable: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,
    onAddClicked: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            textStyle = TextStyle(fontFamily = appFont),
            label = { Text(text = stringResource(id = R.string.category_quick_query_label), fontFamily = appFont) },
            modifier = Modifier.weight(1.0f),
            trailingIcon = {
                if (addable) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "add", modifier = Modifier.clickable { onAddClicked() })
                } else {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }
            }
        )
    }
}