package com.firstapplication.file.ui.ComponentsUI
import android.annotation.SuppressLint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firstapplication.file.DataClass.Question
import com.firstapplication.file.DataClass.Replies
import com.firstapplication.file.DataClass.sealed.ReportType
import com.firstapplication.file.userAuthentication.firestoredb.viewmodel.FirestoreViewModel
import com.firstapplication.file.userAuthentication.presentation.profile.ProfileViewModel
import com.firstapplication.file.userAuthentication.util.ResultState
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.concurrent.TimeUnit

//community feed back updated
@SuppressLint("SuspiciousIndentation", "UnusedMaterial3ScaffoldPaddingParameter")
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
    var userId by remember { mutableStateOf("") }
    var newQuestion by remember { mutableStateOf("") }
    val categories = listOf("Cricket", "Bat", "Ball", "Gloves", "Other")
    var selectedCategory by remember { mutableStateOf(categories[categories.size-1]) }
    val questionsState by viewModel1.questionsState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedCategory) }
    var expandedQuestionId by remember { mutableStateOf<String?>(null) }
    var sortBy by remember { mutableStateOf("") }
    var sortByListName by remember { mutableStateOf("") }

    LaunchedEffect(email) {
        if (email.isNotEmpty()) viewModel1.getUserByEmail(email)
    }
    LaunchedEffect(viewModel1.res1) {
        snapshotFlow { viewModel1.res1.value }.distinctUntilChanged().collect { state -> state.data?.let {
            user -> userName = user.user?.name ?: ""
            userId = user.key ?: ""
        } }
    }
    LaunchedEffect(selectedCategory) {
        viewModel1.fetchQuestions(selectedCategory)
    }

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = { IconButton(onClick = { controller.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            } },
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
                                IconButton(onClick = {}) {
                                    if (expanded) Icon(imageVector = Icons.Default.ArrowDropUp, contentDescription = "", tint = Color.White) else
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "", tint = Color.White)
                                }
                            },
                            modifier = Modifier.menuAnchor(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedText = category
                                        expanded = false
                                        selectedCategory = category
                                    }
                                )
                            }
                        }
                    }
                },
                actions = {
                        var menuExpanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        // Refresh option
                        DropdownMenuItem(
                            text = { Text("Refresh") },
                            onClick = {
                                menuExpanded = false
                                val temp = selectedCategory
                                selectedCategory = ""
                                selectedCategory = temp
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh"
                                )
                            }
                        )

                        // Delete All option
                        DropdownMenuItem(
                            text = { Text("Delete All") },
                            onClick = {
                                menuExpanded = false
                                viewModel1.deleteAllQuestions(selectedCategory)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete All"
                                )
                            }
                        )
                        var sortByExpanded by remember { mutableStateOf(false) }
                        var sortExpanded by remember { mutableStateOf(false) }
                        DropdownMenuItem(
                            text = { Text("Sort By") },
                            onClick = {
                                sortByExpanded = !sortByExpanded
                                sortExpanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = "Sort By"
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = if (sortByExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = "Expand Sort Options"
                                )
                            }
                        )
                        if (sortByExpanded) {
                            DropdownMenuItem(
                                text = { Text("Questions") },
                                onClick = {
                                    sortByListName = "Questions"
                                    sortByExpanded = false
                                    sortExpanded = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = "Sort by Date Ascending"
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Replies") },
                                onClick = {
                                    sortByListName = "Replies"
                                    sortByExpanded = false
                                    sortExpanded = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = "Sort by Date Descending"
                                    )
                                }
                            )

                        }
                        if (sortExpanded) {
                            DropdownMenuItem(
                                text = { Text("Date ↑") },
                                onClick = {
                                    sortBy = "Date Asc"
                                    sortExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = "Sort by Date Ascending"
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Date ↓") },
                                onClick = {
                                    sortBy = "Date Desc"
                                    sortExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = "Sort by Date Descending"
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Highly Reported") },
                                onClick = {
                                    sortBy = "Reports"
                                    sortExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Report,
                                        contentDescription = "Sort by Reports"
                                    )
                                }
                            )
                        }

                    }
                },
                backgroundColor = Color.Black
            )
        },
        bottomBar = {
            BottomAppBar {
                Row (modifier = Modifier
                    .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center) {
                    OutlinedTextField(
                        value = newQuestion,
                        onValueChange = { newQuestion = it },
                        placeholder = { Text("Ask your question...") },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (newQuestion.isNotBlank()) {
                            // Example: Replace with actual offensive keyword checking and saving logic
                            val offensiveList = viewModel1.loadOffensiveKeywords(context)
                            val isOffensive = viewModel1.containsOffensiveKeywords(newQuestion, offensiveList)
                            if (!isOffensive) {
                                val newQuestionItem = Question("", "userName", newQuestion, emptyList(), System.currentTimeMillis(), emptyList())
                                viewModel1.saveQuestion(newQuestionItem, selectedText)
                            } else {
                                Toast.makeText(context, "Offensive keyword detected", Toast.LENGTH_SHORT).show()
                            }
                            newQuestion = ""
                        }
                    }) {
                        Text(text = "Post")
                    }
                }
            }
        },
        content = { padding ->
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(), horizontalAlignment = Alignment.CenterHorizontally) {
                when (val state = questionsState) {
                    is ResultState.Loading -> {
                        item { CircularProgressIndicator() }
                    }
                    is ResultState.Failure -> {
                        item { Text("Failed to fetch questions") }
                    }
                    is ResultState.Success -> {
                        if (state.data.isEmpty()) {
                            item { Text("No questions available", modifier = Modifier.fillMaxSize(), fontSize = 25.sp, textAlign = TextAlign.Center) }
                        } else {
                            val sortedItems = if (sortByListName != "Questions") {
                                if (sortBy == "Reports")
                                    state.data.sortedByDescending { question -> question.replies.sumOf { it.reportedUsers.size } }
                                else
                                    state.data.sortedByDescending { it.timestamp }
                            } else {
                                when (sortBy) {
                                    "Date Asc" -> state.data.sortedBy { it.timestamp }  // Ascending
                                    "Date Desc" -> state.data.sortedByDescending { it.timestamp }  // Descending
                                    "Reports" -> state.data.sortedByDescending { it.reportedUsers.size }  // Highly reported
                                    else -> state.data
                                }
                            }
                            items(sortedItems) { question ->
                                ExpandableCard(
                                    question = question,
                                    expanded = question.id == expandedQuestionId,
                                    onCardClick = { clickedQuestionId ->
                                        expandedQuestionId = if (expandedQuestionId == clickedQuestionId) null else clickedQuestionId
                                    },
                                    onAddReply = { newReply ->
                                        val offensiveList = viewModel1.loadOffensiveKeywords(context)
                                        val isOffensive = viewModel1.containsOffensiveKeywords(newReply.subComment, offensiveList)
                                        if (!isOffensive) {
                                            viewModel1.saveReply(question.id, newReply, selectedCategory)
                                        } else {
                                            Toast.makeText(context, "Offensive keyword detected", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    replyingFrom = userName,
                                    onDeleteClick = { questionId ->
                                        viewModel1.deleteQuestion(questionId, selectedCategory)
                                    },
                                    onDeleteReplyClick = { questionId, replyId ->
                                        viewModel1.deleteReply(questionId, replyId, selectedCategory)
                                    },
                                    currentUserName = userName,
                                    currentUserId = userId,
                                    selectedCategory = selectedCategory,
                                    sortBy = sortBy,
                                    sortByListName = sortByListName
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableCard(
    question: Question,
    expanded: Boolean,
    onCardClick: (String) -> Unit,
    onAddReply: (Replies) -> Unit,
    replyingFrom: String,
    onDeleteClick: (String) -> Unit, // Added onDeleteClick parameter
    onDeleteReplyClick: (String, String) -> Unit, // Added for deleting reply
    currentUserName: String, // Added currentUserName parameter
    currentUserId: String,
    viewModel1: FirestoreViewModel = hiltViewModel(),
    selectedCategory: String,
    sortBy: String,
    sortByListName: String
) {
    val context = LocalContext.current // Get the context
    var showReplies by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }

    var showMenu by remember { mutableStateOf(false) } // For question dropdown
    var showReplyMenu by remember { mutableStateOf<Pair<String, Boolean>?>(null) } // For reply dropdown
    var showReportAlert by remember { mutableStateOf(false) }
    var reportConfirmation by remember { mutableStateOf<ReportType?>(null) }
    var currentReplyId by remember { mutableStateOf<String?>(null) }

    fun handleReportConfirmation() {
        when (reportConfirmation) {
            is ReportType.Question -> {
                viewModel1.updateQuestionReports(question.id, currentUserId, selectedCategory)
                Toast.makeText(context, "Reported successfully.", Toast.LENGTH_SHORT).show()
            }
            is ReportType.Reply -> {
                currentReplyId?.let { replyId ->
                    viewModel1.updateReplyReports(question.id, replyId, currentUserId, selectedCategory)
                    Toast.makeText(context, "Reported successfully.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
        reportConfirmation = null
        currentReplyId = null
    }
    ReportAlertDialog(
        showReportAlert = showReportAlert,
        onDismissRequest = { showReportAlert = false },
        onConfirm = {
            showReportAlert = false
            handleReportConfirmation()
        },
        onCancel = {
            showReportAlert = false
            reportConfirmation = null
        }
    )

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .combinedClickable(
            onClick = { onCardClick(question.id) },
            onLongClick = { showMenu = true } // Show dropdown on long click
        )) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(
                        text = question.userName,
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formatTimestamp(question.timestamp),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ReportProblem,
                            contentDescription = "Report",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = question.reportedUsers.size.toString(), // Adjust based on how reports are stored
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = question.comment,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis
            )
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Report") },
                    onClick = {
                        if (question.reportedUsers.contains(currentUserId)) {
                            Toast.makeText(context, "You have already reported this question.", Toast.LENGTH_SHORT).show()
                            showMenu = false
                        } else {
                            showReportAlert = true
                            showMenu = false
                            reportConfirmation = ReportType.Question
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Report, // Use appropriate icon
                            contentDescription = "Report"
                        )
                    }
                )
                if (question.userName == currentUserName) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = { onDeleteClick(question.id)
                            showMenu = false },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete, // Use appropriate icon
                                contentDescription = "Delete"
                            )
                        }
                    )
                }
            }
            if (expanded) {
                Column {
                    if (!showReplies) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Reply",
                                color = Color.Blue,
                                modifier = Modifier.clickable { showReplies = !showReplies }
                            )
                        }
                    }
                    if (showReplies) {
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val maxHeight = maxHeight
                            val replyCount = question.replies.size
                            val height =
                                if (replyCount > 4) 200.dp else (replyCount * 50).dp // Adjust item height as needed
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(height.coerceAtMost(maxHeight))
                            ) {
                                val sortedItems = if (sortByListName != "Replies") { question.replies.sortedBy { it.timestamp } } else {
                                    when (sortBy) {
                                        "Date Asc" -> question.replies.sortedBy { it.timestamp }  // Ascending
                                        "Date Desc" -> question.replies.sortedByDescending { it.timestamp }  // Descending
                                        "Reports" -> question.replies.sortedByDescending { it.reportedUsers.size }  // Highly reported
                                        else -> question.replies
                                    }
                                }
                                items(sortedItems) { reply ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 16.dp, top = 8.dp)
                                            .combinedClickable(
                                                onClick = {}, // Provide a no-op or desired action here
                                                onLongClick = {
                                                    showReplyMenu = Pair(reply.id, true)
                                                }
                                            ),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Row {
                                                Text(
                                                    text = reply.userName,
                                                    fontSize = 15.sp,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = formatTimestamp(reply.timestamp),
                                                    fontSize = 12.sp,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.Gray
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Filled.ReportProblem,
                                                        contentDescription = "Report",
                                                        tint = Color.Gray
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = reply.reportedUsers.size.toString(), // Adjust based on how reports are stored
                                                        fontSize = 12.sp,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                            Text(
                                                text = reply.subComment,
                                                fontSize = 10.sp,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Start
                                            ) {
                                                Text(
                                                    text = "Reply",
                                                    color = Color.Blue,
                                                    modifier = Modifier.clickable { replyText = "@${reply.userName} " }
                                                )
                                            }
                                        }
                                            DropdownMenu(
                                                expanded = showReplyMenu?.first == reply.id && showReplyMenu?.second == true,
                                                onDismissRequest = { showReplyMenu = null }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text("Report") },
                                                    onClick = {
                                                        if (reply.reportedUsers.contains(currentUserId)) {
                                                            Toast.makeText(context, "You have already reported this reply.", Toast.LENGTH_SHORT).show()
                                                            showReplyMenu = null
                                                        } else {
                                                            // Show report alert and set report confirmation
                                                            showReportAlert = true
                                                            showReplyMenu = null
                                                            reportConfirmation = ReportType.Reply
                                                            currentReplyId = reply.id
                                                        }
                                                    },
                                                    leadingIcon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Report, // Use appropriate icon
                                                            contentDescription = "Report"
                                                        )
                                                    }
                                                )
                                                if (reply.userName == currentUserName) {
                                                    DropdownMenuItem(
                                                        text = { Text("Delete") },
                                                        onClick = { onDeleteReplyClick(question.id, reply.id) },
                                                        leadingIcon = {
                                                            Icon(
                                                                imageVector = Icons.Default.Delete, // Use appropriate icon
                                                                contentDescription = "Delete"
                                                            )
                                                        }
                                                    )
                                                }
                                            }

                                    }
                                }
                            }
                        }
                        ReplyInput(
                            replyText = replyText,
                            onReplyTextChanged = { replyText = it },
                            onSubmitReply = {
                                onAddReply(Replies("", replyingFrom, replyText, 0L, emptyList()))
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

@Composable
fun ReportAlertDialog(
    showReportAlert: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (showReportAlert) {
        AlertDialog(
            onDismissRequest = {
                onDismissRequest()
            },
            title = {
                Text(text = "Report Content")
            },
            text = {
                Text("Are you sure you want to report this content?")
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onCancel()
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}