package com.example.mindstreak.feature.auth.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.components.AuthTextField

@Composable
fun AuthForm(
    tab: AuthTab,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    name: String = "",
    onNameChange: (String) -> Unit = {},
    university: String = "",
    onUniversityChange: (String) -> Unit = {},
    namePlaceholder:String,
    universityPlaceholder:String,
    emailPlaceholder:String,
    passwordPlaceholder:String,
    forgotPasswordText:String,
) {
    var showPass by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = tab,
        transitionSpec = {
            val toLogin = targetState == AuthTab.LOGIN
            (fadeIn(tween(250)) + slideInHorizontally(tween(250)) { if (toLogin) -it / 8 else it / 8 })
                .togetherWith(fadeOut(tween(200)))
        },
        modifier = Modifier.padding(horizontal = 24.dp),
        label = "formFields",
    ) { currentTab ->
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            if (currentTab == AuthTab.REGISTER) {
                AuthTextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = namePlaceholder,
                    leadingIcon = Icons.Default.Person,
                )
                AuthTextField(
                    value = university,
                    onValueChange = onUniversityChange,
                    placeholder = universityPlaceholder,
                    leadingIcon = Icons.Default.School,
                )
            }

            AuthTextField(
                value = email,
                onValueChange = onEmailChange,
                placeholder = emailPlaceholder,
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
            )

            AuthPasswordField(
                password = password,
                onValueChange = onPasswordChange,
                isVisible = showPass,
                onToggleVisibility = { showPass = !showPass },
                text = passwordPlaceholder,
            )
            // Forgot password — solo en login
            if (currentTab == AuthTab.LOGIN) AuthForgotPassword(onClick = {}, text = forgotPasswordText)
        }
    }
}