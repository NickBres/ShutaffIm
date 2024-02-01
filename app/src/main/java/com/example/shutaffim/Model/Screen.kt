package com.example.shutaffim

sealed class Screen(val route: String) {
    object LoginScreen : Screen("LogScr")
    object RegisterScreen : Screen("RegScr")
    object TypeScreen : Screen("TypeScr")
    object EditProfileScreen : Screen("EditProfileScr")
    object PostsSearchScreen : Screen("PostsSrchScr")
    object PostScreen : Screen("PostScr")
    object MyPostsScreen : Screen("MyPostsScr")
    object EditPostScreen : Screen("EditPostScr")
    object InterestedScreen : Screen("InterestedScr")
    object ProfileScreen : Screen("ProfileScr")

}