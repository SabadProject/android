package farayan.sabad.composables

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farayan.sabad.R
import farayan.sabad.db.Category
import farayan.sabad.ui.InvoiceItemFormDialog
import farayan.sabad.ui.appFont
import farayan.sabad.utility.invoke
import farayan.sabad.vms.InvoiceItemFormViewModel
import farayan.sabad.vms.InvoiceItemFormViewModel.Companion.Factory

@Composable
fun CategoriesCategoryOnlyComposable(category: Category, changeNeeded: (Category, Boolean) -> Unit, picked: Boolean) {
    val ctx = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                if (!category.needed) {
                    changeNeeded(category, true)
                } else {
                    val dialogViewModel = Factory.create(InvoiceItemFormViewModel::class.java)
                    val dialog = InvoiceItemFormDialog(
                        ctx as AppCompatActivity,
                        true,
                        null,
                        dialogViewModel
                    )
                    dialog.show()
                    dialog.maximize()
                    dialogViewModel.init(category)
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = picked(R.drawable.ic_baseline_check_box_24, R.drawable.ic_baseline_crop_square_24)),
            modifier = Modifier.alpha(category.needed(1.0f, 0.0f)),
            contentDescription = ""
        )
        Text(
            text = category.displayableName,
            style = TextStyle(fontFamily = appFont, fontSize = 14.sp),
            modifier = Modifier.weight(1.0f)
        )
        Text(
            text = stringResource(id = statusResId(category.needed, category.picked)),
            style = TextStyle(fontFamily = appFont, fontSize = 9.sp, color = Color.DarkGray)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Switch(
            checked = category.needed, onCheckedChange = {
                changeNeeded(category, it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xff_76FF03),
                checkedTrackColor = Color.White,
            )
        )
    }
}

fun statusResId(needed: Boolean, picked: Boolean): Int {
    return if (needed) {
        if (picked) R.string.GroupStatusPicked else R.string.GroupStatusNeeded
    } else {
        R.string.GroupStatusNone
    }
}