package com.example.shutaffim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpView() {

    var interestedMsg by remember {
        mutableStateOf("Enter Message")
    }
    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    )
    {
        Row(

            verticalAlignment = Alignment.CenterVertically
        ) {


            OutlinedTextField(
                value = interestedMsg,
                onValueChange = { interestedMsg = it },
                modifier = Modifier
                    .padding(4.dp)
                    .size(400.dp, 300.dp),

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    cursorColor = MaterialTheme.colorScheme.primary,

                    ),

                )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row (

            verticalAlignment = Alignment.CenterVertically,
        ){
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(8.dp)
                    .size(300.dp, 50.dp),


                ) {
                Text(
                    text = "Send",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.background,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun PopUpViewPreview() {
    PopUpView()
}