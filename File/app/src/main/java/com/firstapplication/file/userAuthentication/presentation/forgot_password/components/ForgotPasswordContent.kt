package com.firstapplication.file.userAuthentication.presentation.forgot_password.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.firstapplication.file.R
import com.firstapplication.file.userAuthentication.components.EmailField
import com.firstapplication.file.userAuthentication.components.SmallSpacer
import com.firstapplication.file.userAuthentication.firestoredb.viewmodel.FirestoreViewModel
import com.firstapplication.file.userAuthentication.util.Constants.EMPTY_STRING
import com.firstapplication.file.userAuthentication.util.Constants.RESET_PASSWORD_BUTTON
import com.firstapplication.file.userAuthentication.util.extentionsFunctions

@Composable
fun ForgotPasswordContent(
    padding: PaddingValues,
    sendPasswordResetEmail: (email: String) -> Unit,
    firestoreViewModel: FirestoreViewModel = hiltViewModel()
) {
    val res = firestoreViewModel.res.value
    val context= LocalContext.current
    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = EMPTY_STRING
                )
            )
        }
    )
    val gradient= Brush.linearGradient(
        0.0f to Color(0xFF63696F),
        500.0f to Color.Transparent,
        start = Offset.Zero,
        end = Offset.Infinite
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.splashscreen),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent), // Add padding here
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp), // Add padding here
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailField(
                    email = email,
                    onEmailValueChange = { newValue ->
                        email = newValue
                    }
                )
                SmallSpacer()
                Button(
                    onClick = {
                        val isEmailPresent = res.data.any { firestoreModel ->
                            firestoreModel.user?.email == email.text
                        }
                        if(isEmailPresent)
                        {
                            sendPasswordResetEmail(email.text)
                        }
                        else{
                            extentionsFunctions.showMessage(context,"Create Account...Email is not registered")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF651FFF),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = RESET_PASSWORD_BUTTON,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
