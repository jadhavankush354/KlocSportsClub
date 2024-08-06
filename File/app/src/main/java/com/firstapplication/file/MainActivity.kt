package com.firstapplication.file

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.firstapplication.file.ui.ComponentsUI.parseCsvData
import com.firstapplication.file.ui.ComponentsUI.uploadDataToFirebase
import com.firstapplication.file.userAuthentication.navigation.NavGraph
import com.firstapplication.file.userAuthentication.navigation.Screen
import com.firstapplication.file.userAuthentication.util.readCsvFromAssets
import com.firstapplication.file.userAuthentication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import uploadAboutDataToFirebase


@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
//    override fun onCreate(savedInstanceState: Bundle?)
//    {
//        super.onCreate(savedInstanceState)
//        setContent {
////            val db = Room.databaseBuilder(
////                applicationContext,
////                SportsEquipmentDb::class.java, "Sports_equipment"
////            ).build()
////            EquipmentSizeCalculatorByAge(db = db)
//
//            NavigationController()
//
////            val painter: Painter = painterResource(id = R.drawable.ic_launcher_foreground)
////            val imageDescription: String = "Image Description"
////            val imageDescription: String = "Image Description"
////            DialogWithImage(
////                onDismissRequest = {
////                    // Handle dismissal if needed
////                },
////                onConfirmation = {
////                    // Handle confirmation if needed
////                },
////                painter = painterResource(id = R.drawable.ic_launcher_foreground),
////                imageDescription = "Image Description"
////            )
////            KashmirWillow()
//        }
//    }
private lateinit var navController: NavHostController
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Read CSV data from assets
        val csvData = readCsvFromAssets(this)
        val csvAboutData=readAboutCsvFromAssets(this)


        // Process CSV data and upload to Firebase
        val dataList = parseCsvData(csvData)
        val dataListForAbout = parseCsvData(csvAboutData)
        uploadDataToFirebase(dataList)
        uploadAboutDataToFirebase(dataListForAbout)

        setContent {
//            val isInsert = remember { mutableStateOf(false) }
            navController = rememberNavController()
            NavGraph(
                navController = navController
            )
            AuthState()
        }
    }

    private fun readAboutCsvFromAssets(context: Context): String {
        return context.assets.open("about.csv").bufferedReader().use { it.readText() }
    }



    @Composable
    private fun AuthState() {

        val isUserSignedOut = viewModel.getAuthState().collectAsState().value
        if (isUserSignedOut) {
            NavigateToSignInScreen()
        } else {
            if (viewModel.isEmailVerified) {
                NavigateToSplashScreen()
            } else {
                NavigateToVerifyEmailScreen()
            }
        }
    }

    @Composable
    private fun NavigateToSignInScreen() = navController.navigate(Screen.SignInScreen.route) {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }

    @Composable
    private fun NavigateToProfileScreen() = navController.navigate(Screen.ProfileScreen.route) {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }
    @Composable
    private fun NavigateToSplashScreen() = navController.navigate(Screen.SplashScreen.route) {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }

    @Composable
    private fun NavigateToVerifyEmailScreen() = navController.navigate(Screen.VerifyEmailScreen.route) {
        popUpTo(navController.graph.id) {
            inclusive = true
        }
    }
//    @Composable
//    private fun NavigateToAdminDashboard() = navController.navigate(Screen.AdminDashboardScreen.route)
//    {
//        popUpTo(navController.graph.id) {
//            inclusive = true
//        }
//    }
}

