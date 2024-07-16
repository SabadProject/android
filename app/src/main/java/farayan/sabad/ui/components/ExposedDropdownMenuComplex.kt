package farayan.sabad.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import farayan.sabad.ui.appFont


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenuComplex(
    selected: String,
    label: String,
    options: List<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(selected) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        modifier = Modifier.padding(5.dp),
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(label, fontFamily = appFont) },
            textStyle = TextStyle(fontFamily = appFont),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        text = option
                        expanded = false
                    }
                ){
                    Text(text = option, style = TextStyle(fontFamily = appFont))
                }
            }
        }
    }
}