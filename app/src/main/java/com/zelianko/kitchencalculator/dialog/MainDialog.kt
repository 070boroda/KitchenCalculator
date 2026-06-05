package com.zelianko.kitchencalculator.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDialog(dialogController: DialogController) {
    val fromWidth = remember { dialogController.fromWidth }
    val fromHeight = remember { dialogController.fromHeight }
    val fromDiametr = remember { dialogController.fromDiametr }

    val toWidth = remember { dialogController.toWidth }
    val toHeight = remember { dialogController.toHeight }
    val toDiametr = remember { dialogController.toDiametr }

    if (!dialogController.openDialog.value) return

    BasicAlertDialog(
        onDismissRequest = { dialogController.onDialogEvent(DialogEvent.OnCancel) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingL),
    ) {
        Card(
            shape = RoundedCornerShape(Dimens.RadiusL),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_surface)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.SpacingL),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.form_in_recipe),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(Dimens.SpacingM))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(Dimens.SpacingM))

                // FROM-секция
                FormSection(
                    title = stringResource(id = R.string.from),
                    isCircle = dialogController.fromFormCircle.value,
                    onShapeChange = {
                        dialogController.onDialogEvent(DialogEvent.OnChangeFromForm)
                    },
                    diameterValue = fromDiametr.value,
                    widthValue = fromWidth.value,
                    heightValue = fromHeight.value,
                    onDiameter = {
                        dialogController.onDialogEvent(DialogEvent.RadiusSetFrom(it))
                    },
                    onWidth = {
                        dialogController.onDialogEvent(DialogEvent.WidthSetFrom(it))
                    },
                    onHeight = {
                        dialogController.onDialogEvent(DialogEvent.HeightSetFrom(it))
                    },
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingS))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.down_arrow_img),
                    contentDescription = null,
                    tint = colorResource(id = R.color.grey),
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.height(Dimens.SpacingS))

                // TO-секция
                FormSection(
                    title = stringResource(id = R.string.convert_to),
                    isCircle = dialogController.toFormCircle.value,
                    onShapeChange = {
                        dialogController.onDialogEvent(DialogEvent.OnChangeFromTo)
                    },
                    diameterValue = toDiametr.value,
                    widthValue = toWidth.value,
                    heightValue = toHeight.value,
                    onDiameter = {
                        dialogController.onDialogEvent(DialogEvent.RadiusSetTo(it))
                    },
                    onWidth = {
                        dialogController.onDialogEvent(DialogEvent.WidthSetTo(it))
                    },
                    onHeight = {
                        dialogController.onDialogEvent(DialogEvent.HeightSetTo(it))
                    },
                )

                Spacer(modifier = Modifier.height(Dimens.SpacingL))
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(onClick = {
                        dialogController.onDialogEvent(DialogEvent.OnCancel)
                    }) {
                        Text(
                            text = stringResource(id = R.string.no),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    TextButton(onClick = {
                        dialogController.onDialogEvent(DialogEvent.OnConfirm)
                    }) {
                        Text(
                            text = stringResource(id = R.string.yes),
                            color = colorResource(id = R.color.table_spoon),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    isCircle: Boolean,
    onShapeChange: () -> Unit,
    diameterValue: String,
    widthValue: String,
    heightValue: String,
    onDiameter: (String) -> Unit,
    onWidth: (String) -> Unit,
    onHeight: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.SpacingXs),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingS),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ShapeOption(
                label = stringResource(id = R.string.round),
                selected = isCircle,
                onClick = { if (!isCircle) onShapeChange() },
                modifier = Modifier.weight(1f),
            )
            ShapeOption(
                label = stringResource(id = R.string.rectangle),
                selected = !isCircle,
                onClick = { if (isCircle) onShapeChange() },
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(Dimens.SpacingS))
        if (isCircle) {
            DimensionField(
                label = stringResource(id = R.string.diameter),
                value = diameterValue,
                onValueChange = onDiameter,
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingS),
            ) {
                DimensionField(
                    label = stringResource(id = R.string.dlinna_shirina),
                    value = widthValue,
                    onValueChange = onWidth,
                    modifier = Modifier.weight(1f),
                )
                DimensionField(
                    label = stringResource(id = R.string.dlinna_shirina),
                    value = heightValue,
                    onValueChange = onHeight,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ShapeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(id = R.color.orange_primary),
                unselectedColor = colorResource(id = R.color.orange_primary),
            ),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun DimensionField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pattern = remember { Regex("[\\d]*[.]?[\\d]*") }
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.isEmpty() || it.matches(pattern)) onValueChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.orange_primary),
            unfocusedBorderColor = colorResource(id = R.color.orange_primary),
        ),
        shape = RoundedCornerShape(Dimens.RadiusM),
        singleLine = true,
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
