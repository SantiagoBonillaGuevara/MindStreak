# UI Components Architecture

## Purpose of `core/components`

`core/components` is the single source of truth for reusable, stateless UI building blocks used across two or more feature screens (or generic enough to be reused). Components here have no knowledge of business logic, navigation, or ViewModels.

---

## What goes here

| Criteria | Yes |
|---|---|
| Pure UI rendering (inputs, cards, toggles, labels) | ✅ |
| Accepts only primitive / UI-state parameters | ✅ |
| No ViewModel, no NavController, no Repository | ✅ |
| No `LaunchedEffect` that triggers side effects | ✅ |
| Could reasonably appear in two or more features | ✅ |

## What does NOT go here

| Criteria | No |
|---|---|
| Feature-specific layouts (e.g. `ProfileHeroCard`, `StreakHeroCard`) | ❌ |
| Composables that take domain model objects (e.g. `User`, `Habit`) | ❌ |
| Navigation logic or route constants | ❌ |
| ViewModel state collection (`collectAsState`) | ❌ |
| Screen-level scaffolds or top-level screens | ❌ |

---

## Components catalogue

### `AuthTextField.kt`
- **`AuthTextField`** — `OutlinedTextField` styled for auth forms; accepts icon, placeholder, and keyboard type.
- **`authTextFieldColors()`** — shared `OutlinedTextFieldDefaults.colors` used by auth fields.

### `CheckButton.kt`
- **`CheckButton`** — animated check/uncheck button used on habit cards.

### `CustomToggle.kt`
- **`CustomToggle`** — animated toggle switch with spring physics and configurable active color.

### `EmptyState.kt`
- **`EmptyState`** — full-width empty-state placeholder with emoji, title, description, and optional CTA button.

### `HabitCard.kt`
- **`HabitCard`** — card displaying a habit with emoji, name, streak, completion rate, and optional check button.

### `ModifierExtensions.kt`
- **`Modifier.clickableWithoutRipple`** — `@Composable` Modifier extension that suppresses ripple on click.

### `ProgressRing.kt`
- **`ProgressRing`** — animated circular progress indicator with optional center content slot.

### `QuickNavButton.kt`
- **`QuickNavButton`** — emoji + label vertical button used in navigation shortcut rows.

### `QuoteCard.kt`
- **`QuoteCard`** — card that displays a motivational quote with a chat-bubble emoji prefix.

### `SectionLabel.kt`
- **`SectionLabel`** — uppercase, letter-spaced section heading label; optional bottom padding.

### `SettingsItems.kt`
- **`SettingsSection`** — grouped settings container with header and a `ColumnScope` content slot.
- **`SettingsToggleItem`** — settings row with colored icon, title, subtitle, and Material `Switch`.
- **`SettingsClickItem`** — settings row with colored icon, label, optional badge, and chevron.
- **`NotificationTypeRow`** — settings row with `CustomToggle` gated by a `masterEnabled` flag.

### `StatCard.kt`
- **`StatItem`** — compact stat cell (value + label) on dark background; used in detail hero rows.
- **`StatSmallCard`** — stat card with emoji, large value, and uppercase label; used in stat grids.
- **`StatMini`** — minimal value + label column; used in streak hero stats.

### `navigation/NavBottom.kt` + `navigation/NavItemButton.kt`
- Bottom navigation bar and individual nav item with animated active state.

---

## Naming conventions

| Pattern | When to use |
|---|---|
| `{Name}Card` | Self-contained card with surface background |
| `{Name}Item` | A row or cell inside a list |
| `{Name}Button` | Interactive element with a primary action |
| `{Name}Section` | Layout container grouping related items |
| `{Name}Row` | Horizontal layout row (settings, list entries) |
| `{Name}State` | Full-screen placeholder (empty, error, loading) |
| `Modifier.{name}` | Modifier extension functions |

---

## Example usage

```kotlin
// EmptyState
EmptyState(
    emoji = "🌱",
    title = "No habits yet",
    description = "Add your first habit to get started.",
    buttonText = "Add Habit",
    onButtonClick = { navController.navigate(Screen.CreateHabit.route) },
)

// CustomToggle
CustomToggle(
    checked = isEnabled,
    onCheckedChange = { isEnabled = it },
    activeColor = HabitPurple,
)

// SettingsSection + SettingsToggleItem
SettingsSection(title = "Preferences") {
    SettingsToggleItem(
        icon = Icons.Default.DarkMode,
        label = "Dark Mode",
        sub = "Always on",
        color = HabitPurple,
        value = darkMode,
        onValueChange = { darkMode = it },
    )
}

// StatCard variants
StatItem(modifier = Modifier.weight(1f), value = "21", label = "Current", color = HabitOrange)
StatSmallCard(emoji = "🔥", value = "34", label = "Best Streak", color = HabitOrange, modifier = Modifier.weight(1f))
StatMini(value = "87%", label = "This Month")

// Modifier extension
Box(
    modifier = Modifier
        .fillMaxWidth()
        .clickableWithoutRipple { selectedTab = tab },
)
```

---

## Rules for adding new components

1. **UI-only contract** — parameters must be primitives, lambdas, or `Modifier`. No domain models.
2. **Single file per logical group** — related small composables (e.g. three stat variants) share one file.
3. **No internal state that drives business outcomes** — local `var` for animation or UI micro-state is fine; collecting a ViewModel flow is not.
4. **Name it by what it looks like, not where it's used** — `EmptyState` not `HomeEmptyHabits`.
5. **Document the intent in one line** — use a KDoc comment above the composable if the purpose isn't obvious from the name.
6. **Screens stay as composition layers** — a screen composable should only wire state → component params, never contain raw layout logic that belongs in a component.
