package com.firstapplication.file.userAuthentication.presentation.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.file.userAuthentication.repository.AuthRepository
import com.firstapplication.file.userAuthentication.repository.SignInResponse
import com.firstapplication.file.userAuthentication.util.ResultState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var signInResponse by mutableStateOf<SignInResponse>(ResultState.Success(false))

    fun signInWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signInResponse = ResultState.Loading
        signInResponse = repo.firebaseSignInWithEmailAndPassword(email, password)
    }
}