package com.example.shutaffim

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.platform.LocalContext
import com.google.ai.client.generativeai.type.content

data class Person(
    val images: List<Int>,
    val name: String,
    val phone: String,
    val date: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterstedItem(person: Person, navController: NavController, screen: Screen) {
    var interestedClick by remember {
        mutableStateOf(false)
    }
    val size = 70.dp

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            focusedElevation = 256.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface

        ),
        onClick = { /* TODO Enter to the item screen*/

            navController.navigate(Screen.ProfileViewScreen.route)


//     PopMessageInterestedView
//             when(screen.route){
//                Screen.PostScreen.route -> navController.navigate(Screen.PostScreen.route)
//                Screen.EditPostScreen.route -> navController.navigate(Screen.EditPostScreen.route)
//            }

        }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(size)
        ) {
            Image(
                painter = painterResource(id = person.images[2]),
                contentDescription = "",
                modifier = Modifier
                    .height(size)
                    .width(size),
                contentScale = ContentScale.Crop
            )
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    Text(
                        text = person.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = person.date
                    )


                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = person.phone,
                        modifier = Modifier
                            .padding(start = 8.dp , end =128.dp),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    val checkedState = remember { mutableStateOf(false) }
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it },
                        modifier = Modifier
                            .fillMaxSize()



                    )
                }
            }


        }
    }

}
