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
import kotlinx.coroutines.delay
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
        loadTopics()
    }

    fun updateUserNamesInTopics() {
        viewModelScope.launch {
            val updateTopics = _topics.value?.map { topic ->
                authViewModel.getUserDataById(topic.email)
                delay(300)
                val userName =
                    authViewModel.getUserFromId.value?.fName + " " + authViewModel.getUserFromId.value?.lName
                Topic(
                    id = topic.id,
                    title = topic.title,
                    description = topic.description,
                    email = topic.email,
                    date = topic.date,
                    userName = userName
                )
            }
            _topics.value = updateTopics!!
        }
    }
    fun loadTopics() {
        viewModelScope.launch {
            //whem->switch case
            when (val result = forumRepo.getTopics()) {
                is Result.Success ->
                {
                    _topics.value = result.data!!
                    updateUserNamesInTopics()
                }
                else -> {
                    println("Error occurred while loading topics")
                }
            }
        }
        println("Topics: ${_topics.value}")
    }

    fun createTopic(title:String,description:String) {
       val topic= Topic(
           title = title,
           description = description,
           email = authViewModel.currentUser.value!!.email,
           date = System.currentTimeMillis(),
           userName = authViewModel.currentUser.value!!.fName + " " + authViewModel.currentUser.value!!.lName
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
            loadTopics()
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
                    authViewModel.getUserDataById(result.data.email)
                    delay(300)
                    val userName =
                        authViewModel.getUserFromId.value?.fName + " " + authViewModel.getUserFromId.value?.lName
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
                authViewModel.getUserDataById(comment.email)
                delay(300)
                val userName =
                    authViewModel.getUserFromId.value?.fName + " " + authViewModel.getUserFromId.value?.lName
                Comment(
                    id = comment.id,
                    text = comment.text,
                    email = comment.email,
                    date = comment.date,
                    userName = userName
                )
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