package com.firstapplication.file.ui.ComponentsUI

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.firstapplication.file.ViewModel.TriviaViewModel

@Composable
fun TriviaDisplay(viewModel: TriviaViewModel) {
    val triviaState = remember { viewModel.currentTrivia }
    LaunchedEffect(triviaState) {
        viewModel.showTrivia()

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Log.d("debug","${viewModel.currentTrivia}")
        BasicText(
            text = triviaState.value,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
