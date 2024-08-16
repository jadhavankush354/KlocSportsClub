package com.firstapplication.file.Dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
import com.firstapplication.file.PdfViewer
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
                    onDismissRequest()
                    currentDialog = DialogType.BallTypeDialog
                },
                onConfirmation = {
                    onConfirmation()
                    currentDialog = DialogType.BallTypeDialog
                },
                painter = painter,
                imageDescription = imageDescription
            )
        }
        DialogType.BallTypeDialog -> {
            BallTypeDialog(navController,
                onDismissRequest = {
                    onDismissRequest()
                    currentDialog = DialogType.BatTypeDialog
                },
                onConfirmation = { onConfirmation() },
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
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler(onBack = {
            controller.navigate("CatogeriesOfEquipments")
        })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.primaryDark))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
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
                    }
                }
                IconButton(
                    onClick = {
                        controller.popBackStack()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
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
    val dialogColor = colorResource(id = dev.chrisbanes.snapper.R.color.vector_tint_theme_color)
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler(onBack = {
            navController.navigate("CatogeriesOfEquipments")
        })
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(dialogColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
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
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Leather")
                        }
                        TextButton(
                            onClick = {
                                onConfirmation()
                                navController.navigate("TennisBatTypeDialog")
                            },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Tennis")
                        }
                    }
                }
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BatTypeDialog(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    val batTypes = listOf(
        "Kashmir Willow" to R.raw.kashmiribats,
        "English Willow" to R.raw.englishwillow,
        "Long Handle Bats" to R.raw.moongoose
    )

    var isShowPdf by remember { mutableStateOf(false) }
    var selectedPdfResId by remember { mutableStateOf(0) }
    var selectedPdfName by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        BackHandler(onBack = {
            if (isShowPdf) {
                isShowPdf = false
            } else {
                navController.navigate("CatogeriesOfEquipments")
            }
        })

        if (isShowPdf) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    TopAppBar(
                        title = { Text(text = selectedPdfName) },
                        navigationIcon = {
                            IconButton(onClick = { isShowPdf = false }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                    )
                    PdfViewer(pdfResId = selectedPdfResId, pdfName = selectedPdfName)
                }
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Adjusted height for the close button
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFCCC2DC)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 25.dp) // Leave space for the close button
                    ) {
                        items(batTypes) { (batType, pdfResId) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(
                                    onClick = {
                                        onConfirmation(batType)
                                        // Navigate to specific screen based on bat type
                                        when (batType) {
                                            "Kashmir Willow" -> navController.navigate("KashmirWillow")
                                            "English Willow" -> navController.navigate("EnglishWillow")
                                            "Long Handle Bats" -> navController.navigate("MangooseBats")
                                        }
                                    }
                                ) {
                                    Text(batType)
                                }

                                IconButton(
                                    onClick = {
                                        selectedPdfResId = pdfResId
                                        selectedPdfName = "$batType.pdf"
                                        isShowPdf = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info"
                                    )
                                }
                            }
                        }
                    }

                    // Close button
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
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
    val batTypes = listOf("Hard Tennis Bats", "Low Tennis Bats")

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
                .height(200.dp), // Adjusted height to accommodate the close button
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp) // Leave space for the close button
                ) {
                    items(batTypes) { batType ->
                        TextButton(
                            onClick = {
                                onConfirmation(batType)
                                when (batType) {
                                    "Hard Tennis Bats" -> navController.navigate("HardTennis")
                                    "Low Tennis Bats" -> navController.navigate("LowTennis")
                                }
                            },
                            modifier = Modifier.padding(4.dp),
                        ) {
                            Text(batType)
                        }
                    }
                }

                // Close button
                IconButton(
                    onClick = { navController.navigate("CatogeriesOfEquipments") },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
        }
    }
}
