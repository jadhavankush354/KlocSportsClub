package com.firstapplication.file

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.firstapplication.file.R

@Composable
fun CatogeriesOfEquipments(controller: NavHostController)
{
//    val backgroundImage = painterResource(id = R.drawable.splashscreen)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
//        BackHandler {
//            controller.navigate("HomeScreen")
//        }
        LazyColumn {
//            item {
//                BackHandler {
//                    controller.popBackStack()
//                }
//            }
            item {
                ExpandableCricketCard(
                    title = "Recommended Bats",
                    imageRes = R.drawable.cricketgroupbatstwo,
                    description = "Recommended Cricket Bats For Better Play\n" +
                            "KashmirWillow\n" +
                            "EnglishWillow\n" +
                            "SoftBallCricketBats\n" +
                            "MongooseBats\n",
                    backgroundColor = Color.Transparent,
                    onClickImage = {
                        controller.navigate("DialogWithImage")
                    }
                )
            }
            item {
                ExpandableCricketCard(
                    title = "Cricket Ball",
                    imageRes = R.drawable.fireballicon,
                    description = "Click here for Best Cricket Balls \n" +
                            "Tennis Ball\n" +
                            "LeatherBall\n",
                    backgroundColor = colorResource(id = R.color.white),
                    onClickImage = {
                        // Navigate to the corresponding dialogue when clicked
                        controller.navigate("BallD")
                    }
                )
            }
            item {
                ExpandableCricketCard(
                    title = "Protective Gears",
                    imageRes = R.drawable.protectivegears,
                    description = "Click here for Best Protective Gears\n" +
                            "Helmets\n" +
                            "Gloves\n" +
                            "Pads\n",
                    backgroundColor = colorResource(id = R.color.white),
                    onClickImage = {
                        controller.navigate("ProtectiveGear")
                    }
                )
            }
            item {
                ExpandableCricketCard(
                    title = "Cricket Shoes",
                    imageRes = R.drawable.cricketshoes,
                    description = "Click here for Best Cricket Shoes\n" +
                            "Spicky\n" +
                            "Non-Spicky\n",
                    backgroundColor = colorResource(id = R.color.white),
                    onClickImage = {
                        controller.navigate("ShoesDialogues")
                    }
                )
            }
        }
    }
}

@Composable
fun ExpandableCricketCard(
    title: String,
    imageRes: Int,
    description: String,
    backgroundColor: Color,
    onClickImage: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .background(backgroundColor),
        shape = RoundedCornerShape(15.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.2f)
                    .background(backgroundColor)
                    .clickable { onClickImage() }, // Make only the image clickable
            ) {

                Image(
                    painter = painterResource(imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color= colorResource(id = R.color.teal_700)
                )

                Spacer(modifier = Modifier.height(5.dp))

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(
//                            style = MaterialTheme.typography.titleSmall.toSpanStyle(),
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = colorResource(id=R.color.Pink40)
                            )
                        ) {
                            append(description)
                        }
                    },
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    onClick = { offset ->
                        // Handle click event if needed
                        expanded = !expanded
                    }
                )
            }
        }
    }
}