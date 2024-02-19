package com.example.shutaffim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.Comment
import com.example.shutaffim.Model.ForumRepo
import com.example.shutaffim.Model.Result
import com.example.shutaffim.Model.Topic
import com.example.shutaffim.Screen
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ForumVM(val authViewModel: AuthViewModel): ViewModel() {

    private val _topics = MutableLiveData<List<Topic>>()
    val topics: MutableLiveData<List<Topic>> get() = _topics

    private val forumRepo: ForumRepo

    val _currTopic = MutableLiveData<Topic>()
    val currTopic: MutableLiveData<Topic> get() = _currTopic

    val _comments = MutableLiveData<List<Comment>>()
    val comments: MutableLiveData<List<Comment>> get() = _comments

    init {
        forumRepo = ForumRepo(
            firestore = Injection.firestoreInstance()
        )
        viewModelScope.launch {
            loadTopics()
        }
    }

    fun updateUserNamesInTopics() {
        viewModelScope.launch {
            val updateTopics = _topics.value?.map { topic ->
                val userResult = async { authViewModel.getUserDataById(topic.email) }.await()
                if (userResult is Result.Success) {
                    val userName =
                        userResult.data.fName + " " + userResult.data.lName
                    Topic(
                        id = topic.id,
                        title = topic.title,
                        description = topic.description,
                        email = topic.email,
                        date = topic.date,
                        userName = userName
                    )
                } else {
                    println("Error occurred while updating user names in topics")
                    topic
                }
            }
            _topics.value = updateTopics!!
        }
    }

    suspend fun loadTopics(): Result<List<Topic>> {
        val resultDeferred = CompletableDeferred<Result<List<Topic>>>()
        viewModelScope.launch {
            //when->switch case
            val result = forumRepo.getTopics()
            when (result) {
                is Result.Success -> {
                    _topics.value = result.data!!
                    updateUserNamesInTopics()
                }

                else -> {
                    println("Error occurred while loading topics")
                }
            }
            resultDeferred.complete(result)
        }
        println("Topics: ${_topics.value}")
        return resultDeferred.await()
    }

    fun createTopic(title:String,description:String) {
       val topic= Topic(
           title = title,
           description = description,
           email = authViewModel.currentUser.value!!.email,
           date = System.currentTimeMillis()
       )
        viewModelScope.launch {
            when (val result = forumRepo.createTopic(topic)) {
                is Result.Success -> {
                    loadTopics()
                }

                else -> {
                    println("Error occurred while creating topic")
                }
            }
        }
    }

    fun sortTopicsByDate(isAscending: Boolean) {
        viewModelScope.launch {

            val sortedTopics = if (isAscending) {
                _topics.value?.sortedBy { it.date }
            } else {
                _topics.value?.sortedByDescending { it.date }
            }
            _topics.value = sortedTopics!!

        }

    }

    fun filterMineTopics() {
        val filteredTopics =
            _topics.value?.filter { it.email == authViewModel.currentUser.value!!.email }
        _topics.value = filteredTopics!!
    }

    fun loadTopic(topicId: String, navController: NavController) {
        viewModelScope.launch {
            when (val result = forumRepo.getTopic(topicId)) {
                is Result.Success -> {
                    val userResult =
                        async { authViewModel.getUserDataById(result.data.email) }.await()
                    if (userResult is Result.Success) {
                        val userName =
                            userResult.data.fName + " " + userResult.data.lName
                        val topicWithName = Topic(
                            id = result.data.id,
                            title = result.data.title,
                            description = result.data.description,
                            email = result.data.email,
                            date = result.data.date,
                            userName = userName
                        )
                        _currTopic.value = topicWithName
                        loadComments()
                        navController.navigate(Screen.TopicScreen.route)
                    } else {
                        println("Error occurred while loading topic, user not found")
                    }
                }
                else -> {
                    println("Error occurred while loading topic")
                }
            }
        }
    }

    fun loadComments() {
        viewModelScope.launch {
            when (val result = forumRepo.getComments(_currTopic.value!!.id)) {
                is Result.Success -> {
                    _comments.value = result.data!!
                    updateUserNamesInComments()
                }

                else -> {
                    println("Error occurred while loading comments")
                }
            }
        }
    }

    fun updateUserNamesInComments() {
        viewModelScope.launch {
            val updateComments = _comments.value?.map { comment ->
                val userResult = async { authViewModel.getUserDataById(comment.email) }.await()
                if (userResult is Result.Success) {
                    val userName =
                        userResult.data.fName + " " + userResult.data.lName
                    Comment(
                        id = comment.id,
                        text = comment.text,
                        email = comment.email,
                        date = comment.date,
                        userName = userName
                    )
                } else {
                    println("Error occurred while updating user names in comments")
                    comment
                }
            }
            _comments.value = updateComments!!
        }
    }

    fun addComment(comment: Comment) {
        viewModelScope.launch {
            when (val result = forumRepo.addCommentToTopic(_currTopic.value!!.id, comment)) {
                is Result.Success -> {
                    loadComments()
                }

                else -> {
                    println("Error occurred while adding comment")
                }
            }
        }
    }

    fun deleteTopic() {
        viewModelScope.launch {
            when (val result = forumRepo.deleteTopic(_currTopic.value!!.id)) {
                is Result.Success -> {
                    loadTopics()
                }
                else -> {
                    println("Error occurred while deleting topic")
                }
            }
        }
    }


}