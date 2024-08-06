package com.firstapplication.file.userAuthentication.presentation.sign_in

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.firstapplication.file.userAuthentication.presentation.sign_in.components.SignIn
import com.firstapplication.file.userAuthentication.presentation.sign_in.components.SignInContent
import com.firstapplication.file.userAuthentication.util.extentionsFunctions.Companion.showMessage


@Composable
@ExperimentalComposeUiApi
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToForgotPasswordScreen: () -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            //SignInTopBar()
        },
        content = { padding ->
            SignInContent(
                padding = padding,
                signIn = {email, password ->
                    viewModel.signInWithEmailAndPassword(email, password)
                },
                navigateToForgotPasswordScreen = navigateToForgotPasswordScreen,
                navigateToSignUpScreen = navigateToSignUpScreen,
            )
        }
    )

    SignIn(
        showErrorMessage = { errorMessage ->
            showMessage(context, errorMessage)
        }
    )
}