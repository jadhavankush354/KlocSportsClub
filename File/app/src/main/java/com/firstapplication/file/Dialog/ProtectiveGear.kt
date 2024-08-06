package com.firstapplication.file.Dialog

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.firstapplication.file.R

/*
@Composable
fun ProtectiveGear(
    navHostController: NavHostController,
    onDismissRequest: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val protectiveGears = listOf("Gloves", "LegPads", "ThighPads", "Helmet")
    val dialogColor = colorResource(id = R.color.primaryContainerLight)

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .background(dialogColor)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),// fillMaxWidth is now used for the Card
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(dialogColor),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Choose the protective gear for your practice",
                        modifier = Modifier.padding(16.dp),
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        protectiveGears.forEach { item ->
                            Button(
                                onClick = {
                                    onItemClick(item)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(item)
                            }
                        }
                    }
                }
            }
        }
    }
}
*/

@Composable
fun ProtectiveGear(
    navHostController: NavHostController,
    onDismissRequest: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val protectiveGears = listOf("Gloves", "LegPads", "ThighPads", "Helmet","Cancel")
    val dialogColor = colorResource(id = R.color.primaryContainerLight)

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        BackHandler(onBack = {
            navHostController.popBackStack()
        })
        Box(
            modifier = Modifier
                .background(dialogColor)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .background(dialogColor),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Choose the protective gear for your practice",
                        modifier = Modifier.padding(16.dp),
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        protectiveGears.forEach { item ->
                            Button(
                                onClick = {
                                    onItemClick(item)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(item)
                            }
                        }
                    }
                }
            }
        }
    }
}
