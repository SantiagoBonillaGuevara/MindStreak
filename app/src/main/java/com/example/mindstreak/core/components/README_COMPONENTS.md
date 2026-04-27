# Arquitectura de Componentes UI

## Propósito de `core/components`

`core/components` es la fuente de verdad única para los bloques de construcción de UI reutilizables y sin estado, compartidos entre dos o más pantallas de funcionalidades (o lo suficientemente genéricos para ser reutilizados). Los componentes aquí definidos no tienen ningún conocimiento de lógica de negocio, navegación ni ViewModels.

---

## Qué va aquí

| Criterio | Sí |
|---|---|
| Renderizado de UI puro (inputs, tarjetas, toggles, etiquetas) | ✅ |
| Solo acepta parámetros primitivos o de estado de UI | ✅ |
| Sin ViewModel, sin NavController, sin Repository | ✅ |
| Sin `LaunchedEffect` que dispare efectos secundarios | ✅ |
| Puede aparecer razonablemente en dos o más funcionalidades | ✅ |

## Qué NO va aquí

| Criterio | No |
|---|---|
| Layouts específicos de una funcionalidad (ej. `ProfileHeroCard`, `StreakHeroCard`) | ❌ |
| Composables que reciben objetos del modelo de dominio (ej. `User`, `Habit`) | ❌ |
| Lógica de navegación o constantes de rutas | ❌ |
| Colección de estado del ViewModel (`collectAsState`) | ❌ |
| Scaffolds a nivel de pantalla o pantallas de nivel superior | ❌ |

---

## Catálogo de componentes

### `AuthTextField.kt`
- **`AuthTextField`** — `OutlinedTextField` estilizado para formularios de autenticación; acepta ícono, placeholder y tipo de teclado.
- **`authTextFieldColors()`** — configuración compartida de `OutlinedTextFieldDefaults.colors` utilizada por los campos de autenticación.

### `CheckButton.kt`
- **`CheckButton`** — botón animado de marcar/desmarcar usado en las tarjetas de hábitos.

### `CustomToggle.kt`
- **`CustomToggle`** — interruptor toggle animado con física de resorte y color activo configurable.

### `EmptyState.kt`
- **`EmptyState`** — placeholder de ancho completo para estados vacíos, con emoji, título, descripción y botón CTA opcional.

### `HabitCard.kt`
- **`HabitCard`** — tarjeta que muestra un hábito con emoji, nombre, racha, tasa de completado y botón de verificación opcional.

### `ModifierExtensions.kt`
- **`Modifier.clickableWithoutRipple`** — extensión de Modifier `@Composable` que suprime el efecto ripple al hacer clic.

### `ProgressRing.kt`
- **`ProgressRing`** — indicador de progreso circular animado con slot opcional para contenido central.

### `QuickNavButton.kt`
- **`QuickNavButton`** — botón vertical de emoji + etiqueta usado en filas de accesos rápidos de navegación.

### `QuoteCard.kt`
- **`QuoteCard`** — tarjeta que muestra una frase motivacional con un emoji de burbuja de chat como prefijo.

### `SectionLabel.kt`
- **`SectionLabel`** — etiqueta de encabezado de sección en mayúsculas con espaciado entre letras; padding inferior opcional.

### `SettingsItems.kt`
- **`SettingsSection`** — contenedor agrupado de ajustes con encabezado y slot de contenido `ColumnScope`.
- **`SettingsToggleItem`** — fila de ajuste con ícono de color, título, subtítulo y `Switch` de Material.
- **`SettingsClickItem`** — fila de ajuste con ícono de color, etiqueta, badge opcional y chevron.
- **`NotificationTypeRow`** — fila de ajuste con `CustomToggle` controlado por un flag `masterEnabled`.

### `StatCard.kt`
- **`StatItem`** — celda de estadística compacta (valor + etiqueta) sobre fondo oscuro; usada en filas hero de detalle.
- **`StatSmallCard`** — tarjeta de estadística con emoji, valor grande y etiqueta en mayúsculas; usada en grids de estadísticas.
- **`StatMini`** — columna mínima de valor + etiqueta; usada en las estadísticas hero de racha.

### `navigation/NavBottom.kt` + `navigation/NavItemButton.kt`
- Barra de navegación inferior e ítem de navegación individual con estado activo animado.

---

## Convenciones de nomenclatura

| Patrón | Cuándo usarlo |
|---|---|
| `{Name}Card` | Tarjeta autónoma con fondo de superficie |
| `{Name}Item` | Fila o celda dentro de una lista |
| `{Name}Button` | Elemento interactivo con una acción principal |
| `{Name}Section` | Contenedor de layout que agrupa elementos relacionados |
| `{Name}Row` | Fila de layout horizontal (ajustes, entradas de lista) |
| `{Name}State` | Placeholder de pantalla completa (vacío, error, cargando) |
| `Modifier.{name}` | Funciones de extensión de Modifier |

---

## Ejemplo de uso

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

## Reglas para agregar nuevos componentes

1. **Contrato solo de UI** — los parámetros deben ser primitivos, lambdas o `Modifier`. Sin modelos de dominio.
2. **Un archivo por grupo lógico** — los composables pequeños relacionados (ej. tres variantes de stat) comparten un mismo archivo.
3. **Sin estado interno que impulse resultados de negocio** — un `var` local para animación o micro-estado de UI está permitido; recolectar un flow de ViewModel no lo está.
4. **Nómbralo por su aspecto, no por dónde se usa** — `EmptyState` y no `HomeEmptyHabits`.
5. **Documenta la intención en una línea** — usa un comentario KDoc encima del composable si el propósito no es evidente por el nombre.
6. **Las pantallas permanecen como capas de composición** — un composable de pantalla solo debe conectar estado → parámetros de componente, nunca contener lógica de layout que pertenece a un componente.
