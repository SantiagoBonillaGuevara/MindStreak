package com.example.mindstreak.core.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.navigation.NAV_ITEMS

@Composable
fun NavBottom(currentRoute: String?, onNavigate: (String) -> Unit) {

    val navBackground = MaterialTheme.colorScheme.surface
    val navBorder = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    Column {
        HorizontalDivider(color = navBorder, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(navBackground)
                .padding(top = 8.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NAV_ITEMS.forEach { item ->
                NavItemButton(
                    item = item,
                    active = currentRoute == item.route,
                    onClick = { onNavigate(item.route) },
                )
            }
        }
    }
}