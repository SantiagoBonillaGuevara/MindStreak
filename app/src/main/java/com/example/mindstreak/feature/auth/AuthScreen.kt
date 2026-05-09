package com.example.mindstreak.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mindstreak.R
import com.example.mindstreak.feature.auth.components.AuthDivider
import com.example.mindstreak.feature.auth.components.AuthForm
import com.example.mindstreak.feature.auth.components.AuthHeader
import com.example.mindstreak.feature.auth.components.AuthSocialLogin
import com.example.mindstreak.feature.auth.components.AuthSubmit
import com.example.mindstreak.feature.auth.components.AuthTab
import com.example.mindstreak.feature.auth.components.AuthTabSelector

@Composable
fun AuthScreen(onLogin: () -> Unit) {
    var tab by remember { mutableStateOf(AuthTab.LOGIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }

    val texts = object {
        val appName = stringResource(R.string.auth_app_name)
        val subtitle = stringResource(R.string.auth_subtitle)
        val greetingLogin = stringResource(R.string.auth_greeting_login)
        val greetingRegister = stringResource(R.string.auth_greeting_register)
        val titleLogin = stringResource(R.string.auth_tab_login)
        val titleRegister = stringResource(R.string.auth_tab_register)
        val namePlaceholder = stringResource(R.string.auth_name_placeholder)
        val universityPlaceholder = stringResource(R.string.auth_university_placeholder)
        val emailPlaceholder = stringResource(R.string.auth_email_placeholder)
        val passwordPlaceholder = stringResource(R.string.auth_password_placeholder)
        val forgotPasswordText = stringResource(R.string.auth_forgot_password)
        val dividerOr = stringResource(R.string.auth_divider_or)
        val loginText = stringResource(R.string.auth_submit_login)
        val registerText = stringResource(R.string.auth_submit_register)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 32.dp)
    ) {
        AuthHeader(
            isLogin = tab == AuthTab.LOGIN,
            appName = texts.appName,
            subtitle = texts.subtitle,
            greetingLogin = texts.greetingLogin,
            greetingRegister = texts.greetingRegister
        )

        AuthTabSelector(
            selectedTab = tab,
            onTabSelected = { tab = it },
            titleLoging = texts.titleLogin,
            titleRegister = texts.titleRegister
        )

        AuthForm(
            tab = tab,
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            name = name,
            onNameChange = { name = it },
            university = university,
            onUniversityChange = { university = it },
            namePlaceholder = texts.namePlaceholder,
            universityPlaceholder = texts.universityPlaceholder,
            emailPlaceholder = texts.emailPlaceholder,
            passwordPlaceholder = texts.passwordPlaceholder,
            forgotPasswordText = texts.forgotPasswordText,
        )

        AuthDivider(text = texts.dividerOr)
        AuthSocialLogin(onClick = {}, providers = listOf("🅖" to "Google"))

        AuthSubmit(
            onClick = onLogin,
            tab = tab,
            loginText = texts.loginText,
            registerText = texts.registerText
        )
    }
}
