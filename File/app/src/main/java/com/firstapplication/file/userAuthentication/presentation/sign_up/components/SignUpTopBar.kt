package com.firstapplication.file.userAuthentication.presentation.sign_up.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.firstapplication.file.userAuthentication.components.BackIcon
import com.firstapplication.file.userAuthentication.util.Constants.SIGN_UP_SCREEN


@Composable
fun SignUpTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = SIGN_UP_SCREEN
            )
        },
        navigationIcon = {
            BackIcon(
                navigateBack = navigateBack
            )
        }
    )
}