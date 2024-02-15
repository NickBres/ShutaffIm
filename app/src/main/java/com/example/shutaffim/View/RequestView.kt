package com.example.shutaffim

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shutaffim.Model.Post
import com.example.shutaffim.Model.User
import com.example.shutaffim.ViewModel.PostsVM
import com.example.shutaffim.ui.theme.primaryContainer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestView(
    user:User,
    post : Post,
    postVM: PostsVM,
    state : String
) {

    if(state == "Click i'm interested"){

        var interestedMsg by remember {
            mutableStateOf("")
        }

        var enableBtn by remember {
            mutableStateOf(true)
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
                    label = { Text(text = "Enter message") },
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
                    onClick = { /*TODO*/
                        postVM.addInterestedToPost(user.email, post.id, interestedMsg)


                        enableBtn = false
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(300.dp, 50.dp),
                    enabled = enableBtn


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
    else{

        val listOfInterested by postVM.interestedInPost.observeAsState(listOf())
        var interestedMsg by remember {
            mutableStateOf("")
        }
        interestedMsg = listOfInterested.find { it.userId == user.email }?.message?:""

        var enableBtn by remember {
            mutableStateOf(true)
        }
        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        )
        {
            Row(

                verticalAlignment = Alignment.CenterVertically
            ) {//todo: text inside a card in the same size as the outline text field

              Card(
                modifier = Modifier
                    .padding(4.dp)
                    .size(400.dp, 300.dp),
                  colors = CardDefaults.cardColors(
                      containerColor = MaterialTheme.colorScheme.surface
                  )
              ){
                  Text(
                      text = interestedMsg,
                      style = TextStyle(
                          fontSize = 16.sp,
                          color = MaterialTheme.colorScheme.onSurface,
                          fontWeight = FontWeight.Bold,
                      ),
                      modifier = Modifier.padding(8.dp)
                  )
              }


            }
            Spacer(modifier = Modifier.height(8.dp))
            Row (

                verticalAlignment = Alignment.CenterVertically,
            ){
                Button(
                    onClick = { /*TODO*/
                        postVM.removeInterestedFromPost(user.email, post.id)

                        enableBtn = false
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(300.dp, 50.dp),
                    enabled = enableBtn ,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)

                ) {
                    Text(
                        text = "Delete",
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
}


@Preview(showBackground = true)
@Composable
fun PopUpViewPreview() {
   // RequestView()
}