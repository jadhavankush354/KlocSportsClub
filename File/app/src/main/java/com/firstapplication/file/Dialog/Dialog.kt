package com.firstapplication.file.Dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.firstapplication.file.DataClass.Enum.DialogType
import com.firstapplication.file.R



@Composable
fun DialogWithImage(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    var currentDialog by remember { mutableStateOf<DialogType?>(DialogType.BattingStyleDialog) }

    when (currentDialog) {
        DialogType.BattingStyleDialog -> {
            BattingStyleDialog(
                controller = navController,
                onDismissRequest = {
//                    onDismissRequest()
//                    currentDialog = DialogType.BallTypeDialog
                },
                onConfirmation = {
//                    onConfirmation()
//                    currentDialog = DialogType.BallTypeDialog
                },
                painter = painter,
                imageDescription = imageDescription
            )
        }
        DialogType.BallTypeDialog -> {
            BallTypeDialog(navController,
                onDismissRequest = {
                    onDismissRequest()
                    currentDialog = DialogType.PlayerTypeDialog
                },
                onConfirmation = { onConfirmation() },
            )
        }
        DialogType.PlayerTypeDialog -> {
            PlayerTypeDialog(navController,
                onDismissRequest = { onDismissRequest() },
                onConfirmationBeginner = {
                    onConfirmation()
                    currentDialog = DialogType.BatTypeDialog
                },
                onConfirmationIntermediate = {
                    onConfirmation()
                    currentDialog = DialogType.BatTypeDialog
                },
                onConfirmationAdvanced = {
                    onConfirmation()
                    currentDialog = DialogType.BatTypeDialog
                }
            )
        }
        DialogType.BatTypeDialog -> {
            // Assuming you have a NavController in your composable hierarchy
            BatTypeDialog(
                navController = navController,
                onDismissRequest = { onDismissRequest() },
                onConfirmation = { /* Handle confirmation logic */ }
            )
        }
        else -> {
        }
    }
}

@Composable
fun BattingStyleDialog(
    controller: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false, // Disable dismiss on back press
            dismissOnClickOutside = false // Disable dismiss on click outside
        )
    ) {
        BackHandler(onBack = {
            controller.popBackStack()
        })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.primaryDark)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painter,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(160.dp)
                )
                Text(
                    text = "Please Select Your Batting Style for your Better Play",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("RHB")
                    }
                    TextButton(
                        onClick = {
                            onConfirmation()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("LHB")
                    }
                    TextButton(
                        onClick = {

                           controller.popBackStack()
//                           controller.navigate("CatogeriesOfEquipments")
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("CANCEL")
                    }
                }
            }
        }
    }

}




@Composable
fun BallTypeDialog(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    val dialogColor= colorResource(id = dev.chrisbanes.snapper.R.color.vector_tint_theme_color)
    Dialog(onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = false, // Disable dismiss on back press
            dismissOnClickOutside = false // Enable dismiss on click outside
        )) {
        BackHandler(onBack = {
           navController.popBackStack()
        })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(dialogColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Choose the type of ball for regular practice",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Leather")
                    }
                    TextButton(
                        onClick = {
                            // Update this line to trigger the navigation to PlayerTypeDialog
                            onConfirmation()
                            navController.navigate("TennisBatTypeDialog")
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Tennis")
                    }
                    TextButton(
                        onClick = {
                            navController.navigate("CatogeriesOfEquipments")
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("CANCEL")
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerTypeDialog(
    navHostController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmationBeginner: () -> Unit,
    onConfirmationIntermediate: () -> Unit,
    onConfirmationAdvanced: () -> Unit,
) {
    val dialogColor= colorResource(id = R.color.black)
    Dialog(onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = false, // Disable dismiss on back press
            dismissOnClickOutside = false // Disable dismiss on click outside
        )) {
        BackHandler(onBack = {
            navHostController.popBackStack()
        })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(12.dp)
                .background(dialogColor),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Choose the player type",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onConfirmationBeginner()
                        },
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Text("Beginner")
                    }
                    TextButton(
                        onClick = {
                            onConfirmationIntermediate()
                        },
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Text("Intermediate")
                    }
                    TextButton(
                        onClick = {
                            onConfirmationAdvanced()
                        },
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Text("Advanced")
                    }
                }
            }
        }
    }
}


@Composable
fun BatTypeDialog(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    val batTypes = listOf("KashmirWillow", "English Willow", "Long Handle Bats")
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler(onBack = {
            navController.popBackStack()
        })
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(batTypes) { batType ->
                    TextButton(
                        onClick = {
                            onConfirmation(batType)
                            // TODO: Navigate to specific screen based on bat type
                            when (batType) {
                                "KashmirWillow" -> navController.navigate("KashmirWillow")
                                "English Willow" -> navController.navigate("EnglishWillow")
                                "Long Handle Bats" -> navController.navigate("MangooseBats")
                            }
                        },
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Text(batType)
                    }
                }
            }
        }
    }
}

@Composable
fun TennisBatTypeDialog(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    val batTypes = listOf("HardTennisBats", "LowTennisBats")
    val dialogColor= colorResource(id = R.color.black)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler(onBack = {
            navController.navigate("CatogeriesOfEquipments")
        })
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(batTypes) { batType ->
                    TextButton(
                        onClick = {
                            onConfirmation(batType)
                            when (batType) {
                                "HardTennisBats" -> navController.navigate("HardTennis")
                                "LowTennisBats" -> navController.navigate("LowTennis")
                            }
                        },
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Text(batType)
                    }
                }
            }
        }
    }
}
