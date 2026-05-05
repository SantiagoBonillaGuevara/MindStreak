# Modelo de Datos MindStreak con Supabase

> **Documento:** Entregable académico — Diseño de Base de Datos de Producción  
> **Proyecto:** MindStreak — Aplicación móvil de seguimiento de hábitos  
> **Motor de base de datos:** Supabase / PostgreSQL  
> **Fecha de análisis:** Mayo 2026  
> **Rama analizada:** `docs/readme`

---

## 1. Análisis del Estado Actual del Proyecto

### 1.1 Mecanismo de persistencia actual

El proyecto MindStreak en su estado actual no cuenta con una base de datos relacional ni con integración a ningún servicio remoto. La persistencia de datos se realiza exclusivamente de forma local mediante **Jetpack DataStore (Preferences)**, implementado en los siguientes archivos:

| Archivo | Responsabilidad |
|--------|----------------|
| `app/src/main/java/com/example/mindstreak/data/local/DataStoreProvider.kt` | Crea y expone el DataStore bajo el nombre `habits_v1.json` |
| `app/src/main/java/com/example/mindstreak/data/local/HabitSerializer.kt` | Define `HabitsStore` y `SerializableHabit`; implementa serialización/deserialización JSON con `kotlinx-serialization` |
| `app/src/main/java/com/example/mindstreak/data/repository/HabitRepository.kt` | Expone `habitsFlow: Flow<List<Habit>>` y `saveHabits()`; es el único repositorio implementado |

La lógica de negocio central — incluyendo el cálculo de streaks, la derivación del historial semanal y la reconciliación de hábitos al reiniciar la app — reside en `AppViewModel.kt`, que actúa como fuente de verdad única durante la sesión.

### 1.2 Datos de prueba y modelos inspeccionados

Todos los datos de entidades como usuarios, amigos, logros, categorías y estadísticas se sirven desde `MockData.kt`, un objeto singleton con listas estáticas. Los ViewModels de las pantallas `SocialScreen`, `StatisticsScreen`, `StreakScreen`, `ProfileScreen`, `AchievementsScreen` y `NotificationsScreen` están declarados pero vacíos, a la espera de integración con una fuente de datos real.

### 1.3 Archivos y clases inspeccionados

```
data/model/Habit.kt          → Entidad central con completionLog: Map<String, Boolean>
data/model/User.kt           → Perfil de usuario con XP, nivel y streaks
data/model/Achievement.kt    → Logros con enum Rarity (COMMON/RARE/EPIC/LEGENDARY)
data/model/Category.kt       → Categorías con emoji y color
data/model/Friend.kt         → Representación social con streak, nivel y puntos
data/model/MonthDay.kt       → Día del mes con estado completed/partial
data/model/WeekDay.kt        → Día de la semana con completados vs. totales
data/mock/MockData.kt        → 6 hábitos, 1 usuario, 6 amigos, 10 logros, 8 categorías
feature/social/SocialScreen.kt → Tablas de clasificación, actividad, grupos y desafíos
feature/notifications/NotificationsScreen.kt → Preferencias por tipo y por hábito
feature/streak/StreakScreen.kt → Hitos, calendario y streaks por hábito
```

---

## 2. Motor de Base de Datos Seleccionado

### Supabase / PostgreSQL

**Supabase** es una plataforma de backend de código abierto que ofrece una instancia PostgreSQL gestionada con servicios adicionales integrados: autenticación, almacenamiento de archivos, funciones edge, tiempo real vía WebSockets y Row Level Security (RLS) nativo.

### Justificación técnica para MindStreak

| Criterio | Supabase / PostgreSQL | Alternativa (Firebase) |
|---|---|---|
| **Modelo de datos** | Relacional con tipado estricto, ideal para hábitos, logs diarios y estadísticas agregadas | Documental, dificulta consultas relacionales complejas |
| **Autenticación** | `auth.users` integrado; soporta OAuth (Google, Apple), email/password y magic links | Comparable, pero acoplado al ecosistema Firebase |
| **Tiempo real** | Realtime subscriptions sobre tablas PostgreSQL para feeds sociales y actualizaciones de streaks | Comparable con Firestore |
| **Consultas analíticas** | SQL nativo para estadísticas de completion rate, tendencias semanales/mensuales/anuales | Requiere Cloud Functions para agregaciones complejas |
| **Row Level Security** | Nativo en PostgreSQL, declarativo, sin lógica extra en el servidor | Reglas de seguridad separadas del esquema |
| **SDK Android** | Cliente Kotlin oficial con soporte para coroutines y Flow | SDK Kotlin disponible |
| **Offline-first** | Compatible con Room como caché local usando el mismo esquema | Compatible con Firestore offline |
| **Costo** | Plan gratuito generoso; open-source autoalojable | Precios escalados por operaciones |
| **Escalabilidad** | PostgreSQL probado en producción a escala; soporta particionamiento y sharding | Escalable pero con costos elevados |

PostgreSQL es especialmente adecuado para MindStreak porque los datos de hábitos son inherentemente relacionales (usuarios → hábitos → logs diarios), las estadísticas requieren agregaciones (`COUNT`, `SUM`, `AVG` por fechas), y la funcionalidad social (amigos, grupos, leaderboard) necesita joins eficientes con índices apropiados.

---

## 3. Entidades Identificadas

| Entidad | Descripción | Origen en el código | Justificación |
|---|---|---|---|
| **profiles** | Perfil público del usuario, extensión de `auth.users` | `User.kt`, `MockData.USER`, `ProfileScreen.kt` | Centraliza datos del usuario separados de las credenciales de autenticación |
| **categories** | Catálogo de categorías de hábitos con emoji y color | `Category.kt`, `MockData.CATEGORIES`, `CreateHabitScreen.kt` | 8 categorías hardcodeadas en mock; deben ser persistentes y extensibles |
| **habits** | Hábito definido por el usuario con frecuencia y recordatorio | `Habit.kt`, `HabitRepository.kt`, `AppViewModel.kt` | Entidad central del sistema; actualmente persiste en DataStore local |
| **habit_logs** | Registro diario de completación por hábito | `Habit.completionLog: Map<String, Boolean>`, `AppViewModel.deriveWeekHistory()` | Actualmente serializado como mapa dentro del hábito; debe normalizarse |
| **streaks** | Estado del streak actual y mejor marca por hábito | `Habit.streak`, `AppViewModel.toggleHabit()`, `reconcileHabitsOnLaunch()` | La lógica de streak existe en ViewModel; debe persistirse en BD |
| **achievements** | Catálogo maestro de logros con rareza y criterios | `Achievement.kt`, `MockData.ACHIEVEMENTS`, `AchievementsScreen.kt` | 10 logros hardcodeados; necesitan tabla semilla en BD |
| **user_achievements** | Asociación usuario-logro con progreso y fecha de obtención | `Achievement.earned`, `Achievement.earnedDate`, `Achievement.progress` | Tabla de unión muchos-a-muchos con estado de progreso individual |
| **friendships** | Relación de amistad bidireccional con estado | `Friend.kt`, `MockData.FRIENDS`, `SocialScreen.kt (Leaderboard)` | Requiere manejo de estados: pendiente, aceptada, bloqueada |
| **groups** | Grupos sociales con streak colectivo | `SocialScreen.kt (Groups tab)`, mock groups | Mostrado en UI como grupos con nombre, miembros y streak |
| **group_members** | Membresía de usuario en un grupo con rol | `SocialScreen.kt`, mock members | Tabla de unión con rol (admin/miembro) |
| **group_challenges** | Desafíos semanales dentro de un grupo | `SocialScreen.kt (WeeklyChallengeBanner)` | Visible en UI; necesita fechas de inicio/fin y descripción |
| **activity_feed** | Eventos sociales publicados al completar acciones | `SocialScreen.kt (Activity tab)`: "completó hábito", "alcanzó racha", "desbloqueó logro" | Feed de actividad generado automáticamente por triggers o RPC |
| **notification_preferences** | Preferencias globales de notificaciones por tipo | `NotificationsScreen.kt`: master toggle, streak, daily, achievements, social, motivational | Mapeado directamente desde la pantalla de notificaciones |
| **habit_notification_settings** | Recordatorio configurado individualmente por hábito | `NotificationsScreen.kt (habit reminders list)`, `Habit.reminderTime` | Permite sobrescribir la hora de recordatorio por hábito |
| **motivational_quotes** | Citas motivacionales mostradas en el home | `MockData.MOTIVATIONAL_QUOTES` (6 citas), `HomeScreen.kt (QuoteCard)` | Actualmente estáticas; deben servirse desde BD para actualizarse sin deploy |

---

## 4. DER Textual

### 4.1 profiles

**Propósito:** Almacena el perfil público del usuario, extendiendo la tabla `auth.users` de Supabase que gestiona las credenciales de autenticación.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK**, FK → `auth.users(id)` | Vincula perfil con cuenta de autenticación |
| `name` | TEXT | NOT NULL | Nombre completo del usuario |
| `username` | TEXT | NOT NULL, UNIQUE | Identificador público único |
| `university` | TEXT | NULLABLE | Institución académica del usuario |
| `avatar_emoji` | TEXT | DEFAULT '🧑' | Emoji seleccionado como avatar |
| `level` | INTEGER | DEFAULT 1 | Nivel de progreso gamificado |
| `xp` | INTEGER | DEFAULT 0 | Puntos de experiencia acumulados |
| `next_level_xp` | INTEGER | DEFAULT 100 | XP requerido para el siguiente nivel |
| `total_streak` | INTEGER | DEFAULT 0 | Suma de todos los streaks activos |
| `best_streak` | INTEGER | DEFAULT 0 | Mayor racha histórica del usuario |
| `total_habits_completed` | INTEGER | DEFAULT 0 | Contador acumulativo de completaciones |
| `join_date` | DATE | DEFAULT CURRENT_DATE | Fecha de registro en la app |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |
| `updated_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de última actualización |

**Relaciones:**
- **1:N** con `habits` (un usuario tiene muchos hábitos)
- **1:N** con `user_achievements` (un usuario tiene muchos logros)
- **N:M** con `profiles` a través de `friendships` (un usuario tiene muchos amigos)
- **N:M** con `groups` a través de `group_members`
- **1:1** con `notification_preferences`

---

### 4.2 categories

**Propósito:** Catálogo de categorías de hábitos. Incluye categorías predeterminadas del sistema y permite categorías personalizadas por usuario.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `name` | TEXT | NOT NULL | Nombre de la categoría |
| `emoji` | TEXT | NOT NULL | Ícono representativo |
| `color` | TEXT | NOT NULL | Color hexadecimal de la categoría |
| `is_default` | BOOLEAN | DEFAULT FALSE | TRUE para las 8 categorías del sistema |
| `created_by` | UUID | FK → `profiles(id)`, NULLABLE | NULL para categorías del sistema |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |

**Relaciones:**
- **1:N** con `habits` (una categoría agrupa muchos hábitos)

---

### 4.3 habits

**Propósito:** Hábito definido y configurado por el usuario. Contiene metadatos del hábito y el estado actual del streak como campos desnormalizados para lectura eficiente.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `user_id` | UUID | NOT NULL, FK → `profiles(id)` | Propietario del hábito |
| `name` | TEXT | NOT NULL | Nombre descriptivo del hábito |
| `emoji` | TEXT | DEFAULT '✨' | Ícono visual del hábito |
| `category_id` | UUID | FK → `categories(id)`, NULLABLE | Categoría asignada |
| `color` | TEXT | NULLABLE | Color personalizado (sobreescribe el de la categoría) |
| `frequency` | habit_frequency | NOT NULL, DEFAULT 'DAILY' | Enum: DAILY, WEEKDAYS, WEEKENDS, CUSTOM |
| `reminder_time` | TIME | NULLABLE | Hora del recordatorio diario |
| `is_active` | BOOLEAN | DEFAULT TRUE | FALSE para hábitos archivados |
| `current_streak` | INTEGER | DEFAULT 0 | Racha actual (desnormalizada para rendimiento) |
| `best_streak` | INTEGER | DEFAULT 0 | Mejor racha histórica del hábito |
| `completion_rate` | REAL | DEFAULT 0.0 | Tasa de completación (0.0 a 1.0) |
| `last_completed_date` | DATE | NULLABLE | Última fecha de completación |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |
| `updated_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de última modificación |

**Relaciones:**
- **N:1** con `profiles` (muchos hábitos pertenecen a un usuario)
- **N:1** con `categories`
- **1:N** con `habit_logs`
- **1:1** con `habit_notification_settings`

---

### 4.4 habit_logs

**Propósito:** Registro normalizado de completación diaria por hábito. Deriva del campo `completionLog: Map<String, Boolean>` del modelo actual, convirtiendo cada entrada del mapa en una fila independiente.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `habit_id` | UUID | NOT NULL, FK → `habits(id)` | Hábito al que pertenece el registro |
| `user_id` | UUID | NOT NULL, FK → `profiles(id)` | Usuario propietario (para RLS eficiente) |
| `completed_date` | DATE | NOT NULL | Fecha del registro |
| `completed` | BOOLEAN | NOT NULL, DEFAULT FALSE | Estado de completación en esa fecha |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de inserción |

**Restricciones adicionales:**
- `UNIQUE(habit_id, completed_date)` — un registro por hábito por día

**Relaciones:**
- **N:1** con `habits`
- **N:1** con `profiles` (referencia directa para RLS sin JOIN)

---

### 4.5 achievements

**Propósito:** Catálogo maestro de todos los logros del sistema. Esta tabla es de solo escritura por administradores y de solo lectura para usuarios.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `name` | TEXT | NOT NULL | Nombre del logro |
| `emoji` | TEXT | NOT NULL | Ícono visual |
| `description` | TEXT | NOT NULL | Descripción del criterio para obtenerlo |
| `rarity` | achievement_rarity | NOT NULL, DEFAULT 'COMMON' | Enum: COMMON, RARE, EPIC, LEGENDARY |
| `total` | INTEGER | NULLABLE | NULL = logro binario; valor = umbral para progreso |
| `sort_order` | INTEGER | DEFAULT 0 | Orden de visualización en la pantalla |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |

**Relaciones:**
- **1:N** con `user_achievements`

---

### 4.6 user_achievements

**Propósito:** Tabla de unión entre usuarios y logros. Rastrea el estado individual de cada logro para cada usuario, incluyendo progreso parcial y fecha de obtención.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `user_id` | UUID | NOT NULL, FK → `profiles(id)` | Usuario al que pertenece |
| `achievement_id` | UUID | NOT NULL, FK → `achievements(id)` | Logro referenciado |
| `earned` | BOOLEAN | NOT NULL, DEFAULT FALSE | TRUE cuando el logro está desbloqueado |
| `progress` | INTEGER | NOT NULL, DEFAULT 0 | Progreso actual hacia el total |
| `earned_at` | TIMESTAMPTZ | NULLABLE | Timestamp de desbloqueo |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación del registro |
| `updated_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de última actualización de progreso |

**Restricciones adicionales:**
- `UNIQUE(user_id, achievement_id)` — un registro por combinación usuario-logro

**Cardinalidad:** N:M — un usuario puede tener muchos logros; un logro puede ser obtenido por muchos usuarios.

---

### 4.7 friendships

**Propósito:** Gestiona las relaciones de amistad bidireccionales con un flujo de solicitud/aceptación/bloqueo.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `requester_id` | UUID | NOT NULL, FK → `profiles(id)` | Usuario que envió la solicitud |
| `addressee_id` | UUID | NOT NULL, FK → `profiles(id)` | Usuario que recibió la solicitud |
| `status` | friendship_status | NOT NULL, DEFAULT 'PENDING' | Enum: PENDING, ACCEPTED, BLOCKED |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de la solicitud |
| `updated_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp del último cambio de estado |

**Restricciones adicionales:**
- `UNIQUE(requester_id, addressee_id)` — una sola solicitud por par
- `CHECK(requester_id <> addressee_id)` — un usuario no puede ser amigo de sí mismo

**Cardinalidad:** N:M auto-referencial sobre `profiles`.

---

### 4.8 groups

**Propósito:** Grupos sociales donde los usuarios comparten hábitos y compiten colectivamente. Mostrado en la pestaña "Groups" de `SocialScreen`.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `name` | TEXT | NOT NULL | Nombre del grupo |
| `description` | TEXT | NULLABLE | Descripción del grupo |
| `emoji` | TEXT | DEFAULT '🏆' | Ícono representativo |
| `created_by` | UUID | NOT NULL, FK → `profiles(id)` | Creador y administrador inicial |
| `current_streak` | INTEGER | DEFAULT 0 | Racha colectiva actual |
| `best_streak` | INTEGER | DEFAULT 0 | Mejor racha colectiva histórica |
| `is_public` | BOOLEAN | DEFAULT TRUE | Visibilidad del grupo para búsquedas |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |
| `updated_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de última actualización |

---

### 4.9 group_members

**Propósito:** Tabla de unión entre usuarios y grupos con control de roles.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `group_id` | UUID | NOT NULL, FK → `groups(id)` | Grupo al que pertenece |
| `user_id` | UUID | NOT NULL, FK → `profiles(id)` | Miembro del grupo |
| `role` | group_member_role | NOT NULL, DEFAULT 'MEMBER' | Enum: ADMIN, MEMBER |
| `joined_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de unión al grupo |

**Restricciones adicionales:**
- `UNIQUE(group_id, user_id)` — un usuario no puede ser miembro duplicado

**Cardinalidad:** N:M entre `profiles` y `groups`.

---

### 4.10 group_challenges

**Propósito:** Desafíos semanales dentro de un grupo. Visible en el banner `WeeklyChallengeBanner` de `SocialScreen`.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `group_id` | UUID | NOT NULL, FK → `groups(id)` | Grupo al que pertenece el desafío |
| `title` | TEXT | NOT NULL | Título del desafío |
| `description` | TEXT | NULLABLE | Descripción detallada |
| `start_date` | DATE | NOT NULL | Fecha de inicio |
| `end_date` | DATE | NOT NULL | Fecha de fin |
| `created_by` | UUID | NOT NULL, FK → `profiles(id)` | Creador del desafío |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |

---

### 4.11 notification_preferences

**Propósito:** Preferencias globales de notificaciones por usuario, mapeo directo de los toggles de `NotificationsScreen`.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `user_id` | UUID | NOT NULL, UNIQUE, FK → `profiles(id)` | Propietario de las preferencias |
| `master_enabled` | BOOLEAN | DEFAULT TRUE | Toggle maestro (apaga todas si FALSE) |
| `streak_alert` | BOOLEAN | DEFAULT TRUE | Alertas de racha en riesgo |
| `daily_summary` | BOOLEAN | DEFAULT TRUE | Resumen diario de completaciones |
| `achievements_enabled` | BOOLEAN | DEFAULT TRUE | Notificaciones de logros |
| `social_enabled` | BOOLEAN | DEFAULT TRUE | Actividad social de amigos |
| `motivational_enabled` | BOOLEAN | DEFAULT TRUE | Frases motivacionales |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |
| `updated_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de última modificación |

**Cardinalidad:** 1:1 con `profiles`.

---

### 4.12 habit_notification_settings

**Propósito:** Configuración de recordatorio individual por hábito. Permite sobrescribir la hora de recordatorio global.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `habit_id` | UUID | NOT NULL, FK → `habits(id)` | Hábito configurado |
| `user_id` | UUID | NOT NULL, FK → `profiles(id)` | Propietario (para RLS) |
| `reminder_enabled` | BOOLEAN | DEFAULT TRUE | Estado del recordatorio para este hábito |
| `reminder_time` | TIME | NULLABLE | Hora específica para este hábito |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |
| `updated_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de última modificación |

**Restricciones adicionales:**
- `UNIQUE(habit_id, user_id)` — una configuración por hábito por usuario

---

### 4.13 activity_feed

**Propósito:** Registro de eventos sociales generados automáticamente cuando un usuario completa un hábito, alcanza un hito de racha o desbloquea un logro. Alimenta la pestaña "Activity" de `SocialScreen`.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `user_id` | UUID | NOT NULL, FK → `profiles(id)` | Usuario que realizó la acción |
| `activity_type` | activity_type | NOT NULL | Enum: HABIT_COMPLETED, STREAK_HIT, ACHIEVEMENT_UNLOCKED, JOINED_GROUP, JOINED_CHALLENGE |
| `habit_id` | UUID | FK → `habits(id)`, NULLABLE | Referencia al hábito (si aplica) |
| `achievement_id` | UUID | FK → `achievements(id)`, NULLABLE | Referencia al logro (si aplica) |
| `group_id` | UUID | FK → `groups(id)`, NULLABLE | Referencia al grupo (si aplica) |
| `metadata` | JSONB | NULLABLE | Datos adicionales flexibles (ej. valor del streak alcanzado) |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp del evento |

---

### 4.14 motivational_quotes

**Propósito:** Almacena las citas motivacionales mostradas en `HomeScreen (QuoteCard)`. Actualmente 6 citas hardcodeadas en `MockData.MOTIVATIONAL_QUOTES`.

| Atributo | Tipo | Restricción | Descripción |
|---|---|---|---|
| `id` | UUID | **PK** | Identificador único |
| `text` | TEXT | NOT NULL | Texto de la cita |
| `author` | TEXT | NULLABLE | Autor de la cita |
| `is_active` | BOOLEAN | DEFAULT TRUE | Permite desactivar citas sin eliminarlas |
| `created_at` | TIMESTAMPTZ | DEFAULT NOW() | Timestamp de creación |

---

## 5. Tablas y Relaciones en Supabase/PostgreSQL

| Tabla | Campos principales | PK | FK | Relaciones |
|---|---|---|---|---|
| `profiles` | id, name, username, university, avatar_emoji, level, xp, best_streak | `id` (UUID) | `auth.users(id)` | 1:N habits, user_achievements, activity_feed; N:M profiles (via friendships), groups (via group_members); 1:1 notification_preferences |
| `categories` | id, name, emoji, color, is_default, created_by | `id` (UUID) | `profiles(id)` | 1:N habits |
| `habits` | id, user_id, name, emoji, category_id, frequency, reminder_time, current_streak, is_active | `id` (UUID) | `profiles(id)`, `categories(id)` | N:1 profiles; N:1 categories; 1:N habit_logs; 1:1 habit_notification_settings |
| `habit_logs` | id, habit_id, user_id, completed_date, completed | `id` (UUID) | `habits(id)`, `profiles(id)` | N:1 habits; N:1 profiles |
| `achievements` | id, name, emoji, description, rarity, total | `id` (UUID) | — | 1:N user_achievements |
| `user_achievements` | id, user_id, achievement_id, earned, progress, earned_at | `id` (UUID) | `profiles(id)`, `achievements(id)` | N:1 profiles; N:1 achievements |
| `friendships` | id, requester_id, addressee_id, status | `id` (UUID) | `profiles(id)` × 2 | N:M auto-referencial sobre profiles |
| `groups` | id, name, emoji, created_by, current_streak, is_public | `id` (UUID) | `profiles(id)` | 1:N group_members, group_challenges |
| `group_members` | id, group_id, user_id, role, joined_at | `id` (UUID) | `groups(id)`, `profiles(id)` | N:1 groups; N:1 profiles |
| `group_challenges` | id, group_id, title, start_date, end_date, created_by | `id` (UUID) | `groups(id)`, `profiles(id)` | N:1 groups |
| `notification_preferences` | id, user_id, master_enabled, streak_alert, daily_summary, achievements_enabled | `id` (UUID) | `profiles(id)` | 1:1 profiles |
| `habit_notification_settings` | id, habit_id, user_id, reminder_enabled, reminder_time | `id` (UUID) | `habits(id)`, `profiles(id)` | 1:1 habits (por usuario) |
| `activity_feed` | id, user_id, activity_type, habit_id, achievement_id, group_id, metadata | `id` (UUID) | `profiles(id)`, `habits(id)`, `achievements(id)`, `groups(id)` | N:1 profiles; referencias opcionales |
| `motivational_quotes` | id, text, author, is_active | `id` (UUID) | — | Independiente |

---

## 6. SQL para Supabase/PostgreSQL

```sql
-- ============================================================
-- MindStreak — Esquema de base de datos para Supabase
-- Motor: PostgreSQL (Supabase)
-- ============================================================

-- Habilitar extensión para generación de UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================
-- TIPOS ENUMERADOS
-- ============================================================

CREATE TYPE habit_frequency AS ENUM (
    'DAILY',
    'WEEKDAYS',
    'WEEKENDS',
    'CUSTOM'
);

CREATE TYPE achievement_rarity AS ENUM (
    'COMMON',
    'RARE',
    'EPIC',
    'LEGENDARY'
);

CREATE TYPE friendship_status AS ENUM (
    'PENDING',
    'ACCEPTED',
    'BLOCKED'
);

CREATE TYPE group_member_role AS ENUM (
    'ADMIN',
    'MEMBER'
);

CREATE TYPE activity_type AS ENUM (
    'HABIT_COMPLETED',
    'STREAK_HIT',
    'ACHIEVEMENT_UNLOCKED',
    'JOINED_GROUP',
    'JOINED_CHALLENGE'
);

-- ============================================================
-- TABLA: profiles
-- Extiende auth.users con datos del perfil público del usuario
-- ============================================================

CREATE TABLE profiles (
    id              UUID        PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    name            TEXT        NOT NULL,
    username        TEXT        NOT NULL UNIQUE,
    university      TEXT,
    avatar_emoji    TEXT        NOT NULL DEFAULT '🧑',
    level           INTEGER     NOT NULL DEFAULT 1 CHECK (level >= 1),
    xp              INTEGER     NOT NULL DEFAULT 0 CHECK (xp >= 0),
    next_level_xp   INTEGER     NOT NULL DEFAULT 100 CHECK (next_level_xp > 0),
    total_streak    INTEGER     NOT NULL DEFAULT 0 CHECK (total_streak >= 0),
    best_streak     INTEGER     NOT NULL DEFAULT 0 CHECK (best_streak >= 0),
    total_habits_completed INTEGER NOT NULL DEFAULT 0 CHECK (total_habits_completed >= 0),
    join_date       DATE        NOT NULL DEFAULT CURRENT_DATE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_profiles_username ON profiles(username);

-- ============================================================
-- TABLA: categories
-- Catálogo de categorías de hábitos (sistema + personalizadas)
-- ============================================================

CREATE TABLE categories (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        TEXT        NOT NULL,
    emoji       TEXT        NOT NULL,
    color       TEXT        NOT NULL,
    is_default  BOOLEAN     NOT NULL DEFAULT FALSE,
    created_by  UUID        REFERENCES profiles(id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_categories_created_by ON categories(created_by);
CREATE INDEX idx_categories_is_default ON categories(is_default);

-- Seed: 8 categorías predeterminadas del sistema (de MockData.CATEGORIES)
INSERT INTO categories (id, name, emoji, color, is_default) VALUES
    (uuid_generate_v4(), 'fitness',      '🏃', '#FF6B35', TRUE),
    (uuid_generate_v4(), 'mindfulness',  '🧘', '#7C6EFF', TRUE),
    (uuid_generate_v4(), 'health',       '💊', '#4ECDC4', TRUE),
    (uuid_generate_v4(), 'learning',     '📚', '#FFD166', TRUE),
    (uuid_generate_v4(), 'sleep',        '😴', '#6ECFF6', TRUE),
    (uuid_generate_v4(), 'academic',     '🎓', '#FF8FAB', TRUE),
    (uuid_generate_v4(), 'social',       '👥', '#A8E063', TRUE),
    (uuid_generate_v4(), 'nutrition',    '🥗', '#56AB2F', TRUE);

-- ============================================================
-- TABLA: habits
-- Hábito definido por el usuario
-- ============================================================

CREATE TABLE habits (
    id                  UUID            PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id             UUID            NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    name                TEXT            NOT NULL,
    emoji               TEXT            NOT NULL DEFAULT '✨',
    category_id         UUID            REFERENCES categories(id) ON DELETE SET NULL,
    color               TEXT,
    frequency           habit_frequency NOT NULL DEFAULT 'DAILY',
    reminder_time       TIME,
    is_active           BOOLEAN         NOT NULL DEFAULT TRUE,
    current_streak      INTEGER         NOT NULL DEFAULT 0 CHECK (current_streak >= 0),
    best_streak         INTEGER         NOT NULL DEFAULT 0 CHECK (best_streak >= 0),
    completion_rate     REAL            NOT NULL DEFAULT 0.0 CHECK (completion_rate >= 0.0 AND completion_rate <= 1.0),
    last_completed_date DATE,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_habits_user_id      ON habits(user_id);
CREATE INDEX idx_habits_category_id  ON habits(category_id);
CREATE INDEX idx_habits_is_active    ON habits(user_id, is_active);

-- ============================================================
-- TABLA: habit_logs
-- Registro diario de completación por hábito (1 fila = 1 día)
-- ============================================================

CREATE TABLE habit_logs (
    id              UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    habit_id        UUID        NOT NULL REFERENCES habits(id) ON DELETE CASCADE,
    user_id         UUID        NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    completed_date  DATE        NOT NULL,
    completed       BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_habit_log_per_day UNIQUE (habit_id, completed_date)
);

CREATE INDEX idx_habit_logs_habit_id      ON habit_logs(habit_id);
CREATE INDEX idx_habit_logs_user_id       ON habit_logs(user_id);
CREATE INDEX idx_habit_logs_completed_date ON habit_logs(completed_date);
CREATE INDEX idx_habit_logs_user_date     ON habit_logs(user_id, completed_date);

-- ============================================================
-- TABLA: achievements
-- Catálogo maestro de logros del sistema
-- ============================================================

CREATE TABLE achievements (
    id          UUID                PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        TEXT                NOT NULL,
    emoji       TEXT                NOT NULL,
    description TEXT                NOT NULL,
    rarity      achievement_rarity  NOT NULL DEFAULT 'COMMON',
    total       INTEGER             CHECK (total IS NULL OR total > 0),
    sort_order  INTEGER             NOT NULL DEFAULT 0,
    created_at  TIMESTAMPTZ         NOT NULL DEFAULT NOW()
);

-- Seed: 10 logros de MockData.ACHIEVEMENTS
INSERT INTO achievements (name, emoji, description, rarity, total, sort_order) VALUES
    ('First Step',         '🌟', 'Completa tu primer hábito',                   'COMMON',    NULL, 1),
    ('Week Warrior',       '⚔️',  'Mantén una racha de 7 días',                   'RARE',      7,    2),
    ('Habit Master',       '🎯', 'Completa 100 hábitos en total',                'EPIC',      100,  3),
    ('Month Champion',     '🏆', 'Mantén una racha de 30 días',                  'LEGENDARY', 30,   4),
    ('Early Bird',         '🌅', 'Completa un hábito antes de las 8am',          'COMMON',    NULL, 5),
    ('Social Butterfly',   '🦋', 'Únete a 3 grupos diferentes',                  'RARE',      3,    6),
    ('Streak Shield',      '🛡️',  'Recupera una racha perdida usando un escudo',  'EPIC',      NULL, 7),
    ('Perfect Week',       '✨', 'Completa todos tus hábitos durante 7 días',    'LEGENDARY', NULL, 8),
    ('Consistency King',   '👑', 'Mantén una tasa de completación del 90%',      'EPIC',      NULL, 9),
    ('Night Owl',          '🦉', 'Completa un hábito después de las 10pm',       'COMMON',    NULL, 10);

-- ============================================================
-- TABLA: user_achievements
-- Estado individual de logros por usuario
-- ============================================================

CREATE TABLE user_achievements (
    id              UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID        NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    achievement_id  UUID        NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    earned          BOOLEAN     NOT NULL DEFAULT FALSE,
    progress        INTEGER     NOT NULL DEFAULT 0 CHECK (progress >= 0),
    earned_at       TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_achievement UNIQUE (user_id, achievement_id)
);

CREATE INDEX idx_user_achievements_user_id ON user_achievements(user_id);
CREATE INDEX idx_user_achievements_earned  ON user_achievements(user_id, earned);

-- ============================================================
-- TABLA: friendships
-- Relaciones de amistad bidireccionales con estado
-- ============================================================

CREATE TABLE friendships (
    id              UUID                PRIMARY KEY DEFAULT uuid_generate_v4(),
    requester_id    UUID                NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    addressee_id    UUID                NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    status          friendship_status   NOT NULL DEFAULT 'PENDING',
    created_at      TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_friendship         UNIQUE (requester_id, addressee_id),
    CONSTRAINT chk_no_self_friendship CHECK (requester_id <> addressee_id)
);

CREATE INDEX idx_friendships_requester ON friendships(requester_id, status);
CREATE INDEX idx_friendships_addressee ON friendships(addressee_id, status);

-- ============================================================
-- TABLA: groups
-- Grupos sociales con racha colectiva
-- ============================================================

CREATE TABLE groups (
    id              UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    name            TEXT        NOT NULL,
    description     TEXT,
    emoji           TEXT        NOT NULL DEFAULT '🏆',
    created_by      UUID        NOT NULL REFERENCES profiles(id) ON DELETE RESTRICT,
    current_streak  INTEGER     NOT NULL DEFAULT 0 CHECK (current_streak >= 0),
    best_streak     INTEGER     NOT NULL DEFAULT 0 CHECK (best_streak >= 0),
    is_public       BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_groups_created_by ON groups(created_by);
CREATE INDEX idx_groups_is_public  ON groups(is_public);

-- ============================================================
-- TABLA: group_members
-- Membresía de usuarios en grupos con control de roles
-- ============================================================

CREATE TABLE group_members (
    id          UUID                PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_id    UUID                NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
    user_id     UUID                NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    role        group_member_role   NOT NULL DEFAULT 'MEMBER',
    joined_at   TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_group_member UNIQUE (group_id, user_id)
);

CREATE INDEX idx_group_members_group ON group_members(group_id);
CREATE INDEX idx_group_members_user  ON group_members(user_id);

-- ============================================================
-- TABLA: group_challenges
-- Desafíos semanales dentro de un grupo
-- ============================================================

CREATE TABLE group_challenges (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_id    UUID        NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
    title       TEXT        NOT NULL,
    description TEXT,
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    created_by  UUID        NOT NULL REFERENCES profiles(id) ON DELETE RESTRICT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT chk_challenge_dates CHECK (end_date > start_date)
);

CREATE INDEX idx_group_challenges_group ON group_challenges(group_id);
CREATE INDEX idx_group_challenges_dates ON group_challenges(start_date, end_date);

-- ============================================================
-- TABLA: notification_preferences
-- Preferencias globales de notificaciones por usuario
-- ============================================================

CREATE TABLE notification_preferences (
    id                      UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id                 UUID        NOT NULL UNIQUE REFERENCES profiles(id) ON DELETE CASCADE,
    master_enabled          BOOLEAN     NOT NULL DEFAULT TRUE,
    streak_alert            BOOLEAN     NOT NULL DEFAULT TRUE,
    daily_summary           BOOLEAN     NOT NULL DEFAULT TRUE,
    achievements_enabled    BOOLEAN     NOT NULL DEFAULT TRUE,
    social_enabled          BOOLEAN     NOT NULL DEFAULT TRUE,
    motivational_enabled    BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ============================================================
-- TABLA: habit_notification_settings
-- Recordatorio individual por hábito
-- ============================================================

CREATE TABLE habit_notification_settings (
    id                  UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    habit_id            UUID        NOT NULL REFERENCES habits(id) ON DELETE CASCADE,
    user_id             UUID        NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    reminder_enabled    BOOLEAN     NOT NULL DEFAULT TRUE,
    reminder_time       TIME,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_habit_notification UNIQUE (habit_id, user_id)
);

CREATE INDEX idx_habit_notif_habit   ON habit_notification_settings(habit_id);
CREATE INDEX idx_habit_notif_user    ON habit_notification_settings(user_id);

-- ============================================================
-- TABLA: activity_feed
-- Eventos sociales para la pestaña de actividad
-- ============================================================

CREATE TABLE activity_feed (
    id              UUID            PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID            NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
    activity_type   activity_type   NOT NULL,
    habit_id        UUID            REFERENCES habits(id) ON DELETE SET NULL,
    achievement_id  UUID            REFERENCES achievements(id) ON DELETE SET NULL,
    group_id        UUID            REFERENCES groups(id) ON DELETE SET NULL,
    metadata        JSONB,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activity_feed_user    ON activity_feed(user_id);
CREATE INDEX idx_activity_feed_created ON activity_feed(created_at DESC);

-- ============================================================
-- TABLA: motivational_quotes
-- Citas motivacionales para HomeScreen (QuoteCard)
-- ============================================================

CREATE TABLE motivational_quotes (
    id          UUID        PRIMARY KEY DEFAULT uuid_generate_v4(),
    text        TEXT        NOT NULL,
    author      TEXT,
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Seed: 6 citas de MockData.MOTIVATIONAL_QUOTES
INSERT INTO motivational_quotes (text, author) VALUES
    ('Small steps every day lead to big changes.', 'Anónimo'),
    ('Discipline is the bridge between goals and accomplishment.', 'Jim Rohn'),
    ('We are what we repeatedly do. Excellence, then, is not an act, but a habit.', 'Aristóteles'),
    ('Your future is created by what you do today, not tomorrow.', 'Anónimo'),
    ('Success is the sum of small efforts, repeated day in and day out.', 'Robert Collier'),
    ('The secret of your future is hidden in your daily routine.', 'Mike Murdock');

-- ============================================================
-- TRIGGER: updated_at automático en tablas relevantes
-- ============================================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_profiles_updated_at
    BEFORE UPDATE ON profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_habits_updated_at
    BEFORE UPDATE ON habits
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_user_achievements_updated_at
    BEFORE UPDATE ON user_achievements
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_friendships_updated_at
    BEFORE UPDATE ON friendships
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_groups_updated_at
    BEFORE UPDATE ON groups
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_notification_prefs_updated_at
    BEFORE UPDATE ON notification_preferences
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_habit_notif_updated_at
    BEFORE UPDATE ON habit_notification_settings
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================================
-- TRIGGER: Crear perfil automáticamente al registrar usuario
-- ============================================================

CREATE OR REPLACE FUNCTION handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO profiles (id, name, username, join_date)
    VALUES (
        NEW.id,
        COALESCE(NEW.raw_user_meta_data->>'name', 'Usuario'),
        COALESCE(NEW.raw_user_meta_data->>'username', 'user_' || substring(NEW.id::text, 1, 8)),
        CURRENT_DATE
    );
    INSERT INTO notification_preferences (user_id)
    VALUES (NEW.id);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

CREATE TRIGGER trg_on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW EXECUTE FUNCTION handle_new_user();
```

---

## 7. Políticas RLS Recomendadas

Row Level Security (RLS) es el mecanismo de Supabase que aplica reglas de acceso directamente en la base de datos PostgreSQL, garantizando que los usuarios solo puedan leer y modificar sus propios datos, incluso si la lógica de la aplicación falla.

### 7.1 profiles

```sql
-- Habilitar RLS
ALTER TABLE profiles ENABLE ROW LEVEL SECURITY;

-- Lectura pública del perfil básico (para leaderboard y búsqueda de amigos)
CREATE POLICY "profiles_select_public"
    ON profiles FOR SELECT
    USING (TRUE);

-- Solo el propio usuario puede actualizar su perfil
CREATE POLICY "profiles_update_own"
    ON profiles FOR UPDATE
    USING (auth.uid() = id);

-- Solo el propio usuario puede insertar su perfil (manejado por trigger)
CREATE POLICY "profiles_insert_own"
    ON profiles FOR INSERT
    WITH CHECK (auth.uid() = id);
```

### 7.2 habits

```sql
ALTER TABLE habits ENABLE ROW LEVEL SECURITY;

-- Solo el propietario puede ver sus hábitos
CREATE POLICY "habits_select_own"
    ON habits FOR SELECT
    USING (auth.uid() = user_id);

-- Solo el propietario puede crear hábitos
CREATE POLICY "habits_insert_own"
    ON habits FOR INSERT
    WITH CHECK (auth.uid() = user_id);

-- Solo el propietario puede modificar sus hábitos
CREATE POLICY "habits_update_own"
    ON habits FOR UPDATE
    USING (auth.uid() = user_id);

-- Solo el propietario puede eliminar sus hábitos
CREATE POLICY "habits_delete_own"
    ON habits FOR DELETE
    USING (auth.uid() = user_id);
```

### 7.3 habit_logs

```sql
ALTER TABLE habit_logs ENABLE ROW LEVEL SECURITY;

-- Solo el propietario puede ver sus registros
CREATE POLICY "habit_logs_select_own"
    ON habit_logs FOR SELECT
    USING (auth.uid() = user_id);

-- Solo el propietario puede insertar registros
CREATE POLICY "habit_logs_insert_own"
    ON habit_logs FOR INSERT
    WITH CHECK (auth.uid() = user_id);

-- Solo el propietario puede actualizar registros del día actual
CREATE POLICY "habit_logs_update_own_today"
    ON habit_logs FOR UPDATE
    USING (auth.uid() = user_id AND completed_date = CURRENT_DATE);
```

### 7.4 achievements y user_achievements

```sql
-- Logros del catálogo: lectura pública, solo admins escriben
ALTER TABLE achievements ENABLE ROW LEVEL SECURITY;
CREATE POLICY "achievements_select_all"
    ON achievements FOR SELECT
    USING (TRUE);

-- user_achievements: solo el propietario puede ver su progreso
ALTER TABLE user_achievements ENABLE ROW LEVEL SECURITY;
CREATE POLICY "user_achievements_select_own"
    ON user_achievements FOR SELECT
    USING (auth.uid() = user_id);

-- El sistema (service_role) puede insertar y actualizar logros de usuario
-- No se otorga INSERT/UPDATE a usuarios finales directamente
```

### 7.5 friendships

```sql
ALTER TABLE friendships ENABLE ROW LEVEL SECURITY;

-- El solicitante o el destinatario pueden ver la relación
CREATE POLICY "friendships_select_involved"
    ON friendships FOR SELECT
    USING (auth.uid() = requester_id OR auth.uid() = addressee_id);

-- Solo el solicitante puede enviar la solicitud
CREATE POLICY "friendships_insert_requester"
    ON friendships FOR INSERT
    WITH CHECK (auth.uid() = requester_id);

-- Cualquiera de los dos puede actualizar el estado (aceptar/bloquear)
CREATE POLICY "friendships_update_involved"
    ON friendships FOR UPDATE
    USING (auth.uid() = requester_id OR auth.uid() = addressee_id);

-- Cualquiera de los dos puede eliminar la amistad
CREATE POLICY "friendships_delete_involved"
    ON friendships FOR DELETE
    USING (auth.uid() = requester_id OR auth.uid() = addressee_id);
```

### 7.6 groups y group_members

```sql
ALTER TABLE groups ENABLE ROW LEVEL SECURITY;

-- Grupos públicos: todos pueden ver
CREATE POLICY "groups_select_public"
    ON groups FOR SELECT
    USING (is_public = TRUE OR EXISTS (
        SELECT 1 FROM group_members
        WHERE group_members.group_id = groups.id
        AND group_members.user_id = auth.uid()
    ));

-- Cualquier usuario puede crear un grupo
CREATE POLICY "groups_insert_any"
    ON groups FOR INSERT
    WITH CHECK (auth.uid() = created_by);

-- Solo el admin del grupo puede actualizar
CREATE POLICY "groups_update_admin"
    ON groups FOR UPDATE
    USING (EXISTS (
        SELECT 1 FROM group_members
        WHERE group_members.group_id = groups.id
        AND group_members.user_id = auth.uid()
        AND group_members.role = 'ADMIN'
    ));

ALTER TABLE group_members ENABLE ROW LEVEL SECURITY;

-- Miembros pueden ver a otros miembros del mismo grupo
CREATE POLICY "group_members_select_same_group"
    ON group_members FOR SELECT
    USING (EXISTS (
        SELECT 1 FROM group_members gm
        WHERE gm.group_id = group_members.group_id
        AND gm.user_id = auth.uid()
    ));

-- Un usuario puede unirse a un grupo (insertar su propia membresía)
CREATE POLICY "group_members_insert_self"
    ON group_members FOR INSERT
    WITH CHECK (auth.uid() = user_id);
```

### 7.7 activity_feed

```sql
ALTER TABLE activity_feed ENABLE ROW LEVEL SECURITY;

-- Un usuario puede ver la actividad de sus amigos y la propia
CREATE POLICY "activity_feed_select_friends"
    ON activity_feed FOR SELECT
    USING (
        auth.uid() = user_id
        OR EXISTS (
            SELECT 1 FROM friendships
            WHERE status = 'ACCEPTED'
            AND (
                (requester_id = auth.uid() AND addressee_id = activity_feed.user_id)
                OR
                (addressee_id = auth.uid() AND requester_id = activity_feed.user_id)
            )
        )
    );

-- Solo el sistema inserta en el feed (via triggers o RPC con service_role)
```

### 7.8 notification_preferences y habit_notification_settings

```sql
ALTER TABLE notification_preferences ENABLE ROW LEVEL SECURITY;
CREATE POLICY "notif_prefs_own"
    ON notification_preferences FOR ALL
    USING (auth.uid() = user_id)
    WITH CHECK (auth.uid() = user_id);

ALTER TABLE habit_notification_settings ENABLE ROW LEVEL SECURITY;
CREATE POLICY "habit_notif_settings_own"
    ON habit_notification_settings FOR ALL
    USING (auth.uid() = user_id)
    WITH CHECK (auth.uid() = user_id);
```

---

## 8. Supuestos Realizados

Durante el análisis del código se identificaron los siguientes elementos que no estaban completamente implementados y fueron inferidos o extendidos para construir un modelo de producción coherente:

| # | Supuesto | Justificación |
|---|---|---|
| 1 | El campo `completionLog: Map<String, Boolean>` del modelo `Habit` se normalizó en la tabla independiente `habit_logs` | La serialización de un mapa dentro del modelo de un hábito es ineficiente para consultas de estadísticas y no escala con el historial a largo plazo |
| 2 | Los campos `current_streak` y `best_streak` se mantienen desnormalizados en `habits` | La pantalla `HabitDetailScreen` y `StreakScreen` requieren acceso frecuente al streak actual; recalcular desde `habit_logs` en cada lectura sería costoso |
| 3 | El campo `completionRate` se almacena desnormalizado en `habits` | Igualmente consultado frecuentemente; debe actualizarse mediante un trigger o función RPC al insertar en `habit_logs` |
| 4 | Los grupos mostrados en `SocialScreen` tienen un `current_streak` colectivo | La lógica de cálculo del streak grupal no está implementada; se asumió que un grupo completa su racha cuando todos los miembros completan al menos un hábito en el día |
| 5 | La tabla `activity_feed` es generada por el sistema vía triggers o funciones RPC | En `SocialScreen (Activity tab)` se muestran eventos automáticos; no hay lógica de generación de eventos en el código actual |
| 6 | Los logros se calculan automáticamente mediante funciones RPC | Los ViewModels de logros están vacíos; se asumió que la evaluación de criterios ocurre en el backend al completar hábitos o alcanzar streaks |
| 7 | La tabla `categories` incluye una bandera `is_default` y un campo `created_by` | El código muestra 8 categorías fijas (`MockData.CATEGORIES`), pero un sistema de producción debe permitir categorías personalizadas por usuario |
| 8 | Las citas motivacionales se sirven desde BD con rotación aleatoria | Actualmente son una lista estática de 6 strings en `MockData`; en producción deben gestionarse sin necesidad de actualizar la app |
| 9 | La autenticación soporta email/password, Google y Apple OAuth | `AuthScreen` muestra botones para los tres métodos; el backend deberá configurar los proveedores OAuth en el panel de Supabase |
| 10 | La tabla `profiles` se crea automáticamente al registrar un usuario en `auth.users` | El trigger `trg_on_auth_user_created` asegura que cada cuenta de autenticación tenga un perfil y preferencias de notificaciones inicializadas |

---

## 9. Recomendaciones Técnicas

### 9.1 Autenticación y seguridad

- **Usar `supabase-kt`** (SDK oficial de Supabase para Kotlin) como cliente Android: soporta autenticación, Realtime, PostgREST y Storage.
- **Configurar OAuth con Google y Apple** en el panel de Supabase para habilitar el flujo ya dibujado en `AuthScreen`.
- **Nunca exponer la `service_role` key** en el cliente móvil. Usar únicamente la `anon key` y dejar que RLS controle el acceso.
- **Usar `SECURITY DEFINER` con precaución** únicamente en funciones que requieren acceso elevado (como el trigger de creación de perfil).

### 9.2 Sincronización y comportamiento offline-first

- **Mantener Room Database como caché local** (`habits`, `habit_logs`, `profiles`) con el mismo esquema normalizado. Room puede ser el "estado de verdad local" mientras DataStore se elimina.
- **Estrategia de sincronización**: las operaciones del usuario (toggle hábito, crear hábito) se escriben primero en Room y se propagan a Supabase en segundo plano usando `WorkManager`. En caso de conflicto, priorizar el servidor como fuente de verdad.
- **Campos `created_at` y `updated_at`** permiten sincronización incremental: al reconectar, solo se deben sincronizar registros con `updated_at > last_sync_timestamp`.
- **Usar Supabase Realtime** para suscribirse a cambios en `activity_feed` y actualizar el feed social sin polling.

### 9.3 Escalabilidad del esquema

- **Particionar `habit_logs` por rango de fecha** (`PARTITION BY RANGE (completed_date)`) cuando la tabla supere millones de filas. PostgreSQL soporta esto nativamente.
- **Índice compuesto `(user_id, completed_date)`** en `habit_logs` es crítico para las consultas de estadísticas semanales, mensuales y anuales de `StatisticsScreen`.
- **Usar `JSONB` en `activity_feed.metadata`** para datos contextuales variables (ej. `{"streak_count": 30}`) sin romper el esquema.
- **Archivar o eliminar `activity_feed` con más de 90 días** de antigüedad mediante un cron job de Supabase Edge Functions para mantener el rendimiento del feed social.

### 9.4 Estadísticas y agregaciones

- **Crear vistas materializadas** para estadísticas costosas que se muestran en `StatisticsScreen`:
  ```sql
  CREATE MATERIALIZED VIEW mv_user_weekly_stats AS
  SELECT user_id,
         DATE_TRUNC('week', completed_date) AS week,
         COUNT(*) FILTER (WHERE completed = TRUE) AS completed_count,
         COUNT(*) AS total_count
  FROM habit_logs
  GROUP BY user_id, DATE_TRUNC('week', completed_date);
  ```
- **Actualizar las vistas materializadas** mediante un cron de Supabase (`pg_cron`) cada hora o cada noche.
- **Almacenar el `completion_rate` desnormalizado** en `habits` y actualizarlo mediante un trigger cada vez que se inserte en `habit_logs`.

### 9.5 Notificaciones push

- **Integrar Firebase Cloud Messaging (FCM)** o **Expo Notifications** para enviar notificaciones push al dispositivo Android.
- **Almacenar el `fcm_token`** en `profiles` (agregar campo `push_token TEXT`) y actualizarlo al iniciar sesión.
- **Usar Supabase Edge Functions** para disparar notificaciones basadas en eventos: racha en riesgo (detectada en la función de reconciliación nocturna), logro desbloqueado, actividad de amigos.
- **Respetar las `notification_preferences`** consultando la tabla antes de enviar cualquier notificación.

### 9.6 Migración desde el estado actual

1. **Fase 1**: Integrar Supabase SDK en Android. Mantener DataStore como fallback temporal.
2. **Fase 2**: Implementar autenticación con Supabase Auth. Crear perfiles en `profiles`.
3. **Fase 3**: Migrar `HabitRepository` para escribir en Supabase y leer desde Room (caché local). Eliminar DataStore.
4. **Fase 4**: Implementar ViewModels vacíos (`SocialViewModel`, `AchievementsViewModel`, etc.) conectados a Supabase.
5. **Fase 5**: Habilitar Realtime para el feed social y las actualizaciones del leaderboard.

### 9.7 Integridad de datos y consistencia

- **El trigger `trg_on_auth_user_created`** garantiza que nunca exista una cuenta de autenticación sin perfil ni preferencias de notificaciones.
- **Los triggers `update_updated_at_column`** garantizan que los timestamps se mantengan automáticamente sin depender de la capa de aplicación.
- **El constraint `CHECK(requester_id <> addressee_id)`** en `friendships` previene autoamistades a nivel de base de datos.
- **El constraint `UNIQUE(habit_id, completed_date)`** en `habit_logs` previene registros duplicados por día, asegurando la integridad del cálculo de streaks.
