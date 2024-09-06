package farayan.sabad.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import farayan.sabad.db.Category
import farayan.sabad.db.Item
import farayan.sabad.utility.appFont


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupsDropdownMenuBox(
    selected: String,
    label: String,
    groups: List<GroupInvoiceItemForm>,
    modifier: Modifier = Modifier,
    readonly: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue(selected)) }
    val editable = !readonly

    ExposedDropdownMenuBox(
        expanded = editable && expanded,
        modifier = modifier.padding(5.dp),
        onExpandedChange = { expanded = editable && it },
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it; expanded = true },
            readOnly = readonly,
            singleLine = true,
            label = { Text(label, fontFamily = appFont) },
            textStyle = TextStyle(fontFamily = appFont),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = editable && expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusEvent { expanded = editable && it.hasFocus },
            colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = if (readonly) Color.LightGray else Color.Transparent),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        )
        ExposedDropdownMenu(
            expanded = editable && expanded,
            onDismissRequest = { expanded = false },
        ) {
            groups.filter { text.text.isEmpty() || it.text.contains(text.text) }.forEach { group ->
                DropdownMenuItem(
                    onClick = {
                        text = TextFieldValue(group.text, selection = TextRange(group.text.length))
                        expanded = false
                    }
                ) {
                    val displayable = buildAnnotatedString {
                        appendInlineContent("prefix", "[prefix]")
                        append(" ")
                        append(group.text)
                    }
                    val ic = mapOf(
                        Pair(
                            "prefix",
                            InlineTextContent(
                                Placeholder(
                                    width = 16.sp,
                                    height = 16.sp,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                )
                            )
                            {
                                Icon(group.status.icon, "", tint = group.status.iconColor)
                            }
                        )
                    )
                    Text(
                        displayable,
                        inlineContent = ic,
                        style = group.status.style
                    )
                }
            }
        }
    }
}

enum class GroupPickState(
    val iconColor: Color,
    val position: Int,
    val icon: ImageVector,
    val style: TextStyle
) {
    NeededButNotPicked(
        Color.Black, 0, Icons.Outlined.CheckCircle,
        TextStyle(
            fontFamily = appFont,
            color = Color.Black,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        )
    ),
    NeededAndPicked(
        Color.Green, 1, Icons.Filled.CheckCircle,
        TextStyle(
            fontFamily = appFont,
            color = Color.hsl(196F, 0.67F, 0.45F),
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal
        )
    ),
    PickedWithoutNeeded(
        Color.Blue, 2, Icons.Filled.Warning,
        TextStyle(
            fontFamily = appFont,
            color = Color.hsl(26F, 0.73F, 0.57F),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal
        )
    ),
    NotNeededNotPicked(
        Color.Gray, 3, Icons.Outlined.Add,
        TextStyle(
            fontFamily = appFont,
            color = Color.Gray,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Normal
        )
    ),
    ;

    companion object {
        fun resolveStatus(
            category: Category,
            pickedItems: List<Item>
        ): GroupPickState {
            @Suppress("KotlinConstantConditions")
            return when {
                category.needed && category.picked -> NeededAndPicked
                category.needed && !category.picked -> NeededButNotPicked
                !category.needed && category.picked -> PickedWithoutNeeded
                !category.needed && !category.picked -> NotNeededNotPicked
                else -> throw RuntimeException()
            }
        }
    }
}

data class GroupInvoiceItemForm(val id: Long, val text: String, val status: GroupPickState)