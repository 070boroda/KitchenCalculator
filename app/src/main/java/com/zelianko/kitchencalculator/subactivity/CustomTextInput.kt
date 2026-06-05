package com.zelianko.kitchencalculator.subactivity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zelianko.kitchencalculator.R
import com.zelianko.kitchencalculator.ui.theme.Dimens

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CustomTextInput(
    inputText: MutableState<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val textState = inputText
        val pattern = remember { Regex("[\\d]*[.]?[\\d]*") }

        Text(
            text = stringResource(id = R.string.quantity),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.SpacingXs),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState.value,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.merge(MaterialTheme.typography.bodyLarge),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.all_background),
                cursorColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            ),
            onValueChange = {
                if (it.isEmpty() || it.matches(pattern)) textState.value = it
            },
            shape = RoundedCornerShape(Dimens.RadiusS),
            singleLine = true,
        )
    }
}
