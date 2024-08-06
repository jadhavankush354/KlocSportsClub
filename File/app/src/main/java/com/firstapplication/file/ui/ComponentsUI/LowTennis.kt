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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.firstapplication.file.R

/*
@Composable
fun LowTennis(navHostController: NavHostController)
{
        Box (Modifier.background(colorResource(id = R.color.purple_200)))
        {
            Column(
                Modifier
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
                                color = colorResource(id = R.color.onTertiaryContainerLightMediumContrast)
                            )
                        ) {
                            append("Explore Recommended Sizes of Low Tennis Bats\n\n")
                        }
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
                                    painter = painterResource(id = R.drawable.lowtennisbats),
                                    contentDescription = "Kashmir Willow",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.newenglishandkashmirwillowlength),
                                    contentDescription = "Kashmir Willow",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                )
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color.Black
                                            )
                                        ) {
                                            append("ISPL Standards for Optimal Player Equipment Selection\n\n")
                                        }

                                        append("Ensure a perfect fit for your cricket Bat based on your height and age. Choose an option below to explore our recommendations.")
                                    },
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            item {
                                Row(
                                    Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = { navHostController.navigate("ExampleScreen") },
                                        modifier = Modifier,
                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                                    ) {
                                        Text(text = "Get Bat By Age")
                                    }
                                    Button(
                                        onClick = {
                                            navHostController.navigate("ExampleScreen1")
                                        },
                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                                    ) {
                                        Text(text = "Get Bat By Height")
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
fun LowTennis(navHostController: NavHostController) {
    // State for the first image
    var scaleImage1 by remember { mutableStateOf(1f) }
    var offsetXImage1 by remember { mutableStateOf(0f) }
    var offsetYImage1 by remember { mutableStateOf(0f) }

    // State for the second image
    var scaleImage2 by remember { mutableStateOf(1f) }
    var offsetXImage2 by remember { mutableStateOf(0f) }
    var offsetYImage2 by remember { mutableStateOf(0f) }

    Box(modifier = Modifier.background(colorResource(id = R.color.purple_200))) {
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
                            color = colorResource(id = R.color.onTertiaryContainerLightMediumContrast)
                        )
                    ) {
                        append("Explore Recommended Sizes of Low Tennis Bats\n\n")
                    }
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
                            // First Image with Zoom & Pan
                            Box(
                                modifier = Modifier
                                    .size(300.dp) // Fixed size for the Box
                                    .padding(16.dp)
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, pan, zoom, _ ->
                                            val newScale = (scaleImage1 * zoom).coerceIn(1f, 5f)
                                            val newOffsetX = (offsetXImage1 + pan.x * scaleImage1).coerceIn(
                                                -((newScale - 1) * size.width / 2),
                                                ((newScale - 1) * size.width / 2)
                                            )
                                            val newOffsetY = (offsetYImage1 + pan.y * scaleImage1).coerceIn(
                                                -((newScale - 1) * size.height / 2),
                                                ((newScale - 1) * size.height / 2)
                                            )

                                            scaleImage1 = newScale
                                            offsetXImage1 = newOffsetX
                                            offsetYImage1 = newOffsetY
                                        }
                                    }
                                    .graphicsLayer(
                                        scaleX = scaleImage1,
                                        scaleY = scaleImage1,
                                        translationX = offsetXImage1,
                                        translationY = offsetYImage1
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.lowtennisbats),
                                    contentDescription = "Low Tennis Bats",
                                    contentScale = ContentScale.Fit, // Maintain aspect ratio
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Second Image with Zoom & Pan
                            Box(
                                modifier = Modifier
                                    .size(300.dp) // Fixed size for the Box
                                    .padding(16.dp)
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, pan, zoom, _ ->
                                            val newScale = (scaleImage2 * zoom).coerceIn(1f, 5f)
                                            val newOffsetX = (offsetXImage2 + pan.x * scaleImage2).coerceIn(
                                                -((newScale - 1) * size.width / 2),
                                                ((newScale - 1) * size.width / 2)
                                            )
                                            val newOffsetY = (offsetYImage2 + pan.y * scaleImage2).coerceIn(
                                                -((newScale - 1) * size.height / 2),
                                                ((newScale - 1) * size.height / 2)
                                            )

                                            scaleImage2 = newScale
                                            offsetXImage2 = newOffsetX
                                            offsetYImage2 = newOffsetY
                                        }
                                    }
                                    .graphicsLayer(
                                        scaleX = scaleImage2,
                                        scaleY = scaleImage2,
                                        translationX = offsetXImage2,
                                        translationY = offsetYImage2
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.newenglishandkashmirwillowlength),
                                    contentDescription = "Tennis Bat Length",
                                    contentScale = ContentScale.Fit, // Maintain aspect ratio
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Text Information
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.Black
                                        )
                                    ) {
                                        append("ISPL Standards for Optimal Player Equipment Selection\n\n")
                                    }
                                    append("Ensure a perfect fit for your tennis bat based on your height and age. Choose an option below to explore our recommendations.")
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        item {
                            // Navigation Buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                                ) {
                                    Text(text = "Get Bat By Age")
                                }
                                Button(
                                    onClick = { navHostController.navigate("ExampleScreen1") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                                ) {
                                    Text(text = "Get Bat By Height")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
