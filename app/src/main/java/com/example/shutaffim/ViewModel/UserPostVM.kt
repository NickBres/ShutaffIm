package com.example.shutaffim.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Request
import com.example.shutaffim.Model.UserPostRepo
import kotlinx.coroutines.launch
import com.example.shutaffim.Model.Result
import com.google.firebase.auth.FirebaseAuth

class UserPostVM : ViewModel() {

    private val userpostRepo : UserPostRepo

    private  val _interestedInPost = MutableLiveData<List<Request>>()
    init {
        userpostRepo = UserPostRepo(
            auth = FirebaseAuth.getInstance(),
            firestore = Injection.instance())
    }
    val interestedInPost: LiveData<List<Request>> get() = _interestedInPost

    private val _upResult = MutableLiveData<Result<Boolean>>()
    val upResult: LiveData<Result<Boolean>> get() = _upResult
    fun addInterestedToPost(userId: String, postId: String,msg: String) {
        val request = Request(userId,postId,true,msg)
        viewModelScope.launch {
            _upResult.value = userpostRepo.addInterestedToPost(request)
            if(_upResult.value is Result.Success){
                println("Post added to user's posts list")
            }
            else if(_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }

    fun removeInterestedFromPost(userId: String, postId: String) {
        val request = Request(userId,postId,true)
        viewModelScope.launch {
            _upResult.value = userpostRepo.removeInterestedFromPost(request)
            if(_upResult.value is Result.Success){
                println("Post removed from user's posts list")
            }
            else if(_upResult.value is Result.Error) {
                println("Error: ${(_upResult.value as Result.Error).exception.message}")
            }
        }
    }

   fun getInterestedInPost(postId: String) {
        viewModelScope.launch {
            when (val result = userpostRepo.getInterestedInPost(postId)) {
                is Result.Success -> {
                    _interestedInPost.value = result.data!!
                    println("Interested users in post: ${result.data}")
                }
                else -> {
                    println("Error occurred while loading interested users")
                }
            }
        }
    }


}