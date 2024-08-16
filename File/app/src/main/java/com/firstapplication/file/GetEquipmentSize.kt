package com.firstapplication.file
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.ScrollView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.firstapplication.file.SportsEquipmentDb
import com.firstapplication.file.R
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel



@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EquipmentSizeCalculatorByAge(
    viewModel: SportsEquipmentViewModel,
    requiredEquipment: String,
    equipmentType: String,
    categories: String
) {
    var playerAge by remember { mutableStateOf("0") }

    val keyboardController = LocalSoftwareKeyboardController.current

    var zoomLevel by remember { mutableStateOf(1f) } // Zoom level for images
    var offsetX by remember { mutableStateOf(0f) } // Horizontal offset for panning
    var offsetY by remember { mutableStateOf(0f) } // Vertical offset for panning

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val backgroundImage = painterResource(R.drawable.splashscreen) // Replace with your image resource
        BackgroundImage(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = requiredEquipment,
                onValueChange = {},
                label = { Text(text = "Required Equipment") },
                readOnly = true,  // Set the TextField as readOnly
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            TextField(
                value = equipmentType,
                onValueChange = {},
                label = { Text(text = "Equipment Type") },
                readOnly = true,  // Set the TextField as readOnly
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            TextField(
                value = categories,
                onValueChange = {},
                label = { Text(text = "Categories") },
                readOnly = true,  // Set the TextField as readOnly
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            OutlinedTextField(
                value = playerAge,
                onValueChange = { playerAge = it
                },
                label = { Text("Player Age", color = Color.White) },
                placeholder = { Text(text = "Age", color = Color.White) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                textStyle = TextStyle(color = Color.White),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    viewModel.getEquipmentSizeByAge(
                        requiredEquipment,
                        equipmentType,
                        categories,
                        playerAge.toIntOrNull() ?: 0
                    )
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Filled.Send, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Get $requiredEquipment", color = Color.White)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    val equipmentFound by viewModel.equipmentFound.collectAsState()
                    val equipmentSize by viewModel.equipmentSize.collectAsState()
                    val equipmentImageResId by viewModel.equipmentImageResId.collectAsState()

                    if (equipmentFound && requiredEquipment.isNotEmpty()) {
                        Text(
                            text = "Suggested Equipment is: $requiredEquipment and length is $equipmentSize ",
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp).background(Color.Yellow)
                        )
                        when {
                            requiredEquipment.equals("Bat", ignoreCase = true) ||
                                    requiredEquipment.equals("Ball", ignoreCase = true) ||
                                    requiredEquipment.equals("ProtectiveGear", ignoreCase = true) -> {
                                Image(
                                    painter = painterResource(equipmentImageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 450.dp, height = 500.dp)
                                        .padding(8.dp)
                                        .graphicsLayer(
                                            scaleX = zoomLevel,
                                            scaleY = zoomLevel,
                                            translationX = offsetX,
                                            translationY = offsetY
                                        )
                                        .pointerInput(Unit) {
                                            detectTransformGestures { _, pan, zoom, _ ->
                                                zoomLevel *= zoom
                                                offsetX += pan.x
                                                offsetY += pan.y
                                            }
                                        }
                                )
                            }
                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No equipment found. Please select valid equipment and age.",
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    androidx.compose.material.DropdownMenuItem(
        onClick = onClick,
        content = content
    )
}

suspend fun getEquipmentSize(
    context: Context,
    equipment: String,
    equipmentType: String,
    categoriesType: String,
    playerAge: Int,
): String? {
    Log.d(
        "EquipmentSize",
        "Params: $equipment, $equipmentType, $categoriesType, $playerAge"
    )
    val sportsDao = SportsEquipmentDb.getInstance(context).sportsEquipmentDao
    val result =
        sportsDao.getEquipmentSizebyAge(equipment, equipmentType, categoriesType, playerAge)

    Log.d("EquipmentSize", "Result: $result")
    return result
}



@Composable
fun ResultCard(equipmentFound: Boolean, equipmentSize: String?, equipmentImageResId: Int) {
    if (equipmentFound && equipmentSize != null) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Suggested Equipment Size: $equipmentSize",
                    color = Color.Black
                )
                Image(
                    painter = painterResource(id = equipmentImageResId),
                    contentDescription = "Suggested Equipment",
                    modifier = Modifier.size(width = 450.dp, height = 500.dp).padding(8.dp).fillMaxWidth()
                )
            }
        }
    } else if (!equipmentFound) {
        Text(
            text = "Equipment not found for the entered details.",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}





@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EquipmentSizeCalculatorByHeight(
    viewModel: SportsEquipmentViewModel,
    requiredEquipment: String,
    equipmentType: String,
    categories: String
) {
    var playerHeight by remember { mutableStateOf("0.0") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var zoomLevel by remember { mutableStateOf(1f) } // Zoom level for images
    var offsetX by remember { mutableStateOf(0f) } // Horizontal offset for panning
    var offsetY by remember { mutableStateOf(0f) } // Vertical offset for panning

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val backgroundImage = painterResource(R.drawable.splashscreen) // Replace with your image resource
        BackgroundImage(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = requiredEquipment,
                onValueChange = { },
                label = { Text(text = "Required Equipment") },
                readOnly = true,
                modifier = Modifier
                    .padding(20.dp)
            )

            TextField(
                value = equipmentType,
                onValueChange = { },
                label = { Text(text = "Equipment Type") },
                readOnly = true,
                modifier = Modifier
                    .padding(20.dp)
            )

            TextField(
                value = categories,
                onValueChange = { },
                label = { Text(text = "Categories") },
                readOnly = true,
                modifier = Modifier
                    .padding(20.dp)
            )

            OutlinedTextField(
                value = playerHeight,
                onValueChange = { playerHeight = it },
                label = {
                    Text(
                        text = if (requiredEquipment.equals("Shoes", ignoreCase = true)) "Heel to Toe Length in cm" else "Player Height",
                        color = Color.White
                    )
                },
                placeholder = {
                    Text(
                        text = if (requiredEquipment.equals("Shoes", ignoreCase = true)) "Enter heel to toe ratio" else "Height",
                        color = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                textStyle = TextStyle(color = Color.White),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()  // Hides the keyboard when 'Done' is pressed
                    }
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    viewModel.getEquipmentSizeByHeight(
                        requiredEquipment,
                        equipmentType,
                        categories,
                        playerHeight.toDoubleOrNull() ?: 0.0
                    )
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Get $requiredEquipment", color = Color.White)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    // Collect State Flow values here directly
                    val equipmentFound by viewModel.equipmentFound.collectAsState()
                    val equipmentSize by viewModel.equipmentSize.collectAsState()
                    val equipmentImageResId by viewModel.equipmentImageResId.collectAsState()

                    if (equipmentFound && requiredEquipment.isNotEmpty()) {
                        Text(
                            text = "Suggested Equipment is: $requiredEquipment and length is $equipmentSize ",
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.Yellow)
                        )
                        when {
                            requiredEquipment.equals("Bat", ignoreCase = true) ||
                                    requiredEquipment.equals("Ball", ignoreCase = true) ||
                                    requiredEquipment.equals("ProtectiveGear", ignoreCase = true) -> {
                                Image(
                                    painter = painterResource(equipmentImageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 450.dp, height = 350.dp)
                                        .graphicsLayer(
                                            scaleX = zoomLevel,
                                            scaleY = zoomLevel,
                                            translationX = offsetX,
                                            translationY = offsetY
                                        )
                                        .pointerInput(Unit) {
                                            detectTransformGestures { _, pan, zoom, _ ->
                                                zoomLevel = (zoomLevel * zoom).coerceIn(0.1f, 3f)
                                                offsetX += pan.x * zoomLevel
                                                offsetY += pan.y * zoomLevel
                                            }
                                        }
                                )
                            }
                            requiredEquipment.equals("Shoes", ignoreCase = true) -> {
                                Image(
                                    painter = painterResource(R.drawable.shoeschartc),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 450.dp, height = 350.dp)
                                        .graphicsLayer(
                                            scaleX = zoomLevel,
                                            scaleY = zoomLevel,
                                            translationX = offsetX,
                                            translationY = offsetY
                                        )
                                        .pointerInput(Unit) {
                                            detectTransformGestures { _, pan, zoom, _ ->
                                                zoomLevel = (zoomLevel * zoom).coerceIn(0.1f, 3f)
                                                offsetX += pan.x * zoomLevel
                                                offsetY += pan.y * zoomLevel
                                            }
                                        }
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Equipment not found for the entered details.",
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}



suspend fun getEquipmentSizeByHeight(
    context: Context,
    equipment: String,
    equipmentType: String,
    categoriesType: String,
    playerHieght: Double,
): String? {
    Log.d(
        "EquipmentSize",
        "Params: $equipment, $equipmentType, $categoriesType, $playerHieght"
    )
    val sportsDao = SportsEquipmentDb.getInstance(context).sportsEquipmentDao
    val result =
        sportsDao.getEquipmentSizebyheight(equipment, equipmentType, categoriesType, playerHieght)

    Log.d("EquipmentSize", "Result: $result")
    return result
}

@Composable
fun BackgroundImage(
    painter: Painter,
    contentDescription: String?,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxSize(),
        contentScale = contentScale
    )
}