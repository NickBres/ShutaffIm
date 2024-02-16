package com.example.shutaffim.Model


import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream



class UserRepo(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) {
    suspend fun signUp(user: User, password: String)
            : Result<Boolean> =
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
    suspend fun getUserPost(email: String): Result<List<UserPost>> = try {
        val userDocument =
            firestore.collection("users").document(email).collection("posts").get().await()
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
            firestore.collection("users").document(email).collection("posts").document(post.PostId)
                .set(post).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getUserDataById(email: String): Result<User> = try {

        val userDocument = firestore.collection("users").document(email).get().await()
        if (userDocument.exists()) {
            val user = User(
                email = userDocument.getString("email") ?: "",
                fName = userDocument.getString("fname") ?: "",
                lName = userDocument.getString("lname") ?: "",
                about = userDocument.getString("about") ?: "",
                pictureName = userDocument.getString("pictureName") ?: "",
                pictureUrl = userDocument.getString("pictureUrl") ?: "",
                type = userDocument.getString("type") ?: ""
            )
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
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
                    pictureName = userDocument.getString("pictureName") ?: "",
                    pictureUrl = userDocument.getString("pictureUrl") ?: "",
                    type = userDocument.getString("type") ?: ""
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

    // ----------------- image management -----------------
    suspend fun uploadImageToFirebase(bitmap: Bitmap): Result<Boolean> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val imageRef = storage.reference.child("images/$uid/${bitmap}")
            val baos = ByteArrayOutputStream()//byte array output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()
            imageRef.putBytes(imageData).await()
            Result.Success(true)
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun removeImageFromUser(): Result<Boolean> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val imageRef = storage.reference.child("images/$uid")
            imageRef.delete().await()
            Result.Success(true)
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun uploadImageToFirebase(bitmap: Bitmap, userId: String): Result<Boolean> =
        try {
            val userRef = firestore.collection("users").document(userId)
            val imageRef = storage.reference.child("images/$userId/Profile/${bitmap}")
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val imageData = baos.toByteArray()

            // Upload the image to Firebase Storage
            val uploadTask = imageRef.putBytes(imageData)

            // Wait for the upload to complete
            val taskSnapshot = uploadTask.await()

            // Get the download URL of the uploaded image
            val downloadUrl = taskSnapshot.storage.downloadUrl.await()
            println("** Image uploaded : $downloadUrl")
            // Update the user's profile picture URL in Firestore
            userRef.update("pictureUrl", downloadUrl.toString()).await()
            println("*** Image uploaded : $downloadUrl")
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "Error: ${e.message}", e)
            Result.Error(e)
        }



    suspend fun removeProfileImageFromUser(bitmap: String, userId: String): Result<Boolean> = try {
        if (bitmap != "") {
            val imageRef = storage.reference.child("images/$userId/Profile/${bitmap}")
            imageRef.delete().await()
        }
        Result.Success(true)

    } catch (e: Exception) {
        Result.Error(e)
    }


    suspend fun getUserProfileImage(bitmap: String): Result<Uri> {//todo: fix this
        var attempt = 0
        val maxAttempts = 1
        while (attempt < maxAttempts) {
            try {
                val uid = auth.currentUser?.email
                println("5. User id: $uid")
                if (uid != null) {
                    val ImageBitmap =
                        storage.reference.child("images/$uid/Profile/${bitmap}").downloadUrl.await()
                    if (ImageBitmap != null) {
                        println("2. Image found: $ImageBitmap")
                        return Result.Success(ImageBitmap)
                    } else {
                        throw Exception("Image not found")
                    }
                } else {
                    throw Exception("User not authenticated")
                }
            } catch (e: Exception) {
                println("Exception: -- $e")
                attempt++
                if (attempt < maxAttempts) {
                    delay(700) // Wait for ---- before the next attempt
                } else {
                    return Result.Error(e)
                }
            }
        }
        return Result.Error(Exception("Max attempts reached"))
    }

    //////------------------- upload file to firebase -------------------

}