package farayan.sabad.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun InvoiceItemProduct(imagePath: String) {
    Image(
        painter = rememberAsyncImagePainter(imagePath),
        contentDescription = "",
        modifier = Modifier
            .width(32.dp)
            .height(32.dp)
    )
}