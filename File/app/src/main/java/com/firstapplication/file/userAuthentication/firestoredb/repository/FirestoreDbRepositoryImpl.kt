package com.firstapplication.file.userAuthentication.firestoredb.repository

import android.util.Log
import com.firstapplication.file.DataClass.Question
import com.firstapplication.file.DataClass.Replies
import com.firstapplication.file.userAuthentication.firestoredb.module.FirestoreModel
import com.firstapplication.file.userAuthentication.util.ResultState
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
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

    override fun getQuestions(): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("questions")
            .get()
            .addOnSuccessListener { snapshot ->
                val questions = mutableListOf<Question>()

                snapshot.forEach { document ->
                    val id = document.id
                    val userName = document.getString("userName")
                    val comment = document.getString("comment")

                    // Fetch replies from the sub-collection
                    db.collection("questions")
                        .document(id)
                        .collection("replies")
                        .get()
                        .addOnSuccessListener { repliesSnapshot ->
                            val replies = repliesSnapshot.map { replyDoc ->
                                val replyId = replyDoc.id
                                println("ReplyID : $replyId")
                                val replyUserName = replyDoc.getString("userName") ?: ""
                                val subComment = replyDoc.getString("subComment") ?: ""
                                Replies(
                                    id = replyId,
                                    userName = replyUserName,
                                    subComment = subComment
                                )
                            }


                            questions.add(
                                Question(
                                    id = id,
                                    userName = userName ?: "",
                                    comment = comment ?: "",
                                    replies = replies
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

    override fun saveQuestion(question: Question): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)

        // Step 1: Save the question
        val saveTask = db.collection("questions")
            .add(question)
            .addOnSuccessListener { documentReference ->
                // Step 2: Fetch all questions after saving
                val fetchTask = db.collection("questions")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val questions = mutableListOf<Question>()
                        val tasks = mutableListOf<Task<*>>() // Collect tasks for fetching replies

                        snapshot.forEach { document ->
                            val id = document.id
                            val userName = document.getString("userName")
                            val comment = document.getString("comment")

                            // Fetch replies for each question
                            val repliesTask = db.collection("questions")
                                .document(id)
                                .collection("replies")
                                .get()
                                .addOnSuccessListener { repliesSnapshot ->
                                    val replies = repliesSnapshot.map { replyDoc ->
                                        val replyId = replyDoc.id
                                        val replyUserName = replyDoc.getString("userName") ?: ""
                                        val subComment = replyDoc.getString("subComment") ?: ""
                                        Replies(id = replyId, userName = replyUserName, subComment = subComment)
                                    }

                                    // Add question with replies to the list
                                    questions.add(
                                        Question(
                                            id = id,
                                            userName = userName ?: "",
                                            comment = comment ?: "",
                                            replies = replies
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

    override fun saveReply(questionId: String, reply: Replies): Flow<ResultState<List<Question>>> = callbackFlow {
        trySend(ResultState.Loading)

        // Prepare reply data
        val replyData = mapOf(
            "userName" to reply.userName,
            "subComment" to reply.subComment
        )

        // Save the reply
        val saveReplyTask = db.collection("questions")
            .document(questionId)
            .collection("replies")
            .add(replyData)
            .addOnSuccessListener {
                // After saving reply, fetch all questions
                val fetchQuestionsTask = db.collection("questions")
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val questions = mutableListOf<Question>()
                        val tasks = mutableListOf<Task<*>>() // Collect tasks for fetching replies

                        snapshot.forEach { document ->
                            val id = document.id
                            val userName = document.getString("userName")
                            val comment = document.getString("comment")

                            // Fetch replies for each question
                            val repliesTask = db.collection("questions")
                                .document(id)
                                .collection("replies")
                                .get()
                                .addOnSuccessListener { repliesSnapshot ->
                                    val replies = repliesSnapshot.map { replyDoc ->
                                        val replyId = replyDoc.id
                                        val replyUserName = replyDoc.getString("userName") ?: ""
                                        val subComment = replyDoc.getString("subComment") ?: ""
                                        Replies(id = replyId, userName = replyUserName, subComment = subComment)
                                    }

                                    // Add question with replies to the list
                                    questions.add(
                                        Question(
                                            id = id,
                                            userName = userName ?: "",
                                            comment = comment ?: "",
                                            replies = replies
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

    override fun deleteQuestion(questionId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("questions")
            .document(questionId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Success("Question deleted successfully."))
                } else {
                    trySend(ResultState.Failure(task.exception ?: Exception("Unknown error")))
                }
            }
            .addOnFailureListener { e ->
                trySend(ResultState.Failure(e))
            }
        awaitClose { close() }
    }

    override fun deleteReply(questionId: String, replyId: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val replyDocRef = db.collection("questions")
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

    override fun deleteAllQuestions(): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        // Fetch all questions
        db.collection("questions")
            .get()
            .addOnSuccessListener { snapshot ->
                // Create a batch to delete all questions
                val batch = db.batch()

                snapshot.forEach { document ->
                    batch.delete(document.reference)
                }

                // Commit the batch
                batch.commit()
                    .addOnSuccessListener { trySend(ResultState.Success("All questions deleted successfully.")) }
                    .addOnFailureListener { trySend(ResultState.Failure(it)) }
            }
            .addOnFailureListener { trySend(ResultState.Failure(it)) }

        awaitClose { close() }
    }
}