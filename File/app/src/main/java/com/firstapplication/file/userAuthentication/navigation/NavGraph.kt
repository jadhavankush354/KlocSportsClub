package com.firstapplication.file.userAuthentication.navigation

import FetchDataScreenofAboutScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.firstapplication.file.Dialog.BatTypeDialog
import com.firstapplication.file.CatogeriesOfEquipments
//import com.firstapplication.file.ui.ComponentsUI.AskQuestionForum
import com.firstapplication.file.ui.ComponentsUI.EnglishWillow
import com.firstapplication.file.ui.ComponentsUI.FetchDataScreen
import com.firstapplication.file.ui.ComponentsUI.Gloves
import com.firstapplication.file.ui.ComponentsUI.HardTennis
import com.firstapplication.file.ui.ComponentsUI.Helmet
import com.firstapplication.file.ui.ComponentsUI.KashmirWillow
import com.firstapplication.file.ui.ComponentsUI.LeatherBall
import com.firstapplication.file.ui.ComponentsUI.Legs
import com.firstapplication.file.ui.ComponentsUI.LowTennis
import com.firstapplication.file.ui.ComponentsUI.MangooseBats
import com.firstapplication.file.ui.ComponentsUI.ShoesSizes
import com.firstapplication.file.ui.ComponentsUI.TennisBall
import com.firstapplication.file.ui.ComponentsUI.Thaigh
import com.firstapplication.file.ui.ComponentsUI.UploadCsvScreen
import com.firstapplication.file.Dialog.ProtectiveGear
import com.firstapplication.file.Dialog.ShoesCatsDialog
import com.firstapplication.file.Dialog.ShoesDialog
import com.firstapplication.file.Dialog.ShoesDialogues
import com.firstapplication.file.Dialog.DialogWithImage
import com.firstapplication.file.ExampleScreen
import com.firstapplication.file.ExampleScreen1
import com.firstapplication.file.HomeScreen
import com.firstapplication.file.MainActivity
import com.firstapplication.file.R
import com.firstapplication.file.SplashScreen.SplashScreen
import com.firstapplication.file.Dialog.TennisBatTypeDialog
import com.firstapplication.file.userAuthentication.navigation.Screen.SignInScreen
import com.firstapplication.file.userAuthentication.navigation.Screen.ForgotPasswordScreen
import com.firstapplication.file.userAuthentication.navigation.Screen.SignUpScreen
import com.firstapplication.file.userAuthentication.navigation.Screen.VerifyEmailScreen
import com.firstapplication.file.userAuthentication.presentation.forgot_password.ForgotPasswordScreen
import com.firstapplication.file.userAuthentication.presentation.sign_in.SignInScreen
import com.firstapplication.file.userAuthentication.presentation.sign_up.SignUpScreen
import com.firstapplication.file.userAuthentication.presentation.verify_email.VerifyEmailScreen
import com.firstapplication.file.Dialog.BallD
import com.firstapplication.file.ui.ComponentsUI.AskQuestionForum

@Composable
@ExperimentalComposeUiApi
fun NavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = SignInScreen.route) {
        composable(route = SignInScreen.route) {
            SignInScreen(
                navigateToForgotPasswordScreen = { navController.navigate(ForgotPasswordScreen.route) },
                navigateToSignUpScreen = { navController.navigate(SignUpScreen.route) }
            )
        }
        composable(route = ForgotPasswordScreen.route) { ForgotPasswordScreen(navigateBack = { navController.popBackStack() }) }
        composable(route = SignUpScreen.route) { SignUpScreen(navigateBack = { navController.popBackStack() }) }
        composable(route = VerifyEmailScreen.route) {
            VerifyEmailScreen(
                navigateToSplashScreen = {
                    navController.navigate(Screen.SplashScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screen.SplashScreen.route) { SplashScreen(navController, context = LocalContext.current as MainActivity) }

        composable("SplashScreen") { SplashScreen(navController, context = LocalContext.current as MainActivity) }
        composable("HomeScreen") { HomeScreen(navController) }
        composable("CatogeriesOfEquipments") { CatogeriesOfEquipments(navController) }
        composable("DialogWithImage") {
            DialogWithImage(navController, // Added comma here
                onDismissRequest = {}, onConfirmation = {},
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                imageDescription = "Image Description"
            )
        }
        composable("BatTypeDialog") { BatTypeDialog(navController = navController, onDismissRequest = {}, onConfirmation = {}) }
//        composable("EquipmentSizeCalculatorByAge") {EquipmentSizeCalculatorByAge(viewModel = hiltViewModel<SportsEquipmentViewModel>()) }
        composable("KashmirWillow") { KashmirWillow(navController) }
        composable(
            route = "ExampleScreen/{requiredEquipment}/{equipmentType}/{categories}",
            arguments = listOf(
                navArgument("requiredEquipment") { type = NavType.StringType },
                navArgument("equipmentType") { type = NavType.StringType },
                navArgument("categories") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val requiredEquipment = backStackEntry.arguments?.getString("requiredEquipment") ?: ""
            val equipmentType = backStackEntry.arguments?.getString("equipmentType") ?: ""
            val categories = backStackEntry.arguments?.getString("categories") ?: ""

            ExampleScreen(
                requiredEquipment = requiredEquipment,
                equipmentType = equipmentType,
                categories = categories
            )
        }
        composable(
            route = "ExampleScreen1/{requiredEquipment}/{equipmentType}/{categories}",
            arguments = listOf(
                navArgument("requiredEquipment") { type = NavType.StringType },
                navArgument("equipmentType") { type = NavType.StringType },
                navArgument("categories") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val requiredEquipment = backStackEntry.arguments?.getString("requiredEquipment") ?: ""
            val equipmentType = backStackEntry.arguments?.getString("equipmentType") ?: ""
            val categories = backStackEntry.arguments?.getString("categories") ?: ""
            ExampleScreen1(requiredEquipment, equipmentType, categories)
        }

        composable("EnglishWillow") { EnglishWillow(navController) }
        composable("MangooseBats") { MangooseBats(navController) }
        composable("TennisBatTypeDialog") { TennisBatTypeDialog(navController, onDismissRequest = { }, onConfirmation = {}) }
        composable("BallD") { BallD(navController, onDismissRequest = {}, onLeatherButtonClick = {}, onTennisButtonClick = {}) }
        composable("LeatherBall") { LeatherBall(navController) }
        composable("TennisBall") { TennisBall(navController) }
        composable("ProtectiveGear") {
            ProtectiveGear(navController,
                onDismissRequest = { },
                onItemClick = { selectedItem ->
                    when (selectedItem) {
                        "Gloves" -> { navController.navigate("Gloves") }
                        "LegPads" -> { navController.navigate("Legs") }
                        "ThighPads" -> { navController.navigate("Thaigh") }
                        "Helmet" -> { navController.navigate("Helmet") }
                        "Cancel" -> { navController.navigate("CatogeriesOfEquipments") }
                    }
                }
            )
        }
        composable("Gloves") { Gloves(navController) }
        composable("Legs") { Legs(navController) }
        composable("Thaigh") { Thaigh(navController) }
        composable("Helmet") { Helmet(navController) }
        composable("ShoesDialogues") {
            ShoesDialogues(
                navController = navController,
                onDismissRequest = {},
                onConfirmation = {},
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                imageDescription = "Image Description"
            )
        }
        composable("ShoesDialog") {
            ShoesDialog(navController = navController, onCategorySelected = {}, onDismiss = {})
        }
        composable("ShoesCatsDialog") {
            ShoesCatsDialog(navController = navController, onSpiky = {}, onNonSpiky = {}, onDismiss = { navController.popBackStack() })
        }
        composable(
            route = "ShoesSizes/{category}/{type}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("type") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val type = backStackEntry.arguments?.getString("type")
            ShoesSizes(navController = navController, category = category, type = type)
        }


        composable("HardTennis") { HardTennis(navController) }
        composable("LowTennis") { LowTennis(navController) }
        composable("AskQuestionForum") { AskQuestionForum(navController) }
        composable("UploadCsvScreen") { UploadCsvScreen(navController) }
        composable("FetchDataScreen") { FetchDataScreen(navController) }
        composable("FetchDataScreenofAboutScreen") { FetchDataScreenofAboutScreen(navController) }
    }
}





