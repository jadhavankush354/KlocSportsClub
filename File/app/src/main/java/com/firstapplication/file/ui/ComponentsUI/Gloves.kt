package com.firstapplication.file.ui.ComponentsUI

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.firstapplication.file.R

/*
@Composable
fun Gloves(navHostController: NavHostController)
{
    Box(Modifier.background(colorResource(id = R.color.primaryContainerLight)))
    {

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
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    ) {
                        append("Discover the Optimal Sizes for Hand Gloves\n\n")
                    }
                    append("Explore our recommended hand glove sizes tailored to enhance your playing skills and match your physical measurements. Choose the perfect fit for a comfortable and secure playing experience.")
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
                                painter = painterResource(id = R.drawable.battingglovessizes),
                                contentDescription = "gloves",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("ICC Standards for Hand Gloves\n\n")
                                    }

                                    append("Explore our recommended hand glove sizes based on age and height. Click the buttons below to find the perfect fit. Please note that measurements may vary slightly.")
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
                                    modifier = Modifier,
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "GlovesByAge")
                                }
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen1") },
                                    modifier = Modifier,
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "GlovesByHeight")
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
fun Gloves(navHostController: NavHostController) {
    // State for image scaling and panning
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

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
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    ) {
                        append("Discover the Optimal Sizes for Hand Gloves\n\n")
                    }
                    append("Explore our recommended hand glove sizes tailored to enhance your playing skills and match your physical measurements. Choose the perfect fit for a comfortable and secure playing experience.")
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
                                    .size(300.dp) // Fixed size for the Box
                                    .padding(16.dp)
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, pan, zoom, _ ->
                                            val newScale = (scale * zoom).coerceIn(1f, 5f)
                                            val newOffsetX = (offsetX + pan.x * scale).coerceIn(
                                                -((newScale - 1) * size.width / 2),
                                                ((newScale - 1) * size.width / 2)
                                            )
                                            val newOffsetY = (offsetY + pan.y * scale).coerceIn(
                                                -((newScale - 1) * size.height / 2),
                                                ((newScale - 1) * size.height / 2)
                                            )

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
                                    painter = painterResource(id = R.drawable.battingglovessizes),
                                    contentDescription = "Hand Gloves Sizes",
                                    contentScale = ContentScale.Fit, // Maintain aspect ratio
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("ICC Standards for Hand Gloves\n\n")
                                    }

                                    append("Explore our recommended hand glove sizes based on age and height. Click the buttons below to find the perfect fit. Please note that measurements may vary slightly.")
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
                                    onClick = { navHostController.navigate("ExampleScreen/ProtectiveGear/Gloves/HandGloves")
                                    },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "Gloves by Age")
                                }
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen1/ProtectiveGear/Gloves/HandGloves") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryContainerLight))
                                ) {
                                    Text(text = "Gloves by Height")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
