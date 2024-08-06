package com.firstapplication.file.userAuthentication.firestoredb.repository

import com.firstapplication.file.DataClass.Question
import com.firstapplication.file.DataClass.Replies
import com.firstapplication.file.userAuthentication.firestoredb.module.FirestoreModel
import com.firstapplication.file.userAuthentication.util.ResultState
import kotlinx.coroutines.flow.Flow


interface FirestoreRepository {

    fun insert(
        user: FirestoreModel.FirestoreUser
    ) : Flow<ResultState<String>>

    fun getUsers() : Flow<ResultState<List<FirestoreModel>>>

    fun delete(key:String) : Flow<ResultState<String>>

    fun updateUser(
        user:FirestoreModel
    ) : Flow<ResultState<String>>
    fun getUserByEmail(email:String) : Flow<ResultState<FirestoreModel>>

    fun saveQuestion(question: Question): Flow<ResultState<List<Question>>>
    fun saveReply(questionId: String, reply: Replies): Flow<ResultState<List<Question>>>
    fun getQuestions(): Flow<ResultState<List<Question>>>
    fun deleteQuestion(questionId: String): Flow<ResultState<String>>
    fun deleteReply(questionId: String, replyId: String): Flow<ResultState<String>>
    fun deleteAllQuestions(): Flow<ResultState<String>>
}