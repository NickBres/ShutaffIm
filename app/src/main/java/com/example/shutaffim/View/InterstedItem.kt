package com.example.shutaffim

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shutaffim.Model.Request
import com.example.shutaffim.ViewModel.AuthViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.navigation.compose.rememberNavController
import com.example.shutaffim.ViewModel.PostsVM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestedItem(navController: NavController,
                   request: Request,
                   postVm: PostsVM,
                   userVM: AuthViewModel
) {
    val size = 70.dp
    val dummyPic = R.drawable.connor_jalbert_5b1mb7sdbg0_unsplash
    var openMsgClick by remember {
        mutableStateOf(false)
    }

    userVM.getUserDataById(request.userId)
    val userOfRequest by userVM.getUserFromId.observeAsState()
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
             ){
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
                    onClick = { /* TODO open the msg of the user*/
                        openMsgClick = !openMsgClick

                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(size)
                    ) {
                        Image(
                            /*TODO: change the image to the user image*/
                            painter = painterResource(dummyPic),
                            contentDescription = "image of the user",
                            modifier = Modifier
                                .height(size)
                                .width(size),
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,

                                ) {
                                Text(//fix alignment

                                    text = (userOfRequest?.fName
                                        ?: "user") + " " + (userOfRequest?.lName
                                        ?: "user"),
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    modifier = Modifier.padding(4.dp)
                                )


                                Text(
                                    text = sdf.format(Date(request.date)),
                                    textAlign = TextAlign.Right,
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp),

                            ) {
                                IconButton(
                                    onClick = {
                                        postVm.removeInterestedFromPost(
                                            request.userId,
                                            request.postId
                                        )

                                    }

                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                                Spacer(modifier = Modifier.width(186.dp))
                                val checkedState = remember { mutableStateOf(false) }
                                Checkbox(
                                    checked = checkedState.value,
                                    onCheckedChange = {
                                        checkedState.value = it
                                        if (checkedState.value) {
                                            /* TODO: build func that update isApprove*/
                                            postVm.updateIsApproved(
                                                request.postId,
                                                request.userId,
                                                true
                                            )
                                        } else {
                                            postVm.updateIsApproved(
                                                request.postId,
                                                request.userId,
                                                false
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()

                                    //place the checkbox in the end of the row

                                )

                            }

                        }
                    }
                    AnimatedVisibility(visible = openMsgClick) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.0f))
                                .clickable {
                                    userVM.insertcurrentintersted(request.userId)
                                    navController.navigate(Screen.ProfileScreen.route)/* do something when clicked */ }
                        ) {
                            Text(
                                text = request.message,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

}


//preview
@Composable
@Preview(showBackground = true)
fun InterestedItemPreview() {
    val navController = rememberNavController()
//    InterestedItem(
//
//        navController = navController, request = Request(
//            "cons@gmail.com",
//            "user1", "2021-10-10", false, "I'm interested in your item"
//        )
//    )
}