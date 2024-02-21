package com.zelianko.kitchencalculator.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDialog(
    dialogController: DialogController
) {

    val fromWidth = remember { dialogController.fromWidth }
    val fromHeight = remember { dialogController.fromHeight }
    val fromRadius = remember { dialogController.fromRadius }

    val toWidth = remember { dialogController.toWidth }
    val toHeight = remember { dialogController.toHeight }
    val toRadius = remember { dialogController.toRadius }


    if (dialogController.openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                dialogController.onDialogEvent(DialogEvent.OnCancel)
            },
            modifier = Modifier
                .height(311.dp)
                .width(314.dp),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                )
            ) {
                Row(
                    modifier = Modifier
                        .height(35.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.form_in_recipe),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )

                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(157.dp)
                        // .background(Color.Black)
                    ) {
                        Spacer(modifier = Modifier.size(50.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.size(20.dp))
                            RadioButton(
                                modifier = Modifier.size(18.dp),
                                selected = dialogController.fromFormCircle.value,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.orange_primary),
                                    unselectedColor = colorResource(id = R.color.orange_primary),
                                    disabledSelectedColor = colorResource(id = R.color.orange_primary),
                                    disabledUnselectedColor = colorResource(id = R.color.orange_primary)
                                ),
                                onClick = {
                                    dialogController.onDialogEvent(DialogEvent.OnChangeFromForm)
                                }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = stringResource(id = R.string.round),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.size(20.dp))
                            RadioButton(
                                modifier = Modifier.size(18.dp),
                                selected = dialogController.fromFormRectangle.value,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.orange_primary),
                                    unselectedColor = colorResource(id = R.color.orange_primary),
                                    disabledSelectedColor = colorResource(id = R.color.orange_primary),
                                    disabledUnselectedColor = colorResource(id = R.color.orange_primary)
                                ),
                                onClick = {
                                    dialogController.onDialogEvent(DialogEvent.OnChangeFromForm)
                                }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = stringResource(id = R.string.rectangle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }

                        //Второй блок
                        Spacer(modifier = Modifier.size(50.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.size(20.dp))
                            RadioButton(
                                modifier = Modifier.size(18.dp),
                                selected = dialogController.toFormCircle.value,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.orange_primary),
                                    unselectedColor = colorResource(id = R.color.orange_primary),
                                    disabledSelectedColor = colorResource(id = R.color.orange_primary),
                                    disabledUnselectedColor = colorResource(id = R.color.orange_primary)
                                ),
                                onClick = {
                                    dialogController.onDialogEvent(DialogEvent.OnChangeFromTo)
                                }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = stringResource(id = R.string.round),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.size(20.dp))
                            RadioButton(
                                modifier = Modifier.size(18.dp),
                                selected = dialogController.toFormRectangle.value,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorResource(id = R.color.orange_primary),
                                    unselectedColor = colorResource(id = R.color.orange_primary),
                                    disabledSelectedColor = colorResource(id = R.color.orange_primary),
                                    disabledUnselectedColor = colorResource(id = R.color.orange_primary)
                                ),
                                onClick = {
                                    dialogController.onDialogEvent(DialogEvent.OnChangeFromTo)
                                }
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = stringResource(id = R.string.rectangle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(157.dp)
                            .padding(end = 10.dp),
                        horizontalAlignment = Alignment.End
                    ) {

                        if (dialogController.fromFormRectangle.value) {
                            Spacer(modifier = Modifier.size(30.dp))
                            Text(
                                modifier = Modifier
                                    .offset((-5).dp),
                                text = "Длинна, ширина",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RectangleInput(
                                    height = 55.dp,
                                    width = 55.dp,
                                    input = fromWidth.value,
                                    dialogSizeType = DialogSizeTypes.FromWidth
                                ) { event ->
                                    dialogController.onDialogEvent(event)
                                }
                                RectangleInput(
                                    height = 55.dp,
                                    width = 55.dp,
                                    input = fromHeight.value,
                                    dialogSizeType = DialogSizeTypes.FromHeight
                                ) { event ->
                                    dialogController.onDialogEvent(event)
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.size(30.dp))
                            Text(
                                modifier = Modifier
                                    .offset((-37).dp),
                                text = "Радиус",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircleInput(
                                    height = 60.dp,
                                    width = 60.dp,
                                    input = fromRadius.value,
                                    dialogSizeType = DialogSizeTypes.FromRadius
                                ) { event ->
                                    dialogController.onDialogEvent(event)
                                }

                            }
                            // Spacer(modifier = Modifier.size(5.dp))
                        }
                        Spacer(modifier = Modifier.size(3.dp))
                        Icon(
                            modifier = Modifier
                                .offset(x = (-55).dp),
                            imageVector = ImageVector.vectorResource(R.drawable.down_arrow_img),
                            contentDescription = "down_arrow_img",
                            tint = colorResource(id = R.color.grey)
                        )

                        Spacer(modifier = Modifier.size(3.dp))
                        if (dialogController.toFormRectangle.value) {
                            Text(
                                modifier = Modifier
                                    .offset((-5).dp),
                                text = stringResource(id = R.string.dlinna_shirina),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RectangleInput(
                                    height = 55.dp,
                                    width = 55.dp,
                                    input = toWidth.value,
                                    dialogSizeType = DialogSizeTypes.ToWidth
                                ) { event ->
                                    dialogController.onDialogEvent(event)
                                }
                                RectangleInput(
                                    height = 55.dp,
                                    width = 55.dp,
                                    input = toHeight.value,
                                    dialogSizeType = DialogSizeTypes.ToHeight
                                ) { event ->
                                    dialogController.onDialogEvent(event)
                                }
                            }
                        } else {
                            Text(
                                modifier = Modifier
                                    .offset((-37).dp),
                                text = stringResource(id = R.string.radius),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 25.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircleInput(
                                    height = 60.dp,
                                    width = 60.dp,
                                    input = toRadius.value,
                                    dialogSizeType = DialogSizeTypes.ToRadius
                                ) { event ->
                                    dialogController.onDialogEvent(event)
                                }

                            }
                            // Spacer(modifier = Modifier.size(5.dp))
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = {
                                dialogController.onDialogEvent(DialogEvent.OnCancel)
                            }) {
                                Text(
                                    text = stringResource(id = R.string.no),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                            TextButton(onClick = {
                                dialogController.onDialogEvent(DialogEvent.OnConfirm)
                            }) {
                                Text(
                                    text = stringResource(id = R.string.yes),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun RectangleInput(
    input: String,
    height: Dp = 54.dp,
    width: Dp = 67.dp,
    dialogSizeType: DialogSizeTypes,
    onEvent: (DialogEvent) -> Unit,

    ) {
    OutlinedTextField(
        modifier = Modifier
            .height(height)
            .width(width),
        value = input,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = colorResource(id = R.color.grey_light),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = colorResource(id = R.color.orange_primary),
            unfocusedBorderColor = colorResource(id = R.color.orange_primary)
        ),
        shape = RoundedCornerShape(12.dp),
        maxLines = 1,
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        onValueChange = { newText ->
            when (dialogSizeType) {
                is DialogSizeTypes.FromHeight -> {
                    onEvent(DialogEvent.HeightSetFrom(newText))
                }

                is DialogSizeTypes.FromWidth -> {
                    onEvent(DialogEvent.WidthSetFrom(newText))
                }

                is DialogSizeTypes.FromRadius -> {
                    onEvent(DialogEvent.RadiusSetFrom(newText))
                }

                is DialogSizeTypes.ToHeight -> {
                    onEvent(DialogEvent.HeightSetTo(newText))
                }

                is DialogSizeTypes.ToWidth -> {
                    onEvent(DialogEvent.WidthSetTo(newText))
                }

                is DialogSizeTypes.ToRadius -> {
                    onEvent(DialogEvent.RadiusSetTo(newText))
                }
            }

        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}


@Composable
fun CircleInput(
    input: String,
    height: Dp = 54.dp,
    width: Dp = 67.dp,
    dialogSizeType: DialogSizeTypes,
    onEvent: (DialogEvent) -> Unit,

    ) {
    OutlinedTextField(
        modifier = Modifier
            .height(height)
            .width(width),
        value = input,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = colorResource(id = R.color.grey_light),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = colorResource(id = R.color.orange_primary),
            unfocusedBorderColor = colorResource(id = R.color.orange_primary)
        ),
        shape = RoundedCornerShape(50.dp),
        maxLines = 1,
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        onValueChange = { newText ->
            when (dialogSizeType) {
                is DialogSizeTypes.FromHeight -> {
                    onEvent(DialogEvent.HeightSetFrom(newText))
                }

                is DialogSizeTypes.FromWidth -> {
                    onEvent(DialogEvent.WidthSetFrom(newText))
                }

                is DialogSizeTypes.FromRadius -> {
                    onEvent(DialogEvent.RadiusSetFrom(newText))
                }

                is DialogSizeTypes.ToHeight -> {
                    onEvent(DialogEvent.HeightSetTo(newText))
                }

                is DialogSizeTypes.ToWidth -> {
                    onEvent(DialogEvent.WidthSetTo(newText))
                }

                is DialogSizeTypes.ToRadius -> {
                    onEvent(DialogEvent.RadiusSetTo(newText))
                }
            }

        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}