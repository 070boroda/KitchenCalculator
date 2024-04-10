package com.zelianko.kitchencalculator.subactivity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zelianko.kitchencalculator.R

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CustomTextInput(inputText: MutableState<String>) {
    Column (
        modifier =
        Modifier
            .width(160.dp)
            .padding(start = 8.dp)
    ){
        val textState = inputText
        val maxLength = 110
        val blue = Color(0xFF146AFC)
        Text(
            text = stringResource(id = R.string.quantity),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            modifier = Modifier
                .width(150.dp)
                .height(54.dp)
                .padding(start = 24.dp),
            value = textState.value,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.all_background),
                cursorColor = Color.Black,
                disabledLabelColor = Color.White,
                focusedBorderColor = Color.Black
            ) ,
            onValueChange = {
                if (it.length <= maxLength) textState.value = it
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
        )
    }
}