package com.zelianko.kitchencalculator.subactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.recipe_add_screen.ProductDraft
import com.zelianko.kitchencalculator.ui.theme.Dimens

/**
 * Карточка ингредиента (вариант A): название (растягивается), количество, единица-чип, ⋮ меню.
 * Полностью stateless — состояние держит вызывающий, мы только дёргаем обработчики.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientCard(
    draft: ProductDraft,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var unitSheetVisible by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val amountPattern = remember { Regex("[\\d]*[.]?[\\d]*") }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingL, vertical = Dimens.SpacingXs),
        shape = RoundedCornerShape(Dimens.RadiusM),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingS, vertical = Dimens.SpacingXs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Имя ингредиента — занимает всё свободное место
            OutlinedTextField(
                value = draft.name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Dimens.SpacingXs),
                label = {
                    Text(
                        text = stringResource(id = R.string.add_ingredient),
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(Dimens.RadiusS),
            )

            // Количество
            OutlinedTextField(
                value = draft.amount,
                onValueChange = { new ->
                    if (new.isEmpty() || new.matches(amountPattern)) onAmountChange(new)
                },
                modifier = Modifier.widthIn(min = 72.dp, max = 96.dp),
                label = {
                    Text(
                        text = stringResource(id = R.string.amount),
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(Dimens.RadiusS),
            )

            Spacer(modifier = Modifier.width(Dimens.SpacingXs))

            // Единица измерения — кликабельный chip
            UnitChip(
                value = draft.unit,
                onClick = { unitSheetVisible = true },
            )

            // ⋮ меню
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(id = R.string.cd_more_actions),
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.delete)) },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }
        }
    }

    if (unitSheetVisible) {
        UnitPickerSheet(
            selected = draft.unit,
            onSelected = {
                onUnitChange(it)
                unitSheetVisible = false
            },
            onDismiss = { unitSheetVisible = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitChip(value: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(Dimens.RadiusPill),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        modifier = Modifier
            .widthIn(min = 56.dp)
            .padding(vertical = 2.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.SpacingS, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = value.ifBlank { stringResource(id = R.string.unit) },
                style = MaterialTheme.typography.bodyMedium,
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = stringResource(id = R.string.cd_unit),
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitPickerSheet(
    selected: String,
    onSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val units = unitOptions()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        units.forEach { unit ->
            ListItem(
                headlineContent = { Text(unit, fontSize = 18.sp) },
                leadingContent = {
                    RadioButton(
                        selected = unit == selected,
                        onClick = { onSelected(unit) },
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.SpacingS),
            )
        }
        Spacer(modifier = Modifier.size(Dimens.SpacingM))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingS),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    }
}

@Composable
private fun unitOptions(): List<String> = listOf(
    stringResource(id = R.string.g),
    stringResource(id = R.string.l),
    stringResource(id = R.string.ml),
    stringResource(id = R.string.kg),
    stringResource(id = R.string.sht),
    stringResource(id = R.string.tea_spoon_short),
    stringResource(id = R.string.table_spoon_short),
    stringResource(id = R.string.glass_short),
    stringResource(id = R.string.oz),
    stringResource(id = R.string.packing_short),
    stringResource(id = R.string.pinch),
    stringResource(id = R.string.lb),
    stringResource(id = R.string.drop),
    stringResource(id = R.string.pinta),
    stringResource(id = R.string.gallon),
)
