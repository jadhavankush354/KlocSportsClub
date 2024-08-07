package com.firstapplication.file.ui.ComponentsUI
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

//community feed back updated
@OptIn(ExperimentalMaterial3Api::class)
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
    val categories = listOf("questions", "Cricket", "Bat", "Ball", "Gloves")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedCategory) }
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

    LaunchedEffect(selectedCategory) {
        viewModel1.fetchQuestions(selectedCategory)
        viewModel1.questionsState.collect { result ->
            when (result) {
                is ResultState.Success -> { listOfQuestions = result.data.sortedByDescending { it.timestamp } }
                is ResultState.Failure -> { Toast.makeText(controller.context, "Failed to fetch questions", Toast.LENGTH_SHORT).show() }
                ResultState.Loading -> {}
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { controller.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                }
            },
            title = {
                ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = { selectedText = it },
                    readOnly = true,
                    trailingIcon = {
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    },
                    modifier = Modifier.menuAnchor(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedText = category
                                expanded = false
                                selectedCategory = category
                            }
                        ) {
                            Text(text = category)
                        }
                    }
                }
            }
            },
            actions = {
                IconButton(onClick = { viewModel1.deleteAllQuestions(selectedCategory) }) {
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
                        val isOffensive=viewModel1.containsOffensiveKeywords(newReply.subComment,offencivelist)
                        if(!isOffensive) {
                            viewModel1.saveReply(question.id, newReply, selectedCategory)
                        }
                        else{
                            Toast.makeText(context,"Offensive keyword detected",Toast.LENGTH_SHORT).show()
                        }
                    },
                    replyingFrom = userName,
                    onDeleteClick = { questionId ->
                        viewModel1.deleteQuestion(questionId, selectedCategory)
                    },
                    onDeleteReplyClick = { questionId, replyId ->
                        viewModel1.deleteReply(questionId, replyId, selectedCategory)
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
                    val newQuestionItem = Question("", userName, newQuestion, emptyList(), 0L)
                    viewModel1.saveQuestion(newQuestionItem, selectedCategory)
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
                Row {
                    Text(
                        text = question.userName,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatTimestamp(question.timestamp),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
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
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)) {
                            items(question.replies.sortedByDescending { it.timestamp }
                                .take(4)) { reply ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, top = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Row {
                                            Text(
                                                text = reply.userName,
                                                fontSize = 10.sp,
                                                style = MaterialTheme.typography.body2
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = formatTimestamp(reply.timestamp),
                                                fontSize = 8.sp,
                                                style = MaterialTheme.typography.body2,
                                                color = Color.Gray
                                            )
                                        }
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
                        }
                        ReplyInput(
                            replyText = replyText,
                            onReplyTextChanged = { replyText = it },
                            onSubmitReply = {
                                onAddReply(Replies("", replyingFrom, replyText, 0L))
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


fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> {
            val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
            "$seconds seconds ago"
        }
        diff < TimeUnit.HOURS.toMillis(1) -> {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            "$minutes minutes ago"
        }
        diff < TimeUnit.DAYS.toMillis(1) -> {
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            "$hours hours ago"
        }
        diff < TimeUnit.DAYS.toMillis(30) -> {
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            "$days days ago"
        }
        diff < TimeUnit.DAYS.toMillis(365) -> {
            val months = TimeUnit.MILLISECONDS.toDays(diff) / 30
            "$months months ago"
        }
        else -> {
            val years = TimeUnit.MILLISECONDS.toDays(diff) / 365
            "$years years ago"
        }
    }
}