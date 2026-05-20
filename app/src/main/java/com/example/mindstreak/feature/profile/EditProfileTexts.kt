package com.example.mindstreak.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class EditProfileTexts(
    val title: String,
    val nameLabel: String,
    val avatarLabel: String,
    val saveBtn: String,
    val successMsg: String,
    val errorMsg: String,
    val placeholderName: String
)

@Composable
fun rememberEditProfileTexts() = EditProfileTexts(
    title = stringResource(R.string.edit_profile_title),
    nameLabel = stringResource(R.string.edit_profile_name_label),
    avatarLabel = stringResource(R.string.edit_profile_avatar_label),
    saveBtn = stringResource(R.string.edit_profile_save_btn),
    successMsg = stringResource(R.string.edit_profile_success),
    errorMsg = stringResource(R.string.edit_profile_error),
    placeholderName = stringResource(R.string.edit_profile_name_placeholder)
)
