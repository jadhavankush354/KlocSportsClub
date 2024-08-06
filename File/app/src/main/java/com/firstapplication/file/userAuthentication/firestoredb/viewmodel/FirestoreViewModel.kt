package com.firstapplication.file.userAuthentication.firestoredb.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.file.DataClass.Question
import com.firstapplication.file.DataClass.Replies
import com.firstapplication.file.R
import com.firstapplication.file.userAuthentication.firestoredb.module.FirestoreModel
import com.firstapplication.file.userAuthentication.firestoredb.repository.FirestoreRepository
import com.firstapplication.file.userAuthentication.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context

@HiltViewModel
class FirestoreViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel(){

    private val _res: MutableState<FirestoreState> = mutableStateOf(FirestoreState())
    val res:State<FirestoreState> = _res
    private val _res1: MutableState<singleState> = mutableStateOf(singleState())
    val res1:State<singleState> = _res1
    fun insert(user: FirestoreModel.FirestoreUser) = repo.insert(user)

    private val _questionsState = MutableStateFlow<ResultState<List<Question>>>(ResultState.Loading)
    val questionsState: StateFlow<ResultState<List<Question>>> = _questionsState


    private val _updateData: MutableState<FirestoreModel> = mutableStateOf(
        FirestoreModel(
            user = FirestoreModel.FirestoreUser()
        )
    )
    val updateData:State<FirestoreModel> = _updateData

    fun setData(data:FirestoreModel){
        _updateData.value = data
    }

    init {
        getUsers()
        fetchQuestions()
    }

    fun getUsers() = viewModelScope.launch {
        repo.getUsers().collect{
            when(it){
                is ResultState.Success->{
                    _res.value = FirestoreState(
                        data = it.data
                    )
                }
                is ResultState.Failure->{
                    _res.value = FirestoreState(
                        error = it.toString()
                    )
                }
                ResultState.Loading->{
                    _res.value = FirestoreState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete(key:String) = repo.delete(key)
    fun update(user: FirestoreModel) = repo.updateUser(user)
    fun getUserByEmail(email:String)= viewModelScope.launch {
        repo.getUserByEmail(email).collect{
            when(it){
                is ResultState.Success->{
                    _res1.value = singleState(
                        data = it.data
                    )
                }
                is ResultState.Failure->{
                    _res1.value = singleState(
                        error = it.toString()
                    )
                }
                ResultState.Loading->{
                    _res1.value = singleState(
                        isLoading = true
                    )
                }
            }
        }
    }
    fun updateUserScore(email: String, score: Int) = viewModelScope.launch {
        repo.getUserByEmail(email).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    val user = result.data
                    var prevCoin=user.user?.coins
                    val updatedUser = user.copy(
                        user = user.user?.copy(coins = (( score * 2)+ prevCoin!!))
                    )
                    repo.updateUser(updatedUser).collect { updateResult ->
                        // Handle the update result here
                        when (updateResult) {
                            is ResultState.Success -> {
                                Log.d("debug", "Update result: ${updateResult.data}")
                            }
                            is ResultState.Failure -> {
                                Log.e("debug", "Failed to update user: ${updateResult.e}")
                            }
                            ResultState.Loading -> {
                                Log.d("debug", "Updating user data...")
                            }
                        }
                    }
                }
                is ResultState.Failure -> {
                    Log.e("debug", "Failed to fetch user: ${result.e}")
                }
                ResultState.Loading -> {
                    Log.d("debug", "Loading user data...")
                }
            }
        }
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            repo.getQuestions().collect { result ->
                _questionsState.value = result
            }
        }
    }
    fun saveQuestion(question: Question) {
        viewModelScope.launch {
            repo.saveQuestion(question).collect { result ->
                _questionsState.value = result
            }
        }
    }
    fun saveReply(questionId: String, reply: Replies) = viewModelScope.launch {
        viewModelScope.launch {
            repo.saveReply(questionId, reply).collect { result ->
                _questionsState.value = result
            }
        }
    }
    fun loadOffensiveKeywords(context: Context): Set<String> {
        val inputStream = context.resources.openRawResource(R.raw.en)  // `R.raw.en` refers to `en.txt` in the raw directory
        return inputStream.bufferedReader().useLines { lines ->
            lines.toSet()
        }
    }
    fun containsOffensiveKeywords(message: String, offensiveKeywords: Set<String>): Boolean {
        val words = message.split(" ", "\n", "\t").map { it.lowercase() }
        return words.any { it in offensiveKeywords }
    }
    fun deleteQuestion(questionId: String) = viewModelScope.launch {
        repo.deleteQuestion(questionId).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    _questionsState.value = _questionsState.value.let { currentState ->
                        if (currentState is ResultState.Success) {
                            val updatedQuestions = currentState.data.filter { it.id != questionId }
                            ResultState.Success(updatedQuestions)
                        } else {
                            currentState
                        }
                    }
                    Log.d("debug", "Question deleted: $questionId")
                }
                is ResultState.Failure -> {
                    Log.e("debug", "Failed to delete question: ${result.e}")
                }
                ResultState.Loading -> {
                    Log.d("debug", "Deleting question...")
                }
            }
        }
    }
    fun deleteReply(questionId: String, replyId: String) = viewModelScope.launch {
        repo.deleteReply(questionId, replyId).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    _questionsState.value = _questionsState.value.let { currentState ->
                        if (currentState is ResultState.Success) {
                            val updatedQuestions = currentState.data.map { question ->
                                if (question.id == questionId) {
                                    val updatedReplies = question.replies.filter { it.id != replyId }
                                    question.copy(replies = updatedReplies)
                                } else {
                                    question
                                }
                            }
                            ResultState.Success(updatedQuestions)
                        } else {
                            currentState
                        }
                    }
                    Log.d("debug", "Reply deleted: $replyId")
                }
                is ResultState.Failure -> {
                    Log.e("debug", "Failed to delete reply: ${result.e}")
                }
                ResultState.Loading -> {
                    Log.d("debug", "Deleting reply...")
                }
            }
        }
    }
    fun deleteAllQuestions() = viewModelScope.launch {
        repo.deleteAllQuestions().collect { result ->
            when (result) {
                is ResultState.Success -> { _questionsState.value = ResultState.Success(emptyList()) }
                is ResultState.Failure -> { _questionsState.value = ResultState.Failure(result.e) }
                ResultState.Loading -> { _questionsState.value = ResultState.Loading }
            }
        }
    }

}

data class FirestoreState(
    val data:List<FirestoreModel> = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false
)
data class singleState(
    val data: FirestoreModel? = null,
    val error:String = "",
    val isLoading:Boolean = false
)
