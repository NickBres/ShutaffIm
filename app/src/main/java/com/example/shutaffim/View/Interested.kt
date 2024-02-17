package com.example.shutaffim

//import com.example.shutaffim.Model.InterestedInPost
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.PostsVM


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Interested(
    navController: NavController,
    postsVM : PostsVM,
    userVM: AuthViewModel
) {


    val postImg = listOf(
        R.drawable.connor_jalbert_5b1mb7sdbg0_unsplash,
        R.drawable._reka_us_24,
        R.drawable.joao_marcelo_martins_hfoprrwjvgg_unsplash,
    )

    var new_post by remember {
        mutableStateOf(false)
    }

    val listOfInterested by postsVM.interestedInPost.observeAsState(initial = listOf())
    postsVM.getInterestedInPost(postsVM.currPost.value!!.id)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .padding(start = 4.dp, end = 4.dp),
    ) {
        items(listOfInterested) { request ->
            InterestedItem(request = request, userVM = userVM, postVm = postsVM)
        }
    }

    if (new_post) {
        ModalBottomSheet(onDismissRequest = { new_post = false }, content = {

        })
    }
}


@Preview(showBackground = true)
@Composable
fun MyInterstedScreenViewPreview() {
    // Intersted(navController = NavController(LocalContext.current))
}
