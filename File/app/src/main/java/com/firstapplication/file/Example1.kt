package com.firstapplication.file

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel
import com.firstapplication.file.EquipmentSizeCalculatorByHeight


@Composable
fun ExampleScreen1(controller: NavHostController)
{
    val viewmodel: SportsEquipmentViewModel = hiltViewModel()
    EquipmentSizeCalculatorByHeight(viewModel = viewmodel)
}
