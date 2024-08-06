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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.firstapplication.file.R

@Composable
fun BallD(
    navHostController: NavHostController,
    onDismissRequest: () -> Unit,
    onLeatherButtonClick: () -> Unit,
    onTennisButtonClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
       BackHandler {
           navHostController.popBackStack()
       }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
                .background(colorResource(id = R.color.purple_200)),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .height(250.dp)
                    .background(colorResource(id = R.color.purple_200))
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Text(
                        text = "Choose the type of ball for regular practice",
                        modifier = Modifier.padding(16.dp),
                    )
                }

                item {
                    Button(
                        onClick = {
                            onLeatherButtonClick()
                            navHostController.navigate("LeatherBall")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Leather")
                    }
                }

                item {
                    Button(
                        onClick = {
                            onTennisButtonClick()
                            navHostController.navigate("TennisBall")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Tennis")
                    }
                }

                item {
                    Button(
                        onClick = {
                            navHostController.navigate("CatogeriesOfEquipments")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("CANCEL")
                    }
                }
            }
        }
    }
}


