package com.example.mindstreak.feature.create_habit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.SectionLabel
import com.example.mindstreak.data.model.Category
import androidx.core.graphics.toColorInt

@Composable
fun CategoryGrid(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelect: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionLabel(label)
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            maxItemsInEachRow = 4,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategoryId == cat.id
                val catColor = remember(cat.color) { Color(cat.color.toColorInt()) }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) catColor.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.secondary
                        )
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) catColor else Color.Transparent,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable { onCategorySelect(cat.id) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(cat.emoji, fontSize = 20.sp)
                        Text(
                            text = cat.name,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) catColor
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}
