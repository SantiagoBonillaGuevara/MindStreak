package com.example.mindstreak.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.AuthTextField
import com.example.mindstreak.core.components.authTextFieldColors
import com.example.mindstreak.core.theme.*

private enum class AuthTab { LOGIN, REGISTER }

@Composable
fun AuthScreen(
    onLogin: () -> Unit,
    //onBack: () -> Unit = {},
) {
    // Todo estado local — equivalente a los useState del componente
    var tab by remember { mutableStateOf(AuthTab.LOGIN) }
    var showPass by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        // ── Hero ─────────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {

            // Logo animado — equivalente a motion.div spring scale 0.5→1
            var logoVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { logoVisible = true }

            val logoScale by animateFloatAsState(
                targetValue = if (logoVisible) 1f else 0.5f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessHigh,   // stiffness: 200
                ),
                label = "logoScale",
            )

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .graphicsLayer { scaleX = logoScale; scaleY = logoScale },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    HabitTeal, // teal-400
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("🔥", fontSize = 24.sp)
                }
                Column {
                    Text(
                        text = "MindStreak",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "For university students",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }

            // Título dinámico según tab
            Text(
                text = if (tab == AuthTab.LOGIN) "Welcome\nback 👋" else "Let's get\nyou started 🚀",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 24.dp),
            )
        }

        // ── Tab switcher ──────────────────────────────────────
        // Equivalente al motion.button con animate backgroundColor
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                .padding(4.dp),
        ) {
            AuthTab.entries.forEach { t ->
                val isSelected = tab == t
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.secondary
                    else Color.Transparent,
                    animationSpec = tween(200),
                    label = "tabBg_$t",
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .clickable { tab = t }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (t == AuthTab.LOGIN) "Login" else "Sign Up",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }
        }

        // ── Campos del formulario (AnimatePresence mode="wait") ──
        AnimatedContent(
            targetState = tab,
            transitionSpec = {
                val toLogin = targetState == AuthTab.LOGIN
                // initial x: tab===login ? -20 : 20
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
                        onValueChange = { name = it },
                        placeholder = "Full name",
                        leadingIcon = Icons.Default.Person,
                    )
                    AuthTextField(
                        value = university,
                        onValueChange = { university = it },
                        placeholder = "University & Year",
                        leadingIcon = Icons.Default.School,
                    )
                }

                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email address",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                )

                // Campo password con toggle
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Password",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontSize = 15.sp,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                    },
                    trailingIcon = {
                        // Eye / EyeOff toggle — equivalente al button de showPass
                        IconButton(onClick = { showPass = !showPass }) {
                            Icon(
                                imageVector = if (showPass) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = if (showPass) "Hide password" else "Show password",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            )
                        }
                    },
                    visualTransformation = if (showPass) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    colors = authTextFieldColors(),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                )

                // Forgot password — solo en login
                if (currentTab == AuthTab.LOGIN) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        TextButton(onClick = {}) {
                            Text(
                                text = "Forgot password?",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }

        // ── Divider "or continue with" ────────────────────────
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "OR CONTINUE WITH",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        // ── Social login ──────────────────────────────────────
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            listOf("🅖" to "Google", "🍎" to "Apple").forEach { (emoji, label) ->
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    ),
                ) {
                    Text(emoji, fontSize = 16.sp)
                    Spacer(Modifier.width(6.dp))
                    Text(
                        label,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                }
            }
        }

        // ── Submit + Guest ────────────────────────────────────
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .navigationBarsPadding(),   // respeta la barra de navegación de Android
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                contentPadding = PaddingValues(0.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                )
                            ),
                            shape = RoundedCornerShape(16.dp),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (tab == AuthTab.LOGIN) "Log In" else "Create Account",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            TextButton(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Continue as Guest",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }
    }
}

