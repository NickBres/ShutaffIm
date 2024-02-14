package com.example.shutaffim.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shutaffim.Injection
import com.example.shutaffim.Model.ForumRepo
import com.example.shutaffim.Model.Result
import com.example.shutaffim.Model.Topic
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ForumVM(val authViewModel: AuthViewModel): ViewModel() {

    private val _topics = MutableLiveData<List<Topic>>()
    val topics: MutableLiveData<List<Topic>> get() = _topics

    private val forumRepo: ForumRepo

    init {
        forumRepo = ForumRepo(
            firestore = Injection.instance()
        )
       loadTopics()
    }

    fun updateUserNames()
    {
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
                    updateUserNames()
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





}