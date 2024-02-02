package com.example.shutaffim.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.User
import com.example.shutaffim.Model.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import com.example.shutaffim.Model.Result


class AuthViewModel : ViewModel(){
    private val userRepo : UserRepo

    init {
        userRepo = UserRepo(
            auth = FirebaseAuth.getInstance(),
            firestore = Injection.instance()
        )
    }
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(email: String, password: String,
               firstName: String, lastName: String,
               about : String,picture : String = "", type : String ="admin")
    {
        val user = User(email, firstName, lastName, about, picture, type)
        viewModelScope.launch {
            _authResult.value = userRepo.signUp(user, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepo.login(email, password)
        }
    }

}