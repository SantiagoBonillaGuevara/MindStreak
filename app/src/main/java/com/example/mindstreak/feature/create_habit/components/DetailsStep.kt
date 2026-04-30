package com.example.mindstreak.feature.create_habit.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.data.model.Category

@Composable
fun DetailsStep(
    name: String,
    onNameChange: (String) -> Unit,
    nameTouched: Boolean,
    selectedEmoji: String,
    onEmojiSelect: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    category: Category,
    emojis: List<String>,
    nameLabel: String,
    namePlaceholder: String,
    nameError: String,
    emojiLabel: String,
    categoryLabel: String,
    previewPlaceholder: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HabitNameInput(
            name = name,
            onNameChange = onNameChange,
            nameTouched = nameTouched,
            selectedEmoji = selectedEmoji,
            label = nameLabel,
            placeholder = namePlaceholder,
            errorText = nameError
        )

        EmojiPicker(
            emojis = emojis,
            selectedEmoji = selectedEmoji,
            onEmojiSelect = onEmojiSelect,
            label = emojiLabel
        )

        CategoryGrid(
            categories = MockData.CATEGORIES,
            selectedCategoryId = selectedCategory,
            onCategorySelect = onCategorySelect,
            label = categoryLabel
        )

        HabitPreviewCard(
            name = name,
            emoji = selectedEmoji,
            category = category,
            placeholderName = previewPlaceholder
        )
    }
}
