package com.example.shutaffim.ViewModel

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Filter
import com.example.shutaffim.Model.Post
import com.example.shutaffim.Model.PostsRepository
import com.example.shutaffim.Model.Request
import com.example.shutaffim.Model.Result
import com.example.shutaffim.Screen
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PostsVM : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: MutableLiveData<List<Post>> get() = _posts
/////////////////////////////////////////////
    private val _postsUser = MutableLiveData<List<Post>>()
    val postsUser: MutableLiveData<List<Post>> get() = _postsUser
////////////////////////////////////////

    private var _filter = MutableLiveData<Filter>()
    var filter: MutableLiveData<Filter>
        get() = _filter
        set(value) {
            _filter.value = value.value
        }


    private val postsRepo: PostsRepository

    private val _currPost = MutableLiveData<Post>()
    val currPost: MutableLiveData<Post> get() = _currPost
//-------------loading variable ----------------

    var isLoading = mutableStateOf(false)


    init {
        postsRepo = PostsRepository(
            firestore = Injection.firestoreInstance(),
            storage = Injection.storageInstance()
        )
        viewModelScope.launch {
            loadPosts()
        }
    }

    suspend fun loadPosts(): Result<List<Post>> {
        // CompletableDeferred to hold the result of the async operation
        val resultDeferred = CompletableDeferred<Result<List<Post>>>()

        viewModelScope.launch {
            val result = postsRepo.getPosts() // This is a suspend function call to your repository
            when (result) {
                is Result.Success -> {
                    _posts.value = result.data!! // Update LiveData on the main thread
                }

                is Result.Error -> {
                    // Handle error if needed
                    println("Error occurred while loading posts")
                }
            }
            resultDeferred.complete(result) // Complete the deferred with the result
        }

        return resultDeferred.await() // Wait and return the result
    }


    fun loadPost(postId: String) {
        viewModelScope.launch {
            when (val result = postsRepo.getPost(postId)) {
                is Result.Success -> {
                    _currPost.value = result.data!!

                }

                else -> {
                    // handle error
                    println("Error occurred while loading post")
                }
            }
        }
    }

    fun resetPost() {
        _currPost.value = Post(
            id = "",
            date = 0L,
            city = "",
            street = "",
            house_num = 0,
            curr_roommates = 0,
            max_roommates = 0,
            price = 0,
            tags = listOf(),
            about = "",
            pictures = listOf(),
            userId = ""
        )
    }

    fun applyFilter() {
        viewModelScope.launch {
            val filter = _filter.value ?: Filter()
            filterPosts(filter)
            _filter.value = Filter()
        }
    }

    fun filterPosts(filter: Filter) {
        // Convert filter tags string to list and trim each tag
        val listTags =
            tagsToList(filter.tags).filter { it.isNotEmpty() } // Ensure empty strings are not considered as valid tags

        viewModelScope.launch {
            val updatedPosts = _posts.value?.filter { post ->
                val matchesId =
                    filter.id.isBlank() || post.id.contains(filter.id, ignoreCase = true)
                val matchesCity =
                    filter.city.isBlank() || post.city.contains(filter.city, ignoreCase = true)
                val matchesStreet = filter.street.isBlank() || post.street.contains(
                    filter.street,
                    ignoreCase = true
                )
                val matchesPrice = (filter.minPrice <= post.price && post.price <= filter.maxPrice)

                val matchesTags =
                    listTags.isEmpty() || post.tags.any { it.toLowerCase() in listTags }

                val matchesEmail =
                    filter.email.isBlank() || post.userId == filter.email

                matchesId && matchesCity && matchesStreet && matchesPrice && matchesTags && matchesEmail
            }

            _posts.value = updatedPosts ?: listOf()
        }
    }




    fun tagsToList(tags: String): List<String> {
        return tags.split(",").map { it.trim() }
    }


    fun createNewPost(post: Post, bitmaps: List<Bitmap>, navController: NavController) {
        isLoading.value = true
        post.date = System.currentTimeMillis()
        viewModelScope.launch {
            when (val result = postsRepo.createPost(post)) {
                is Result.Success -> {
                  val res1 =  uploadPostImages(result.data.id, bitmaps).await()
                    if(res1){
                        isLoading.value = false
                        navController.navigateUp()
                    }
                    else{
                        isLoading.value = false
                        println("Error occurred while creating post")
                    }

                }

                else -> {
                    isLoading.value = false
                    println("Error occurred while creating post")
                }
            }
        }
    }

    private fun uploadPostImages(postId: String, bitmaps: List<Bitmap>) : Deferred<Boolean> {
        return viewModelScope.async {
            when (val result = postsRepo.getPost(postId)) {
                is Result.Success -> {
                    _currPost.value = result.data.copy()
                    val uploadPostsResult = viewModelScope.async {
                        postsRepo.uploadPostsImages(postId, bitmaps)
                    }.await()

                    if (uploadPostsResult is Result.Success) {
                        println("Post images uploaded successfully")
                        loadPosts()
                       true
                    } else {
                        println("Error occurred while uploading post images")
                        false
                    }

                }

                else -> {
                    println("Error occurred while getting post")
                    false
                }
            }
        }
    }


    fun updatePost(post: Post, newImages: List<Bitmap>, imagesToDelete: List<String>, navController: NavController) {
        isLoading.value = true
        viewModelScope.launch {
            when (val result = postsRepo.updatePost(post, newImages, imagesToDelete)) {

                is Result.Success -> {

                    println("1. Post updated successfully")

                    // Load post here
                    when (val loadResult = postsRepo.getPost(post.id)) {
                        is Result.Success -> {
                            _currPost.value = loadResult.data!!
                            println("1. Post loaded successfully")
                            isLoading.value = false
                            navController.navigate(Screen.MyPostsScreen.route)
                        }
                        else -> {
                            isLoading.value = false
                            println("q. Error occurred while loading post")
                        }
                    }
                }
                else -> {
                    isLoading.value = false
                    println("1. Error occurred while updating post")
                }
            }
        }
    }

    fun deletePost(postId: String,navController: NavController) {
        viewModelScope.launch {
            when (val result = postsRepo.deletePost(postId)) {
                is Result.Success -> {
                   removeInterestedCollection(postId)
                    Toast.makeText(
                        navController.context,
                        "Post deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Load posts here
                    val loadPostsResult = postsRepo.getPosts()
                    when (loadPostsResult) {
                        is Result.Success -> {
                            _posts.value = loadPostsResult.data!!
                            navController.navigate(Screen.MyPostsScreen.route)
                            println("2. Post deleted and posts loaded successfully")
                        }
                        is Result.Error -> {
                            println("2. Error occurred while loading posts")
                        }
                    }
                    println("2. Post deleted successfully")
                    /* TODO: Filter to my posts */
                }

                else -> {
                    Toast.makeText(
                        navController.context,
                        "Error occurred while deleting post",
                        Toast.LENGTH_SHORT
                    ).show()
                    println("2. Error occurred while deleting post")
                }
            }
        }
    }



    private  val _interestedInPost = MutableLiveData<List<Request>>()
    val interestedInPost: LiveData<List<Request>> get() = _interestedInPost

    //for id strings
    //private  val _interestedInPostId = mutableListOf<String>()
   // val interestedInPostId: MutableList<String> get() = _interestedInPostId

    private val _upResult = MutableLiveData<Result<Boolean>>()



    fun addInterestedToPost(userId: String, postId: String,msg: String) {
        val date = System.currentTimeMillis()
        val request = Request(userId, postId, date, msg)
        viewModelScope.launch {
            _upResult.value = postsRepo.addInterestedToPost(request)
            if(_upResult.value is Result.Success){
                println("Request of %s added to post".format(userId))
                getInterestedInPost(postId)
            }
            else if(_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }

    fun removeInterestedFromPost(userId: String, postId: String) {
        val request = Request(userId, postId)
        viewModelScope.launch {
            _upResult.value = postsRepo.removeInterestedFromPost(request)
            if(_upResult.value is Result.Success){
                //deleteInterestedIdFromList(userId)
                println("Request of %s removed from post".format(userId))
                getInterestedInPost(postId)
            } else if (_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }

    suspend fun getInterestedInPost(postId: String): Result<List<Request>> {
        val resultDeferred = CompletableDeferred<Result<List<Request>>>()
        viewModelScope.launch {
            val result = postsRepo.getInterestedInPost(postId)
            when (result) {
                is Result.Success -> {
                    _interestedInPost.value = result.data!!
                    println("*Interested users in post: ${result.data}")

                }

                else -> {
                    println("Error occurred while loading interested users")
                }
            }
            resultDeferred.complete(result)
        }
        return resultDeferred.await()
    }

    //for current post
    fun getInterestedInPost() {
        viewModelScope.launch {
            when (val result = postsRepo.getInterestedInPost(_currPost.value!!.id)) {
                is Result.Success -> {
                    _interestedInPost.value = result.data!!
                    println("Interested users in post: ${result.data}")
                    //getInterestedIdList()
                }
                else -> {
                    println("Error occurred while loading interested users")
                }
            }
        }

    }



fun updateIsApproved(postId: String, userId: String, isApproved: Boolean) {
    viewModelScope.launch {
        _upResult.value = postsRepo.updateIsApproved(postId, userId, isApproved)
        if(_upResult.value is Result.Success){
            println("isApproved updated")
        }
        else if(_upResult.value is Result.Error) {
            println("Error: ${(_upResult.value as Result.Error).exception.message}")
        }
    }
}
    fun removeInterestedCollection(postId: String) {
        viewModelScope.launch {
            _upResult.value = postsRepo.removeInterestedCollection(postId)
            if(_upResult.value is Result.Success){
                println("Interested collection removed")
            }
            else if(_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }



    fun resetInterestedInPost() {
        _interestedInPost.value = listOf()
       // _interestedInPostId.clear()
    }


    fun tagsToString(tags: List<String>): String {
        return tags.joinToString(", ")
    }

    fun filterInterestedInPost(email: String) {
        viewModelScope.launch {
            when (val result = loadPosts()) {
                is Result.Success -> {
                    val updatedInterested = result.data.filter { post ->
                        val interestedResult = async { getInterestedInPost(post.id) }.await()
                        if (interestedResult is Result.Success) {
                            for (request in interestedResult.data) {
                                if (request.userId == email) {
                                    return@filter true
                                }
                            }
                        } else {
                            println("Error occurred while loading interested users")
                        }
                        return@filter false
                    }
                    _posts.value = updatedInterested ?: listOf()
                }

                else -> {
                    println("Posts not loaded")
                }
            }
        }

    }

}
