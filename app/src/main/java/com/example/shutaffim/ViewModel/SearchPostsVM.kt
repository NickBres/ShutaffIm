package com.example.shutaffim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Post
import com.example.shutaffim.Model.PostsRepository
import com.example.shutaffim.Model.Result
import kotlinx.coroutines.launch

class SearchPostsVM : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: MutableLiveData<List<Post>> get() = _posts


    private val postsRepo: PostsRepository

    init {
        postsRepo = PostsRepository(Injection.instance())
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            when (val result = postsRepo.getPosts()) {
                is Result.Success -> _posts.value = result.data!!
                else -> {
                    // handle error
                    println("Error occurred while loading posts")
                }
            }
        }
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

}