package com.example.shutaffim.ViewModel


import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Picture
import com.example.shutaffim.Model.Result
import com.example.shutaffim.Model.User
import com.example.shutaffim.Model.UserRepo
import com.example.shutaffim.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val userRepo: UserRepo
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    //------------------- for interested users -------------------
    private val _currentintersted = MutableLiveData<User>()

    val currentintersted: LiveData<User> get() = _currentintersted

    private val _isMyPost = MutableLiveData<Boolean>()

    val isMyPost: LiveData<Boolean> get() = _isMyPost


    init {
        userRepo = UserRepo(
            auth = FirebaseAuth.getInstance(),
            firestore = Injection.firestoreInstance(),
            storage = Injection.storageInstance()
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
        picture: Picture,
        type: String,
        bitmap: Bitmap,
        birthYear: Int,
        sex: String

    ) {
        val user = User(email, firstName, lastName, about, picture, type, birthYear, sex)
        viewModelScope.launch {
            _authResult.value = userRepo.signUp(user, password)
            //signUpInProgress.value = false
            if(_authResult.value is Result.Success){
                //navigate to type screen
                uploadProfileImageToFirebase(bitmap, email)
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
        password: String,
        rememberMe: Boolean,
        context: Context
    ) {
        viewModelScope.launch {
            _authResult.value = userRepo.login(email, password)
            if (_authResult.value is Result.Success) {
                saveRememberMePreference(rememberMe, context)
                updateUser()
                navController.navigate(Screen.TypeScreen.route)
            } else if (_authResult.value is Result.Error) {
                Toast.makeText(
                    navController.context,
                    "Error: ${(_authResult.value as Result.Error).exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                println("Error: ${(_authResult.value as Result.Error).exception.message}")
            }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            when (val result = userRepo.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = result.data.copy()///add copy
                    println("4.1 User data: ${result.data}")

                }

                is Result.Error -> {
                    println("Failed to get user data")
                }
            }
        }
    }

    private val _usersList = MutableLiveData<List<User>>()
    val usersList: LiveData<List<User>> get() = _usersList

    suspend fun getUsersList(emails: List<String>) {
        viewModelScope.launch {
            _usersList.value = listOf() // Clear the list
            emails.forEach {
                getUserDataById(it)
            }
        }
    }

    suspend fun getUserDataById(email: String): Result<User> {
        val resultDeferred = CompletableDeferred<Result<User>>()
        viewModelScope.launch {
            val result = userRepo.getUserDataById(email)
            when (result) {
                is Result.Success -> {
                    _usersList.value = _usersList.value?.plus(result.data) ?: listOf(result.data)
                    println("4.2 User data: ${result.data}")
                }

                is Result.Error -> {
                    println("Failed to get user data")
                }
            }
            resultDeferred.complete(result)
        }
        return resultDeferred.await()
    }

    fun getUserFromList(email: String): User? {
        return _usersList.value?.find { it.email == email }
    }


    fun insertIsMyPost(boolean: Boolean) {
        viewModelScope.launch {
            _isMyPost.value = boolean
        }
    }

    fun logout(context: Context) {
        viewModelScope.launch {
            userRepo.logout()
            saveRememberMePreference(false, context) // Reset Remember Me preference on logout
            _authResult.value = Result.Success(false) // Update the auth result as needed
        }
    }

    fun updateInfo(user: User) {
        viewModelScope.launch {
            _authResult.value = userRepo.update(user)
        }
    }

    //------------------- image management -------------------

    fun uploadProfileImageToFirebase(bitmap: Bitmap, userId: String) {//todo:cange name

        viewModelScope.launch {
            when (val result = userRepo.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = result.data.copy()///add copy
                    println("1.1 User data: ${result.data}")
                    println("OLD IMAGE: ${_currentUser.value!!.picture.pictureName}  " + "NEW IMAGE: $bitmap  " + "USER ID: $userId")
                    // Use async to launch the first operation
                    val removeImageDeferred = viewModelScope.async {
                        userRepo.removeProfileImageFromUser(
                            _currentUser.value!!.picture.pictureName,
                            userId
                        )
                    }
                    // Wait for the completion of the first operation
                    val removeImageResult = removeImageDeferred.await()

                    if (removeImageResult is Result.Success) {
                        println("1.1 User Image removed successfully")

                        // Use async to launch the second operation
                        val uploadImageDeferred = viewModelScope.async {
                            userRepo.uploadProfileImageToFirebase(bitmap, userId)
                        }

                        // Wait for the completion of the second operation
                        val uploadImageResult = uploadImageDeferred.await()

                        if (uploadImageResult is Result.Success) {
                            println("1.1 Image uploaded successfully")
                            updateUser()
                        } else if (uploadImageResult is Result.Error) {
                            println("Failed to upload image")
                        }
                    } else if (removeImageResult is Result.Error) {
                        println("Failed to remove User image")
                    }
                }

                is Result.Error -> {
                    println("Failed to get user data")
                }
            }
        }
    }

    fun saveRememberMePreference(rememberMe: Boolean, context: Context) {
        val prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("RememberMe", rememberMe).apply()
    }

    fun getRememberMePreference(context: Context): Boolean {
        val prefs = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return prefs.getBoolean("RememberMe", false)
    }


}