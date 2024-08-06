package com.firstapplication.file.ui.ComponentsUI

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.firstapplication.file.R

/*
@Composable
fun Thaigh(navHostController: NavHostController)
{
    Box(Modifier.background(colorResource(id = R.color.primaryContainerLight))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.black)
                        )
                    ) {
                        append("Here are the following sizes of ThighPads\n")
                    }
                    append("Recommended based on your playing skills and physical measures")
                },
                modifier = Modifier.padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item {
                            Image(
                                painter = painterResource(id = R.drawable.thighpadsexact),
                                contentDescription = "gloves",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            color = colorResource(id = R.color.black)
                                        )
                                    ) {
                                        append("These are ICC standards for ThighPads\n")
                                    }
                                    append("If a player wants them based on his age and height,\n")
                                    append("click the buttons below. Measurements are slightly different.")
                                },
                                modifier = Modifier.padding(16.dp)
                            )

                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "GetByAge")
                                }
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen1") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "GetByHeight")
                                }
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
fun Thaigh(navHostController: NavHostController) {
    var scale by remember { mutableStateOf(1f) } // Zoom scale
    var offsetX by remember { mutableStateOf(0f) } // Horizontal panning
    var offsetY by remember { mutableStateOf(0f) } // Vertical panning

    Box(modifier = Modifier.background(colorResource(id = R.color.primaryContainerLight))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackHandler(onBack = {
                navHostController.navigate("CatogeriesOfEquipments")
            })
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.black)
                        )
                    ) {
                        append("Here are the following sizes of ThighPads\n")
                    }
                    append("Recommended based on your playing skills and physical measures")
                },
                modifier = Modifier.padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f) // Maintain aspect ratio
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, pan, zoom, _ ->
                                            val newScale = (scale * zoom).coerceIn(1f, 5f)

                                            // Calculate new offsets with constraints
                                            val newOffsetX = (offsetX + pan.x * scale).coerceIn(
                                                -((newScale - 1) * size.width / 2),
                                                ((newScale - 1) * size.width / 2)
                                            )
                                            val newOffsetY = (offsetY + pan.y * scale).coerceIn(
                                                -((newScale - 1) * size.height / 2),
                                                ((newScale - 1) * size.height / 2)
                                            )

                                            // Update state
                                            scale = newScale
                                            offsetX = newOffsetX
                                            offsetY = newOffsetY
                                        }
                                    }
                                    .graphicsLayer(
                                        scaleX = scale,
                                        scaleY = scale,
                                        translationX = offsetX,
                                        translationY = offsetY
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.thighpadsexact),
                                    contentDescription = "Thigh Pads",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    contentScale = ContentScale.Fit // Maintain aspect ratio
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            color = colorResource(id = R.color.black)
                                        )
                                    ) {
                                        append("These are ICC standards for ThighPads\n")
                                    }
                                    append("If a player wants them based on his age and height,\n")
                                    append("click the buttons below. Measurements are slightly different.")
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "GetByAge")
                                }
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen1") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "GetByHeight")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
