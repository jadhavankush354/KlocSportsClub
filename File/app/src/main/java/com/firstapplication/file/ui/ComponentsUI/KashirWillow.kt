package com.firstapplication.file.ui.ComponentsUI
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.runtime.DisposableEffect
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
fun KashmirWillow(navcontroller: NavHostController)
{
    Box (Modifier.background(colorResource(id = R.color.purple_200))) {


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
                        append("Explore Recommended Sizes of Kashmir Willow Bats\n\n")
                    }

                    append("Discover the optimal bat sizes based on your cricket skills and physical measurements. Our recommendations adhere to ICC standards, ensuring you find the perfect fit for an enhanced playing experience.")
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
                                painter = painterResource(id = R.drawable.kashmirwillow),
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
                                        append("ICC Standards for Optimal Player Equipment Selection\n\n")
                                    }
                                    append("Ensure a perfect fit for your cricket Bat based on your height and age. Choose an option below to explore our recommendations.")
                                    append("It Should ensure the following dimensions")
                                    append("Width: 4.25in / 10.8 cm\n\n")
                                    append("Depth: 2.64in / 6.7 cm\n\n")
                                    append(" Edges: 1.56in / 4.0cm.\n\n")
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
                                    onClick = { navcontroller.navigate("ExampleScreen") },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                                ) {
                                    Text(text = "GetByAge")
                                }
                                Button(
                                    onClick = {
                                        navcontroller.navigate("ExampleScreen1")
                                    },
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
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
fun KashmirWillow(navController: NavHostController) {
    // States for image zoom and pan
    var scaleImage1 by remember { mutableStateOf(1f) }
    var offsetXImage1 by remember { mutableStateOf(0f) }
    var offsetYImage1 by remember { mutableStateOf(0f) }

    var scaleImage2 by remember { mutableStateOf(1f) }
    var offsetXImage2 by remember { mutableStateOf(0f) }
    var offsetYImage2 by remember { mutableStateOf(0f) }

    Box(Modifier.background(colorResource(id = R.color.purple_200))) {
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
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = colorResource(id = R.color.onTertiaryContainerLightMediumContrast)
                        )
                    ) {
                        append("Explore Recommended Sizes of Kashmir Willow Bats\n\n")
                    }
                    append("Discover the optimal bat sizes based on your cricket skills and physical measurements. Our recommendations adhere to ICC standards, ensuring you find the perfect fit for an enhanced playing experience.")
                },
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp), // Add spacing between items
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Image 1 with zoom and pan
                    Box(
                        modifier = Modifier
                            .size(300.dp) // Set a fixed size for the Box
                            .padding(16.dp)
                            .aspectRatio(1f) // Maintain aspect ratio
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scaleImage1 = (scaleImage1 * zoom).coerceIn(1f, 5f)
                                    val newOffsetX = offsetXImage1 + pan.x * scaleImage1
                                    val newOffsetY = offsetYImage1 + pan.y * scaleImage1

                                    // Constrain the new offsets to prevent the image from sliding out of view
                                    val maxX = (size.width * (scaleImage1 - 1)) / 2
                                    val maxY = (size.height * (scaleImage1 - 1)) / 2

                                    offsetXImage1 = newOffsetX.coerceIn(-maxX, maxX)
                                    offsetYImage1 = newOffsetY.coerceIn(-maxY, maxY)
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
                            painter = painterResource(id = R.drawable.kashmirwillow),
                            contentDescription = "Kashmir Willow",
                            contentScale = ContentScale.Fit, // Ensure the image scales correctly
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                item {
                    // Image 2 with zoom and pan
                    Box(
                        modifier = Modifier
                            .size(300.dp) // Set a fixed size for the Box
                            .padding(16.dp)
                            .aspectRatio(1f) // Maintain aspect ratio
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scaleImage2 = (scaleImage2 * zoom).coerceIn(1f, 5f)
                                    val newOffsetX = offsetXImage2 + pan.x * scaleImage2
                                    val newOffsetY = offsetYImage2 + pan.y * scaleImage2

                                    // Constrain the new offsets to prevent the image from sliding out of view
                                    val maxX = (size.width * (scaleImage2 - 1)) / 2
                                    val maxY = (size.height * (scaleImage2 - 1)) / 2

                                    offsetXImage2 = newOffsetX.coerceIn(-maxX, maxX)
                                    offsetYImage2 = newOffsetY.coerceIn(-maxY, maxY)
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
                            contentDescription = "Kashmir Willow Length",
                            contentScale = ContentScale.Fit, // Ensure the image scales correctly
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                item {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                            ) {
                                append("ICC Standards for Optimal Player Equipment Selection\n\n")
                            }
                            append("Ensure a perfect fit for your cricket Bat based on your height and age. Choose an option below to explore our recommendations.")
                            append("It should ensure the following dimensions:")
                            append("Width: 4.25in / 10.8 cm\n")
                            append("Depth: 2.64in / 6.7 cm\n")
                            append("Edges: 1.56in / 4.0 cm.\n\n")
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
                            onClick = { navController.navigate("ExampleScreen/Bat/Leather/KashmirWillow")
                            },
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                        ) {
                            Text(text = "Get By Age")
                        }
                        Button(
                            onClick = { navController.navigate("ExampleScreen1/Bat/Leather/KashmirWillow") },
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                        ) {
                            Text(text = "Get By Height")
                        }
                    }
                }
            }
        }
    }
}
