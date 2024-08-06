package com.firstapplication.file.userAuthentication.navigation

import com.firstapplication.file.userAuthentication.util.Constants.FORGOT_PASSWORD_SCREEN
import com.firstapplication.file.userAuthentication.util.Constants.PROFILE_SCREEN
import com.firstapplication.file.userAuthentication.util.Constants.SIGN_IN_SCREEN
import com.firstapplication.file.userAuthentication.util.Constants.SIGN_UP_SCREEN
import com.firstapplication.file.userAuthentication.util.Constants.SPLASH_SCREEN
import com.firstapplication.file.userAuthentication.util.Constants.VERIFY_EMAIL_SCREEN


sealed class Screen(val route: String) {
    object SignInScreen: Screen(SIGN_IN_SCREEN)
    object ForgotPasswordScreen: Screen(FORGOT_PASSWORD_SCREEN)
    object SignUpScreen: Screen(SIGN_UP_SCREEN)
    object VerifyEmailScreen: Screen(VERIFY_EMAIL_SCREEN)
    object ProfileScreen: Screen(PROFILE_SCREEN)
    object SplashScreen: Screen(SPLASH_SCREEN)
}