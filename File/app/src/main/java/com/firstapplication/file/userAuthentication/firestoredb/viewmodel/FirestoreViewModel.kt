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

    private val _questionsState = MutableStateFlow<ResultState<List<Question>>>(ResultState.Loading)
    val questionsState: StateFlow<ResultState<List<Question>>> = _questionsState

    private val _updateData: MutableState<FirestoreModel> = mutableStateOf(FirestoreModel(user = FirestoreModel.FirestoreUser()))
    val updateData:State<FirestoreModel> = _updateData

    init {
        getUsers()
        fetchQuestions("questions")
    }
    fun insert(user: FirestoreModel.FirestoreUser) = repo.insert(user)
    fun setData(data:FirestoreModel) { _updateData.value = data }
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
    fun fetchQuestions(subCategory: String) {
        viewModelScope.launch {
            repo.getQuestions(subCategory).collect { _questionsState.value = it }
        }
    }

    fun saveQuestion(question: Question, subCategory: String) {
        viewModelScope.launch {
            val currentQuestions = (_questionsState.value as? ResultState.Success)?.data?.toMutableList() ?: mutableListOf()
            repo.saveQuestion(question, subCategory).collect { updateQuestionState(it, currentQuestions) }
        }
    }

    fun saveReply(questionId: String, reply: Replies, subCategory: String) {
        viewModelScope.launch {
            val currentQuestions = (_questionsState.value as? ResultState.Success)?.data?.toMutableList() ?: mutableListOf()
            repo.saveReply(questionId, reply, subCategory).collect { updateReplyState(it, currentQuestions, questionId) }
        }
    }

    fun deleteQuestion(questionId: String, subCategory: String) = viewModelScope.launch {
        repo.deleteQuestion(questionId, subCategory).collect { result ->
            val unitResult = when (result) {
                is ResultState.Success -> ResultState.Success(Unit)
                is ResultState.Failure -> result // Keep failure as it is
                ResultState.Loading -> ResultState.Loading
            }
            updateDeleteState(unitResult, questionId)
        }
    }

    fun deleteReply(questionId: String, replyId: String, subCategory: String) = viewModelScope.launch {
        repo.deleteReply(questionId, replyId, subCategory).collect { result ->
            val unitResult = when (result) {
                is ResultState.Success -> ResultState.Success(Unit)
                is ResultState.Failure -> result // Keep failure as it is
                ResultState.Loading -> ResultState.Loading
            }
            updateDeleteReplyState(unitResult, questionId, replyId)
        }
    }


    fun updateQuestionReports(questionId: String, userId: String, subCategory: String) = viewModelScope.launch {
        repo.updateQuestionReports(questionId, userId, subCategory).collect { result ->
            updateReportState(result, questionId)
        }
    }

    fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String) = viewModelScope.launch {
        repo.updateReplyReports(questionId, replyId, userId, subCategory).collect { result ->
            updateReplyReportState(result, questionId, replyId)
        }
    }

    fun deleteAllQuestions(subCategory: String) = viewModelScope.launch {
        repo.deleteAllQuestions(subCategory).collect { _questionsState.value = ResultState.Success(emptyList()) }
    }

    private fun <T> updateState(result: ResultState<T>, state: MutableState<T>) {
        when (result) {
            is ResultState.Success -> state.value = result.data as T
            is ResultState.Failure -> Log.e("debug", "Failed: ${result.e}")
            ResultState.Loading -> Log.d("debug", "Loading...")
        }
    }

    private fun updateQuestionState(result: ResultState<Question>, currentQuestions: MutableList<Question>) {
        when (result) {
            is ResultState.Success -> {
                currentQuestions.add(result.data)
                _questionsState.value = ResultState.Success(currentQuestions)
            }
            is ResultState.Failure -> Log.e("debug", "Failed to save question: ${result.e}")
            ResultState.Loading -> Log.d("debug", "Saving question...")
        }
    }

    private fun updateReplyState(result: ResultState<Replies>, currentQuestions: MutableList<Question>, questionId: String) {
        if (result is ResultState.Success) {
            val updatedQuestions = currentQuestions.map { question ->
                if (question.id == questionId) question.copy(replies = question.replies + result.data) else question
            }
            _questionsState.value = ResultState.Success(updatedQuestions)
        }
    }

    private fun updateDeleteState(result: ResultState<Unit>, questionId: String) {
        if (result is ResultState.Success) {
            val updatedQuestions = (_questionsState.value as? ResultState.Success)?.data?.filter { it.id != questionId }
            _questionsState.value = ResultState.Success(updatedQuestions ?: emptyList())
        }
    }

    private fun updateDeleteReplyState(result: ResultState<Unit>, questionId: String, replyId: String) {
        if (result is ResultState.Success) {
            val updatedQuestions = (_questionsState.value as? ResultState.Success)?.data?.map { question ->
                if (question.id == questionId) question.copy(replies = question.replies.filter { it.id != replyId }) else question
            }
            _questionsState.value = ResultState.Success(updatedQuestions ?: emptyList())
        }
    }

    private fun updateReportState(result: ResultState<Question>, questionId: String) {
        if (result is ResultState.Success) {
            val updatedQuestions = (_questionsState.value as? ResultState.Success)?.data?.map { question ->
                if (question.id == questionId) result.data else question
            }
            _questionsState.value = ResultState.Success(updatedQuestions ?: emptyList())
        }
    }

    private fun updateReplyReportState(result: ResultState<Replies>, questionId: String, replyId: String) {
        if (result is ResultState.Success) {
            val updatedQuestions = (_questionsState.value as? ResultState.Success)?.data?.map { question ->
                if (question.id == questionId) {
                    question.copy(replies = question.replies.map { reply -> if (reply.id == replyId) result.data else reply })
                } else question
            }
            _questionsState.value = ResultState.Success(updatedQuestions ?: emptyList())
        }
    }

    /*
    fun containsOffensiveKeywords(message: String, offensiveKeywords: Set<String>): Boolean {
        val words = message.split(" ", "\n", "\t").map { it.lowercase() }
        return words.any { it in offensiveKeywords }
    }
    fun fetchQuestions(subCategory: String) {
        viewModelScope.launch {
            _questionsState.value = ResultState.Loading // Set to loading before fetching new data
            repo.getQuestions(subCategory).collect { result ->
                _questionsState.value = if (result is ResultState.Success && result.data.isEmpty()) {
                    ResultState.Success(emptyList())
                } else {
                    result
                }
            }
        }
    }

    fun saveQuestion(question: Question, subCategory: String) {
        viewModelScope.launch {
            // Fetch the current list of questions from the state
            val currentQuestions = (_questionsState.value as? ResultState.Success)?.data?.toMutableList() ?: mutableListOf()
            Log.d("saveQuestion", "Current questions list size: ${currentQuestions.size}")

            repo.saveQuestion(question, subCategory).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _questionsState.value = ResultState.Loading
                    }
                    is ResultState.Success -> {
                        currentQuestions.add(result.data)
                        Log.d("saveQuestion", "Updated questions list size: ${currentQuestions.size}")

                        _questionsState.value = ResultState.Success(currentQuestions)
                    }
                    is ResultState.Failure -> {
//                        _questionsState.value = ResultState.Failure(result.exception)
                    }
                }
            }
        }
    }

    fun saveReply(questionId: String, reply: Replies, subCategory: String) {
        viewModelScope.launch {
            // Create a copy of the current questions list
            val currentQuestions = (_questionsState.value as? ResultState.Success)?.data?.toMutableList() ?: mutableListOf()

            repo.saveReply(questionId, reply, subCategory).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _questionsState.value = ResultState.Loading
                    }
                    is ResultState.Success -> {
                        // Update the relevant question with the new reply
                        val updatedQuestions = currentQuestions.map { question ->
                            if (question.id == questionId) {
                                val updatedReplies = question.replies.toMutableList().apply {
                                    add(result.data) // Add the new reply
                                }
                                question.copy(replies = updatedReplies)
                            } else {
                                question // Keep other questions unchanged
                            }
                        }

                        // Update the state with the new list
                        _questionsState.value = ResultState.Success(updatedQuestions)
                    }
                    is ResultState.Failure -> {
                        // Optionally handle the error state here
                    }
                }
            }
        }
    }

    /*fun saveQuestion(question: Question, subCategory: String) {
        viewModelScope.launch {
            repo.saveQuestion(question, subCategory).collect { result ->
                _questionsState.value = result
            }
        }
    }
    fun saveReply(questionId: String, reply: Replies, subCategory: String) = viewModelScope.launch {
        viewModelScope.launch {
            repo.saveReply(questionId, reply, subCategory).collect { result ->
                _questionsState.value = result
            }
        }
    }*/
    fun deleteAllQuestions(subCategory: String) = viewModelScope.launch {
        repo.deleteAllQuestions(subCategory).collect { result ->
            when (result) {
                is ResultState.Success -> { _questionsState.value = ResultState.Success(emptyList()) }
                is ResultState.Failure -> { _questionsState.value = ResultState.Failure(result.e) }
                ResultState.Loading -> { _questionsState.value = ResultState.Loading }
            }
        }
    }fun deleteQuestion(questionId: String, subCategory: String) = viewModelScope.launch {
        repo.deleteQuestion(questionId, subCategory).collect { result ->
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

    fun deleteReply(questionId: String, replyId: String, subCategory: String) = viewModelScope.launch {
        repo.deleteReply(questionId, replyId, subCategory).collect { result ->
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

    fun updateQuestionReports(questionId: String, userId: String, subCategory: String) = viewModelScope.launch {
        repo.updateQuestionReports(questionId, userId, subCategory).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    _questionsState.value = _questionsState.value.let { currentState ->
                        if (currentState is ResultState.Success) {
                            val updatedQuestions = currentState.data.map { question ->
                                if (question.id == questionId) result.data else question
                            }
                            ResultState.Success(updatedQuestions)
                        } else {
                            currentState
                        }
                    }
                }
                is ResultState.Failure -> {
                    Log.e("debug", "Failed to update question reports: ${result.e}")
                }
                ResultState.Loading -> {
                    Log.d("debug", "Updating question reports...")
                }
            }
        }
    }

    fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String) = viewModelScope.launch {
        repo.updateReplyReports(questionId, replyId, userId, subCategory).collect { result ->
            when (result) {
                is ResultState.Success -> {
                    val currentState = _questionsState.value
                    if (currentState is ResultState.Success) {
                        val updatedQuestions = currentState.data.map { question ->
                            // Check if the question ID matches
                            if (question.id == questionId) {
                                // Update the specific reply in the replies list
                                val updatedReplies = question.replies.map { reply ->
                                    if (reply.id == replyId) {
                                        result.data // Replace only the specific reply
                                    } else {
                                        reply // Keep other replies unchanged
                                    }
                                }
                                question.copy(replies = updatedReplies) // Create a new question with updated replies
                            } else {
                                question // Keep other questions unchanged
                            }
                        }
                        _questionsState.value = ResultState.Success(updatedQuestions) // Update the state with the modified questions list
                    }
                }
                is ResultState.Failure -> {
                    Log.e("debug", "Failed to update reply reports: ${result.e}")
                }
                ResultState.Loading -> {
                    Log.d("debug", "Updating reply reports...")
                }
            }
        }
    }
*/





    /*
    fun deleteQuestion(questionId: String, subCategory: String) = viewModelScope.launch {
        repo.deleteQuestion(questionId, subCategory).collect { result ->
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
    fun deleteReply(questionId: String, replyId: String, subCategory: String) = viewModelScope.launch {
        repo.deleteReply(questionId, replyId, subCategory).collect { result ->
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


    fun updateQuestionReports(questionId: String, userId: String, subCategory: String) = viewModelScope.launch {
        repo.updateQuestionReports(questionId, userId, subCategory).collect { result ->
            _questionsState.value = result
        }
    }

    fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String) = viewModelScope.launch {
        repo.updateReplyReports(questionId, replyId, userId, subCategory).collect { result ->
            _questionsState.value = result
        }
    }
*/
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
