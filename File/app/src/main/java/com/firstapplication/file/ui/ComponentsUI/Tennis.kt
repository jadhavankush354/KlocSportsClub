package com.firstapplication.file.ui.ComponentsUI

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.firstapplication.file.R

@Composable
fun TennisBall(navController: NavHostController) {
    // State for image 1 (Tennis Ball)
    var scale1 by remember { mutableStateOf(1f) }
    var offsetX1 by remember { mutableStateOf(0f) }
    var offsetY1 by remember { mutableStateOf(0f) }
    var image1Size by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    // State for image 2 (Cricket Ball Guide)
    var scale2 by remember { mutableStateOf(1f) }
    var offsetX2 by remember { mutableStateOf(0f) }
    var offsetY2 by remember { mutableStateOf(0f) }
    var image2Size by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackHandler(onBack = {
            navController.navigate("CatogeriesOfEquipments")
        })

        Text(
            text = "Here are the following sizes of BALL \n" +
                    "Recommended based on your playing Skills and physical measures " +
                    "for Tennis Ball",
            fontStyle = FontStyle.Normal,
            color = colorResource(id = R.color.black),
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                // Tennis Ball Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // Maintain aspect ratio
                        .padding(8.dp)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newScale = (scale1 * zoom).coerceIn(1f, 5f)

                                // Calculate new offsets with constraints
                                val newOffsetX = (offsetX1 + pan.x * scale1).coerceIn(
                                    -((newScale - 1) * image1Size.width / 2),
                                    ((newScale - 1) * image1Size.width / 2)
                                )
                                val newOffsetY = (offsetY1 + pan.y * scale1).coerceIn(
                                    -((newScale - 1) * image1Size.height / 2),
                                    ((newScale - 1) * image1Size.height / 2)
                                )

                                // Update state
                                scale1 = newScale
                                offsetX1 = newOffsetX
                                offsetY1 = newOffsetY
                            }
                        }
                        .onGloballyPositioned { coordinates ->
                            image1Size = coordinates.size.toSize()
                        }
                        .graphicsLayer(
                            scaleX = scale1,
                            scaleY = scale1,
                            translationX = offsetX1,
                            translationY = offsetY1
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tennisball),
                        contentDescription = "Tennis Ball",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cricket Ball Guide Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // Maintain aspect ratio
                        .padding(8.dp)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newScale = (scale2 * zoom).coerceIn(1f, 5f)

                                // Calculate new offsets with constraints
                                val newOffsetX = (offsetX2 + pan.x * scale2).coerceIn(
                                    -((newScale - 1) * image2Size.width / 2),
                                    ((newScale - 1) * image2Size.width / 2)
                                )
                                val newOffsetY = (offsetY2 + pan.y * scale2).coerceIn(
                                    -((newScale - 1) * image2Size.height / 2),
                                    ((newScale - 1) * image2Size.height / 2)
                                )

                                // Update state
                                scale2 = newScale
                                offsetX2 = newOffsetX
                                offsetY2 = newOffsetY
                            }
                        }
                        .onGloballyPositioned { coordinates ->
                            image2Size = coordinates.size.toSize()
                        }
                        .graphicsLayer(
                            scaleX = scale2,
                            scaleY = scale2,
                            translationX = offsetX2,
                            translationY = offsetY2
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cricketballguideforall),
                        contentDescription = "Cricket Ball Guide",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "These all are ICC standards " +
                            "Please choose in the above chart.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
