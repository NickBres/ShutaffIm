package com.example.shutaffim.ViewModel


import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Result
import com.example.shutaffim.Model.User
import com.example.shutaffim.Model.UserRepo
import com.example.shutaffim.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val userRepo: UserRepo
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    //////
    private val _currentintersted = MutableLiveData<User>()

    val currentintersted: LiveData<User> get() = _currentintersted

    private val _isMyPost = MutableLiveData<Boolean>()

    val isMyPost: LiveData<Boolean> get() = _isMyPost

    init {
        userRepo = UserRepo(
            auth = FirebaseAuth.getInstance(),
            firestore = Injection.instance()
        )
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult


    /*TODO: for loading indicator */
    //private var signUpInProgress = mutableStateOf(false)
   // private var signInInProgress = mutableStateOf(false)

    fun signUp(
        navController: NavController,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        about: String,
        picture: String = "",
        type: String
    ) {
        //signUpInProgress.value = true
        val user = User(email, firstName, lastName, about, picture, type)
        viewModelScope.launch {
            _authResult.value = userRepo.signUp(user, password)
            //signUpInProgress.value = false
            if(_authResult.value is Result.Success){
                //navigate to type screen
                navController.navigateUp()

            }
            else if(_authResult.value is Result.Error) {
                Toast.makeText(
                    navController.context,
                    "Error: ${(_authResult.value as Result.Error).exception.message}",
                    Toast.LENGTH_SHORT).show()
                println("Error: ${(_authResult.value as Result.Error).exception.message}")
            }
        }
    }

    fun login(
                navController: NavController,
                email: String,
                 password: String) {
        viewModelScope.launch {
            //signInInProgress.value = true
            _authResult.value = userRepo.login(email, password)
           // signInInProgress.value = false
            updateUser()
            if(_authResult.value is Result.Success){
                //navigate to type screen
                navController.navigate(Screen.TypeScreen.route)
            }
            else if(_authResult.value is Result.Error) {
                Toast.makeText(
                    navController.context,
                    "Error: ${(_authResult.value as Result.Error).exception.message}",
                    Toast.LENGTH_SHORT).show()
                println("Error: ${(_authResult.value as Result.Error).exception.message}")
            }

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
    private val _getUserFromId = MutableLiveData<User>()
    val getUserFromId: LiveData<User> get() = _getUserFromId
    fun getUserDataById(email: String) {
        viewModelScope.launch {
            when (val result = userRepo.getUserDataById(email)) {
                is Result.Success -> {
                    _getUserFromId.value = result.data.copy()///add copy
                    println("User data: ${result.data}")
                }

                is Result.Error -> {
                    println("Failed to get user data")
                }
            }
        }
    }

    fun insertCurrentInterested(email: String) {
        viewModelScope.launch {
            when (val result = userRepo.getUserDataById(email)) {
                is Result.Success -> {
                    _isMyPost.value = false
                    _currentintersted.value = result.data.copy()///add copy
                    println("User data: ${result.data}")
                }

                is Result.Error -> {
                    println("Failed to get user data")
                }
            }
        }
    }
    fun insertIsMyPost(boolean: Boolean) {
        viewModelScope.launch {
            _isMyPost.value = boolean
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