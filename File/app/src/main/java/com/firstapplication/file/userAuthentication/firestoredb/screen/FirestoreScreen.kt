package com.firstapplication.file.userAuthentication.firestoredb.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.firstapplication.file.userAuthentication.common.CommonDialog
import com.firstapplication.file.userAuthentication.firestoredb.module.FirestoreModel
import com.firstapplication.file.userAuthentication.firestoredb.viewmodel.FirestoreViewModel
import com.firstapplication.file.userAuthentication.util.ResultState
import com.firstapplication.file.userAuthentication.util.extentionsFunctions
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Update(
    user: FirestoreModel,
    isDialog: MutableState<Boolean>,
    viewModel: FirestoreViewModel,
    isRefresh: MutableState<Boolean>
) {

    var name by remember { mutableStateOf(user.user?.name) }
    var email by remember { mutableStateOf(user.user?.email) }
    var password by remember { mutableStateOf(user.user?.password) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var progress by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = { isDialog.value = false },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Update List", modifier = Modifier.padding(vertical = 10.dp))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(value = name!!, onValueChange = {
                    name = it
                },
                    placeholder = { Text(text = "Enter name") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(value = email!!, onValueChange = {
                    email = it
                },
                    placeholder = { Text(text = "Enter email") }
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextField(value = password!!, onValueChange ={password=it}, placeholder = {
                    Text(text = "Enter password")
                } )
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    scope.launch {
                        viewModel.update(
                            FirestoreModel(
                                key = user.key,
                                user = FirestoreModel.FirestoreUser(
                                    name,
                                    email,
                                   password
                                )
                            )
                        ).collect {
                            when (it) {
                                is ResultState.Success<*> -> {
                                    extentionsFunctions.showMessage(context,"${it.data}")
                                    isDialog.value = false
                                    progress = false
                                    isRefresh.value = true
                                }
                                is ResultState.Failure -> {
                                    extentionsFunctions.showMessage(context,"${it.e}")
                                    isDialog.value = false
                                    progress = false
                                }
                                is ResultState.Loading -> {
                                    progress = true
                                }



                            }
                        }
                    }
                }) {
                    Text(text = "Update")
                }
            }
        }
    )

    if (progress)
        CommonDialog()
}