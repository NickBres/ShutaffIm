package com.example.shutaffim


import EditPost
import NewPost
import PostsSearch
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shutaffim.Model.Screen.DisplayPost
import com.example.shutaffim.View.Forum
import com.example.shutaffim.View.LoginScreen
import com.example.shutaffim.View.Profile
import com.example.shutaffim.View.RegisterScreen
import com.example.shutaffim.View.TopicView
import com.example.shutaffim.ViewModel.AuthViewModel
import com.example.shutaffim.ViewModel.PostsVM


@Composable
fun AppNavigator(postsVM: PostsVM,authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.TopicScreen.route) {
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
        composable(Screen.InterestedScreen.route) {
            Interested(navController, postsVM, authViewModel)
        }
        composable(Screen.ProfileScreen.route) {
            Profile(navController, authViewModel)
        }

        composable(Screen.NewPostScreen.route) {
            NewPost(navController, postsVM, authViewModel)
                    }
        composable(Screen.ForumScreen.route) {
            Forum(navController)
        }
        composable(Screen.TopicScreen.route) {
            TopicView(navController)

        }
    }
}