package farayan.sabad.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farayan.sabad.R
import farayan.sabad.core.commons.localize
import farayan.sabad.db.Category
import farayan.sabad.utility.appFont

@Composable
fun CategoriesCategoriesDeleteComposable(
    categories: List<Category>,
    onConfirmed: () -> Unit,
    onCancelled: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelled,
        title = {
            Text(text = stringResource(id = R.string.category_delete_dialog_title), fontFamily = appFont)
        },
        text = {
            Column {
                Text(text = stringResource(id = R.string.category_delete_dialog_message), fontFamily = appFont, fontSize = 10.sp)
                for (category in categories.take(3)) {
                    Text(text = "- ${category.displayableName}", fontFamily = appFont, fontSize = 10.sp)
                }
                if (categories.size > 3) {
                    Text(text = stringResource(id = R.string.category_delete_and_more_template, (categories.size - 3)).localize(), fontFamily = appFont, fontSize = 10.sp)
                }
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
                        onConfirmed()
                    }
                ) {
                    Text(stringResource(id = R.string.category_delete_delete_command), fontFamily = appFont)
                }
                TextButton(
                    modifier = Modifier.weight(1.0f),
                    onClick = onCancelled
                ) {
                    Text(stringResource(id = R.string.category_delete_cancel_command), fontFamily = appFont)
                }
            }
        }
    )
}