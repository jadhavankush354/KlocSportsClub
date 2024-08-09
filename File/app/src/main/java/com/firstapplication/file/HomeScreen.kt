package com.firstapplication.file

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.firstapplication.file.ui.ComponentsUI.SimpleCenterAlignedTopAppBar
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel
import com.firstapplication.file.Repository.SportsEquipmetRepository

@Composable
fun HomeScreen(controller:NavHostController)
{
  val viewmodel: SportsEquipmentViewModel = hiltViewModel()
  val primaryColor = colorResource(id = R.color.black)


  lateinit var repository: SportsEquipmetRepository

  ReadAndInsertDataFromCSV(LocalContext.current, viewModel =viewmodel )

  Box {
    Modifier
      .fillMaxWidth()
      .background(primaryColor)
    Column(
      Modifier
        .fillMaxSize()
        .background(primaryColor)
    )
    {
      SimpleCenterAlignedTopAppBar(controller)
    }
  }
}