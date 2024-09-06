package farayan.sabad.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import farayan.sabad.utility.appFont
import farayan.sabad.db.Unit as PersistenceUnit

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UnitsDropdownMenuBox(
    selected: PersistenceUnit?,
    label: String,
    units: List<PersistenceUnit>,
    onValueChanged: (PersistenceUnit?) -> Unit,
    modifier: Modifier = Modifier,
    readonly: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var text = TextFieldValue(selected?.displayableName ?: "")
    val editable = !readonly

    ExposedDropdownMenuBox(
        expanded = editable && expanded,
        modifier = modifier,
        onExpandedChange = { expanded = editable && it },
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; expanded = true },
            readOnly = readonly,
            singleLine = true,
            label = {
                Text(
                    label, fontFamily = appFont, modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                )
            },
            textStyle = TextStyle(fontFamily = appFont),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = editable && expanded) },
            modifier = Modifier.onFocusEvent { expanded = editable && it.hasFocus },
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = if (readonly) Color.LightGray else Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        )
        ExposedDropdownMenu(
            expanded = editable && expanded,
            onDismissRequest = { expanded = false },
        ) {
            units.forEach { groupUnit ->
                DropdownMenuItem(
                    onClick = {
                        text = TextFieldValue(
                            groupUnit.displayableName,
                            selection = TextRange(groupUnit.displayableName.length)
                        )
                        onValueChanged(groupUnit)
                        expanded = false
                    }
                ) {
                    Text(
                        groupUnit.displayableName,
                        style = TextStyle(fontFamily = appFont)
                    )
                }
            }
        }
    }
}
