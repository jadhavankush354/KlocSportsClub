package com.firstapplication.file.userAuthentication.presentation.sign_up.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.firstapplication.file.userAuthentication.components.ProgressBar
import com.firstapplication.file.userAuthentication.presentation.sign_up.SignUpViewModel
import com.firstapplication.file.userAuthentication.util.ResultState


@Composable
fun SendEmailVerification(
    viewModel: SignUpViewModel = hiltViewModel()
) {
    when(val sendEmailVerificationResponse = viewModel.sendEmailVerificationResponse) {
        is ResultState.Loading -> ProgressBar()
        is ResultState.Success<*> -> Unit
        is ResultState.Failure -> sendEmailVerificationResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}