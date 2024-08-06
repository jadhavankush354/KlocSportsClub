package com.firstapplication.file

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel



@Composable
fun ExampleScreen(controller: NavHostController)
{
    val viewmodel: SportsEquipmentViewModel = hiltViewModel()
    EquipmentSizeCalculatorByAge(viewModel = viewmodel)
}
