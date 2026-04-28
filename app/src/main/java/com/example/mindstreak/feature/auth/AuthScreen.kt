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

@Composable
fun AuthScreen(onLogin: () -> Unit) {
    // estado local — equivalente a los useState del componente
    var tab by remember { mutableStateOf(AuthTab.LOGIN) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 32.dp)
    ) {
        AuthHeader(
            isLogin = tab == AuthTab.LOGIN,
            appName = "MindStreak",
            subtitle = "For university students",
            greetingLogin = "Welcome back 👋",
            greetingRegister = "Let's get you started 🚀"
        )

        AuthTabSelector(
            selectedTab = tab,
            onTabSelected = { tab = it },
            titleLoging = "Log In",
            titleRegister = "Sign Up"
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
            namePlaceholder = "Full name",
            universityPlaceholder = "University & Year",
            emailPlaceholder = "Email address",
            passwordPlaceholder = "Password",
            forgotPasswordText = "Forgot password?",
        )

        AuthDivider(text = "OR CONTINUE WITH")
        AuthSocialLogin(onClick = {}, providers = listOf("🅖" to "Google"))

        AuthSubmit(
            onClick = onLogin,
            tab = tab,
            loginText = "Log In",
            registerText = "Create Account"
        )
    }
}

