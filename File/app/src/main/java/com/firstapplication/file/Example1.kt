package com.firstapplication.file

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel
import com.firstapplication.file.EquipmentSizeCalculatorByHeight


@Composable
fun ExampleScreen1(
                   requiredEquipment: String,
                   equipmentType: String,
                   categories: String)
{
    val viewmodel: SportsEquipmentViewModel = hiltViewModel()
    EquipmentSizeCalculatorByHeight(viewModel = viewmodel,requiredEquipment,equipmentType,categories)
}
