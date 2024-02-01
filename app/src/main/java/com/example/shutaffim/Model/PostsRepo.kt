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
        val querySnapshot = firestore.collection("posts").get().await()
        val posts = querySnapshot.documents.mapNotNull { it.toObject(Post::class.java) }
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

    suspend fun deletePost(post: Post): Result<Boolean> = try {
        firestore.collection("posts").document(post.id).delete().await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getPost(postId: String): Result<Post> = try {
        val documentSnapshot = firestore.collection("posts").document(postId).get().await()
        val post = documentSnapshot.toObject(Post::class.java)
        if (post != null) {
            Result.Success(post)
        } else {
            Result.Error(Exception("Post not found"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getInterestedInPost(postId: String): Result<List<InterestedInPost>> = try {
        val querySnapshot =
            firestore.collection("posts").document(postId).collection("interested").get().await()
        val interestedInPost =
            querySnapshot.documents.mapNotNull { it.toObject(InterestedInPost::class.java) }
        Result.Success(interestedInPost)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun addInterestedInPost(interestedInPost: InterestedInPost): Result<Boolean> = try {
        firestore.collection("posts").document(interestedInPost.postId).collection("interested")
            .add(interestedInPost).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun deleteInterestedInPost(interestedInPost: InterestedInPost): Result<Boolean> = try {
        firestore.collection("posts").document(interestedInPost.postId).collection("interested")
            .document(interestedInPost.userEmail).delete().await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun updateInterestedInPost(interestedInPost: InterestedInPost): Result<Boolean> = try {
        firestore.collection("posts").document(interestedInPost.postId).collection("interested")
            .document(interestedInPost.userEmail).set(interestedInPost).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

}