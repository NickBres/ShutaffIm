package com.example.shutaffim.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class UserRepo(private val auth: FirebaseAuth,
               private val firestore: FirebaseFirestore)
{
    suspend fun signUp(user: User, password: String)
        :Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(user.email, password).await()
            //save user to firestore
            firestore.collection("users").document(user.email).set(user).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun login(email: String, password: String): Result<Boolean> =
         try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun update(user: User): Result<Boolean> =
         try {
            firestore.collection("users").document(user.email).set(user).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun delete(user: User): Result<Boolean> =
         try {
            firestore.collection("users").document(user.email).delete().await()
            Result.Success(true)
        } catch (e: Exception) {

            Result.Error(e)
        }

    suspend fun logout(): Result<Boolean> =
        try {
            auth.signOut()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }


    suspend fun getUser(email: String): Result<User> = try {
        val userDocument = firestore.collection("users").document(email).get().await()
        val user = userDocument.toObject(User::class.java)
        if (user != null) {
            Result.Success(user)
        } else {
            Result.Error(Exception("User data not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
    //get user posts list
    suspend fun getUserPost(email: String): Result <List<UserPost>> = try {
        val userDocument = firestore.collection("users").document(email).collection("posts").get().await()
        val postList = userDocument.documents.mapNotNull { it.toObject(UserPost::class.java) }
        if (postList != null) {
            Result.Success(postList)
        } else {
            Result.Error(Exception("User data not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
    suspend fun setUserPost(email: String, post: UserPost): Result<Boolean> {
        return try {
            firestore.collection("users").document(email).collection("posts").document(post.PostId).set(post).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    suspend fun getUserDataById(email: String): Result<User> = try {

        val userDocument = firestore.collection("users").document(email).get().await()
        if(userDocument.exists()){
            val user = User(
                email = userDocument.getString("email") ?: "",
                fName = userDocument.getString("fname") ?: "",
                lName = userDocument.getString("lname") ?: "",
                about = userDocument.getString("about") ?: "",
                picture = userDocument.getString("picture") ?: "",
                type = userDocument.getString("type") ?: "",
                age = userDocument.getString("age")?.toInt() ?: 0,
                sex = userDocument.getString("sex") ?: ""

            )
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        }
        else{
            Result.Error(Exception("User data not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()
            if (userDocument.exists()) {
                val user = User(
                    email = userDocument.getString("email") ?: "",
                    fName = userDocument.getString("fname") ?: "",
                    lName = userDocument.getString("lname") ?: "",
                    about = userDocument.getString("about") ?: "",
                    picture = userDocument.getString("picture") ?: "",
                    type = userDocument.getString("type") ?: "",
                    age = userDocument.getString("age")?.toInt() ?: 0,
                    sex = userDocument.getString("sex") ?: ""
                )
                Log.d("user2", "$uid")
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

}