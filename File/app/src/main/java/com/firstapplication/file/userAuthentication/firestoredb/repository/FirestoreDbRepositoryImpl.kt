package com.firstapplication.file.userAuthentication.firestoredb.repository

import android.util.Log
import com.firstapplication.file.DataClass.Question
import com.firstapplication.file.DataClass.Replies
import com.firstapplication.file.userAuthentication.firestoredb.module.FirestoreModel
import com.firstapplication.file.userAuthentication.util.ResultState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FirestoreRepository {

    override fun insert(user: FirestoreModel.FirestoreUser): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("user")
            .add(user)
            .addOnSuccessListener {
                trySend(ResultState.Success("Data is inserted with ${it.id}"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun getUsers(): Flow<ResultState<List<FirestoreModel>>> =  callbackFlow{
        trySend(ResultState.Loading)
        db.collection("user")
            .get()
            .addOnSuccessListener {
                val users =  it.map { data->
                    FirestoreModel(
                        user = FirestoreModel.FirestoreUser(
                            name = data["name"] as String?,
                            email = data["email"] as String?,
                            password =data["password"] as String?,
                            coins = (data["coins"] as? Long)?.toInt()
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(users))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("user")
            .document(key)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(ResultState.Success("Deleted successfully.."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun updateUser(user: FirestoreModel): Flow<ResultState<String>> = callbackFlow {
        try {

            val map = HashMap<String, Any>()
            map["name"] = user.user?.name!!
            map["email"] = user.user.email!!
            map["password"] = user.user.password!!
            map["coins"] = user.user.coins!!
            db.collection("user")
                .document(user.key!!)
                .update(map)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(ResultState.Success("Update successfully..."))
                    } else {
                        trySend(ResultState.Failure(task.exception ?: Exception("Unknown error")))
                    }
                    close()
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                    close()
                }
        } catch (e: Exception) {
            trySend(ResultState.Failure(e))
            close()
        }

        awaitClose {}
    }

    override fun getUserByEmail(email: String): Flow<ResultState<FirestoreModel>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("user")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                val user =  it.map { data->
                    FirestoreModel(
                        user = FirestoreModel.FirestoreUser(
                            name = data["name"] as String?,
                            email = data["email"] as String?,
                            password =data["password"] as String?,
                            coins = (data["coins"] as? Long)?.toInt()
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(user.first()))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    /*   override fun saveQuestion(question: Question, subCategory: String): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)

        // Add timestamp to question
        val questionWithTimestamp = question.copy(timestamp = System.currentTimeMillis())

        // Prepare question data including reports
        val questionData = mapOf(
            "userName" to questionWithTimestamp.userName,
            "comment" to questionWithTimestamp.comment,
            "timestamp" to questionWithTimestamp.timestamp,
            "reports" to questionWithTimestamp.reportedUsers // Include reports field
        )

        // Step 1: Save the question
        val saveTask = db.collection(subCategory)
            .add(questionData )
            .addOnSuccessListener { documentReference ->
                // Step 2: Fetch all questions after saving
                val fetchTask = db.collection(subCategory)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val questions = mutableListOf<Question>()
                        val tasks = mutableListOf<Task<*>>() // Collect tasks for fetching replies

                        snapshot.forEach { document ->
                            val id = document.id
                            val userName = document.getString("userName")
                            val comment = document.getString("comment")
                            val timestamp = document.getLong("timestamp") ?: 0L
                            val reports = document.get("reports") as? List<String> ?: emptyList()

                            // Fetch replies for each question
                            val repliesTask = db.collection(subCategory)
                                .document(id)
                                .collection("replies")
                                .get()
                                .addOnSuccessListener { repliesSnapshot ->
                                    val replies = repliesSnapshot.map { replyDoc ->
                                        val replyId = replyDoc.id
                                        val replyUserName = replyDoc.getString("userName") ?: ""
                                        val subComment = replyDoc.getString("subComment") ?: ""
                                        val replyTimestamp = replyDoc.getLong("timestamp") ?: 0L
                                        val replyReports = replyDoc.get("reports") as? List<String> ?: emptyList()
                                        Replies(id = replyId, userName = replyUserName, subComment = subComment, timestamp = replyTimestamp, reportedUsers = replyReports)
                                    }

                                    // Add question with replies to the list
                                    questions.add(
                                        Question(
                                            id = id,
                                            userName = userName ?: "",
                                            comment = comment ?: "",
                                            replies = replies,
                                            timestamp = timestamp,
                                            reportedUsers = reports
                                        )
                                    )

                                    // If all questions are processed, send the result
                                    if (questions.size == snapshot.size()) {
                                        trySend(ResultState.Success(questions))
                                    }
                                }
                                .addOnFailureListener { e ->
                                    trySend(ResultState.Failure(e))
                                }

                            tasks.add(repliesTask)
                        }

                        // Wait for all replies fetching tasks to complete
                        Tasks.whenAllComplete(tasks)
                            .addOnCompleteListener {
                                // Handle result of all tasks completion
                                if (it.isSuccessful) {
                                    // Successful completion, but result is already handled in the individual tasks
                                } else {
                                    trySend(ResultState.Failure(it.exception ?: Exception("Unknown error")))
                                }
                            }
                    }
                    .addOnFailureListener { e ->
                        trySend(ResultState.Failure(e))
                    }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }

    override fun saveReply(questionId: String, reply: Replies, subCategory: String): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)

        // Add timestamp to reply
        val replyWithTimestamp = reply.copy(timestamp = System.currentTimeMillis())

        // Prepare reply data
        val replyData = mapOf(
            "userName" to reply.userName,
            "subComment" to reply.subComment,
            "timestamp" to replyWithTimestamp.timestamp, // Include timestamp
            "reports" to replyWithTimestamp.reportedUsers // Include reports field
        )

        // Save the reply
        val saveReplyTask = db.collection(subCategory)
            .document(questionId)
            .collection("replies")
            .add(replyData)
            .addOnSuccessListener {
                // After saving reply, fetch all questions
                val fetchQuestionsTask = db.collection(subCategory)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val questions = mutableListOf<Question>()
                        val tasks = mutableListOf<Task<*>>() // Collect tasks for fetching replies

                        snapshot.forEach { document ->
                            val id = document.id
                            val userName = document.getString("userName")
                            val comment = document.getString("comment")
                            val timestamp = document.getLong("timestamp") ?: 0L
                            val reports = document.get("reports") as? List<String> ?: emptyList()

                            // Fetch replies for each question
                            val repliesTask = db.collection(subCategory)
                                .document(id)
                                .collection("replies")
                                .get()
                                .addOnSuccessListener { repliesSnapshot ->
                                    val replies = repliesSnapshot.map { replyDoc ->
                                        val replyId = replyDoc.id
                                        val replyUserName = replyDoc.getString("userName") ?: ""
                                        val subComment = replyDoc.getString("subComment") ?: ""
                                        val replyTimestamp = replyDoc.getLong("timestamp") ?: 0L
                                        val replyReports = replyDoc.get("reports") as? List<String> ?: emptyList()
                                        Replies(id = replyId, userName = replyUserName, subComment = subComment, timestamp = replyTimestamp, reportedUsers = replyReports)
                                    }

                                    // Add question with replies to the list
                                    questions.add(
                                        Question(
                                            id = id,
                                            userName = userName ?: "",
                                            comment = comment ?: "",
                                            replies = replies,
                                            timestamp = timestamp,
                                            reportedUsers = reports
                                        )
                                    )

                                    // If all questions are processed, send the result
                                    if (questions.size == snapshot.size()) {
                                        trySend(ResultState.Success(questions))
                                    }
                                }
                                .addOnFailureListener { e ->
                                    trySend(ResultState.Failure(e))
                                }

                            tasks.add(repliesTask)
                        }

                        // Wait for all replies fetching tasks to complete
                        Tasks.whenAllComplete(tasks)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    // Successful completion, but result is already handled in the individual tasks
                                } else {
                                    trySend(ResultState.Failure(it.exception ?: Exception("Unknown error")))
                                }
                            }
                    }
                    .addOnFailureListener { e ->
                        trySend(ResultState.Failure(e))
                    }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }
*/





    override fun getQuestions(subCategory: String): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection(subCategory).get()
            .addOnSuccessListener { snapshot ->
                val questions = mutableListOf<Question>()
                val tasks = snapshot.documents.map { document ->
                    val id = document.id
                    val userName = document.getString("userName") ?: ""
                    val comment = document.getString("comment") ?: ""
                    val timestamp = document.getLong("timestamp") ?: 0L
                    val reports = document.get("reports") as? List<String> ?: emptyList()

                    db.collection(subCategory).document(id).collection("replies").get()
                        .continueWith { replyTask ->
                            val replies = replyTask.result?.documents?.map { replyDoc ->
                                Replies(
                                    id = replyDoc.id,
                                    userName = replyDoc.getString("userName") ?: "",
                                    subComment = replyDoc.getString("subComment") ?: "",
                                    timestamp = replyDoc.getLong("timestamp") ?: 0L,
                                    reportedUsers = replyDoc.get("reports") as? List<String> ?: emptyList()
                                )
                            } ?: emptyList()

                            questions.add(
                                Question(
                                    id = id,
                                    userName = userName,
                                    comment = comment,
                                    replies = replies,
                                    timestamp = timestamp,
                                    reportedUsers = reports
                                )
                            )
                        }
                }

                Tasks.whenAll(tasks).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ResultState.Success(questions))
                    } else {
                        trySend(ResultState.Failure(it.exception ?: Exception("Error fetching questions")))
                    }
                }.addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }

    override fun saveQuestion(question: Question, subCategory: String): Flow<ResultState<Question>> = callbackFlow {
        trySend(ResultState.Loading)

        val questionData = mapOf(
            "userName" to question.userName,
            "comment" to question.comment,
            "timestamp" to System.currentTimeMillis(),
            "reports" to question.reportedUsers
        )

        db.collection(subCategory).add(questionData)
            .addOnSuccessListener { documentReference ->
                documentReference.get()
                    .addOnSuccessListener { document ->
                        trySend(ResultState.Success(Question(
                            id = document.id,
                            userName = document.getString("userName") ?: "",
                            comment = document.getString("comment") ?: "",
                            replies = emptyList(),
                            timestamp = document.getLong("timestamp") ?: 0L,
                            reportedUsers = document.get("reports") as? List<String> ?: emptyList()
                        )))
                    }
                    .addOnFailureListener { e ->
                        trySend(ResultState.Failure(e))
                    }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }

    override fun saveReply(questionId: String, reply: Replies, subCategory: String): Flow<ResultState<Replies>> = callbackFlow {
        trySend(ResultState.Loading)

        val replyData = mapOf(
            "userName" to reply.userName,
            "subComment" to reply.subComment,
            "timestamp" to System.currentTimeMillis(),
            "reports" to reply.reportedUsers
        )

        db.collection(subCategory).document(questionId).collection("replies").add(replyData)
            .addOnSuccessListener { documentReference ->
                documentReference.get()
                    .addOnSuccessListener { replyDoc ->
                        trySend(ResultState.Success(Replies(
                            id = replyDoc.id,
                            userName = replyDoc.getString("userName") ?: "",
                            subComment = replyDoc.getString("subComment") ?: "",
                            timestamp = replyDoc.getLong("timestamp") ?: 0L,
                            reportedUsers = replyDoc.get("reports") as? List<String> ?: emptyList()
                        )))
                    }
                    .addOnFailureListener { e ->
                        trySend(ResultState.Failure(e))
                    }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }

    override fun deleteAllQuestions(subCategory: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection(subCategory).get()
            .addOnSuccessListener { snapshot ->
                val batch = db.batch()
                val tasks = snapshot.documents.map { document ->
                    // Fetch replies and add delete operations to the batch
                    val repliesRef = document.reference.collection("replies").get()
                    repliesRef.addOnSuccessListener { repliesSnapshot ->
                        repliesSnapshot.documents.forEach { replyDoc ->
                            batch.delete(replyDoc.reference)
                        }
                        batch.delete(document.reference)
                    }.addOnFailureListener { e ->
                        trySend(ResultState.Failure(e))
                    }
                    repliesRef // Return the replies task for whenAll
                }

                Tasks.whenAll(tasks)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            batch.commit()
                                .addOnSuccessListener { trySend(ResultState.Success("All questions and replies deleted")) }
                                .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }
                        } else {
                            trySend(ResultState.Failure(task.exception ?: Exception("Failed to delete all questions and replies")))
                        }
                    }
                    .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }
            }
            .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }

        awaitClose { close() }
    }



    override fun deleteQuestion(questionId: String, subCategory: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val questionRef = db.collection(subCategory).document(questionId)
        val repliesRef = questionRef.collection("replies").get()
            .addOnSuccessListener { repliesSnapshot ->
                val batch = db.batch()
                repliesSnapshot.documents.forEach { batch.delete(it.reference) }
                batch.delete(questionRef).commit()
                    .addOnSuccessListener { trySend(ResultState.Success(questionId)) }
                    .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }
            }
            .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }

        awaitClose { close() }
    }

    override fun deleteReply(questionId: String, replyId: String, subCategory: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection(subCategory).document(questionId).collection("replies").document(replyId).delete()
            .addOnSuccessListener { trySend(ResultState.Success(replyId)) }
            .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }

        awaitClose { close() }
    }

    override fun updateQuestionReports(questionId: String, userId: String, subCategory: String): Flow<ResultState<Question>> = callbackFlow {
        trySend(ResultState.Loading)

        val questionRef = db.collection(subCategory).document(questionId)
        questionRef.get()
            .addOnSuccessListener { document ->
                val reports = document.get("reports") as? List<String> ?: emptyList()
                if (userId !in reports) {
                    val updatedReports = reports + userId
                    questionRef.update("reports", updatedReports)
                        .addOnSuccessListener {
                            trySend(ResultState.Success(Question(
                                id = document.id,
                                userName = document.getString("userName") ?: "",
                                comment = document.getString("comment") ?: "",
                                replies = emptyList(),
                                timestamp = document.getLong("timestamp") ?: 0L,
                                reportedUsers = updatedReports
                            )))
                        }
                        .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }
                } else {
                    trySend(ResultState.Failure(Exception("User already reported this question")))
                }
            }
            .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }

        awaitClose { close() }
    }

    override fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String): Flow<ResultState<Replies>> = callbackFlow {
        trySend(ResultState.Loading)

        val replyRef = db.collection(subCategory).document(questionId).collection("replies").document(replyId)
        replyRef.get()
            .addOnSuccessListener { document ->
                val reports = document.get("reports") as? List<String> ?: emptyList()
                if (userId !in reports) {
                    val updatedReports = reports + userId
                    replyRef.update("reports", updatedReports)
                        .addOnSuccessListener {
                            trySend(ResultState.Success(Replies(
                                id = document.id,
                                userName = document.getString("userName") ?: "",
                                subComment = document.getString("subComment") ?: "",
                                timestamp = document.getLong("timestamp") ?: 0L,
                                reportedUsers = updatedReports
                            )))
                        }
                        .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }
                } else {
                    trySend(ResultState.Failure(Exception("User already reported this reply")))
                }
            }
            .addOnFailureListener { e -> trySend(ResultState.Failure(e)) }

        awaitClose { close() }
    }









/*
    override fun getQuestions(subCategory: String): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection(subCategory)
            .get()
            .addOnSuccessListener { snapshot ->
                val questions = mutableListOf<Question>()

                snapshot.forEach { document ->
                    val id = document.id
                    val userName = document.getString("userName")
                    val comment = document.getString("comment")
                    val timestamp = document.getLong("timestamp") ?: 0L // Fetch timestamp
                    val reports = document.get("reports") as? List<String> ?: emptyList()

                    // Fetch replies from the sub-collection
                    db.collection(subCategory)
                        .document(id)
                        .collection("replies")
                        .get()
                        .addOnSuccessListener { repliesSnapshot ->
                            val replies = repliesSnapshot.map { replyDoc ->
                                val replyId = replyDoc.id
                                println("ReplyID : $replyId")
                                val replyUserName = replyDoc.getString("userName") ?: ""
                                val subComment = replyDoc.getString("subComment") ?: ""
                                val replyTimestamp = replyDoc.getLong("timestamp") ?: 0L // Fetch reply timestamp
                                val replyReports = replyDoc.get("reports") as? List<String> ?: emptyList()
                                Replies(
                                    id = replyId,
                                    userName = replyUserName,
                                    subComment = subComment,
                                    timestamp = replyTimestamp,
                                    reportedUsers = replyReports
                                )
                            }
                            questions.add(
                                Question(
                                    id = id,
                                    userName = userName ?: "",
                                    comment = comment ?: "",
                                    replies = replies,
                                    timestamp = timestamp,
                                    reportedUsers = reports
                                )
                            )

                            // Send the result when all documents are processed
                            trySend(ResultState.Success(questions))
                        }
                        .addOnFailureListener { e ->
                            trySend(ResultState.Failure(e))
                        }
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }
        awaitClose { close() }
    }

    override fun saveQuestion(question: Question, subCategory: String): Flow<ResultState<Question>> = callbackFlow {
        trySend(ResultState.Loading)

        // Add timestamp to question
        val questionWithTimestamp = question.copy(timestamp = System.currentTimeMillis())

        // Prepare question data including reports
        val questionData = mapOf(
            "userName" to questionWithTimestamp.userName,
            "comment" to questionWithTimestamp.comment,
            "timestamp" to questionWithTimestamp.timestamp,
            "reports" to questionWithTimestamp.reportedUsers // Include reports field
        )

        // Save the question
        val saveTask = db.collection(subCategory)
            .add(questionData)
            .addOnSuccessListener { documentReference ->
                // Fetch the newly added question
                documentReference.get().addOnSuccessListener { document ->
                    val id = document.id
                    val userName = document.getString("userName")
                    val comment = document.getString("comment")
                    val timestamp = document.getLong("timestamp") ?: 0L
                    val reports = document.get("reports") as? List<String> ?: emptyList()

                    val newQuestion = Question(
                        id = id,
                        userName = userName ?: "",
                        comment = comment ?: "",
                        replies = emptyList(),
                        timestamp = timestamp,
                        reportedUsers = reports
                    )

                    trySend(ResultState.Success(newQuestion))
                }.addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }

    override fun saveReply(questionId: String, reply: Replies, subCategory: String): Flow<ResultState<Replies>> = callbackFlow {
        trySend(ResultState.Loading)

        // Add timestamp to reply
        val replyWithTimestamp = reply.copy(timestamp = System.currentTimeMillis())

        // Prepare reply data
        val replyData = mapOf(
            "userName" to replyWithTimestamp.userName,
            "subComment" to replyWithTimestamp.subComment,
            "timestamp" to replyWithTimestamp.timestamp, // Include timestamp
            "reports" to replyWithTimestamp.reportedUsers // Include reports field
        )

        // Save the reply
        val saveReplyTask = db.collection(subCategory)
            .document(questionId)
            .collection("replies")
            .add(replyData)
            .addOnSuccessListener { documentReference ->
                // Fetch the newly added reply
                documentReference.get().addOnSuccessListener { replyDoc ->
                    val replyId = replyDoc.id
                    val replyUserName = replyDoc.getString("userName") ?: ""
                    val subComment = replyDoc.getString("subComment") ?: ""
                    val replyTimestamp = replyDoc.getLong("timestamp") ?: 0L
                    val replyReports = replyDoc.get("reports") as? List<String> ?: emptyList()

                    val newReply = Replies(
                        id = replyId,
                        userName = replyUserName,
                        subComment = subComment,
                        timestamp = replyTimestamp,
                        reportedUsers = replyReports
                    )

                    trySend(ResultState.Success(newReply))
                }.addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }


    override fun deleteAllQuestions(subCategory: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        // Fetch all questions in the subCategory
        db.collection(subCategory)
            .get()
            .addOnSuccessListener { snapshot ->
                val batch = db.batch()

                val tasks = snapshot.map { document ->
                    val repliesCollectionRef = document.reference.collection("replies")

                    repliesCollectionRef.get()
                        .continueWith { repliesTask ->
                            if (repliesTask.isSuccessful) {
                                repliesTask.result?.forEach { replyDoc ->
                                    batch.delete(replyDoc.reference)
                                }
                            }
                            // Add the deletion of the main question document to the batch
                            batch.delete(document.reference)
                        }
                }

                // Wait for all tasks to complete
                Tasks.whenAll(tasks)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            batch.commit()
                                .addOnSuccessListener {
                                    trySend(ResultState.Success("All questions and their replies deleted successfully."))
                                }
                                .addOnFailureListener { e ->
                                    trySend(ResultState.Failure(e))
                                }
                        } else {
                            trySend(ResultState.Failure(task.exception ?: Exception("Failed to delete all questions and replies.")))
                        }
                    }
                    .addOnFailureListener { e ->
                        trySend(ResultState.Failure(e))
                    }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }
    override fun deleteQuestion(questionId: String, subCategory: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val questionDocRef = db.collection(subCategory).document(questionId)
        val repliesCollectionRef = questionDocRef.collection("replies")

        // Delete replies sub-collection
        repliesCollectionRef.get()
            .addOnCompleteListener { repliesTask ->
                if (repliesTask.isSuccessful) {
                    val batch = db.batch()
                    for (document in repliesTask.result) {
                        batch.delete(document.reference)
                    }
                    batch.commit()
                        .addOnCompleteListener { batchTask ->
                            if (batchTask.isSuccessful) {
                                // Now delete the question document
                                questionDocRef.delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            trySend(ResultState.Success(questionId)) // Return only the deleted question ID
                                        } else {
                                            trySend(ResultState.Failure(task.exception ?: Exception("Failed to delete question.")))
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        trySend(ResultState.Failure(e))
                                    }
                            } else {
                                trySend(ResultState.Failure(batchTask.exception ?: Exception("Failed to delete replies.")))
                            }
                        }
                        .addOnFailureListener { e ->
                            trySend(ResultState.Failure(e))
                        }
                } else {
                    trySend(ResultState.Failure(repliesTask.exception ?: Exception("Failed to retrieve replies.")))
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }

    override fun deleteReply(questionId: String, replyId: String, subCategory: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val replyDocRef = db.collection(subCategory)
            .document(questionId)
            .collection("replies")
            .document(replyId)

        replyDocRef
            .delete()
            .addOnSuccessListener {
                trySend(ResultState.Success(replyId)) // Return only the deleted reply ID
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }
        awaitClose { close() }
    }

    override fun updateQuestionReports(questionId: String, userId: String, subCategory: String): Flow<ResultState<Question>> = callbackFlow {
        trySend(ResultState.Loading)

        val questionDocRef = db.collection(subCategory).document(questionId)
        questionDocRef.get()
            .addOnSuccessListener { questionDoc ->
                if (questionDoc.exists()) {
                    val reports = questionDoc.get("reports") as? List<String> ?: emptyList()

                    if (userId !in reports) {
                        val updatedReports = reports + userId
                        questionDocRef.update("reports", updatedReports)
                            .addOnSuccessListener {
                                // Return the updated question
                                val updatedQuestion = Question(
                                    id = questionDoc.id,
                                    userName = questionDoc.getString("userName") ?: "",
                                    comment = questionDoc.getString("comment") ?: "",
                                    replies = emptyList(), // You can update replies separately if needed
                                    timestamp = questionDoc.getLong("timestamp") ?: 0L,
                                    reportedUsers = updatedReports
                                )
                                trySend(ResultState.Success(updatedQuestion))
                            }
                            .addOnFailureListener { e ->
                                trySend(ResultState.Failure(e))
                            }
                    } else {
                        trySend(ResultState.Failure(Exception("User already reported this question")))
                    }
                } else {
                    trySend(ResultState.Failure(Exception("Question not found")))
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }

    override fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String): Flow<ResultState<Replies>> = callbackFlow {
        trySend(ResultState.Loading)

        val replyDocRef = db.collection(subCategory)
            .document(questionId)
            .collection("replies")
            .document(replyId)

        replyDocRef.get()
            .addOnSuccessListener { replyDoc ->
                if (replyDoc.exists()) {
                    val reports = replyDoc.get("reports") as? List<String> ?: emptyList()

                    if (userId !in reports) {
                        val updatedReports = reports + userId
                        replyDocRef.update("reports", updatedReports)
                            .addOnSuccessListener {
                                // Return the updated reply
                                val updatedReply = Replies(
                                    id = replyDoc.id,
                                    userName = replyDoc.getString("userName") ?: "",
                                    subComment = replyDoc.getString("subComment") ?: "",
                                    timestamp = replyDoc.getLong("timestamp") ?: 0L,
                                    reportedUsers = updatedReports
                                )
                                trySend(ResultState.Success(updatedReply))
                            }
                            .addOnFailureListener { e ->
                                trySend(ResultState.Failure(e))
                            }
                    } else {
                        trySend(ResultState.Failure(Exception("User already reported this reply")))
                    }
                } else {
                    trySend(ResultState.Failure(Exception("Reply not found")))
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }

        awaitClose { close() }
    }
*/

    /*
        override fun deleteQuestion(questionId: String, subCategory: String): Flow<ResultState<String>> = callbackFlow {
            trySend(ResultState.Loading)

            val questionDocRef = db.collection(subCategory).document(questionId)
            val repliesCollectionRef = questionDocRef.collection("replies")

            // Delete replies sub-collection
            repliesCollectionRef.get()
                .addOnCompleteListener { repliesTask ->
                    if (repliesTask.isSuccessful) {
                        val batch = db.batch()
                        for (document in repliesTask.result) {
                            batch.delete(document.reference)
                        }
                        batch.commit()
                            .addOnCompleteListener { batchTask ->
                                if (batchTask.isSuccessful) {
                                    // Now delete the question document
                                    questionDocRef.delete()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                trySend(ResultState.Success("Question and replies deleted successfully."))
                                            } else {
                                                trySend(ResultState.Failure(task.exception ?: Exception("Failed to delete question.")))
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            trySend(ResultState.Failure(e))
                                        }
                                } else {
                                    trySend(ResultState.Failure(batchTask.exception ?: Exception("Failed to delete replies.")))
                                }
                            }
                            .addOnFailureListener { e ->
                                trySend(ResultState.Failure(e))
                            }
                    } else {
                        trySend(ResultState.Failure(repliesTask.exception ?: Exception("Failed to retrieve replies.")))
                    }
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                }

            awaitClose { close() }
        }

        override fun deleteReply(questionId: String, replyId: String, subCategory: String): Flow<ResultState<String>> = callbackFlow {
            trySend(ResultState.Loading)
            val replyDocRef = db.collection(subCategory)
                .document(questionId)
                .collection("replies")
                .document(replyId)

            // Logging the document reference
            Log.d("FirestoreDebug", "Attempting to delete document at: ${replyDocRef.path}")

            replyDocRef
                .delete()
                .addOnSuccessListener {
                    Log.d("FirestoreDebug", "Reply deleted successfully: $replyId")
                    trySend(ResultState.Success("Reply deleted successfully."))
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreDebug", "Failed to delete reply: $replyId", e)
                    trySend(ResultState.Failure(e))
                }
            awaitClose { close() }
        }



        override fun updateQuestionReports(questionId: String, userId: String, subCategory: String): Flow<ResultState<List<Question>>> = callbackFlow {
            trySend(ResultState.Loading)

            // Fetch the question document
            val questionDocRef = db.collection(subCategory).document(questionId)
            questionDocRef.get()
                .addOnSuccessListener { questionDoc ->
                    if (questionDoc.exists()) {
                        val reports = questionDoc.get("reports") as? List<String> ?: emptyList()

                        if (userId in reports) {
                            // User is already in the list, fetch the latest list of questions
                            val fetchQuestionsTask = db.collection(subCategory).get()
                            fetchQuestionsTask
                                .addOnSuccessListener { snapshot ->
                                    val questions = snapshot.map { document ->
                                        val id = document.id
                                        val userName = document.getString("userName") ?: ""
                                        val comment = document.getString("comment") ?: ""
                                        val timestamp = document.getLong("timestamp") ?: 0L
                                        val reports = document.get("reports") as? List<String> ?: emptyList()
                                        Question(id, userName, comment, emptyList(), timestamp, reports)
                                    }
                                    trySend(ResultState.Success(questions))
                                }
                                .addOnFailureListener { e ->
                                    trySend(ResultState.Failure(e))
                                }
                        } else {
                            // User is not in the list, update the list and fetch questions
                            val updatedReports = reports + userId
                            questionDocRef.update("reports", updatedReports)
                                .addOnSuccessListener {
                                    val fetchQuestionsTask = db.collection(subCategory).get()
                                    fetchQuestionsTask
                                        .addOnSuccessListener { snapshot ->
                                            val questions = snapshot.map { document ->
                                                val id = document.id
                                                val userName = document.getString("userName") ?: ""
                                                val comment = document.getString("comment") ?: ""
                                                val timestamp = document.getLong("timestamp") ?: 0L
                                                val reports = document.get("reports") as? List<String> ?: emptyList()
                                                Question(id, userName, comment, emptyList(), timestamp, reports)
                                            }
                                            trySend(ResultState.Success(questions))
                                        }
                                        .addOnFailureListener { e ->
                                            trySend(ResultState.Failure(e))
                                        }
                                }
                                .addOnFailureListener { e ->
                                    trySend(ResultState.Failure(e))
                                }
                        }
                    } else {
                        trySend(ResultState.Failure(Exception("Question not found")))
                    }
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                }

            awaitClose { close() }
        }
        override fun updateReplyReports(questionId: String, replyId: String, userId: String, subCategory: String): Flow<ResultState<List<Question>>> = callbackFlow {
            trySend(ResultState.Loading)

            // Fetch the reply document
            val replyDocRef = db.collection(subCategory)
                .document(questionId)
                .collection("replies")
                .document(replyId)

            replyDocRef.get()
                .addOnSuccessListener { replyDoc ->
                    if (replyDoc.exists()) {
                        val reports = replyDoc.get("reports") as? List<String> ?: emptyList()

                        if (userId in reports) {
                            // User is already in the list, fetch the latest list of questions
                            val fetchQuestionsTask = db.collection(subCategory).get()
                            fetchQuestionsTask
                                .addOnSuccessListener { snapshot ->
                                    val questions = snapshot.map { document ->
                                        val id = document.id
                                        val userName = document.getString("userName") ?: ""
                                        val comment = document.getString("comment") ?: ""
                                        val timestamp = document.getLong("timestamp") ?: 0L
                                        val reports = document.get("reports") as? List<String> ?: emptyList()
                                        Question(id, userName, comment, emptyList(), timestamp, reports)
                                    }
                                    trySend(ResultState.Success(questions))
                                }
                                .addOnFailureListener { e ->
                                    trySend(ResultState.Failure(e))
                                }
                        } else {
                            // User is not in the list, update the list and fetch questions
                            val updatedReports = reports + userId
                            replyDocRef.update("reports", updatedReports)
                                .addOnSuccessListener {
                                    val fetchQuestionsTask = db.collection(subCategory).get()
                                    fetchQuestionsTask
                                        .addOnSuccessListener { snapshot ->
                                            val questions = snapshot.map { document ->
                                                val id = document.id
                                                val userName = document.getString("userName") ?: ""
                                                val comment = document.getString("comment") ?: ""
                                                val timestamp = document.getLong("timestamp") ?: 0L
                                                val reports = document.get("reports") as? List<String> ?: emptyList()
                                                Question(id, userName, comment, emptyList(), timestamp, reports)
                                            }
                                            trySend(ResultState.Success(questions))
                                        }
                                        .addOnFailureListener { e ->
                                            trySend(ResultState.Failure(e))
                                        }
                                }
                                .addOnFailureListener { e ->
                                    trySend(ResultState.Failure(e))
                                }
                        }
                    } else {
                        trySend(ResultState.Failure(Exception("Reply not found")))
                    }
                }
                .addOnFailureListener { e ->
                    trySend(ResultState.Failure(e))
                }

            awaitClose { close() }
        }
    */
}