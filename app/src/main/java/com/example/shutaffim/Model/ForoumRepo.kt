package com.example.shutaffim.Model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ForoumRepo(private val firestore: FirebaseFirestore)
{
    suspend fun createTitle(foroum: Foroum): Result<Boolean> = try {
        val documentReference = firestore.collection("foroum").add(foroum).await()
        val titleId = documentReference.id
//        val titleId
        documentReference.update("id", titleId).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getTitles(): Result<List<Foroum>> = try {
        val querySnapshot = firestore.collection("foroum")
            .get()
            .await()
        // Print out the documents before converting them to title objects
        val titles = querySnapshot.documents.mapNotNull { document ->
            try {
                println("Document data: ${document.data}")
                // Manually create a Post object from the document
                Foroum(
                    id = document.id, // Assuming 'id' is always present and is the document ID
                    title = document.getString("title") ?: "",
                    description = document.getString("description") ?: "",
                    email = document.getString("email") ?: "",
                    date = document.getLong("date") ?: 0L,
                    userName = document.getString("userName") ?: ""
                ).also { title ->
                    println("Mapped to title: $title")
                }
            } catch (e: Exception) {
                println("Exception while manually mapping document to Title: $e")
                null
            }
        }
        Result.Success(titles)
    } catch (e: Exception) {
        Result.Error(e)
    }
//
//    suspend fun updateTitle(foroum: Foroum): Result<Boolean> = try {
//        firestore.collection("foroum").document(foroum.id).set(foroum).await()
//        Result.Success(true)
//    } catch (e: Exception) {
//        Result.Error(e)
//    }

    suspend fun deleteTitle(titleId: String): Result<Boolean> = try {
        firestore.collection("foroum").document(titleId).delete().await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getTitle(titleId: String): Result<Foroum> = try {
        val documentSnapshot = firestore.collection("foroum").document(titleId).get().await()
        val foroum = Foroum(
            id = documentSnapshot.id, // Assuming 'id' is always present and is the document ID
            title = documentSnapshot.getString("title") ?: "",
            description = documentSnapshot.getString("description") ?: "",
            email = documentSnapshot.getString("email") ?: "",
            date = documentSnapshot.getLong("date") ?: 0L,
            userName = documentSnapshot.getString("userName") ?: ""
        )
        if (foroum != null) {
            Result.Success(foroum)
        } else {
            Result.Error(Exception("Title not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun addCommentToTitle(titleId: String,foroum: Foroum): Result<Boolean>? =
        try {
            val newTitle = Foroum(
                id = foroum.id,
                title = foroum.title,
                description = foroum.description,
                email = foroum.email,
                date = foroum.date,
                userName = foroum.userName

            )
            firestore.collection("foroum").document(titleId)
                .collection("Comments")
                .document(newTitle.id)
                .set(newTitle)
                .await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun removeCommentFromTitle(titleId: String,commentId :String): Result<Boolean>? =
        try {
            firestore.collection("foroum").document(titleId)
                .collection("Comments")
                .document(commentId)
                .delete()
                .await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getComments(titleId: String): Result<List<Foroum>>? =
        try {
            val querySnapshot = firestore.collection("foroum").document(titleId)
                .collection("Comments")
                .get()
                .await()

            val commets = querySnapshot.documents.mapNotNull { document ->
                try {
                    println("1: comments data: ${document.data}")
                    Foroum(
                        id = document.id, // Assuming 'id' is always present and is the document ID
                        title = document.getString("title") ?: "",
                        description = document.getString("description") ?: "",
                        email = document.getString("email") ?: "",
                        date = document.getLong("date") ?: 0L,
                        userName = document.getString("userName") ?: ""
                    ).also { comment1 ->
                        println("2: Mapped to Title: $comment1")
                    }

                } catch (e: Exception) {
                    println("Exception while manually mapping document to Request: $e")
                    null
                }
            }
            Result.Success(commets)
        } catch (e: Exception) {

            Result.Error(e)
        }


}