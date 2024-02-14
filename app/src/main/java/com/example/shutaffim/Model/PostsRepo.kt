package com.example.shutaffim.Model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * This class is responsible for all the data operations related to the posts in Firestore.
 * @param firestore an instance of FirebaseFirestore to perform the operations on
 */
class PostsRepository(
    private val firestore: FirebaseFirestore
) {
    /**
     * Function creates a new post in Firestore.
     * @param post the post object to create
     * @return a Result<Boolean> object indicating whether the operation was successful or not
     */
    suspend fun createPost(post: Post): Result<Boolean> = try {
        val documentReference = firestore.collection("posts").add(post).await()
        val postId = documentReference.id
        documentReference.update("id", postId).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getPosts(): Result<List<Post>> = try {
        val querySnapshot = firestore.collection("posts")
            .get()
            .await()
        // Print out the documents before converting them to Post objects
        val posts = querySnapshot.documents.mapNotNull { document ->
            try {
                println("Document data: ${document.data}")
                // Manually create a Post object from the document
                Post(
                    id = document.id, // Assuming 'id' is always present and is the document ID
                    date = document.getLong("date") ?: 0L,
                    city = document.getString("city") ?: "",
                    street = document.getString("street") ?: "",
                    house_num = document.getLong("house_num")?.toInt() ?: 0,
                    curr_roommates = document.getLong("curr_roommates")?.toInt() ?: 0,
                    max_roommates = document.getLong("max_roommates")?.toInt() ?: 0,
                    price = document.getLong("price")?.toInt() ?: 0,
                    tags = document.get("tags") as List<String>? ?: listOf(),
                    about = document.getString("about") ?: "",
                    pic1 = document.getString("pic1") ?: "",
                    pic2 = document.getString("pic2") ?: "",
                    pic3 = document.getString("pic3") ?: "",
                    pic4 = document.getString("pic4") ?: "",
                    userId = document.getString("userId") ?: ""
                ).also { post ->
                    println("Mapped to Post: $post")
                }
            } catch (e: Exception) {
                println("Exception while manually mapping document to Post: $e")
                null
            }
        }
        Result.Success(posts)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun updatePost(post: Post): Result<Boolean> = try {
        firestore.collection("posts").document(post.id).set(post).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun deletePost(postid: String): Result<Boolean> = try {
        firestore.collection("posts").document(postid).delete().await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }


    suspend fun getPost(postId: String): Result<Post> = try {
        val documentSnapshot = firestore.collection("posts").document(postId).get().await()
        val post = Post(
            id = documentSnapshot.id,
            date = documentSnapshot.getLong("date") ?: 0L,
            city = documentSnapshot.getString("city") ?: "",
            street = documentSnapshot.getString("street") ?: "",
            house_num = documentSnapshot.getLong("house_num")?.toInt() ?: 0,
            curr_roommates = documentSnapshot.getLong("curr_roommates")?.toInt() ?: 0,
            max_roommates = documentSnapshot.getLong("max_roommates")?.toInt() ?: 0,
            price = documentSnapshot.getLong("price")?.toInt() ?: 0,
            tags = documentSnapshot.get("tags") as List<String>? ?: listOf(),
            about = documentSnapshot.getString("about") ?: "",
            pic1 = documentSnapshot.getString("pic1") ?: "",
            pic2 = documentSnapshot.getString("pic2") ?: "",
            pic3 = documentSnapshot.getString("pic3") ?: "",
            pic4 = documentSnapshot.getString("pic4") ?: "",
            userId = documentSnapshot.getString("userId") ?: ""
        )
        if (post != null) {
            Result.Success(post)
        } else {
            Result.Error(Exception("Post not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }


    suspend fun addInterestedToPost(request: Request): Result<Boolean>? =
        try {
            val newRequest = Request(
                postId = request.postId,
                userId = request.userId,
                date = request.date,
                isApproved = request.isApproved,
                message = request.message
            )
            firestore.collection("posts").document(request.postId)
                .collection("interested")
                .document(request.userId)
                .set(newRequest)
                .await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun removeInterestedFromPost(request: Request): Result<Boolean>? =
        try {
            firestore.collection("posts").document(request.postId)
                .collection("interested")
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
                .collection("interested")
                .get()
                .await()

            val users = querySnapshot.documents.mapNotNull { document ->
                try {
                    println("1: users data: ${document.data}")
                    Request(
                        postId = document.getString("postId") ?: "",
                        userId = document.getString("userId") ?: "",
                        date = document.getLong("date") ?: 0L,
                        isApproved = document.getBoolean("isApproved") ?: false,
                        message = document.getString("message") ?: ""
                    ).also { user ->
                        println("2: Mapped to Request: $user")
                    }

                } catch (e: Exception) {
                    println("Exception while manually mapping document to Request: $e")
                    null
                }
            }
            Result.Success(users)
        } catch (e: Exception) {

            Result.Error(e)
        }
    suspend fun updateIsApproved(postId: String, userId: String, isApproved: Boolean): Result<Boolean>? =
        try {
            firestore.collection("posts").document(postId)
                .collection("interested")
                .document(userId)
                .update("isApproved", isApproved)
                .await()

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

}