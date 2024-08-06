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
fun MangooseBats(navcontroller: NavHostController)
{
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.black)
                    )
                ) {
                    append("Here are the following sizes of MongooseBat\n\n")
                }
                append("Recommended based on your playing skills and physical measures\n")
                append("for Kashmir Willow Mongoose Bats")
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
                            painter = painterResource(id = R.drawable.mangoosebatdemo),
                            contentDescription = "Kashmir Willow",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.mangoosebatlength),
                            contentDescription = "Kashmir Willow",
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
                                    append("These all are ICC standards\n\n")
                                }
                                append("If a player  wants based on his height and age. Please choose an option below.\n")
                                append("Width: 4.25in / 10.8 cm\n\n")
                                append("Depth: 2.64in / 6.7 cm\n\n")
                                append(" Edges: 1.56in / 4.0cm.\n\n")
                                append("Length/size of the bat is the same; make adjustments to the handle based on player requirements, as shown in the above image.")
                            },
                            modifier = Modifier.padding(16.dp)
                        )

                    }
                    item {
                        Row(
                            Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
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
*/

@Composable
fun MangooseBats(navController: NavHostController) {
    // States for image zoom and pan
    var scaleImage1 by remember { mutableStateOf(1f) }
    var offsetXImage1 by remember { mutableStateOf(0f) }
    var offsetYImage1 by remember { mutableStateOf(0f) }

    var scaleImage2 by remember { mutableStateOf(1f) }
    var offsetXImage2 by remember { mutableStateOf(0f) }
    var offsetYImage2 by remember { mutableStateOf(0f) }

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
                        color = colorResource(id = R.color.black)
                    )
                ) {
                    append("Here are the following sizes of Mongoose Bat\n\n")
                }
                append("Recommended based on your playing skills and physical measures\n")
                append("for Kashmir Willow Mongoose Bats")
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
                // First Image with Zoom & Pan
                Box(
                    modifier = Modifier
                        .size(300.dp) // Fixed size for the Box
                        .padding(16.dp)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newScale = (scaleImage1 * zoom).coerceIn(1f, 5f)
                                val newOffsetX = (offsetXImage1 + pan.x * newScale).coerceIn(
                                    -((newScale - 1) * 300.dp.toPx() / 2),
                                    ((newScale - 1) * 300.dp.toPx() / 2)
                                )
                                val newOffsetY = (offsetYImage1 + pan.y * newScale).coerceIn(
                                    -((newScale - 1) * 300.dp.toPx() / 2),
                                    ((newScale - 1) * 300.dp.toPx() / 2)
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
                        painter = painterResource(id = R.drawable.mangoosebatdemo),
                        contentDescription = "Mongoose Bat Demo",
                        contentScale = ContentScale.Fit, // Ensure the image scales correctly
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            item {
                // Second Image with Zoom & Pan
                Box(
                    modifier = Modifier
                        .size(300.dp) // Fixed size for the Box
                        .padding(16.dp)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val newScale = (scaleImage2 * zoom).coerceIn(1f, 5f)
                                val newOffsetX = (offsetXImage2 + pan.x * newScale).coerceIn(
                                    -((newScale - 1) * 300.dp.toPx() / 2),
                                    ((newScale - 1) * 300.dp.toPx() / 2)
                                )
                                val newOffsetY = (offsetYImage2 + pan.y * newScale).coerceIn(
                                    -((newScale - 1) * 300.dp.toPx() / 2),
                                    ((newScale - 1) * 300.dp.toPx() / 2)
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
                        painter = painterResource(id = R.drawable.mangoosebatlength),
                        contentDescription = "Mongoose Bat Length",
                        contentScale = ContentScale.Fit, // Ensure the image scales correctly
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            item {
                // Text Information
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.black)
                            )
                        ) {
                            append("These all are ICC standards\n\n")
                        }
                        append("If a player wants based on his height and age, please choose an option below.\n")
                        append("Width: 4.25in / 10.8 cm\n\n")
                        append("Depth: 2.64in / 6.7 cm\n\n")
                        append("Edges: 1.56in / 4.0 cm.\n\n")
                        append("Length/size of the bat is the same; make adjustments to the handle based on player requirements, as shown in the above image.")
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
                        onClick = { navController.navigate("ExampleScreen") },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                    ) {
                        Text(text = "Get By Age")
                    }
                    Button(
                        onClick = { navController.navigate("ExampleScreen1") },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.purple_200))
                    ) {
                        Text(text = "Get By Height")
                    }
                }
            }
        }
    }
}
