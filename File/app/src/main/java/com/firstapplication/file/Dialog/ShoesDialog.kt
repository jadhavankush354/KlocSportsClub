package com.firstapplication.file.Dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.firstapplication.file.R


@Composable
fun ShoesDialogues(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    var currentDialog by remember { mutableStateOf<DialogTypes>(DialogTypes.ShoesDialog) }

    when (currentDialog) {
        DialogTypes.ShoesDialog -> {
            ShoesDialog(
                navController = navController,
                onCategorySelected = {
                    currentDialog = DialogTypes.ShoesCatsDialog
                },
                onDismiss = {
                    onDismissRequest()
                }
            )
        }
        DialogTypes.ShoesCatsDialog -> {
            ShoesCatsDialog(
                navController = navController,
                onSpiky = {
                    // Handle spiky selection
                },
                onNonSpiky = {
                    // Handle non-spiky selection
                },
                onDismiss = {
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun ShoesDialog(
    navController: NavHostController,
    onCategorySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val categories = listOf("Male", "Female", "Cancel")
    val dialogColor = colorResource(id = R.color.primaryDark)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = false, // Disable dismiss on back press
            dismissOnClickOutside = false // Disable dismiss on click outside
        )
    ) {
        BackHandler(onBack = {
            // Handle back button to dismiss dialog
           navController.popBackStack()
        })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
                .background(colorResource(id = R.color.PurpleGrey80)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.PurpleGrey80)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose the category of shoes",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    categories.forEach { category ->
                        TextButton(
                            onClick = {
                                if (category == "Cancel") {
                                    onDismiss()
                                    navController.popBackStack() // Navigate to home screen
                                } else {
                                    onCategorySelected(category)
                                    onDismiss()
                                }
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(category)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoesCatsDialog(
    navController: NavHostController,
    onSpiky: () -> Unit,
    onNonSpiky: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogColor = colorResource(id = R.color.tertiaryContainerDark)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = false, // Disable dismiss on back press
            dismissOnClickOutside = false // Disable dismiss on click outside
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            BackHandler(onBack = {
                // Handle back button to dismiss dialog
                navController.navigate("CatogeriesOfEquipments")
            })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.PurpleGrey80)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BackHandler(onBack = {
                    // Handle back button to dismiss dialog
                    navController.navigate("CatogeriesOfEquipments")
                })
                Text(
                    text = "Choose the type of shoes for",
                    modifier = Modifier
                        .padding(16.dp)
                        .background(colorResource(id = R.color.PurpleGrey80)),
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    LazyColumn {
                        item {
                            TextButton(
                                onClick = {
                                    onSpiky()
                                    navController.navigate("ShoesSizes")
                                    onDismiss()
                                },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                Text("Spiky")
                            }
                        }

                        item {
                            TextButton(
                                onClick = {
                                    onNonSpiky()
                                    navController.navigate("ShoesSizes")
                                    onDismiss()
                                },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                Text("Non-Spiky")
                            }
                        }
                    }
                }

            }
        }
    }
}

enum class DialogTypes {
    ShoesDialog,
    ShoesCatsDialog
}
