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

    fun saveQuestion(question: Question, subCategory: String): Flow<ResultState<Question>>
    fun saveReply(questionId: String, reply: Replies, subCategory: String): Flow<ResultState<Replies>>
    /* fun saveQuestion(question: Question, subCategory: String): Flow<ResultState<List<Question>>>
     fun saveReply(questionId: String, reply: Replies, subCategory: String): Flow<ResultState<List<Question>>>

     */
    fun deleteAllQuestions(subCategory: String): Flow<ResultState<String>>
    fun getQuestions(subCategory: String): Flow<ResultState<List<Question>>>
    fun deleteQuestion(questionId: String, subCategory: String): Flow<ResultState<String>>
    fun deleteReply(questionId: String, replyId: String, subCategory: String): Flow<ResultState<String>>


    // New methods

    fun updateQuestionReports(questionId: String, userId: String, subCategory: String): Flow<ResultState<Question>>
    fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String): Flow<ResultState<Replies>>
//    fun updateQuestionReports(questionId: String, userId: String, subCategory: String): Flow<ResultState<List<Question>>>
//    fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String): Flow<ResultState<List<Question>>>
}