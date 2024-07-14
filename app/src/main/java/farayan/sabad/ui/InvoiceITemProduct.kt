package farayan.sabad.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import farayan.sabad.R

@Composable
fun InvoiceItemProduct(imagePath: String, onRemove: (String) -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imagePath),
            contentDescription = "",
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .padding(4.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.icons_clear_circle),
            contentDescription = "",
            modifier = Modifier
                .width(12.dp)
                .height(12.dp)
                .align(Alignment.TopEnd)
                .clickable { onRemove(imagePath) }
        )
    }
}