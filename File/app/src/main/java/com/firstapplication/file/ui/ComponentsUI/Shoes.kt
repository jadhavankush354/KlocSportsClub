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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
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


@Composable
fun ShoesSizes(navController: NavHostController, category: String?, type: String?) {
    var scale by remember { mutableStateOf(1f) } // Zoom scale
    var offsetX by remember { mutableStateOf(0f) } // Horizontal panning
    var offsetY by remember { mutableStateOf(0f) } // Vertical panning

    Box(Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.onPrimary)) {
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
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.white) // Ensure text is visible on dark background
                        )
                    ) {
                        append("Here are the following sizes of Shoes\n")
                    }
                    append("Recommended based on your playing skills and physical measures\n")
                    append("Category: $category\n")
                    append("Type: $type")
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
                                    painter = painterResource(id = R.drawable.shoeschartc),
                                    contentDescription = "Shoe Size Chart",
                                    contentScale = ContentScale.Fit, // Maintain aspect ratio
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 16.sp,
                                            color = colorResource(id = R.color.white) // Ensure text is visible on dark background
                                        )
                                    ) {
                                        append("These are ICC standards for Cricket Shoes\n")
                                    }
                                    append("If a player wants them based on his age and height, ")
                                    append("click the button below. Measurements are slightly different, ")
                                    append("and sizes from UK to US are also slightly different.")
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
                                    onClick = { navController.navigate("ExampleScreen1/Shoes/$category/$type") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primaryDark))
                                ) {
                                    Text(text = "Get By Heel to Toe Length")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
