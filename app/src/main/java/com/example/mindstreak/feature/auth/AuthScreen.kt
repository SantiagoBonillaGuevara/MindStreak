package com.example.mindstreak.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.feature.auth.components.AuthDivider
import com.example.mindstreak.feature.auth.components.AuthForm
import com.example.mindstreak.feature.auth.components.AuthHeader
import com.example.mindstreak.feature.auth.components.AuthSocialLogin
import com.example.mindstreak.feature.auth.components.AuthSubmit
import com.example.mindstreak.feature.auth.components.AuthTab
import com.example.mindstreak.feature.auth.components.AuthTabSelector

import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun AuthScreen(
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var tab by remember { mutableStateOf(AuthTab.LOGIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val texts = rememberAuthTexts()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                val success = authState as AuthState.Success
                Toast.makeText(context, success.message, Toast.LENGTH_LONG).show()
                
                if (success.isSignUp) tab = AuthTab.LOGIN // Si fue registro, solo cambiamos a la pestaña de login
                else onLogin() // Si fue login o Google, navegamos al Home
                viewModel.resetState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).error, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
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
            namePlaceholder = texts.namePlaceholder,
            emailPlaceholder = texts.emailPlaceholder,
            passwordPlaceholder = texts.passwordPlaceholder,
            forgotPasswordText = texts.forgotPasswordText,
            onForgotPassword = onForgotPassword,
        )

        AuthDivider(text = texts.dividerOr)
        AuthSocialLogin(
            onClick = { viewModel.signInWithGoogle() },
            providers = listOf("🅖" to "Google")
        )

        if (authState is AuthState.Loading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }

        AuthSubmit(
            onClick = {
                if (tab == AuthTab.LOGIN) viewModel.signIn(email, password)
                else viewModel.signUp(email, password, name)
            },
            tab = tab,
            loginText = texts.loginText,
            registerText = texts.registerText
        )
    }
}
