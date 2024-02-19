package com.example.shutaffim.Model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ForumRepo(private val firestore: FirebaseFirestore)
{
    suspend fun createTopic(topic: Topic): Result<Boolean> = try {
        val documentReference = firestore.collection("topics").add(topic).await()
        val topicId = documentReference.id
        documentReference.update("id", topicId).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getTopics(): Result<List<Topic>> = try {
        val querySnapshot = firestore.collection("topics")
            .get()
            .await()
        // Print out the documents before converting them to title objects
        val topics = querySnapshot.documents.mapNotNull { document ->
            try {
                println("Document data: ${document.data}")
                // Manually create a Post object from the document
                Topic(
                    id = document.id, // Assuming 'id' is always present and is the document ID
                    title = document.getString("title") ?: "",
                    description = document.getString("description") ?: "",
                    email = document.getString("email") ?: "",
                    date = document.getLong("date") ?: 0L,
                    userName = document.getString("userName") ?: ""
                ).also { topic ->
                    println("Mapped to topic: $topic")
                }
            } catch (e: Exception) {
                println("Exception while manually mapping document to Topic: $e")
                null
            }
        }
        Result.Success(topics)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun deleteTopic(topicId: String): Result<Boolean> = try {
        firestore.collection("topics").document(topicId).collection("comments").get()
            .await().documents.forEach {
            firestore.collection("topics").document(topicId).collection("comments").document(it.id)
                .delete().await()
        }
        firestore.collection("topics").document(topicId).delete().await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getTopic(topicId: String): Result<Topic> = try {
        val documentSnapshot = firestore.collection("topics").document(topicId).get().await()
        val topic = Topic(
            id = documentSnapshot.id, // Assuming 'id' is always present and is the document ID
            title = documentSnapshot.getString("title") ?: "",
            description = documentSnapshot.getString("description") ?: "",
            email = documentSnapshot.getString("email") ?: "",
            date = documentSnapshot.getLong("date") ?: 0L,
            userName = ""
        )
        if (topic != null) {
            Result.Success(topic)
        } else {
            Result.Error(Exception("Topic not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun updateTopic(topic: Topic): Result<Boolean> = try {
        firestore.collection("topics").document(topic.id).set(topic).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }


    suspend fun addCommentToTopic(topicId: String,comment: Comment): Result<Boolean>? =
        try {
            val documentReference =  firestore.collection("topics").document(topicId)
                .collection("comments")
                .add(comment).await()
            documentReference.update("id", documentReference.id).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun removeCommentFromTopic(topicId: String,commentId :String): Result<Boolean>? =
        try {
            firestore.collection("topics").document(topicId)
                .collection("comments")
                .document(commentId)
                .delete()
                .await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun getComments(topicId: String): Result<List<Comment>>? =
        try {
            val querySnapshot = firestore.collection("topics").document(topicId)
                .collection("comments")
                .get()
                .await()
            val comments = querySnapshot.documents.mapNotNull { document ->
                try {
                    println("1: comments data: ${document.data}")
                    Comment(
                        id = document.id, // Assuming 'id' is always present and is the document ID
                        text = document.getString("text") ?: "",
                        email = document.getString("email") ?: "",
                        date = document.getLong("date") ?: 0L,
                        userName = ""
                    ).also { comment ->
                        println("2: Mapped to Comment: $comment")
                    }

                } catch (e: Exception) {
                    println("Exception while manually mapping document to comment: $e")
                    null
                }
            }
            Result.Success(comments)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun updateComment(topicId: String,comment: Comment): Result<Boolean> = try {
        firestore.collection("topics").document(topicId)
            .collection("comments").document(comment.id).set(comment).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

}