package com.example.shutaffim


import EditPost
import PostsSearch
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shutaffim.Model.Screen.DisplayPost
import com.example.shutaffim.View.Profile
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.PostsVM

@Composable
fun AppNavigator(postsVM: PostsVM,authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(navController, authViewModel)
        }
        composable(Screen.TypeScreen.route) {
            MenuScreen(navController, authViewModel)
        }
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(navController)
        }
        composable(Screen.PostsSearchScreen.route) {
            PostsSearch(navController, postsVM)
        }
        composable(Screen.PostScreen.route) {
            DisplayPost(navController)
        }
        composable(Screen.MyPostsScreen.route) {
            MyPosts(navController, postsVM)
        }
        composable(Screen.EditPostScreen.route) {
            EditPost(navController)
        }
        composable(Screen.InterestedScreen.route) {
            Intersted(navController)
        }
        composable(Screen.ProfileScreen.route) {
            Profile(navController)
        }
    }
}