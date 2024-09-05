package farayan.sabad.utility

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import farayan.sabad.R

fun Modifier.defaults(): Modifier {
    return this.padding(5.dp)
}

fun Modifier.errorBorder(add: Boolean): Modifier {
    if (add) {
        this.border(1.dp, Color.Red, RectangleShape)
    }
    return this
}

val appFont = FontFamily(
    Font(R.font.vazir)
)