package farayan.sabad.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import farayan.sabad.R
import farayan.sabad.ui.ItemRich

@Composable
fun CategoriesCategoryRemoteItemComposable(removingItem: ItemRich, onConfirmed: () -> Unit, onCancelled: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelled,
        title = {
            Text(text = stringResource(id = R.string.home_item_remove_dialog_title))
        },
        text = {
            Text(text = stringResource(id = R.string.home_item_remove_dialog_text, removingItem.product.displayableName))
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = onConfirmed
                ) {
                    Text(stringResource(id = R.string.home_item_remove_dialog_action_remove_text))
                }
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = onCancelled
                ) {
                    Text(stringResource(id = R.string.home_item_remove_dialog_action_skip_text))
                }
            }
        }
    )
}