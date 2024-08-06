package com.firstapplication.file.ui.ComponentsUI

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.firstapplication.file.R
import com.firstapplication.file.userAuthentication.presentation.profile.ProfileViewModel
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(controller: NavHostController)
{
    val primaryColor = colorResource(id = R.color.md_theme_inversePrimary)
    val barColour= colorResource(id = R.color.md_theme_outline)
    TopAppBar(
        title = {
            Text(
                text = "KlocSportsClub",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp), // Adjust padding as needed
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Default, // You can replace this with your custom font
                    fontSize = 30.sp // Adjust the font size as needed
                )
            )
        }, modifier = Modifier.background(barColour),
        navigationIcon = {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Localized description"
                )
            }
        },
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Text(text = "INFORMATION AND COMMUNITY")
    }
    Spacer(modifier = Modifier.height(16.dp))
    ClickableSportRow(
        onCricketClick = {
            // Handle the Cricket click
            controller.navigate("CatogeriesOfEquipments")
        },
        onBadmintonClick = {

            controller.navigate("SomeOtherDestination")
        }
    )
  Bhavana()
}
*/

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(controller: NavHostController) {
    val primaryColor = colorResource(id = R.color.md_theme_inversePrimary)
    val barColour = colorResource(id = R.color.md_theme_outline)

    // Add your background image here
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Your existing content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "KlocSportsClub",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            fontSize = 30.sp
                        )
                    )
                },
                modifier = Modifier.background(barColour),
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "INFORMATION AND COMMUNITY")
            }
            Spacer(modifier = Modifier.height(16.dp))
            ClickableSportRow(
                onCricketClick = {
                    controller.navigate("CatogeriesOfEquipments")
                },
                onBadmintonClick = {
                    controller.navigate("SomeOtherDestination")
                }
            )

        }
    }
}
 */

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(controller: NavHostController) {
    val primaryColor = colorResource(id = R.color.black)
    val barColour = colorResource(id = R.color.black)

    // Add your background image here
    Box(
        modifier = Modifier.fillMaxSize()
            .background(barColour)
    ) {
        Image(
            painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "KlocSportsClub",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Default,
                            fontSize = 30.sp,
                            color = primaryColor // Title text color
                        )
                    )
                },
                Modifier.background(barColour), // Set the background color for the TopAppBar
                navigationIcon = {
                    IconButton(onClick =
                    {

                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Localized description",
                            tint = Color.Red // Icon color
                        )
                    }
                },

                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Localized description",
                            tint = Color.Red // Icon color
                        )
                    }
                },
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "INFORMATION AND COMMUNITY", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                ClickableSportRow(
                    onCricketClick = {
                        controller.navigate("CatogeriesOfEquipments")
                    },
                    onBadmintonClick = {
                        controller.navigate("SomeOtherDestination")
                    }
                )
            }
        }
    }
}
*/

/*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(
    controller: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "KlocSportsClub",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    },
                    backgroundColor = Color.White,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Localized description",
                                tint = Color.Black
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Localized description",
                                tint = Color.Black
                            )
                        }
                    }
                )
            },
            drawerContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Logout",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                // Implement logout logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = Color.White)
                    )
                    Text(
                        text = "FAQ",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                // Implement FAQ logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = Color.White)
                    )
                    Text(
                        text = "About",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                // Implement About logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = Color.White)
                    )
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splashscreen),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "INFORMATION AND COMMUNITY",
                        style = MaterialTheme.typography.h5.copy(color = Color.Black)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Implement your ClickableSportRow or other content here
                }
            }
        )
    }
}

@Composable
fun SimpleCenterAlignedTopAppBarPreview() {
    val controller = rememberNavController()
    SimpleCenterAlignedTopAppBar(controller = controller)
}

*/


/*
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(controller: NavHostController)
{
    val primaryColor = Color.White // Adjust text color
    val barColour = Color.Black // Adjust background color

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
    )
    {
        Image(
            painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
        // Scaffold with TopAppBar
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "KlocSportsClub",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = primaryColor
                            )
                        )
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = barColour // Set background color here
                    ), // Set background color here
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Localized description",
                                tint = primaryColor // Icon color
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { /* Handle search icon click */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Localized description",
                                tint = primaryColor // Icon color
                            )
                        }
                    }
                )
            },
            drawerContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(barColour)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Adjust image size as needed
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Logout",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                // Implement logout logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = primaryColor)
                    )
                    Text(
                        text = "FAQ",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                // Implement FAQ logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = primaryColor)
                    )
                    Text(
                        text = "About",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                // Implement About logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = primaryColor)
                    )
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp), // Add padding values from Scaffold content
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                )
                {

                    Text(
                        text = "INFORMATION AND COMMUNITY",
                        style = MaterialTheme.typography.h5.copy(color = barColour)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Implement your ClickableSportRow or other content here
                    ClickableSportRow(
                        onCricketClick = {
                            controller.navigate("CatogeriesOfEquipments")
                        },
                        onBadmintonClick = {
                            controller.navigate("SomeOtherDestination")
                        }
                    )
                }
            }
        )
    }
}
*/

/*
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(
    controller: NavHostController,viewmodel: ProfileViewModel= hiltViewModel()
)
{
    val primaryColor = Color.White // Adjust text color
    val barColour = Color.Black // Adjust background color

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KlocSportsClub",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = primaryColor
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = barColour // Set background color here
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Side drawer Menu",
                            tint = primaryColor // Icon color
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {


                         }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Localized description",
                            tint = primaryColor // Icon color
                        )
                    }
                }
            )
        },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(barColour)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Adjust image size as needed
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Logout",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            viewmodel.signOut()
                            // Implement logout logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "FAQ",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            // Implement FAQ logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "About",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            // Implement About logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop // Ensure the image covers the entire background
                )

                // Overlay content on top of the background image
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Add padding values from Scaffold content
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "INFORMATION AND COMMUNITY",
                        style = MaterialTheme.typography.h5.copy(color = Color.White)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Implement your ClickableSportRow or other content here
                    ClickableSportRow(
                        onCricketClick = {
                            controller.navigate("CatogeriesOfEquipments")
                        },
                        onBadmintonClick = {
                            controller.navigate("SomeOtherDestination")
                        }
                    )
                }
            }
        }
    )
}
*/


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.firstapplication.file.userAuthentication.firestoredb.viewmodel.FirestoreViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.json.JsonNull.content
import org.intellij.lang.annotations.JdkConstants

/*
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(
    controller: NavHostController,
    viewmodel: ProfileViewModel = hiltViewModel()
) {
    val primaryColor = Color.White // Adjust text color
    val barColour = Color.Black // Adjust background color

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    if (showSearch) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                // Hide keyboard or perform search
                            }),
                            textStyle = TextStyle(color = primaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.Transparent),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search...",
                                        style = TextStyle(color = primaryColor.copy(alpha = 0.6f))
                                    )
                                }
                                innerTextField()
                            }
                        )
                    } else {
                        Text(
                            text = "KlocSportsClub",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = primaryColor
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = barColour // Set background color here
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Side drawer Menu",
                            tint = primaryColor // Icon color
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showSearch = !showSearch
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = primaryColor // Icon color
                        )
                    }
                }
            )
        },
   /*
        drawerContent =
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(barColour)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Adjust image size as needed
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Logout",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            viewmodel.signOut()
                            // Implement logout logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "FAQ",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                controller.navigate("FetchDataScreen")
                                scaffoldState.drawerState.close()
                            }
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "About",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                controller.navigate("FetchDataScreenofAboutScreen")
                                scaffoldState.drawerState.close()
                            }
                            // Implement About logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "Community_Feedback",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {

                            coroutineScope.launch {
                                controller.navigate("CommentSessionScreen")
                                scaffoldState.drawerState.close()
                            }
                            // Implement About logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
            }
        },*/
        drawerContent =
        {
            val context = LocalContext.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(barColour)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Adjust image size as needed
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Logout",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            viewmodel.signOut()
                            // Implement logout logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "FAQ",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                controller.navigate("FetchDataScreen")
                                scaffoldState.drawerState.close()
                            }
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "About",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            coroutineScope.launch {
                                controller.navigate("FetchDataScreenofAboutScreen")
                                scaffoldState.drawerState.close()
                            }
                            // Implement About logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )
                Text(
                    text = "Community_Feedback",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {

                            coroutineScope.launch {
                                controller.navigate("CommentSessionScreen")
                                scaffoldState.drawerState.close()
                            }
                            // Implement About logic here
                        },
                    style = MaterialTheme.typography.body1.copy(color = primaryColor)
                )

                Spacer(modifier = Modifier.weight(1f)) // Push the contact section to the bottom

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:1234567890") // Replace with your phone number
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = "Call", tint = Color.White)
                        }

                        Text(
                            text = "Contact for help or feedback",
                            style = MaterialTheme.typography.body1.copy(
                                fontWeight = FontWeight.Bold,
                                color = primaryColor // Replace with your actual color
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:example@example.com") // Replace with your email
                                putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                            }
                            context.startActivity(intent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color.White, // Ensure the icon is tinted white
                                modifier = Modifier.size(24.dp) // Optional: Set size if needed for better visibility
                            )
                        }

                    }
                }
            }
        },
                content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop // Ensure the image covers the entire background
                )

                // Overlay content on top of the background image
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Add padding values from Scaffold content
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "INFORMATION AND COMMUNITY",
                        style = MaterialTheme.typography.h5.copy(color = Color.White)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Implement your ClickableSportRow or other content here
                    ClickableSportRow(
                        onCricketClick = {
                            controller.navigate("CatogeriesOfEquipments")
                        },
                        onBadmintonClick = {
                            controller.navigate("SomeOtherDestination")
                        },
                        searchQuery = searchQuery // Pass the search query to the ClickableSportRow
                    )
                }
            }
        }
    )
}
*/

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCenterAlignedTopAppBar(
    controller: NavHostController,
    viewmodel: ProfileViewModel = hiltViewModel(),
    viewmodel1:FirestoreViewModel =hiltViewModel()
) {
    val primaryColor = Color.White // Adjust text color
    val barColour = Color.Black // Adjust background color

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    if (showSearch) {
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                // Hide keyboard or perform search
                            }),
                            textStyle = TextStyle(color = primaryColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.Transparent),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search your Sport...",
                                        style = TextStyle(color = primaryColor.copy(alpha = 0.6f))
                                    )
                                }
                                innerTextField()
                            }
                        )
                    } else {
                        Text(
                            text = "KlocSportsClub",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = primaryColor
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = barColour // Set background color here
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Side drawer Menu",
                            tint = primaryColor // Icon color
                        )
                    }
                },
                actions = {
                    BackHandler {
                        if (showSearch) {
                            showSearch = false
                        }
                    }
                    IconButton(
                        onClick = {
                            showSearch = !showSearch
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = primaryColor // Icon color
                        )
                    }
                }

            )
        },
        drawerContent = {
            val context = LocalContext.current
            BackHandler(onBack = {
                controller.popBackStack()
            })

            val email = viewmodel.currentUser?.takeIf { it.isNotEmpty() } ?: ""
            var userName by remember { mutableStateOf("") }
            LaunchedEffect(email) {
                if (email.isNotEmpty()) {
                    viewmodel1.getUserByEmail(email)
                }
            }
            LaunchedEffect(viewmodel1.res1) {
                snapshotFlow { viewmodel1.res1.value }
                    .distinctUntilChanged()
                    .collect { state ->
                        state.data?.let { user ->
                            userName = user.user?.name ?: ""
                        }
                    }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(barColour)
            ) {
                item {
                    BackHandler {
                    }
                }
                item {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.user), // Replace with your image resource
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .padding(10.dp) // Adjust image size as needed
                        )
                        Spacer(modifier = Modifier.height(8.dp) )
                        Text(
                            text = userName,
                            modifier = Modifier
                                .fillMaxWidth() // This ensures the Text takes up the full width of its container
                                .padding(16.dp), // Optional: Add background color if needed
                            style = MaterialTheme.typography.body1.copy(color = primaryColor),
                            textAlign = TextAlign.Center // This aligns the text horizontally within the Text composable
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    Text(
                        text = "Logout",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                viewmodel.signOut()
                                // Implement logout logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = primaryColor)
                    )
                }
                item {
                    Text(
                        text = "FAQ",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    controller.navigate("FetchDataScreen")
                                    scaffoldState.drawerState.close()
                                }
                            },
                        style = MaterialTheme.typography.body1.copy(color = primaryColor)
                    )
                }
                item {
                    Text(
                        text = "About",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    controller.navigate("FetchDataScreenofAboutScreen")
                                    scaffoldState.drawerState.close()
                                }
                                // Implement About logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = primaryColor)
                    )
                }
                item {
                    Text(
                        text = "Community_Feedback",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    controller.navigate("AskQuestionForum")
                                    scaffoldState.drawerState.close()
                                }
                                // Implement About logic here
                            },
                        style = MaterialTheme.typography.body1.copy(color = primaryColor)
                    )
                }
                item { Spacer(modifier = Modifier.weight(1f)) } // Push the contact section to the bottom
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // IconButton for Call or WhatsApp
                            IconButton(onClick = { showDialog = true }) {
                                Icon(imageVector = Icons.Default.Phone, contentDescription = "Call", tint = Color.White)
                            }

                            // Text
                            Text(
                                text = "Contact for support or feedback",
                                style = MaterialTheme.typography.body1.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = primaryColor // Replace with your actual color
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            // IconButton for Email
                            IconButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:gula.gundim@kloctechnologies.com") // Replace with your email
                                        putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                                    }
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email, // Corrected from Icons.Default.Mail to Icons.Default.Email
                                    contentDescription = "Email",
//                                    tint = primaryColor,
                                    modifier = Modifier // Set a larger size for better visibility
                                )
                            }
                        }
                        // AlertDialog
                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text(text = "Choose an action") },
                                text = { Text("Would you like to call or send a WhatsApp message?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showDialog = false
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:+91 8546893467") // Replace with your phone number
                                        }
                                        context.startActivity(intent)
                                    }) {
                                        Text("Call")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        showDialog = false
                                        val url = "https://api.whatsapp.com/send?phone=+918546893467" // Replace with your phone number
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(url)
                                        }
                                        context.startActivity(intent)
                                    }) {
                                        Text("WhatsApp")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                // Background Image
                Image(
                    painter = painterResource(id = R.drawable.splashscreen), // Replace with your image resource
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop // Ensure the image covers the entire background
                )

                // Overlay content on top of the background image
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Add padding values from Scaffold content
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "INFORMATION AND COMMUNITY",
                        style = MaterialTheme.typography.h6.copy(color = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), // Ensure it doesn't take up more vertical space than needed
                        maxLines = 1, // Ensure text stays on a single line
                        textAlign = TextAlign.Center // Center the text horizontally within its container
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // Implement your ClickableSportRow or other content here
                    ClickableSportRow(
                        onCricketClick = {
                            controller.navigate("CatogeriesOfEquipments")
                        },
                        onBadmintonClick = {
                            controller.navigate("")
                        },
                        searchQuery = searchQuery // Pass the search query to the ClickableSportRow
                    )
                }
            }
        }
    )
}

@Composable
fun SimpleCenterAlignedTopAppBarPreview() {
    val controller = rememberNavController()
    SimpleCenterAlignedTopAppBar(controller = controller)
}

/*
@Composable
fun ClickableSportRow(onCricketClick: () -> Unit, onBadmintonClick: () -> Unit)
{
    val sportImages = listOf(
        R.drawable.theshowmancricket,
        R.drawable.badmintonshowman,
        R.drawable.swimmingtheshowman,
        R.drawable.hockeyshowman,
        R.drawable.basketballshowman
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

            items(5) { index ->
                Image(
                    painter = painterResource(id = sportImages[index]),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                when (index) {
                    0 -> SportBox("Cricket", onClick = onCricketClick, fontSize = 23f, fontWeight = FontWeight.Bold)
                    1 -> SportBox("Badminton", onClick = null, fontSize = 17.5f, fontWeight = FontWeight.ExtraBold)
                    2 -> SportBox("Swimming", onClick = null, fontSize = 17.5f, fontWeight = FontWeight.ExtraBold)
                    3 -> SportBox("Hockey", onClick = null, fontSize = 17.5f, fontWeight = FontWeight.ExtraBold)
                    4 -> SportBox("BasketBall", onClick = null, fontSize = 17.5f, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
}
*/

@Composable
fun ClickableSportRow(
    onCricketClick: () -> Unit,
    onBadmintonClick: () -> Unit,
    searchQuery: String // Add searchQuery as a parameter
) {
    val sportImages = listOf(
        R.drawable.theshowmancricket,
        R.drawable.badmintonshowman,
        R.drawable.swimmingtheshowman,
        R.drawable.hockeyshowman,
        R.drawable.basketballshowman
    )
    val sports = listOf("Cricket", "Badminton", "Swimming", "Hockey", "Basketball")
    val filteredSports = sports.filter { it.contains(searchQuery, ignoreCase = true) }
    var showExitDialog by remember { mutableStateOf(false)}
    BackHandler {
        showExitDialog = true
    }
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(text = "Exit App") },
            text = { Text(text = "Are you sure you want to exit the app?") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        // Exit the app
                        // Use the appropriate method to exit the app
                        // For example, you might use System.exit(0) if you want to forcefully exit
                        // But it's better to just finish the activity, which is typically preferred in Android apps.
                        // In a real app, you might want to use something like:
                        // (LocalContext.current as? Activity)?.finish()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
        LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

        }
        items(filteredSports.indices.toList()) { index ->
            val sport = filteredSports[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        when (sport) {
                            "Cricket" -> onCricketClick()
                            /*       "Badminton" -> onBadmintonClick()  */
                            // Handle other sports click actions if needed
                        }
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = sportImages[sports.indexOf(sport)]),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                SportBox(
                    text = sport,
                    fontSize = 17.5f,
                    fontWeight = FontWeight.ExtraBold,
                    backgroundColor = Color.Transparent // Adjust background color as needed
                )
            }
        }
    }
}

@Composable
fun SportBox(
    text: String,
    onClick: (() -> Unit)? = null,
    fontSize: Float = 16f,
    fontWeight: FontWeight = FontWeight.Normal,
    backgroundColor: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .size(100.dp, 100.dp)
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .clickable { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        BackHandler(onBack = {

        })
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = fontWeight
            )
        )
    }
}










