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
import kotlinx.coroutines.launch

class PostsVM : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: MutableLiveData<List<Post>> get() = _posts

    private val _interestedInPost: MutableLiveData<List<InterestedInPost>> = MutableLiveData()
    val interestedInPost: MutableLiveData<List<InterestedInPost>> get() = _interestedInPost

    private var _filter = MutableLiveData<Filter>()
    var filter: MutableLiveData<Filter>
        get() = _filter
        set(value) {
            _filter.value = value.value
        }


    private val postsRepo: PostsRepository


    init {
        postsRepo = PostsRepository(
            firestore = Injection.instance()
        )
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
                    val postIndex = _posts.value?.indexOfFirst { it.id == postId }
                    postIndex?.let {
                        val updatedPosts = _posts.value?.toMutableList()
                        updatedPosts?.set(it, result.data)
                        _posts.value = updatedPosts!!
                    }
                }

                else -> {
                    // handle error
                    println("Error occurred while loading post")
                }
            }
        }
    }

    fun applyFilter() {
        loadPosts()
        filterPosts(_filter.value!!)
    }

    fun filterPosts(
        filter: Filter
    ) {
        val listTags = tagsToList(filter.tags)
        viewModelScope.launch {
            val updatedPosts = _posts.value?.filter {
                it.id.contains(filter.id, ignoreCase = true) &&
                        it.city.contains(filter.city, ignoreCase = true) &&
                        it.street.contains(filter.street, ignoreCase = true) &&
                        it.price in filter.minPrice..filter.maxPrice &&
                        (listTags.isEmpty() || it.tags.any { tag -> listTags.contains(tag) })
            }
            _posts.value = updatedPosts!!
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

    fun createNewPost(post: Post) {
        post.date = System.currentTimeMillis().toString()
        viewModelScope.launch {
            when (val result = postsRepo.createPost(post)) {
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

    fun deletePost(post: Post) {
        viewModelScope.launch {
            when (val result = postsRepo.deletePost(post)) {
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


}
