package com.firstapplication.file

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CatogeriesOfEquipments(controller: NavHostController)
{
    var showMenu by remember { mutableStateOf(false) }
    var showProtectiveGearsMenu by remember { mutableStateOf(false) }
    var pdfData by remember { mutableStateOf(0) }
    var selectedPdf by remember { mutableStateOf("") }
    var isShowPdf by remember {
        mutableStateOf(false)
    }
    BackHandler {
        controller.navigate("HomeScreen")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { controller.navigate("HomeScreen") }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                title = { Text("Cricket", modifier = Modifier.fillMaxWidth(), fontSize = 25.sp, textAlign = TextAlign.Center, color = Color.White) },
                actions = {
                    IconButton(onClick = {
                        if (isShowPdf) {
                            isShowPdf = false
                            showMenu = false
                            showProtectiveGearsMenu = false
                            selectedPdf = ""
                        }
                        else showMenu = !showMenu
                    }) {
                       if (isShowPdf) Icon(imageVector = Icons.Default.Close, contentDescription = "Menu", tint = Color.White)
                       else Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            showMenu = false
                            showProtectiveGearsMenu = false
                            isShowPdf=true
                            pdfData = R.raw.cricketcompleteguide
                            selectedPdf = "cricketcompleteguide.pdf"
                        }) {
                            Text("Complete guides")
                        }
                    }
                },
                backgroundColor = Color.Black
            )
        },
        content = { padding ->
            if (!isShowPdf) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn {
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
                                },
                                onClickShowPdf = {
                                    showMenu = false
                                    isShowPdf=true
                                    pdfData = R.raw.cricketbatguide
                                    selectedPdf = "cricketbatguide.pdf"
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
                                },
                                onClickShowPdf = {
                                    showMenu = false
                                    isShowPdf=true
                                    pdfData = R.raw.cricketball
                                    selectedPdf = "cricketball.pdf"
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
                                },
                                onClickShowPdf = {
                                    showProtectiveGearsMenu = true
                                }
                            )
                            DropdownMenu(
                                expanded = showProtectiveGearsMenu,
                                onDismissRequest = { showProtectiveGearsMenu = false },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                DropdownMenuItem(onClick = {
                                    showMenu = false
                                    showProtectiveGearsMenu = false
                                    isShowPdf=true
                                    pdfData = R.raw.helmetguide
                                    selectedPdf = "helmetguide.pdf"
                                }) {
                                    Text("Helmets")
                                }
                                DropdownMenuItem(onClick = {
                                    showMenu = false
                                    showProtectiveGearsMenu = false
                                    isShowPdf=true
                                    pdfData = R.raw.battinggloves
                                    selectedPdf = "battinggloves.pdf"
                                }) {
                                    Text("Gloves")
                                }
                                DropdownMenuItem(onClick = {
                                    showMenu = false
                                    showProtectiveGearsMenu = false
                                    isShowPdf=true
                                    pdfData = R.raw.cricketpadsinfo
                                    selectedPdf = "cricketpadsinfo.pdf"
                                }) {
                                    Text("Pads")
                                }
                            }
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
                                },
                                onClickShowPdf = {
                                    showMenu = false
                                    showProtectiveGearsMenu = false
                                    isShowPdf=true
                                    pdfData = R.raw.cricketshoes
                                    selectedPdf = "cricketshoes.pdf"
                                }
                            )
                        }
                    }
                }
            } else {
                PdfViewer(pdfResId = pdfData, pdfName = selectedPdf)
            }
        }
    )
}

@Composable
fun ExpandableCricketCard(
    title: String,
    imageRes: Int,
    description: String,
    backgroundColor: Color,
    onClickImage: () -> Unit,
    onClickShowPdf: () -> Unit
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
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color= colorResource(id = R.color.teal_700)
                    )
                    IconButton(onClick = { onClickShowPdf() }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Menu")
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(
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


@Composable
fun PdfViewer(pdfResId: Int, pdfName: String) {
    val context = LocalContext.current
    val pdfFile = File(context.cacheDir, pdfName)
    val bitmaps = remember { mutableStateListOf<Bitmap>() }
    var isLoading by remember { mutableStateOf(true) }

    // Perform PDF rendering in a background thread
    LaunchedEffect(pdfResId) {
        withContext(Dispatchers.IO) {
            try {
                // Copy the PDF from raw resource to cache
                context.resources.openRawResource(pdfResId).use { input ->
                    FileOutputStream(pdfFile).use { output ->
                        input.copyTo(output)
                    }
                }

                val pdfRenderer = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
                val pageCount = pdfRenderer.pageCount

                // Clear existing bitmaps
                bitmaps.clear()

                // Render each page and store in the bitmaps list
                for (i in 0 until pageCount) {
                    val page = pdfRenderer.openPage(i)
                    val bitmap = Bitmap.createBitmap(3000, 3500, Bitmap.Config.ARGB_8888)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    page.close()
                    bitmaps.add(bitmap)
                }

                pdfRenderer.close()
                pdfFile.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { Spacer(modifier = Modifier.height(30.dp)) }
            items(bitmaps) { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}