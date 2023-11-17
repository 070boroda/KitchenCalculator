package com.zelianko.kitchencalculator.subactivity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
            Modifier.width(160.dp)
                .padding(start = 20.dp)
    ){
        val textState = inputText
        val maxLength = 110
        val lightBlue = Color(0xFF105FEC)
        val blue = Color(0xFF146AFC)
        Text(
            text = stringResource(id = R.string.quantity),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = blue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        TextField(
            modifier = Modifier
                .width(170.dp)
                .padding(start = 24.dp),
            value = textState.value,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 22.sp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.backgroud_row),
                cursorColor = Color.Black,
                disabledLabelColor = colorResource(id = R.color.backgroud_row),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = lightBlue
            ),
            onValueChange = {
                if (it.length <= maxLength) textState.value = it
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
//            trailingIcon = {
//                if (textState.isNotEmpty()) {
//                    IconButton(onClick = { textState = "" }) {
//                        Icon(
//                            imageVector = Icons.Outlined.Close,
//                            contentDescription = null
//                        )
//                    }
//                }
//            }
        )
//        Text(
//            text = "${textState.length} / $maxLength",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 4.dp),
//            textAlign = TextAlign.End,
//            color = blue
//        )
    }
}