package com.example.mindstreak.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class AuthTexts(
    val appName: String,
    val subtitle: String,
    val greetingLogin: String,
    val greetingRegister: String,
    val titleLogin: String,
    val titleRegister: String,
    val namePlaceholder: String,
    val universityPlaceholder: String,
    val emailPlaceholder: String,
    val passwordPlaceholder: String,
    val forgotPasswordText: String,
    val dividerOr: String,
    val loginText: String,
    val registerText: String
)

@Composable
fun rememberAuthTexts() = AuthTexts(
    appName = stringResource(R.string.auth_app_name),
    subtitle = stringResource(R.string.auth_subtitle),
    greetingLogin = stringResource(R.string.auth_greeting_login),
    greetingRegister = stringResource(R.string.auth_greeting_register),
    titleLogin = stringResource(R.string.auth_tab_login),
    titleRegister = stringResource(R.string.auth_tab_register),
    namePlaceholder = stringResource(R.string.auth_name_placeholder),
    universityPlaceholder = stringResource(R.string.auth_university_placeholder),
    emailPlaceholder = stringResource(R.string.auth_email_placeholder),
    passwordPlaceholder = stringResource(R.string.auth_password_placeholder),
    forgotPasswordText = stringResource(R.string.auth_forgot_password),
    dividerOr = stringResource(R.string.auth_divider_or),
    loginText = stringResource(R.string.auth_submit_login),
    registerText = stringResource(R.string.auth_submit_register)
)