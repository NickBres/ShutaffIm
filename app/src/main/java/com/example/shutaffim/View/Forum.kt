package com.example.shutaffim.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.shutaffim.Model.Topic
import com.example.shutaffim.R
import com.example.shutaffim.Screen
import com.example.shutaffim.ViewModel.ForumVM
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forum(navController: NavController,forumVM: ForumVM) {

    val topics by forumVM.topics.observeAsState(listOf())
    var sortMenu by remember { mutableStateOf(false) }
    val sortOptions = listOf("Newest", "Oldest", "Mine")
    var selectedOption by remember { mutableStateOf("Newest") }

    var createWindow by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Forum") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.TypeScreen.route)
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    Box() {
                        IconButton(onClick = {
                            sortMenu = true
                            forumVM.viewModelScope.launch {
                                forumVM.loadTopics()
                            }
                        }) {
                            Icon(
                                Icons.Default.List,
                                contentDescription = "Sort"
                            )
                        }
                        DropdownMenu(expanded = sortMenu,
                            onDismissRequest = { sortMenu = false }) {
                            sortOptions.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedOption = label
                                        sortMenu = false
                                        when (label) {
                                            "Newest" -> {
                                                forumVM.sortTopicsByDate(false)
                                            }

                                            "Oldest" -> {
                                                forumVM.sortTopicsByDate(true)
                                            }

                                            "Mine" -> {
                                                forumVM.filterMineTopics()
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.surface,
                    actionIconContentColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    createWindow = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Search And Filter")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                items(topics) { topic ->
                    ForumItem(topic) { topicId ->
                        forumVM.loadTopic(topicId, navController)
                    }
                }
            }
        }
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    if (createWindow) {
        ModalBottomSheet(
            onDismissRequest = { createWindow = false },
            content = {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    text = "Create New Topic",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .fillMaxWidth(),
                    value = title,
                    label = { Text(text = "Title") },
                    onValueChange = { title = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Box {

                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(256.dp),
                            value = description,
                            onValueChange = { description = it },
                            label = { Text(text = "Description") },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )
                    }

                    FloatingActionButton(
                        modifier = Modifier
                            .padding(20.dp)
                            .align(androidx.compose.ui.Alignment.BottomEnd)
                            .offset(y = 32.dp),
                        onClick = {
                            if (title.isNotEmpty() && description.isNotEmpty()) {
                                forumVM.createTopic(title,description)
                                title =""
                                description=""
                                createWindow = false

                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
                Spacer(modifier = Modifier.height(128.dp))

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumItem(topic: Topic, clicked: (topicID: String) -> Unit) {

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            clicked(topic.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(64.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = topic.title,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    Text(
                        text = sdf.format(Date(topic.date)),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F)
                    )
                }
                Text(
                    text = "By: ${topic.userName}"
                )
            }

        }
    }

}
