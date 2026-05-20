package com.example.mindstreak.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindstreak.core.components.AuthTextField
import com.example.mindstreak.core.theme.HabitPurple
import com.example.mindstreak.core.theme.HabitTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: EditProfileViewModel = viewModel()
) {
    val user by viewModel.user.collectAsState()
    val state by viewModel.state.collectAsState()
    val texts = rememberEditProfileTexts()

    var name by remember { mutableStateOf("") }
    var avatarEmoji by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            if (name.isEmpty()) name = it.name
            if (avatarEmoji.isEmpty()) avatarEmoji = it.avatarEmoji
        }
    }

    LaunchedEffect(state) {
        if (state is EditProfileState.Success) {
            onBack()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(texts.title, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Preview Circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Brush.linearGradient(listOf(HabitPurple, HabitTeal)),
                        RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) { Text(avatarEmoji, fontSize = 48.sp) }

            Spacer(Modifier.height(32.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    texts.nameLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AuthTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = texts.placeholderName,
                    leadingIcon = Icons.Default.Person
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    texts.avatarLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AuthTextField(
                    value = avatarEmoji,
                    onValueChange = { if (it.length <= 4) avatarEmoji = it },
                    placeholder = "😊",
                    leadingIcon = Icons.Default.EmojiEmotions
                )
            }

            Spacer(Modifier.height(48.dp))

            Button(
                onClick = { viewModel.updateProfile(name, avatarEmoji) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HabitPurple),
                enabled = state !is EditProfileState.Loading && name.isNotBlank() && avatarEmoji.isNotBlank()
            ) {
                if (state is EditProfileState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else Text(texts.saveBtn, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (state is EditProfileState.Error) {
                Spacer(Modifier.height(16.dp))
                Text(
                    (state as EditProfileState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
