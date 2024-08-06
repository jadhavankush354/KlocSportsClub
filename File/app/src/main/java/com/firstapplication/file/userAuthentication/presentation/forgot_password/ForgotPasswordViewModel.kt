package com.firstapplication.file.userAuthentication.presentation.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.file.userAuthentication.repository.AuthRepository
import com.firstapplication.file.userAuthentication.repository.SendPasswordResetEmailResponse
import com.firstapplication.file.userAuthentication.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var sendPasswordResetEmailResponse by mutableStateOf<SendPasswordResetEmailResponse>(ResultState.Success(false))

    fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = ResultState.Loading
        sendPasswordResetEmailResponse = repo.sendPasswordResetEmail(email)
    }
}