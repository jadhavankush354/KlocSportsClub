package com.firstapplication.file.ViewModel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.file.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
class TriviaViewModel(private val context: Context) : ViewModel() {
    private val _triviaList = mutableStateOf(listOf<String>())
    val triviaList: State<List<String>> = _triviaList

    private val _currentTrivia = mutableStateOf("")
    val currentTrivia: State<String> = _currentTrivia

    init {
        loadTrivia()
    }

    private fun loadTrivia() {
        viewModelScope.launch {
            // Access the raw resource
            val inputStream = context.resources.openRawResource(R.raw.trivia)
            val triviaContent = inputStream.bufferedReader().use { it.readText() }
            _triviaList.value = triviaContent.split("\n").filter { it.isNotBlank() }
            showTrivia()
        }
    }

    suspend fun showTrivia() {
        while (true) {
            for (trivia in triviaList.value) {
                _currentTrivia.value = trivia
                delay(3000) // Display each trivia for 3 seconds
            }
        }
    }
}


