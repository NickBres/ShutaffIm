package com.example.shutaffim.ViewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Filter
import com.example.shutaffim.Model.Post
import com.example.shutaffim.Model.PostsRepository
import com.example.shutaffim.Model.Request
import com.example.shutaffim.Model.Result
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
            firestore = Injection.instance()
        )
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            when (val result = postsRepo.getPosts()) {
                is Result.Success -> _posts.value = result.data!!
                else -> {
                    println("Error occurred while loading posts")
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
            pic1 = "",
            pic2 = "",
            pic3 = "",
            pic4 = "",
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
//        viewModelScope.launch {
            loadPosts()
            filterPosts(email)
//        }

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

//    fun loadInterestedInPost(postId: String) {
//        viewModelScope.launch {
//            when (val result = postsRepo.getInterestedInPost(postId)) {
//                is Result.Success -> _interestedInPost.value = result.data!!
//                else -> {
//                    _interestedInPost.value = listOf()
//                    println("Error occurred while loading interested in post")
//                }
//            }
//        }
//    }

    fun createNewPost(post: Post ) {
        post.date = System.currentTimeMillis()
        viewModelScope.launch {
            when (val result = postsRepo.createPost(post )) {
                is Result.Success -> {
                    loadPosts()
                    /* TODO: Filter to my posts */
                }

                else -> {
                    println("Error occurred while creating post")
                }
            }
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            when (val result = postsRepo.updatePost(post)) {
                is Result.Success -> {
                    loadPosts()
                    /* TODO: Filter to my posts */
                }

                else -> {
                    println("Error occurred while updating post")
                }
            }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            when (val result = postsRepo.deletePost(postId)) {
                is Result.Success -> {
                    loadPosts()
                    /* TODO: Filter to my posts */
                }

                else -> {
                    println("Error occurred while deleting post")
                }
            }
        }
    }



    private  val _interestedInPost = MutableLiveData<List<Request>>()
    val interestedInPost: LiveData<List<Request>> get() = _interestedInPost

    //for id strings
    private  val _interestedInPostId = mutableListOf<String>()
    val interestedInPostId: MutableList<String> get() = _interestedInPostId

    private val _upResult = MutableLiveData<Result<Boolean>>()



    fun addInterestedToPost(userId: String, postId: String,msg: String) {
        val date = System.currentTimeMillis()
        val request = Request(userId, postId,date, isApproved = false, msg)
        viewModelScope.launch {
            _upResult.value = postsRepo.addInterestedToPost(request)
            if(_upResult.value is Result.Success){
                println("Post added to user's posts list")
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
                deleteInterestedIdFromList(userId)
                println("Post removed from user's posts list")
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
                    println("Interested users in post: ${result.data}")
                    getInterestedIdList()
                }
                else -> {
                    println("Error occurred while loading interested users")
                }
            }
        }
    }

    fun getInterestedIdList(){
        _interestedInPostId.clear()
        for (interested in _interestedInPost.value!!) {

            _interestedInPostId.add(interested.userId)
            println("gogo interested.userId: ${interested.userId}")
        }
    }
    fun deleteInterestedIdFromList(userId: String){
        _interestedInPostId.remove(userId)
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



    fun resetInterestedInPost() {
        _interestedInPost.value = listOf()
        _interestedInPostId.clear()
    }


    fun tagsToString(tags: List<String>): String {
        return tags.joinToString(", ")
    }

    fun filterInterestedInPost(email: String) {
        viewModelScope.launch {
//            loadPosts()
//            delay(500)
            for (post in _posts.value!!) {
                getInterestedInPost(post.id)
                delay(100)
                if (!_interestedInPostId.contains(email)) {
                    _posts.value = _posts.value?.minus(post)
                }
                resetInterestedInPost()
            }
        }
    }



}
