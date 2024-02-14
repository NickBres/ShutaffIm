package com.example.shutaffim.ViewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Filter
import com.example.shutaffim.Model.Foroum
import com.example.shutaffim.Model.ForoumRepo
import com.example.shutaffim.Model.Post
import com.example.shutaffim.Model.PostsRepository
import com.example.shutaffim.Model.Request
import com.example.shutaffim.Model.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForoumVM : ViewModel() {

    private val _titles = MutableLiveData<List<Foroum>>()
    val titles: MutableLiveData<List<Foroum>> get() = _titles
/////////////////////////////////////////////
//    private val _postsUser = MutableLiveData<List<Post>>()
//    val postsUser: MutableLiveData<List<Post>> get() = _postsUser
////////////////////////////////////////


    private val foroumRepo: ForoumRepo

    init {
        foroumRepo = ForoumRepo(
            firestore = Injection.instance()
        )
        loadTitles()
    }

    private val _currTitle = MutableLiveData<Foroum>()
    val currTitle: MutableLiveData<Foroum> get() = _currTitle



    fun loadTitles() {
        viewModelScope.launch {
            when (val result = foroumRepo.getTitles()) {
                is Result.Success -> _titles.value = result.data!!
                else -> {
                    println("Error occurred while loading titles")
                }
            }
        }
        println("Titles: ${_titles.value}")
    }

    fun loadTitle(titleId: String) {
        viewModelScope.launch {
            when (val result = foroumRepo.getTitle(titleId)) {
                is Result.Success -> {
                    _currTitle.value = result.data!!

                }
                else -> {
                    // handle error
                    println("Error occurred while loading title")
                }
            }
        }
    }

//    fun resetPost() {
//        _currTitle.value = Post(
//            id = "",
//            date = 0L,
//            city = "",
//            street = "",
//            house_num = 0,
//            curr_roommates = 0,
//            max_roommates = 0,
//            price = 0,
//            tags = listOf(),
//            about = "",
//            pic1 = "",
//            pic2 = "",
//            pic3 = "",
//            pic4 = "",
//            userId = ""
//        )
//    }



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

    fun createNewTitle(foroum: Foroum ) {
        foroum.date = System.currentTimeMillis()
        viewModelScope.launch {
            when (val result = foroumRepo.createTitle(foroum )) {
                is Result.Success -> {
                    loadTitles()
                    /* TODO: Filter to my posts */
                }

                else -> {
                    println("Error occurred while creating title")
                }
            }
        }
    }

//    fun updateTitle(post: Post) {
//        viewModelScope.launch {
//            when (val result = postsRepo.updatePost(post)) {
//                is Result.Success -> {
//                    loadPosts()
//                    /* TODO: Filter to my posts */
//                }
//
//                else -> {
//                    println("Error occurred while updating post")
//                }
//            }
//        }
//    }

    fun deleteTitle(titleId: String) {
        viewModelScope.launch {
            when (val result = foroumRepo.deleteTitle(titleId)) {
                is Result.Success -> {
                    loadTitles()
                    /* TODO: Filter to my posts */
                }

                else -> {
                    println("Error occurred while deleting post")
                }
            }
        }
    }


//    private  val _interestedInPost = MutableLiveData<List<Request>>()
//    val interestedInPost: LiveData<List<Request>> get() = _interestedInPost
//
//    //for id strings
//    private  val _interestedInPostId = mutableListOf<String>()
//    val interestedInPostId: MutableList<String> get() = _interestedInPostId
//
//    private val _upResult = MutableLiveData<Result<Boolean>>()

    private  val _comment = MutableLiveData<List<Foroum>>()
    val comment: LiveData<List<Foroum>> get() = _comment

    //for id strings
    private  val _commentsID = mutableListOf<String>()
    val commentsID: MutableList<String> get() = _commentsID

    private val _upResult = MutableLiveData<Result<Boolean>>()


    fun addCommentToTitle(titleId: String , foroum: Foroum) {
        foroum.date = System.currentTimeMillis()

        viewModelScope.launch {
            _upResult.value = foroumRepo.addCommentToTitle(titleId , foroum)
            if(_upResult.value is Result.Success){
                println("Comment added to Titles list")
            }
            else if(_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }

    fun removeCommentFromTitle(titleId: String,commentId :String) {
        viewModelScope.launch {
            _upResult.value = foroumRepo.removeCommentFromTitle(titleId , commentId)
            if(_upResult.value is Result.Success){
                deleteComment(commentId)
                println("comment removed from Titles list")
                getComments(titleId)
            }
            else if(_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }

    fun getComments(titleId: String) {
        viewModelScope.launch {
            when (val result =foroumRepo.getComments(titleId) ) {
                is Result.Success -> {
                    _comment.value = result.data!!
                    println("comments in Titles: ${result.data}")
                    getCommentsID()
                }
                else -> {
                    println("Error occurred while loading comments of titles")
                }
            }
        }
    }

    fun getCommentsID(){
        _commentsID.clear()
        for (i in _comment.value!!) {

            _commentsID.add(i.id)
            println("gogo i.id: ${i.id}")
        }
    }
    fun deleteComment(commentId: String){
        _commentsID.remove(commentId)
    }


    fun resetComments() {
        _comment.value = listOf()
        _commentsID.clear()
    }

}
