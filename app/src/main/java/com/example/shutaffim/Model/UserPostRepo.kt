package com.example.shutaffim.Model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserPostRepo(private val firestore: FirebaseFirestore,
                   private val auth: FirebaseAuth)
{
   suspend fun addInterestedToPost(request: Request): Result<Boolean>? =
       try {
           firestore.collection("posts").document(request.postId)
               .collection("intrerested")
               .document(request.userId)
               .set(request)
               .await()

           Result.Success(true)
       } catch (e: Exception) {
           Result.Error(e)
       }

    suspend fun removeInterestedFromPost(request: Request): Result<Boolean>? =
        try {
            firestore.collection("posts").document(request.postId)
                .collection("intrerested")
                .document(request.userId)
                .delete()
                .await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getInterestedInPost(postId: String): Result<List<Request>>? =
        try {
            val querySnapshot = firestore.collection("posts").document(postId)
                .collection("intrerested")
                .get()
                .await()
            val requests = querySnapshot.documents.mapNotNull { document ->
                try {
                    document.toObject(Request::class.java)
                } catch (e: Exception) {
                    null
                }
            }
            Result.Success(requests)
        } catch (e: Exception) {
            Result.Error(e)
        }

}