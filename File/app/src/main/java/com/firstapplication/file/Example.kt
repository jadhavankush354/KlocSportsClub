package com.firstapplication.file

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel



@Composable
fun ExampleScreen(
                  requiredEquipment: String,
                  equipmentType: String,
                  categories: String)
{
    val viewmodel: SportsEquipmentViewModel = hiltViewModel()
    EquipmentSizeCalculatorByAge(viewModel = viewmodel,requiredEquipment,equipmentType,categories)
}
