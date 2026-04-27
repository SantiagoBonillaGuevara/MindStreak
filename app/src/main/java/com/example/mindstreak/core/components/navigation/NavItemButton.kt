package com.example.mindstreak.core.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.navigation.NavItem
import com.example.mindstreak.core.theme.HabitPurple

@Composable
fun NavItemButton(
    item: NavItem,
    active: Boolean,
    onClick: () -> Unit,
) {
    val activeColor = HabitPurple
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant
    val activeBg = activeColor.copy(alpha = 0.12f)

    val contentColor by animateColorAsState(
        targetValue = if (active) activeColor else inactiveColor,
        animationSpec = tween(200),
        label = "navColor_${item.route}",
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (active) activeBg else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        if (item.emoji != null) {
            Text(
                text = item.emoji,
                fontSize = 20.sp,
                lineHeight = 20.sp,
            )
        } else if (item.icon != null) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(if (active) 20.dp else 18.dp),
            )
        }

        Text(
            text = item.label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            letterSpacing = 0.02.sp,
        )
    }
}