package com.firstapplication.file.userAuthentication.presentation.verify_email

import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.firstapplication.file.userAuthentication.components.TopBar
import com.firstapplication.file.userAuthentication.presentation.profile.ProfileViewModel
import com.firstapplication.file.userAuthentication.presentation.profile.components.RevokeAccess
import com.firstapplication.file.userAuthentication.presentation.verify_email.components.ReloadUser
import com.firstapplication.file.userAuthentication.presentation.verify_email.components.VerifyEmailContent
import com.firstapplication.file.userAuthentication.util.Constants.EMAIL_NOT_VERIFIED_MESSAGE
import com.firstapplication.file.userAuthentication.util.Constants.VERIFY_EMAIL_SCREEN
import com.firstapplication.file.userAuthentication.util.extentionsFunctions.Companion.showMessage


@Composable
fun VerifyEmailScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToSplashScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                title = VERIFY_EMAIL_SCREEN,
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }

            )
        },
        content = { padding ->
            VerifyEmailContent(
                padding = padding,
                reloadUser = {
                    viewModel.reloadUser()
                }
            )
        }
    )

    ReloadUser(
        navigateToProfileScreen = {
            if (viewModel.isEmailVerified) {
                navigateToSplashScreen()
            } else {
                showMessage(context, EMAIL_NOT_VERIFIED_MESSAGE)
            }
        }
    )

    RevokeAccess(
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        signOut = {
            viewModel.signOut()
        }
    )
}

