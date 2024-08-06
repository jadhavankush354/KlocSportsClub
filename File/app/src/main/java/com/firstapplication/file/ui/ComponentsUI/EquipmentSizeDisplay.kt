package com.firstapplication.file.ui.ComponentsUI


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firstapplication.file.R
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel

@Composable
fun EquipmentImage(viewModel: SportsEquipmentViewModel) {
    val imageResId by viewModel.equipmentImageResId.collectAsState()

    Column {
        // Assuming you want to enable zoom only for certain images
        if (imageResId in listOf(R.drawable.batsizes, R.drawable.battingglovesbestimage)) {
            ZoomableImage(
                modifier = Modifier.fillMaxWidth(),
                imageResId = imageResId
            )
        } else {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Static Image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }
    }
}
