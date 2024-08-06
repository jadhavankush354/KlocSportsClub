package com.firstapplication.file.userAuthentication.presentation.sign_in.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.firstapplication.file.userAuthentication.components.ProgressBar
import com.firstapplication.file.userAuthentication.presentation.sign_in.SignInViewModel
import com.firstapplication.file.userAuthentication.util.ResultState


@Composable
fun SignIn(
    viewModel: SignInViewModel = hiltViewModel(),
    showErrorMessage: (errorMessage: String?) -> Unit
) {
    when(val signInResponse = viewModel.signInResponse) {
        is ResultState.Loading -> ProgressBar()
        is ResultState.Success -> Unit
        is ResultState.Failure -> signInResponse.apply {
            LaunchedEffect(e) {
                print(e)
                showErrorMessage(e.message)
            }
        }
    }
}