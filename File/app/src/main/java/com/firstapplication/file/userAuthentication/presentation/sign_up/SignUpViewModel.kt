package com.firstapplication.file.userAuthentication.presentation.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firstapplication.file.userAuthentication.repository.AuthRepository
import com.firstapplication.file.userAuthentication.repository.SendEmailVerificationResponse
import com.firstapplication.file.userAuthentication.repository.SignUpResponse
import com.firstapplication.file.userAuthentication.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {

    var signUpResponse by mutableStateOf<SignUpResponse>(ResultState.Success(false))
        private set
    var sendEmailVerificationResponse by mutableStateOf<SendEmailVerificationResponse>(
        ResultState.Success(
            false
        )
    )
        private set

    fun signUpWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        signUpResponse = ResultState.Loading
        signUpResponse = repo.firebaseSignUpWithEmailAndPassword(email, password)
    }

    fun sendEmailVerification() = viewModelScope.launch {
        sendEmailVerificationResponse = ResultState.Loading
        sendEmailVerificationResponse = repo.sendEmailVerification()
    }
//    suspend fun isEmailVerified(email:String):Boolean {
//
//      return repo.isEmailAlreadyVerified(email)
//
//    }
}