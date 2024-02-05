package com.example.shutaffim.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Result
import com.example.shutaffim.Model.User
import com.example.shutaffim.Model.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val userRepo: UserRepo
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        userRepo = UserRepo(
            auth = FirebaseAuth.getInstance(),
            firestore = Injection.instance()
        )
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        about: String,
        picture: String = "",
        type: String
    ) {
        val user = User(email, firstName, lastName, about, picture, type)
        viewModelScope.launch {
            _authResult.value = userRepo.signUp(user, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepo.login(email, password)
            updateUser()
        }

    }

    fun updateUser() {
        viewModelScope.launch {
            when (val result = userRepo.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = result.data.copy()///add copy
                    println("User data: ${result.data}")
                }

                is Result.Error -> {
                    println("Failed to get user data")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authResult.value = userRepo.logout()
        }
    }

    fun updateInfo(user: User) {
        viewModelScope.launch {
            _authResult.value = userRepo.update(user)
        }
    }
}