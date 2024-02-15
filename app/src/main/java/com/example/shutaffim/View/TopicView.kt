package com.example.shutaffim.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shutaffim.Model.Comment
import com.example.shutaffim.Model.Topic
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.ForumVM
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicView(navController: NavController, forumVM: ForumVM, authViewModel: AuthViewModel) {

    val defaultTopic = Topic(
        id = "1",
        title = "Title",
        description = "Description",
        email = "",
        date = 0L
    )
    val currTopic by forumVM.currTopic.observeAsState(defaultTopic)

    val comments by forumVM.comments.observeAsState(listOf())

    val owner = authViewModel.currentUser.value?.email == currTopic.email;

    var newComment by remember { mutableStateOf("") }

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = currTopic.title) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ForumScreen.route)
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (owner) {
                        IconButton(onClick = {
                            forumVM.deleteTopic()
                            navController.navigate(Screen.ForumScreen.route)
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete topic"
                            )
                        }
                    }

                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp)
                            .height(108.dp),
                        value = newComment,
                        onValueChange = { newComment = it },
                        label = { Text("Add a comment") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    if (newComment.isNotEmpty()) {
                        FloatingActionButton(
                            onClick = {
                                val comment = Comment(
                                    text = newComment,
                                    email = authViewModel.currentUser.value?.email!!,
                                    date = System.currentTimeMillis()
                                )
                                forumVM.addComment(comment)
                                newComment = ""
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send"
                            )
                        }
                    }
                }

            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = currTopic.userName,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
                        Text(
                            text = sdf.format(currTopic.date)
                        )
                    }
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = currTopic.description,
                        style = TextStyle(
                            fontSize = 14.sp
                        )
                    )
                }
                LazyColumn(
                ) {
                    items(comments) { comment ->
                        CommentItem(comment = comment)
                    }
                }
            }

        }
    )

}

@Composable
fun CommentItem(comment: Comment) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = comment.userName,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(comment.date),
                    style = TextStyle(
                        fontSize = 10.sp

                    )
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = comment.text,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )


        }
    }
}
