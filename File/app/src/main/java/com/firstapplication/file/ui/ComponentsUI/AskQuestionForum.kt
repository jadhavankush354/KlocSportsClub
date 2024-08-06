package com.firstapplication.file.ui.ComponentsUI
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firstapplication.file.DataClass.Question
import com.firstapplication.file.DataClass.Replies
import com.firstapplication.file.userAuthentication.firestoredb.viewmodel.FirestoreViewModel
import com.firstapplication.file.userAuthentication.presentation.profile.ProfileViewModel
import com.firstapplication.file.userAuthentication.util.ResultState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
//community feed back updated
@Composable
fun AskQuestionForum(
    controller: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    viewModel1: FirestoreViewModel = hiltViewModel())
{
    val context= LocalContext.current
    val email = viewModel.currentUser?.takeIf { it.isNotEmpty() } ?: ""
    var userName by remember { mutableStateOf("") }
    var newQuestion by remember { mutableStateOf("") }
    var listOfQuestions by remember { mutableStateOf(emptyList<Question>())}
    var expandedQuestionId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            viewModel1.getUserByEmail(email)
        }
    }
    LaunchedEffect(viewModel1.res1) {
        snapshotFlow { viewModel1.res1.value }.distinctUntilChanged().collect { state -> state.data?.let { user -> userName = user.user?.name ?: "" } }
    }

    LaunchedEffect(Unit) {
        viewModel1.questionsState.collect { result ->
            when (result) {
                is ResultState.Success -> { listOfQuestions = result.data }
                is ResultState.Failure -> { Toast.makeText(controller.context, "Failed to fetch questions", Toast.LENGTH_SHORT).show() }
                ResultState.Loading -> {}
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Questions") },
            actions = {
                IconButton(onClick = { viewModel1.deleteAllQuestions() }) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Use a delete icon from material icons
                        contentDescription = "Delete"
                    )
                }
            }
        )
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            items(listOfQuestions) { question ->
                ExpandableCard(
                    question = question,
                    expanded = question.id == expandedQuestionId,
                    onCardClick = { clickedQuestionId ->
                        expandedQuestionId = if (expandedQuestionId == clickedQuestionId) null else clickedQuestionId
                    },
                    onAddReply = { newReply ->
                        val updatedQuestions = listOfQuestions.map {
                            if (it.id == question.id) { it.copy(replies = it.replies + newReply) } else { it }
                        }
                        val offencivelist =viewModel1.loadOffensiveKeywords(context)
                        val isOffensive=viewModel1.containsOffensiveKeywords(newQuestion,offencivelist)
                        if(!isOffensive) {
                            viewModel1.saveReply(question.id, newReply)
                        }
                        else{
                            Toast.makeText(context,"Offensive keyword detected",Toast.LENGTH_SHORT).show()
                        }
                    },
                    replyingFrom = userName,
                    onDeleteClick = { questionId ->
                        viewModel1.deleteQuestion(questionId)
                    },
                    onDeleteReplyClick = { questionId, replyId ->
                        viewModel1.deleteReply(questionId, replyId)
                    },
                    currentUserName = userName
                )
            }
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.Center) {
            OutlinedTextField(value = newQuestion , onValueChange = {newQuestion = it}, placeholder = { Text("Ask your question...") }, modifier = Modifier
                .fillMaxWidth()
                .weight(8f))
            Spacer(modifier = Modifier.width(8.dp))
            Button(modifier = Modifier ,onClick = { if (newQuestion.isNotBlank()) {
                val offencivelist =viewModel1.loadOffensiveKeywords(context)
                val isOffensive=viewModel1.containsOffensiveKeywords(newQuestion,offencivelist)
                if(!isOffensive) {
                    val newQuestionItem = Question("", userName, newQuestion, emptyList())
                    viewModel1.saveQuestion(newQuestionItem)
                }
                else{
                    Toast.makeText(context,"Offensive keyword detected",Toast.LENGTH_SHORT).show()
                }
                newQuestion = ""
            } }) { Text(text = "Post") }
        }
    }
}
@Composable
fun ExpandableCard(
    question: Question,
    expanded: Boolean,
    onCardClick: (String) -> Unit,
    onAddReply: (Replies) -> Unit,
    replyingFrom: String,
    onDeleteClick: (String) -> Unit, // Added onDeleteClick parameter
    onDeleteReplyClick: (String, String) -> Unit, // Added for deleting reply
    currentUserName: String // Added currentUserName parameter
) {
    var showReplies by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onCardClick(question.id) }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = question.userName,
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.body1,
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (question.userName == currentUserName) {
                    IconButton(onClick = {
                        Log.d("debug", "onDeleteClick(${question.id}ss)")
                        onDeleteClick(question.id)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
            Text(
                text = question.comment,
                fontSize = 12.sp,
                style = MaterialTheme.typography.body1,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis
            )
            if (expanded) {
                Column {
                    if (!showReplies) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                    text = "Reply",
                                    color = Color.Green,
                                    modifier = Modifier.clickable { showReplies = !showReplies }
                            )
                        }
                    }
                    if (showReplies) {
                        question.replies.forEach { reply ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = reply.userName,
                                        fontSize = 10.sp,
                                        style = MaterialTheme.typography.body2
                                    )
                                    Text(
                                        text = reply.subComment,
                                        fontSize = 10.sp,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                                if (reply.userName == currentUserName) {
                                    IconButton(onClick = {
                                        onDeleteReplyClick(question.id, reply.id)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                        ReplyInput(
                            replyText = replyText,
                            onReplyTextChanged = { replyText = it },
                            onSubmitReply = {
                                onAddReply(Replies("", replyingFrom, replyText))
                                replyText = "@${question.userName} "
                            }
                        )
                    }
                }
            } else {
                replyText = "@${question.userName} "
            }
        }
    }
}
@Composable
fun ReplyInput(replyText: String, onReplyTextChanged: (String) -> Unit, onSubmitReply: () -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        TextField(
            value = replyText,
            onValueChange = onReplyTextChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Type your reply to ") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSubmitReply,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}