package com.example.shutaffim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Filter
import com.example.shutaffim.Model.InterestedInPost
import com.example.shutaffim.Model.Post
import com.example.shutaffim.Model.PostsRepository
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
    private val _interestedInPost: MutableLiveData<List<InterestedInPost>> = MutableLiveData()
    val interestedInPost: MutableLiveData<List<InterestedInPost>> get() = _interestedInPost

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
            date = "",
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

    fun loadInterestedInPost(postId: String) {
        viewModelScope.launch {
            when (val result = postsRepo.getInterestedInPost(postId)) {
                is Result.Success -> _interestedInPost.value = result.data!!
                else -> {
                    _interestedInPost.value = listOf()
                    println("Error occurred while loading interested in post")
                }
            }
        }
    }

    fun createNewPost(post: Post ) {
        post.date = System.currentTimeMillis().toString()
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

    fun deletePost(postid: String) {
        viewModelScope.launch {
            when (val result = postsRepo.deletePost(postid)) {
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

    fun addInterestedInPost(interestedInPost: InterestedInPost) {
        viewModelScope.launch {
            when (val result = postsRepo.addInterestedInPost(interestedInPost)) {
                is Result.Success -> {
                    loadInterestedInPost(interestedInPost.postId)
                }

                else -> {
                    println("Error occurred while adding interested in post")
                }
            }
        }
    }

    fun deleteInterestedInPost(interestedInPost: InterestedInPost) {
        viewModelScope.launch {
            when (val result = postsRepo.deleteInterestedInPost(interestedInPost)) {
                is Result.Success -> {
                    loadInterestedInPost(interestedInPost.postId)
                }

                else -> {
                    println("Error occurred while removing interested in post")
                }
            }
        }
    }

    fun updateInterestedInPost(interestedInPost: InterestedInPost) {
        viewModelScope.launch {
            when (val result = postsRepo.updateInterestedInPost(interestedInPost)) {
                is Result.Success -> {
                    loadInterestedInPost(interestedInPost.postId)
                }

                else -> {
                    println("Error occurred while updating interested in post")
                }
            }
        }
    }

    fun tagsToString(tags: List<String>): String {
        return tags.joinToString(", ")
    }


}
