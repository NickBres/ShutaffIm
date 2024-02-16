package com.example.shutaffim.ViewModel


import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
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

    //------------------- for image of user  -------------------
    private val _userImage = MutableLiveData<Uri>()
    val userImage: LiveData<Uri> get() = _userImage


    init {
        userRepo = UserRepo(
            auth = FirebaseAuth.getInstance(),
            firestore = Injection.firestoreInstance(),
            storage = Injection.storageInstance()
        )
    }

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    private val _authResult1 = MutableLiveData<Result<Boolean>>()
    val authResult1: LiveData<Result<Boolean>> get() = _authResult1


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
        pictureName: String,
        pictureUrl: String,
        type: String,
        bitmap: Bitmap
    ) {
        //signUpInProgress.value = true
        val user = User(email, firstName, lastName, about, pictureName,pictureUrl, type)
        viewModelScope.launch {
            _authResult.value = userRepo.signUp(user, password)
            //signUpInProgress.value = false
            if(_authResult.value is Result.Success){
                //navigate to type screen
                uploadImageToFirebase(bitmap, email, "")
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
                    println("4.1 User data: ${result.data}")

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
                    println("4.2 User data: ${result.data}")
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

    //------------------- image management -------------------

    fun uploadImageToFirebase(bitmap : Bitmap,userId :String,oldPIc:String) {//todo:cange name
        println("OLD IMAGE: $oldPIc  "+"NEW IMAGE: $bitmap  " + "USER ID: $userId")
        viewModelScope.launch {
            when (val result = userRepo.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = result.data.copy()///add copy
                    println("1.1 User data: ${result.data}")

                    // Use async to launch the first operation
                    val removeImageDeferred = viewModelScope.async {
                        userRepo.removeProfileImageFromUser(oldPIc, userId)
                    }

                    // Wait for the completion of the first operation
                    val removeImageResult = removeImageDeferred.await()

                    if (removeImageResult is Result.Success) {
                        println("1.1 User Image removed successfully")

                        // Use async to launch the second operation
                        val uploadImageDeferred = viewModelScope.async {
                            userRepo.uploadImageToFirebase(bitmap, userId)
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

    fun removeImageFromUser() {//don't use without adding path
        viewModelScope.launch {
            _authResult.value = userRepo.removeImageFromUser()
        }
        if(_authResult.value is Result.Success){
            println("Image removed successfully")
        }
        else if(_authResult.value is Result.Error){
            println("Failed to remove image")
        }
    }


    fun getUserProfileImage(bitmap : String){

        viewModelScope.launch {
            when (val result = userRepo.getUserProfileImage(bitmap )) {
                is Result.Success -> {
                    _userImage.value = result.data!!
                    println("3. User image: ${result.data}")
                }
                is Result.Error -> {
                    println("Failed to get user image")
                }
            }
        }

    }
}