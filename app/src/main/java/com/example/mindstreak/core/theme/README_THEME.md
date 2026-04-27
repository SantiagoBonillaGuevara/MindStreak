# 🎨 MindStreak Design System

Este documento detalla la identidad visual de **MindStreak**, incluyendo la paleta de colores y el sistema tipográfico utilizado en la aplicación.

---

## 🌈 Colores

Nuestra paleta se divide en colores de marca (hábitos), ranking y colores semánticos para el soporte de modo claro y oscuro.

### 1. Colores de Hábitos (Brand)
Utilizados para identificar categorías de hábitos, gráficos de progreso y acentos visuales.

| Color | Hex | Variable | Uso Sugerido |
| :--- | :--- | :--- | :--- |
| ![#FFFF6B35](https://via.placeholder.com/15/FF6B35?text=+) | `#FFFF6B35` | `HabitOrange` | Energía, Fitness, Rachas (Streaks) |
| ![#FF7C6EFF](https://via.placeholder.com/15/7C6EFF?text=+) | `#FF7C6EFF` | `HabitPurple` | Productividad, Meditación |
| ![#FF4ECDC4](https://via.placeholder.com/15/4ECDC4?text=+) | `#FF4ECDC4` | `HabitTeal` | Salud, Hidratación |
| ![#FFFFD166](https://via.placeholder.com/15/FFD166?text=+) | `#FFFFD166` | `HabitYellow` | Aprendizaje, Sol, Logros |
| ![#FF6ECFF6](https://via.placeholder.com/15/6ECFF6?text=+) | `#FF6ECFF6` | `HabitBlue` | Sueño, Relax |
| ![#FFFF8FAB](https://via.placeholder.com/15/FF8FAB?text=+) | `#FFFF8FAB` | `HabitPink` | Social, Relaciones |
| ![#FFA8E063](https://via.placeholder.com/15/A8E063?text=+) | `#FFA8E063` | `HabitGreen` | Naturaleza, Crecimiento |

### 2. Colores de Ranking (Podium)
Específicos para la pantalla social y gamificación.

| Color | Hex | Variable | Rango |
| :--- | :--- | :--- | :--- |
| ![#FFFFD166](https://via.placeholder.com/15/FFD166?text=+) | `#FFFFD166` | `HabitYellow` | 🥇 Oro / 1er Lugar |
| ![#FFC0C0D0](https://via.placeholder.com/15/C0C0D0?text=+) | `#FFC0C0D0` | `RankSilver` | 🥈 Plata / 2do Lugar |
| ![#FFCD7F32](https://via.placeholder.com/15/CD7F32?text=+) | `#FFCD7F32` | `RankBronze` | 🥉 Bronce / 3er Lugar |

### 3. Sistema Temático (Material3)
Estos colores cambian automáticamente según el modo del sistema (`Theme.kt`).

| Elemento | Modo Claro | Modo Oscuro |
| :--- | :--- | :--- |
| **Fondo** | `#FFFFFF` | `#09090B` |
| **Superficie (Cards)** | `#FFFFFF` | `#09090B` |
| **Primario** | `#030213` (Indigo Dark) | `HabitPurple` |
| **Error** | `#D4183D` | `#7F1D1D` |
| **Bordes/Outline** | `#E9EBEF` | `#27272A` |

---

## 	🔡 Tipografía

MindStreak utiliza la familia de fuentes **Inter** para todas sus interfaces, optimizada para legibilidad en pantallas móviles.

### Familia de Fuente
*   **Fuente Principal:** Inter
*   **Pesos soportados:** Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700), ExtraBold (800).

### Escala Tipográfica (Material3)

| Estilo | Peso | Tamaño (sp) | Interlineado | Uso |
| :--- | :--- | :--- | :--- | :--- |
| `headlineLarge` | Medium | 28sp | 34sp | Títulos de gran impacto |
| `headlineMedium` | Medium | 24sp | 32sp | Títulos de sección |
| `headlineSmall` | Medium | 20sp | 28sp | Títulos de tarjetas |
| `titleMedium` | Medium | 16sp | 24sp | Subtítulos |
| `bodyLarge` | Normal | 16sp | 24sp | Texto de lectura principal |
| `bodyMedium` | Normal | 14sp | 20sp | Texto secundario o descriptivo |
| `labelMedium` | Medium | 12sp | 16sp | Etiquetas, botones pequeños |

---

## 🛠️ Cómo usar en el código

### Usar colores del tema (Recomendado)
```kotlin
Text(
    text = "Hola Mundo",
    color = MaterialTheme.colorScheme.onBackground
)
```

### Usar colores de marca específicos
```kotlin
Icon(
    imageVector = Icons.Default.Fire,
    tint = HabitOrange
)
```

### Usar tipografía
```kotlin
Text(
    text = "Estadísticas",
    style = MaterialTheme.typography.headlineMedium
)
```
