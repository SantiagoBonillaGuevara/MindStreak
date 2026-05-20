package com.example.mindstreak.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class ForgotPasswordTexts(
    val title: String,
    val desc: String,
    val emailPlaceholder: String,
    val submitBtn: String,
    val backToLogin: String
)

@Composable
fun rememberForgotPasswordTexts() = ForgotPasswordTexts(
    title = stringResource(R.string.forgot_password_title),
    desc = stringResource(R.string.forgot_password_desc),
    emailPlaceholder = stringResource(R.string.auth_email_placeholder),
    submitBtn = stringResource(R.string.forgot_password_submit),
    backToLogin = stringResource(R.string.forgot_password_back)
)
