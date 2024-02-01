package com.example.shutaffim

import EditSecreenView
import PostsSearchView
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shutaffim.Data.Screen.PostScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen(navController)
        }
        composable(Screen.TypeScreen.route) {
            TypeScreen(navController)
        }
        composable(Screen.EditProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(Screen.PostsSearchScreen.route) {
            PostsSearchView(navController)
        }
        composable(Screen.PostScreen.route) {
            PostScreen(navController)
        }
        composable(Screen.MyPostsScreen.route) {
            MyPostsScreenView(navController)
        }
        composable(Screen.EditPostScreen.route) {
            EditSecreenView(navController)
        }
        composable(Screen.InterestedScreen.route) {
            MyInterstedScreenView(navController)
        }
    }
}