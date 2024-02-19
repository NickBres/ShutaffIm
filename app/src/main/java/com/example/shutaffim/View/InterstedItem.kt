package com.example.shutaffim

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.shutaffim.Model.Request
import com.example.shutaffim.Model.User
import com.example.shutaffim.ViewModel.PostsVM
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)

//navController: NavController,
//request: Request,
//postVm: PostsVM,
//userVM: AuthViewModel
@Composable
fun InterestedItem(
    request: Request,
    user: User,
    postVm: PostsVM
) {

    val userImage = if (user.picture.pictureUrl != "") {
        rememberAsyncImagePainter(user.picture.pictureUrl)
    } else {
        painterResource(id = R.drawable.logo_background)
    }
    val userName = user.fName + " " + user.lName
    val currentYear = LocalDate.now().year
    val birthYear = user.birthYear ?: 0
    val userSex = when (user.sex) {
        "Male" -> "♂"
        "Female" -> "♀"
        else -> "⚧"
    }
    val userInfo = "${currentYear - birthYear}, $userSex"
    val userAbout = user.about ?: ""
    val userEmail = user.email ?: ""
    val message = request.message
    var showMessage by remember { mutableStateOf(false) }
    var showUserAbout by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp)
                    .clickable {
                        showUserAbout = !showUserAbout
                        showMessage = false
                    },
                painter = userImage,
                contentDescription = "user image",
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = userName,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                )
                Text(
                    text = userInfo,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )

            }
            FloatingActionButton(
                modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp),
                onClick = {
                    showMessage = !showMessage
                    showUserAbout = false
                },
                contentColor = MaterialTheme.colorScheme.surface,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Default.Message, contentDescription = "message")
            }


        }// row
        AnimatedVisibility(visible = showMessage) {
            Column {


                Text(
                    text = message,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                )
                Text(
                    text = "To contact: $userEmail",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

        }
        AnimatedVisibility(visible = showUserAbout) {
            Text(
                text = userAbout,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                modifier = Modifier
                    .padding(8.dp)
            )

        }


    }


}
