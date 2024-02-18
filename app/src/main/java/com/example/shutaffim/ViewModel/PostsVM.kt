package com.example.shutaffim.ViewModel

import android.graphics.Bitmap
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
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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




    init {
        postsRepo = PostsRepository(
            firestore = Injection.firestoreInstance(),
            storage = Injection.storageInstance()
        )
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            when (val result = postsRepo.getPosts()) {
                is Result.Success -> {
                    _posts.value = result.data!!
                    println("3. Posts loaded here data: ${result.data}")
                    println("3. Posts loaded successfully")
                }

                else -> {
                    println("3. Error occurred while loading posts")
                }
            }
        }
        println("Posts: ${_posts.value}")
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
            loadPosts()
            delay(500)
            if (_filter.value == null) _filter.value = Filter()
            filterPosts(_filter.value!!)
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

                matchesId && matchesCity && matchesStreet && matchesPrice && matchesTags
            }

            _posts.value = updatedPosts ?: listOf()
        }
    }

    //////////////////////////////////////////////////////
    fun applyFilter(email: String) {
            loadPosts()
            filterPosts(email)
    }
    fun filterPosts(email: String) {
        // Convert filter tags string to list and trim each tag
                viewModelScope.launch {
            val updatedPosts = _posts.value?.filter { post ->
                val matchesemail =
                    post.userId.contains(email, ignoreCase = true)


                matchesemail
            }

        _postsUser.value = updatedPosts ?: listOf()
        }
    }




    fun tagsToList(tags: String): List<String> {
        return tags.split(",").map { it.trim() }
    }


    fun createNewPost(post: Post, bitmaps: List<Bitmap>, navController: NavController) {
        post.date = System.currentTimeMillis()
        viewModelScope.launch {
            when (val result = postsRepo.createPost(post)) {
                is Result.Success -> {
                    uploadPostImages(result.data.id, bitmaps)

                    navController.navigateUp()
                }

                else -> {
                    println("Error occurred while creating post")
                }
            }
        }
    }

    private fun uploadPostImages(postId: String, bitmaps: List<Bitmap>) {
        viewModelScope.launch {
            when (val result = postsRepo.getPost(postId)) {
                is Result.Success -> {
                    _currPost.value = result.data.copy()
                    val uploadPostsResult = viewModelScope.async {
                        postsRepo.uploadPostsImages(postId, bitmaps)
                    }.await()

                    if (uploadPostsResult is Result.Success) {
                        println("Post images uploaded successfully")
                        loadPosts()
                    } else {
                        println("Error occurred while uploading post images")
                    }

                }

                else -> {
                    println("Error occurred while getting post")
                }
            }
        }
    }


    fun updatePost(post: Post, newImages: List<Bitmap>, imagesToDelete: List<String>) {
        viewModelScope.launch {
            when (val result = postsRepo.updatePost(post, newImages, imagesToDelete)) {
                is Result.Success -> {
                    println("1. Post updated successfully")
                    loadPosts()
                }
                else -> {
                    println("1. Error occurred while updating post")
                }
            }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            when (val result = postsRepo.deletePost(postId)) {
                is Result.Success -> {
                   removeInterestedCollection(postId)
                    loadPosts()
                    println("2. Post deleted successfully")
                    /* TODO: Filter to my posts */
                }

                else -> {
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
        val request = Request(userId, postId,date, isApproved = false, msg)
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
        val request = Request(userId,postId, isApproved = true)
        viewModelScope.launch {
            _upResult.value = postsRepo.removeInterestedFromPost(request)
            if(_upResult.value is Result.Success){
                //deleteInterestedIdFromList(userId)
                println("Request of %s removed from post".format(userId))
                getInterestedInPost(postId)
            }
            else if(_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }

    fun getInterestedInPost(postId: String) {
        viewModelScope.launch {
            when (val result = postsRepo.getInterestedInPost(postId)) {
                is Result.Success -> {
                    _interestedInPost.value = result.data!!
                    println("*Interested users in post: ${result.data}")
                   // getInterestedIdList()
                }
                else -> {
                    println("Error occurred while loading interested users")
                }
            }
        }
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

    }



}
