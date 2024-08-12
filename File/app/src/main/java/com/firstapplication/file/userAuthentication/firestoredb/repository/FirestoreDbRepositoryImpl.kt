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
                                Replies(
                                    id = replyId,
                                    userName = replyUserName,
                                    subComment = subComment,
                                    timestamp = replyTimestamp
                                )
                            }
                            questions.add(
                                Question(
                                    id = id,
                                    userName = userName ?: "",
                                    comment = comment ?: "",
                                    replies = replies,
                                    timestamp = timestamp
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

    override fun saveQuestion(question: Question, subCategory: String): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)

        // Add timestamp to question
        val questionWithTimestamp = question.copy(timestamp = System.currentTimeMillis())
        // Step 1: Save the question
        val saveTask = db.collection(subCategory)
            .add(questionWithTimestamp)
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
                                        Replies(id = replyId, userName = replyUserName, subComment = subComment, timestamp = replyTimestamp)
                                    }

                                    // Add question with replies to the list
                                    questions.add(
                                        Question(
                                            id = id,
                                            userName = userName ?: "",
                                            comment = comment ?: "",
                                            replies = replies,
                                            timestamp = timestamp
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
            "timestamp" to replyWithTimestamp.timestamp // Include timestamp
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
                                        Replies(id = replyId, userName = replyUserName, subComment = subComment, timestamp = replyTimestamp)
                                    }

                                    // Add question with replies to the list
                                    questions.add(
                                        Question(
                                            id = id,
                                            userName = userName ?: "",
                                            comment = comment ?: "",
                                            replies = replies,
                                            timestamp = timestamp
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

}