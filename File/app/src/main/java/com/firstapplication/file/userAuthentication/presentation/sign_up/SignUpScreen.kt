package com.firstapplication.file.userAuthentication.presentation.sign_up

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.firstapplication.file.userAuthentication.presentation.sign_up.components.SendEmailVerification
import com.firstapplication.file.userAuthentication.presentation.sign_up.components.SignUp
import com.firstapplication.file.userAuthentication.presentation.sign_up.components.SignUpContent
import com.firstapplication.file.userAuthentication.util.Constants.VERIFY_EMAIL_MESSAGE
import com.firstapplication.file.userAuthentication.util.extentionsFunctions.Companion.showMessage


@Composable
@ExperimentalComposeUiApi
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
//            SignUpTopBar(
//                navigateBack = navigateBack
//            )
        },
        content = { padding ->
            SignUpContent(
                padding = padding,
                signUp = { email, password ->
                    viewModel.signUpWithEmailAndPassword(email, password)
                },
                navigateBack = navigateBack
            )
        }
    )

    SignUp(
        sendEmailVerification = {
            viewModel.sendEmailVerification()
        },
        showVerifyEmailMessage = {
            showMessage(context, VERIFY_EMAIL_MESSAGE)
        }
    )

    SendEmailVerification()
}