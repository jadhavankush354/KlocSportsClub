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
fun EquipmentSizeCalculatorByAge(viewModel: SportsEquipmentViewModel) {
    var playerAge by remember { mutableStateOf(0) }
    var selectedEquipment by remember { mutableStateOf("") }
    var selectedEquipmentType by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    val equipmentOptions = listOf("Bat", "ProtectiveGear")
    val equipmentTypeMap = mapOf(
        "Bat" to listOf("Leather", "Tennis"),
        "ProtectiveGear" to listOf("Gloves", "Pads", "Helmet"),
        "Shoes" to listOf("Male", "Female")
    )
    val categoriesMap = mapOf(
        "Leather" to listOf("KashmirWillow", "EnglishWillow", "LongHandleBats"),
        "Tennis" to listOf("HardTennis", "lowTennis"),
        "Gloves" to listOf("HandGloves"),
        "Pads" to listOf("LegPads", "ThighGuards"),
        "Helmet" to listOf("Helmet"),
    )

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var expandedEquipment by remember { mutableStateOf(false) }
    var expandedEquipmentType by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }

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
            ExposedDropdownMenuBox(
                expanded = expandedEquipment,
                onExpandedChange = { expandedEquipment = it }
            ) {
                TextField(
                    value = selectedEquipment,
                    onValueChange = { newEquipment ->
                        if (newEquipment != selectedEquipment) {
                            selectedEquipment = newEquipment
                            // Reset dependent selections
                            selectedEquipmentType = ""
                            selectedCategory = ""
                            playerAge = 0  // Reset the age if needed
                            viewModel.resetEquipmentDetails()  // Clear or reset view model state if such a function exists
                        }
                    },
                    label = { Text(text = "Please Select required Equipment") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipment) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp),
                    readOnly = true  // Set the TextField as readOnly to prevent user typing
                )
                ExposedDropdownMenu(
                    expanded = expandedEquipment,
                    onDismissRequest = { expandedEquipment = false }
                ) {
                    equipmentOptions.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                if (item != selectedEquipment) {  // Check if the item selected is different
                                    selectedEquipment = item
                                    // Reset all related fields when a new item is selected
                                    selectedEquipmentType = ""
                                    selectedCategory = ""
                                    playerAge = 0
                                    viewModel.resetEquipmentDetails()
                                }
                                expandedEquipment = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text(text = item)
                        }
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedEquipmentType,
                onExpandedChange = { expandedEquipmentType = it }
            ) {
                TextField(
                    value = selectedEquipmentType,
                    onValueChange = { newEquipmentType ->
                        if (newEquipmentType != selectedEquipmentType) {
                            selectedEquipmentType = newEquipmentType
                            // Reset dependent selections
                            selectedCategory = ""
                            playerAge = 0  // Reset the age if needed
                            viewModel.resetEquipmentDetails()  // Clear or reset view model state if such a function exists
                        }
                    },
                    label = { Text(text = "Please Select Equipment Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipmentType) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )
                val filteredEquipmentTypes =
                    equipmentTypeMap[selectedEquipment]?.filter { it.contains(selectedEquipmentType, ignoreCase = true) } ?: emptyList()

                if (filteredEquipmentTypes.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedEquipmentType,
                        onDismissRequest = { expandedEquipmentType = false }
                    ) {
                        filteredEquipmentTypes.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedEquipmentType = item
                                    expandedEquipmentType = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    viewModel.resetEquipmentDetails()
                                }
                            ) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = { selectedCategory = it },
                    label = { Text(text = "Please Select Categories") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )
                val filteredCategories =
                    categoriesMap[selectedEquipmentType]?.filter { it.contains(selectedCategory, ignoreCase = true) }
                        ?: emptyList()

                if (filteredCategories.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        filteredCategories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                    Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = category)
                            }
                        }
                    }
                }
            }

            LaunchedEffect(key1 = selectedEquipment) {
                if (selectedEquipment.isEmpty()) {
                    playerAge = 0
                }
            }

            OutlinedTextField(
                value = playerAge.toString(),
                onValueChange = { input ->
                    playerAge = input.toIntOrNull() ?: 0
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
                        selectedEquipment,
                        selectedEquipmentType,
                        selectedCategory,
                        playerAge
                    )
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Filled.Send, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Get $selectedEquipment", color = Color.White)
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

                    if (equipmentFound && selectedEquipment.isNotEmpty()) {
                        Text(
                            text = "Suggested Equipment is: $selectedEquipment and length is $equipmentSize ",
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp).background(Color.Yellow)
                        )
                        when {
                            selectedEquipment.equals("Bat", ignoreCase = true) ||
                                    selectedEquipment.equals("Ball", ignoreCase = true) ||
                                    selectedEquipment.equals("ProtectiveGear", ignoreCase = true) -> {
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    value: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
    context: Context
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        TextField(
            value = value,
            onValueChange = { },
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .padding(20.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(item)
                        onExpandedChange(false)
                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(text = item)
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


/*
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EquipmentSizeCalculatorByHeight(viewModel: SportsEquipmentViewModel) {
    var playerHeight by remember { mutableStateOf(0.0) }
    var selectedEquipment by remember { mutableStateOf("") }
    var selectedEquipmentType by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    val equipmentOptions = listOf("Bat", "Ball", "ProtectiveGear", "Shoes")
    val typeOfEquipmentOptions = listOf("Leather", "Tennis", "Male", "Female", "Gloves", "Pads", "Helmet")
    val categoriesOptions = listOf(
        "KashmirWillow", "EnglishWillow", "HandGloves", "LegPads",
        "ThighGuards", "Helmet", "Spiky", "Non-Spiky"
    )

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var expandedEquipment by remember { mutableStateOf(false) }
    var expandedEquipmentType by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedHeight by remember { mutableStateOf(false) }

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

            // Equipment Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedEquipment,
                onExpandedChange = { expandedEquipment = it }
            ) {
                TextField(
                    value = selectedEquipment,
                    onValueChange = { selectedEquipment = it },
                    label = { Text(text = "Please Select required Equipment") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipment) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )

                val filteredOptions =
                    equipmentOptions.filter { it.contains(selectedEquipment, ignoreCase = true) }

                if (filteredOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedEquipment,
                        onDismissRequest = { expandedEquipment = false }
                    ) {
                        filteredOptions.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedEquipment = item
                                    expandedEquipment = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            val equipmentTypeMap = mapOf(
                "Bat" to listOf("Leather", "Tennis"),
                "Ball" to listOf("Leather", "Tennis"),
                "ProtectiveGear" to listOf("Gloves", "Pads", "Helmet"),
                "Shoes" to listOf("Male", "Female")
            )


            ExposedDropdownMenuBox(
                expanded = expandedEquipmentType,
                onExpandedChange = { expandedEquipmentType = it }
            ) {
                TextField(
                    value = selectedEquipmentType,
                    onValueChange = { selectedEquipmentType = it },
                    label = { Text(text = "Please Select Equipment Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipmentType) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )

                val filteredEquipmentTypes =
                    equipmentTypeMap[selectedEquipment]?.filter { it.contains(selectedEquipmentType, ignoreCase = true) } ?: emptyList()

                if (filteredEquipmentTypes.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedEquipmentType,
                        onDismissRequest = { expandedEquipmentType = false }
                    ) {
                        filteredEquipmentTypes.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedEquipmentType = item
                                    expandedEquipmentType = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            val categoriesMap = mapOf(
                "Leather" to listOf("KashmirWillow", "EnglishWillow","LongHandleBats"),
                "Tennis" to listOf("HardTennis", "SoftTennis"),
                "Gloves" to listOf("HandGloves"),
                "Pads" to listOf("LegPads", "ThighGuards"),
                "Helmet" to listOf("Helmet"),
                "Male" to listOf("Spiky","Non-Spiky"),
                "Female" to listOf("Spiky","Non-Spiky")
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = { selectedCategory = it },
                    label = { Text(text = "Please Select Categories") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )


                val filteredCategories =
                    categoriesMap[selectedEquipmentType]?.filter { it.contains(selectedCategory, ignoreCase = true) }
                        ?: emptyList()

                if (filteredCategories.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        filteredCategories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                    Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = category)
                            }
                        }
                    }
                }
            }

//            OutlinedTextField(
//                value = playerHeight.toString(),
//                onValueChange = { input ->
//                    playerHeight = input.toDoubleOrNull() ?: 0.0
//                },
//                label = { Text(if (selectedEquipment.equals("Shoes", ignoreCase = true)) "Heel to Toe Length in cm" else "Player Height", color = Color.Yellow) },
//                placeholder = { Text(if (selectedEquipment.equals("Shoes", ignoreCase = true)) "Enter heel to toe ratio" else "Height") },
//                textStyle = TextStyle(color = Color.Yellow),
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        keyboardController?.hide()
//                    }
//                ),
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            Text(
//                text = "Please mention height in feet (e.g., 5.5)",
//                color = Color.Yellow,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
            if (selectedEquipment.equals("Shoes", ignoreCase = true)) {
                OutlinedTextField(
                    value = playerHeight.toString(),
                    onValueChange = { input ->
                        playerHeight = input.toDoubleOrNull() ?: 0.0
                    },
                    label = { Text(text = "Heel to Toe Length in cm", color = Color.Yellow) },
                    placeholder = { Text(text = "Enter heel to toe ratio", color = Color.Yellow) },
                    textStyle = TextStyle(color = Color.Yellow),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Please mention heel to toe Length in cm",
                    color = Color.Yellow,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                // Dropdown menu for heights
                var selectedHeight by remember { mutableStateOf("") }
                val heightOptions = (35..70).map { it.toDouble() / 10 }.map { it.toString() } // Add more options as needed

                ExposedDropdownMenuBox(
                    expanded = expandedHeight,
                    onExpandedChange = { expandedHeight = it }
                ) {
                    TextField(
                        value = selectedHeight,
                        onValueChange = { selectedHeight = it },
                        label = { Text("Please Select Height", color = Color.White) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHeight) },
                        modifier = Modifier
                            .menuAnchor()
                            .padding(20.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expandedHeight,
                        onDismissRequest = { expandedHeight = false }
                    ) {
                        heightOptions.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedHeight = item
                                    expandedHeight = false
                                    playerHeight = item.toDoubleOrNull() ?: 0.0
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.getEquipmentSizeByHeight(
                        selectedEquipment,
                        selectedEquipmentType,
                        selectedCategory,
                        playerHeight
                    )
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Get $selectedEquipment", color = Color.White)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val equipmentFound by viewModel.equipmentFound.collectAsState()
                    val equipmentSize by viewModel.equipmentSize.collectAsState()
                    val equipmentImageResId by viewModel.equipmentImageResId.collectAsState()

                    if (equipmentFound) {
                        Text(
                            text = "Suggested Equipment is: $selectedEquipment and length is $equipmentSize ",
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        when {
                            selectedEquipment.equals("Bat", ignoreCase = true) ||
                                    selectedEquipment.equals("Ball", ignoreCase = true) ||
                                    selectedEquipment.equals("ProtectiveGear", ignoreCase = true) -> {
                                Image(
                                    painter = painterResource(equipmentImageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 450.dp, height = 500.dp)
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Equipment not found for the entered details.",
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
*/

/*
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EquipmentSizeCalculatorByHeight(viewModel: SportsEquipmentViewModel) {
    var playerHeight by remember { mutableStateOf(0.0) }
    var selectedEquipment by remember { mutableStateOf("") }
    var selectedEquipmentType by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    val equipmentOptions = listOf("Bat", "ProtectiveGear", "Shoes")
    val typeOfEquipmentOptions = listOf("Leather", "Tennis", "Male", "Female", "Gloves", "Pads", "Helmet")
    val categoriesOptions = listOf(
        "KashmirWillow", "EnglishWillow", "HandGloves", "LegPads",
        "ThighGuards", "Helmet", "Spiky", "Non-Spiky"
    )

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var expandedEquipment by remember { mutableStateOf(false) }
    var expandedEquipmentType by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }

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
            ExposedDropdownMenuBox(
                expanded = expandedEquipment,
                onExpandedChange = { expandedEquipment = it }
            ) {
                TextField(
                    value = selectedEquipment,
                    onValueChange = { newEquipment ->
                        if (newEquipment != selectedEquipment) {
                            selectedEquipment = newEquipment
                            // Reset dependent selections
                            selectedEquipmentType = ""
                            selectedCategory = ""
                            playerHeight = 0.0  // Reset the height if needed
                            viewModel.resetEquipmentDetails()  // Clear or reset view model state if such a function exists
                        }
                    },
                    label = { Text(text = "Please Select required Equipment") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipment) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp),
                    readOnly = true  // Set the TextField as readOnly to prevent user typing
                )
                ExposedDropdownMenu(
                    expanded = expandedEquipment,
                    onDismissRequest = { expandedEquipment = false }
                ) {
                    equipmentOptions.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                if (item != selectedEquipment) {  // Check if the item selected is different
                                    selectedEquipment = item
                                    // Reset all related fields when a new item is selected
                                    selectedEquipmentType = ""
                                    selectedCategory = ""
                                    playerHeight = 0.0
                                    viewModel.resetEquipmentDetails()
                                }
                                expandedEquipment = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text(text = item)
                        }
                    }
                }
            }

            val equipmentTypeMap = mapOf(
                "Bat" to listOf("Leather", "Tennis"),
                "Ball" to listOf("Leather", "Tennis"),
                "ProtectiveGear" to listOf("Gloves", "Pads", "Helmet"),
                "Shoes" to listOf("Male", "Female")
            )

            ExposedDropdownMenuBox(
                expanded = expandedEquipmentType,
                onExpandedChange = { expandedEquipmentType = it }
            ) {
                TextField(
                    value = selectedEquipmentType,
                    onValueChange = { newEquipmentType ->
                        if (newEquipmentType != selectedEquipmentType) {
                            selectedEquipmentType = newEquipmentType
                            // Reset dependent selections
                            selectedCategory = ""
                            playerHeight = 0.0  // Reset the height if needed
                            viewModel.resetEquipmentDetails()  // Clear or reset view model state if such a function exists
                        }
                    },
                    label = { Text(text = "Please Select Equipment Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipmentType) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )
                val filteredEquipmentTypes =
                    equipmentTypeMap[selectedEquipment]?.filter { it.contains(selectedEquipmentType, ignoreCase = true) } ?: emptyList()

                if (filteredEquipmentTypes.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedEquipmentType,
                        onDismissRequest = { expandedEquipmentType = false }
                    ) {
                        filteredEquipmentTypes.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedEquipmentType = item
                                    expandedEquipmentType = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    viewModel.resetEquipmentDetails()
                                }
                            ) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            val categoriesMap = mapOf(
                "Leather" to listOf("KashmirWillow", "EnglishWillow","LongHandleBats"),
                "Tennis" to listOf("HardTennis", "SoftTennis"),
                "Gloves" to listOf("HandGloves"),
                "Pads" to listOf("LegPads", "ThighGuards"),
                "Helmet" to listOf("Helmet"),
                "Male" to listOf("Spiky","Non-Spiky"),
                "Female" to listOf("Spiky","Non-Spiky")
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = { selectedCategory = it },
                    label = { Text(text = "Please Select Categories") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )
                val filteredCategories =
                    categoriesMap[selectedEquipmentType]?.filter { it.contains(selectedCategory, ignoreCase = true) }
                        ?: emptyList()

                if (filteredCategories.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        filteredCategories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                    Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = category)
                            }
                        }
                    }
                }
            }

            // Player Height TextField
            OutlinedTextField(
                value = playerHeight.toString(),
                onValueChange = { input ->
                    playerHeight = input.toDoubleOrNull() ?: 0.0
                },
                label = { Text(if (selectedEquipment.equals("Shoes", ignoreCase = true)) "Heel to Toe Length in cm" else "Player Height", color = Color.White) },
                placeholder = { Text(if (selectedEquipment.equals("Shoes", ignoreCase = true)) "Enter heel to toe ratio" else "Height", color = Color.White) },
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
                    viewModel.getEquipmentSizeByHeight(
                        selectedEquipment,
                        selectedEquipmentType,
                        selectedCategory,
                        playerHeight
                    )
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Get $selectedEquipment", color = Color.White)
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

                    if (equipmentFound && !(selectedEquipment.equals(""))) {
                        Text(
                            text = "Suggested Equipment is: $selectedEquipment and length is $equipmentSize ",
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        when {
                            selectedEquipment.equals("Bat", ignoreCase = true) ||
                                    selectedEquipment.equals("Ball", ignoreCase = true) ||
                                    selectedEquipment.equals("ProtectiveGear", ignoreCase = true) -> {
                                Image(
                                    painter = painterResource(equipmentImageResId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 450.dp, height = 350.dp)
                                )
                            }
                            selectedEquipment.equals("Shoes", ignoreCase = true) -> {
                                Image(
                                    painter = painterResource(R.drawable.shoeschartc),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 450.dp, height = 350.dp)
                                )
                            }
                        }
                    }
                    else {
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
*/


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EquipmentSizeCalculatorByHeight(viewModel: SportsEquipmentViewModel) {
    var playerHeight by remember { mutableStateOf(0.0) }
    var selectedEquipment by remember { mutableStateOf("") }
    var selectedEquipmentType by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }

    val equipmentOptions = listOf("Bat", "ProtectiveGear", "Shoes")
    val typeOfEquipmentOptions = listOf("Leather", "Tennis", "Male", "Female", "Gloves", "Pads", "Helmet")
    val categoriesOptions = listOf(
        "KashmirWillow", "EnglishWillow", "HardTennis", "lowTennis","HandGloves", "LegPads",
        "ThighGuards", "Helmet", "Spiky", "Non-Spiky"
    )

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var expandedEquipment by remember { mutableStateOf(false) }
    var expandedEquipmentType by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }

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
            ExposedDropdownMenuBox(
                expanded = expandedEquipment,
                onExpandedChange = { expandedEquipment = it }
            ) {
                TextField(
                    value = selectedEquipment,
                    onValueChange = { newEquipment ->
                        if (newEquipment != selectedEquipment) {
                            selectedEquipment = newEquipment
                            // Reset dependent selections
                            selectedEquipmentType = ""
                            selectedCategory = ""
                            playerHeight = 0.0  // Reset the height if needed
                            viewModel.resetEquipmentDetails()  // Clear or reset view model state if such a function exists
                        }
                    },
                    label = { Text(text = "Please Select required Equipment") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipment) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp),
                    readOnly = true  // Set the TextField as readOnly to prevent user typing
                )
                ExposedDropdownMenu(
                    expanded = expandedEquipment,
                    onDismissRequest = { expandedEquipment = false }
                ) {
                    equipmentOptions.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                if (item != selectedEquipment) {  // Check if the item selected is different
                                    selectedEquipment = item
                                    // Reset all related fields when a new item is selected
                                    selectedEquipmentType = ""
                                    selectedCategory = ""
                                    playerHeight = 0.0
                                    viewModel.resetEquipmentDetails()
                                }
                                expandedEquipment = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text(text = item)
                        }
                    }
                }
            }

            val equipmentTypeMap = mapOf(
                "Bat" to listOf("Leather", "Tennis"),
                "Ball" to listOf("Leather", "Tennis"),
                "ProtectiveGear" to listOf("Gloves", "Pads", "Helmet"),
                "Shoes" to listOf("Male", "Female")
            )

            ExposedDropdownMenuBox(
                expanded = expandedEquipmentType,
                onExpandedChange = { expandedEquipmentType = it }
            ) {
                TextField(
                    value = selectedEquipmentType,
                    onValueChange = { newEquipmentType ->
                        if (newEquipmentType != selectedEquipmentType) {
                            selectedEquipmentType = newEquipmentType
                            // Reset dependent selections
                            selectedCategory = ""
                            playerHeight = 0.0  // Reset the height if needed
                            viewModel.resetEquipmentDetails()  // Clear or reset view model state if such a function exists
                        }
                    },
                    label = { Text(text = "Please Select Equipment Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipmentType) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )
                val filteredEquipmentTypes =
                    equipmentTypeMap[selectedEquipment]?.filter { it.contains(selectedEquipmentType, ignoreCase = true) } ?: emptyList()

                if (filteredEquipmentTypes.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedEquipmentType,
                        onDismissRequest = { expandedEquipmentType = false }
                    ) {
                        filteredEquipmentTypes.forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedEquipmentType = item
                                    expandedEquipmentType = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    viewModel.resetEquipmentDetails()
                                }
                            ) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            val categoriesMap = mapOf(
                "Leather" to listOf("KashmirWillow", "EnglishWillow","LongHandleBats"),
                "Tennis" to listOf("HardTennis", "lowTennis"),
                "Gloves" to listOf("HandGloves"),
                "Pads" to listOf("LegPads","ThighGuards"),
                "Helmet" to listOf("Helmet"),
                "Male" to listOf("Spiky","Non-Spiky"),
                "Female" to listOf("Spiky","Non-Spiky")
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = { selectedCategory = it },
                    label = { Text(text = "Please Select Categories") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .menuAnchor()
                        .padding(20.dp)
                )
                val filteredCategories =
                    categoriesMap[selectedEquipmentType]?.filter { it.contains(selectedCategory, ignoreCase = true) }
                        ?: emptyList()

                if (filteredCategories.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        filteredCategories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                    Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text(text = category)
                            }
                        }
                    }
                }
            }

            // Player Height TextField
            OutlinedTextField(
                value = playerHeight.toString(),
                onValueChange = { input ->
                    playerHeight = input.toDoubleOrNull() ?: 0.0
                },
                label = { Text(if (selectedEquipment.equals("Shoes", ignoreCase = true)) "Heel to Toe Length in cm" else "Player Height", color = Color.White) },
                placeholder = { Text(if (selectedEquipment.equals("Shoes", ignoreCase = true)) "Enter heel to toe ratio" else "Height", color = Color.White) },
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
                    viewModel.getEquipmentSizeByHeight(
                        selectedEquipment,
                        selectedEquipmentType,
                        selectedCategory,
                        playerHeight
                    )
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Get $selectedEquipment", color = Color.White)
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

                    if (equipmentFound && !(selectedEquipment.equals(""))) {
                        Text(
                            text = "Suggested Equipment is: $selectedEquipment and length is $equipmentSize ",
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp).background(Color.Yellow)
                        )
                        when {
                            selectedEquipment.equals("Bat", ignoreCase = true) ||
                                    selectedEquipment.equals("Ball", ignoreCase = true) ||
                                    selectedEquipment.equals("ProtectiveGear", ignoreCase = true) -> {
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
                            selectedEquipment.equals("Shoes", ignoreCase = true) -> {
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
                    }
                    else {
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