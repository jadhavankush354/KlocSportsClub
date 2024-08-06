package com.firstapplication.file.NavController

import FetchDataScreenofAboutScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firstapplication.file.Dialog.BatTypeDialog
import com.firstapplication.file.CatogeriesOfEquipments
import com.firstapplication.file.ui.ComponentsUI.AskQuestionForum
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
import com.firstapplication.file.Dialog.ShoesDialog
import com.firstapplication.file.Dialog.ShoesDialogues
import com.firstapplication.file.Dialog.DialogWithImage
import com.firstapplication.file.EquipmentSizeCalculatorByAge
import com.firstapplication.file.ExampleScreen
import com.firstapplication.file.ExampleScreen1
import com.firstapplication.file.HomeScreen
import com.firstapplication.file.MainActivity
import com.firstapplication.file.R
import com.firstapplication.file.SplashScreen.SplashScreen
import com.firstapplication.file.Dialog.TennisBatTypeDialog
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel
import com.firstapplication.file.Dialog.BallD



@Composable
fun NavigationController()
{
    val controller = rememberNavController()
    NavHost(navController = controller, startDestination = "SplashScreen")
    {
        composable("SplashScreen")
        {
            SplashScreen(controller, context = LocalContext.current as MainActivity)
        }
        composable("HomeScreen") {
            HomeScreen(controller)
        }
        composable("CatogeriesOfEquipments") {
            CatogeriesOfEquipments(controller)

        }
        composable("DialogWithImage") {
            val painter: Painter = painterResource(id = R.drawable.ic_launcher_foreground)
            val imageDescription: String = "Image Description"

            // Call DialogWithImage within the composable
            DialogWithImage(
                controller, // Added comma here
                onDismissRequest = {
                    // Handle dismissal if needed
                },
                onConfirmation = {
                    // Handle confirmation if needed
                },
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                imageDescription = "Image Description"
            )

        }
        composable("BatTypeDialog")
        {
            BatTypeDialog(navController = controller,
                onDismissRequest = {},
                onConfirmation = {})

        }
        composable("EquipmentSizeCalculatorByAge")
        {
            val viewModel: SportsEquipmentViewModel = viewModel()
            EquipmentSizeCalculatorByAge(viewModel = hiltViewModel<SportsEquipmentViewModel>())
        }
        composable("KashmirWillow")
        {
            KashmirWillow(controller)

        }
        composable("ExampleScreen")
        {
            ExampleScreen(controller)

        }
        composable("ExampleScreen1")
        {
            ExampleScreen1(controller = controller)

        }
        composable("EnglishWillow")
        {
            EnglishWillow(controller)

        }
        composable("MangooseBats")
        {
            MangooseBats(controller)

        }
        composable("TennisBatTypeDialog")
        {
            TennisBatTypeDialog(controller,
                onDismissRequest = { },
                onConfirmation = {})

        }
        composable("BallD")
        {
            BallD(
                controller,
                onDismissRequest = {},
                onLeatherButtonClick = {},
                onTennisButtonClick = {}
            )

        }
        composable("LeatherBall")
        {
            LeatherBall(controller)

        }
        composable("TennisBall")
        {
            TennisBall(controller)

        }
        composable("ProtectiveGear")
        {
            ProtectiveGear(controller,
                onDismissRequest = { },
                onItemClick = { selectedItem ->
                    when (selectedItem) {
                        "Gloves" -> {
                            controller.navigate("Gloves")
                        }

                        "LegPads" -> {
                            controller.navigate("Legs")
                        }

                        "ThighPads" -> {
                            controller.navigate("Thaigh")
                        }

                        "Helmet" -> {
                            controller.navigate("Helmet")
                        }

                        "Cancel" -> {
                            controller.navigate("CatogeriesOfEquipments")
                        }
                    }
                }
            )

        }
        composable("Gloves")
        {
            Gloves(controller)

        }
        composable("Legs")
        {
            Legs(controller)
        }

        composable("Thaigh")
        {
            Thaigh(controller)
        }
        composable("Helmet")
        {
            Helmet(controller)
        }
        composable("ShoesDialogues") {
            ShoesDialogues(
                navController = controller,
                onDismissRequest = {},
                onConfirmation = {},
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                imageDescription = "Image Description"
            )

        }
        composable("ShoesDialog") {
            ShoesDialog(
                navController = controller,
                onCategorySelected = {},
                onDismiss = {}
            )

        }
        composable("ShoesSizes")
        {
            ShoesSizes(controller)
        }
        composable("HardTennis")
        {
            HardTennis(controller)
        }
        composable("LowTennis")
        {
            LowTennis(controller)
        }
        composable("AskQuestionForum")
        {
            AskQuestionForum(controller)
        }
        composable("UploadCsvScreen")
        {
            UploadCsvScreen(controller)
        }
        composable("FetchDataScreen")
        {
            FetchDataScreen(controller)
        }
        composable("FetchDataScreenofAboutScreen")
        {
            FetchDataScreenofAboutScreen(controller)
        }
    }
}


/*
@Composable
fun NavigationController() {
    val controller = rememberNavController()

    NavHost(navController = controller, startDestination = "SplashScreen")
    {
        composable("SplashScreen") {
            SplashScreen(controller, context = LocalContext.current as MainActivity)
        }

        composable("HomeScreen") {
            HomeScreen(controller = controller)
        }

        composable("CatogeriesOfEquipments") {
            CatogeriesOfEquipments(controller = controller)
        }

        composable("DialogWithImage") {
            DialogWithImage(
                controller,
                onDismissRequest = { /* Handle dismissal if needed */ },
                onConfirmation = { /* Handle confirmation if needed */ },
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                imageDescription = "Image Description"
            )
        }

        composable("BatTypeDialog") {
            BatTypeDialog(
                navController = controller,
                onDismissRequest = { /* TODO */ },
                onConfirmation = { /* Handle confirmation if needed */ }
            )
        }

        composable("EquipmentSizeCalculatorByAge") {
            val viewModel: SportsEquipmentViewModel = viewModel()
            EquipmentSizeCalculatorByAge(viewModel = viewModel)
        }
        composable("EquipmentSizeCalculatorByHeight") {
            val viewModel: SportsEquipmentViewModel = viewModel()
            EquipmentSizeCalculatorByHeight(viewModel = viewModel)
        }

        composable("KashmirWillow") {
            KashmirWillow( controller)
        }

        composable("ExampleScreen") {
            ExampleScreen(controller = controller)
        }
        composable("ExampleScreen1") {
            ExampleScreen1(controller = controller)
        }

        // Example for ProtectiveGear destination
        composable("ProtectiveGear") {
            ProtectiveGear(
            controller,
                onDismissRequest = { /* Handle dismissal if needed */ },
                onItemClick = { selectedItem ->
                    when (selectedItem) {
                        "Gloves" -> controller.navigate("Gloves")
                        "LegPads" -> controller.navigate("Legs")
                        "ThighPads" -> controller.navigate("Thaigh")
                    }
                }
            )
        }

        // Example for ShoesDialogues destination
        composable("ShoesDialogues") {
            ShoesDialogues(
                navController = controller,
                onDismissRequest = {},
                onConfirmation = {},
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                imageDescription = "Image Description"
            )
        }
        // Example for ShoesSizes destination
        composable("ShoesSizes") {
            ShoesSizes(controller)
        }
    }
}
*/



