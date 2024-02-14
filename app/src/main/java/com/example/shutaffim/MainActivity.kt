package com.example.shutaffim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.ForumVM
import com.example.shutaffim.ViewModel.PostsVM
import com.example.shutaffim.ui.theme.ShutaffImTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val postsVM = PostsVM()
            val authViewModel = AuthViewModel()
            val forumVM = ForumVM(authViewModel)
            ShutaffImTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    AppNavigator(postsVM = postsVM,authViewModel = authViewModel,forumVM=forumVM)
                }
            }
        }
    }
}