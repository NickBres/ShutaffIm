package com.example.shutaffim.Model

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

/**
 * This class is responsible for all the data operations related to the posts in Firestore.
 * @param firestore an instance of FirebaseFirestore to perform the operations on
 */
class PostsRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    /**
     * Function creates a new post in Firestore.
     * @param post the post object to create
     * @return a Result<Boolean> object indicating whether the operation was successful or not
     */
    suspend fun createPost(post: Post): Result<Post> = try {
        val documentReference = firestore.collection("posts").add(post).await()
        val postId = documentReference.id
        documentReference.update("id", postId).await()
        Result.Success(post.copy(id = postId))
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
                val picturesList = document.get("pictures") as? List<HashMap<String, Any>>
                val pictures = picturesList?.map { hashMapToPicture(it) } ?: listOf()
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
                    userId = document.getString("userId") ?: "",
                    pictures = pictures
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

    fun hashMapToPicture(hashMap: HashMap<String, Any>): Picture {
        val pictureUrl = hashMap["pictureUrl"] as? String ?: ""
        val pictureName = hashMap["pictureName"] as? String ?: ""
        // Add other fields if Picture has more
        return Picture(pictureUrl, pictureName)
    }

    suspend fun updatePost(
        post: Post,
        newImages: List<Bitmap>,
        imagesToDelete: List<String>
    ): Result<Boolean> = try {
        for (image in imagesToDelete) {
            removeImageFromPost(post.id, image)
        }
        firestore.collection("posts").document(post.id).set(post).await()
        if (newImages.isNotEmpty()) {
            uploadPostsImages(post.id, newImages)
        }
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun deletePost(postid: String): Result<Boolean> = try {
        deleteImagesFromPost(postid)
        firestore.collection("posts").document(postid).delete().await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun deleteImagesFromPost(postId: String): Result<Boolean> = try {
        val listAllTask = storage.reference.child("postsImages/$postId").listAll().await()
        for (item in listAllTask.items) {
            item.delete().await()
        }
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }


    suspend fun getPost(postId: String): Result<Post> = try {
        val documentSnapshot = firestore.collection("posts").document(postId).get().await()
        val picturesList = documentSnapshot.get("pictures") as? List<HashMap<String, Any>>
        val pictures = picturesList?.map { hashMapToPicture(it) } ?: listOf()
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
            userId = documentSnapshot.getString("userId") ?: "",
            pictures = pictures
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

    suspend fun getInterestedInPost(postId: String): Result<List<Request>> =
        try {
            val querySnapshot = firestore.collection("posts").document(postId)
                .collection("interested")
                .get()
                .await()

            val users = querySnapshot.documents.mapNotNull { document ->
                try {
                    //println("1: users data: ${document.data}")
                    Request(
                        postId = document.getString("postId") ?: "",
                        userId = document.getString("userId") ?: "",
                        date = document.getLong("date") ?: 0L,
                        isApproved = document.getBoolean("isApproved") ?: false,
                        message = document.getString("message") ?: ""
                    ).also { user ->
                        //println("2: Mapped to Request: $user")
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

    suspend fun uploadPostsImages(postId: String, bitmaps: List<Bitmap>): Result<Boolean> =
        try {
            val postRef = firestore.collection("posts").document(postId)
            val currPictures = postRef.get().await().get("pictures") as? List<HashMap<String, Any>>
            val pictures =
                currPictures?.map { hashMapToPicture(it) }?.toMutableList() ?: mutableListOf()
            for (bitmap in bitmaps) {
                val imageRef = storage.reference.child("postsImages/$postId/${bitmap}")
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                val imageData = baos.toByteArray()

                val uploadTask = imageRef.putBytes(imageData).await()

                val downloadUrl = uploadTask.storage.downloadUrl.await()

                pictures.add(
                    Picture(
                        pictureUrl = downloadUrl.toString(),
                        pictureName = bitmap.toString()
                    )
                )
            }
            postRef.update("pictures", pictures).await()
            println("*images uploaded successfully")
            Result.Success(true)

        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun removeImageFromPost(postId: String, pictureName: String): Result<Boolean> =
        try {
            if (pictureName != "") {
                val imageRef = storage.reference.child("postsImages/$postId/${pictureName}")
                imageRef.delete().await()
            }
            println("*image removed successfully")
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    suspend fun removeInterestedCollection(postId: String): Result<Boolean>
    {
        return try {
            val collection = firestore.collection("posts").document(postId).collection("interested").get().await()
            for (document in collection.documents) {
                document.reference.delete().await()
            }
            //remove the collection
            firestore.collection("posts").document(postId).collection("interested").document("interested").delete().await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    }

}