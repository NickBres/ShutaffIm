package com.example.shutaffim


import EditPost
import NewPost
import PostsSearch
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shutaffim.Model.Screen.DisplayPost
import com.example.shutaffim.Model.Screen.Register
import com.example.shutaffim.View.Forum
import com.example.shutaffim.View.LoginScreen
import com.example.shutaffim.View.Profile
import com.example.shutaffim.View.TopicView
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.ForumVM
import com.example.shutaffim.ViewModel.PostsVM


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigator(postsVM: PostsVM, authViewModel: AuthViewModel, forumVM: ForumVM) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val rememberMePreference = authViewModel.getRememberMePreference(context)
    val isUserLoggedIn = authViewModel.currentUser != null
    val startDestination =
        if (rememberMePreference && isUserLoggedIn) Screen.TypeScreen.route else Screen.LoginScreen.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.RegisterScreen.route) {
            Register(navController, authViewModel)
        }
        composable(Screen.TypeScreen.route) {
            MenuScreen(navController, authViewModel)
        }
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(navController, authViewModel)
        }
        composable(Screen.PostsSearchScreen.route) {
            PostsSearch(navController, postsVM, authViewModel)
        }
        composable(Screen.PostScreen.route) {
            DisplayPost(navController, postsVM, authViewModel)
        }
        composable(Screen.MyPostsScreen.route) {
            MyPosts(navController, postsVM, authViewModel)
        }
        composable(Screen.EditPostScreen.route) {
            EditPost(navController, postsVM, authViewModel)
        }
        composable(Screen.ProfileScreen.route) {
            Profile(navController, authViewModel)
        }

        composable(Screen.NewPostScreen.route) {
            NewPost(navController, postsVM, authViewModel)
        }
        composable(Screen.ForumScreen.route) {
            Forum(navController,forumVM)
        }
        composable(Screen.TopicScreen.route) {
            TopicView(navController, forumVM, authViewModel)

        }

    }
}