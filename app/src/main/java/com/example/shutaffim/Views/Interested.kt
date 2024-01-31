package com.example.shutaffim

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyInterstedScreenView(navController: NavController) {

    val postImg = listOf(
        R.drawable.connor_jalbert_5b1mb7sdbg0_unsplash,
        R.drawable._reka_us_24,
        R.drawable.joao_marcelo_martins_hfoprrwjvgg_unsplash,
    )

    val dummyListperson = listOf(
        Person(postImg , "alis" , "050" , "18.01.2023"),
        Person(postImg , "bob" , "051" , "18.02.2023"),
        Person(postImg , "yair" , "050" ,"18.03.2023")
        )
    var new_post by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Intersted") }, navigationIcon = {
                IconButton(onClick = { navController.navigateUp()}) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.primary,
                navigationIconContentColor = MaterialTheme.colorScheme.primary
            )
            )
        }, floatingActionButton = {

        }, content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(start = 4.dp, end = 4.dp),
            ) {
                items(dummyListperson) { person ->
                    InterstedItem(person ,navController ,Screen.TypeScreen)
                }
            }

            if (new_post) {
                ModalBottomSheet(onDismissRequest = { new_post = false }, content = {

                })
            }
        })
}


@Preview(showBackground = true)
@Composable
fun MyInterstedScreenViewPreview() {
    MyInterstedScreenView(navController = NavController(LocalContext.current))
}
