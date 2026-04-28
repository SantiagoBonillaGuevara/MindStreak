# MindStreak – Estado del Proyecto

> Aplicación móvil de seguimiento de hábitos, motivación y crecimiento personal para estudiantes universitarios.

---

## 1. Descripción del Proyecto

### ¿Qué es MindStreak?

MindStreak es una aplicación móvil nativa para Android, desarrollada en Kotlin con Jetpack Compose, orientada a estudiantes universitarios que desean construir hábitos positivos de manera consistente. La app combina seguimiento diario de hábitos, un sistema de rachas motivacional, gamificación progresiva y competencia social para crear una experiencia de desarrollo personal atractiva y sostenible.

### El problema que resuelve

La vida universitaria es una de las etapas más exigentes para la salud mental y la disciplina personal. Los estudiantes enfrentan dificultades para:

- Mantener rutinas saludables (sueño, ejercicio, alimentación) bajo presión académica.
- Sostener hábitos de estudio sin supervisión externa.
- Encontrar motivación intrínseca para mantener la constancia día a día.
- Construir responsabilidad sin herramientas de apoyo social.

MindStreak ataca este problema con un enfoque basado en evidencia: la repetición guiada, el refuerzo positivo inmediato y la presión social positiva son los tres pilares que convierten acciones aisladas en hábitos duraderos.

### Propuesta de valor

| Para quién | Qué ofrece | Diferenciador |
|---|---|---|
| Estudiantes universitarios | Seguimiento de hábitos diarios con sistema de rachas | Diseñado específicamente para el contexto académico |
| Usuarios motivados por logros | Sistema de XP, niveles y achievements desbloqueables | Gamificación profunda e integrada |
| Personas con mentalidad social | Leaderboard de amigos y retos semanales | Competencia positiva entre pares |
| Quienes buscan claridad | Estadísticas visuales de progreso personal | Gráficas y métricas accionables |

---

## 2. ¿Cómo funciona la aplicación?

Esta sección describe en detalle el flujo completo de usuario dentro de MindStreak, desde el primer acceso hasta el uso diario consolidado.

### 2.1 Onboarding — Primer contacto con la app

Al abrir la aplicación por primera vez, el usuario es recibido por una pantalla de onboarding compuesta por **tres slides animados**, cada uno con una propuesta de valor clara:

1. **"Build Habits That Stick"** — Presenta la mecánica central: registrar hábitos con un solo toque y ver cómo la vida se transforma con la constancia.
2. **"Streaks Keep You Going"** — Introduce el sistema de rachas 🔥 y los logros que se desbloquean al mantener la cadena.
3. **"Better Together With Friends"** — Presenta la dimensión social: leaderboards, hitos compartidos y motivación entre amigos.

Cada slide cuenta con una ilustración animada específica (lista de hábitos, gráfica de barras, tarjetas de amigos), animaciones de entrada con física de resorte, y un color de acento temático. El usuario puede avanzar con "Next" o saltar el onboarding en cualquier momento.

### 2.2 Autenticación — Acceso a la plataforma

Tras el onboarding, el usuario accede a la pantalla de autenticación, que ofrece dos modos en un selector animado:

- **Login** — Acceso con correo electrónico y contraseña.
- **Sign Up** — Registro con nombre completo, universidad y año académico, correo y contraseña.

Ambos modos incluyen validación de campos, indicador de visibilidad de contraseña, y botones de acceso social (Google, Apple). El usuario también puede ingresar en **modo invitado** sin crear cuenta. El diseño aplica transiciones animadas entre los dos formularios para una experiencia fluida.

### 2.3 Pantalla principal (Home) — El corazón de la experiencia diaria

La pantalla Home es la interfaz central de uso diario. Está organizada como un scroll vertical con las siguientes secciones:

#### Header personalizado
Un saludo dinámico con el nombre del usuario, la fecha actual, un acceso directo a notificaciones (con indicador visual de notificaciones pendientes) y un avatar que lleva al perfil.

#### Streak Card — La racha actual
Una tarjeta prominente que muestra el **número de días consecutivos de uso activo** con animación de escala al cargar. Incluye:
- Contador animado de días con el color naranja característico 🔥.
- Barra de progreso hacia el próximo hito (ej. "30 días").
- Mini-calendario de la semana en curso con días marcados en color.
- Indicador de la mejor racha histórica.

Esta tarjeta es el elemento de mayor peso visual de la pantalla, ya que la racha es el motor motivacional principal de la app.

#### Today's Progress — Progreso del día
Una tarjeta que muestra cuántos hábitos han sido completados sobre el total del día:
- **ProgressRing animado** con el porcentaje de completado en el centro.
- Barra de progreso horizontal con gradiente de color.
- Mensaje de motivación contextual.

#### Quote Card — Motivación diaria
Una frase motivacional del día, seleccionada del pool de citas de la app (ej. *"Consistency beats perfection every time."*), presentada en una tarjeta con bordes sutiles.

#### Lista de hábitos del día
El listado de todos los hábitos activos para la fecha, cada uno representado como una **HabitCard** que muestra:
- Emoji identificador y nombre del hábito.
- Indicador visual de racha individual.
- Tasa de completado histórico (barra de progreso).
- **CheckButton** interactivo para marcar el hábito como completado, con animación de cambio de estado.

Al completar todos los hábitos del día, la app muestra una notificación de celebración. Si no hay hábitos creados, se presenta un **EmptyState** con llamada a la acción para crear el primero.

### 2.4 Creación de hábitos — Flujo de dos pasos

Al presionar "Add" en la pantalla Home, el usuario accede a un formulario dividido en dos pasos con transiciones animadas:

#### Paso 1 — Detalles del hábito
- **Nombre del hábito** con validación en tiempo real.
- **Selector de emoji** con cuadrícula de 15 opciones (🏃 🧘 💧 📚 😴 📖 🥗 🏋️ y más), con animación de escala al seleccionar.
- **Selector de categoría** con 8 categorías disponibles: Fitness, Mindfulness, Health, Learning, Sleep, Academic, Social, Nutrition — cada una con su color y emoji identificador.
- **Vista previa** del hábito en tiempo real, que muestra cómo se verá la tarjeta con los datos ingresados.

#### Paso 2 — Programación
- **Frecuencia**: Daily, Weekdays, 3× a week, Weekly.
- **Hora de recordatorio**: selector de horarios predefinidos (06:00 a 22:00).
- **Resumen final** del hábito configurado antes de confirmar la creación.

Un indicador de pasos en el header (dots animados) guía al usuario a través del proceso.

### 2.5 Detalle de hábito — Seguimiento individual

Al tocar cualquier hábito de la lista, el usuario accede a la pantalla de detalle, que ofrece:

- **Hero card** con información completa: emoji, nombre, categoría, frecuencia y hora de recordatorio.
- **StatItems** con las métricas clave: racha actual, mejor racha histórica y tasa de completado.
- **Vista semanal** con los 7 días de la semana y marca visual de los días completados.
- **Mapa de calor mensual (Heatmap)** con un grid de color por intensidad de completado durante el mes.
- **ProgressRing** de tasa de completado en los últimos 30 días.
- Botones de acción para editar o eliminar el hábito.

### 2.6 Sistema de Rachas (Streak Screen)

La pantalla de Streak es una vista de celebración y motivación profunda del progreso del usuario:

- **Hero card central** con el número de días de racha en tamaño tipográfico grande, con emoji 🔥 pulsante (animación infinita).
- **Estadísticas clave**: mejor racha histórica, total de registros, porcentaje de completado del mes.
- **Tarjeta "Next Milestone"** con barra de progreso hacia el próximo hito.
- **Lista de hitos**: jalones desbloqueables en días (7, 14, 21, 30, 50, 100), con emoji, nombre y estado (logrado / pendiente / en progreso).
- **Calendario mensual** con el historial de días de racha marcados.
- **Rachas por hábito**: un listado de todos los hábitos activos con su racha individual y barra de progreso.
- **CTA motivacional** que invita al usuario a no romper la cadena y redirige a Home.

### 2.7 Estadísticas — Datos accionables

La pantalla de estadísticas transforma los datos de uso en visualizaciones claras:

- **Selector de período**: Semana / Mes / Año, con tabs animados.
- **Tarjetas de métricas rápidas**: hábitos completados en el período, mejor racha, promedio diario.
- **Gráfica de barras interactiva** de hábitos completados por período, con tooltip al tocar cada barra.
- **Gráfica de líneas (Completion Trend)** con curvas suavizadas y punto de selección interactivo.
- **Desglose por hábito**: lista ordenada con barra de progreso individual y porcentaje.
- **Resumen global del día**: ProgressRing con el porcentaje total de completado y hábitos restantes.

### 2.8 Logros y Gamificación (Achievements)

El sistema de gamificación transforma el progreso en recompensas tangibles:

- **Sistema de XP y Niveles**: cada hábito completado suma puntos de experiencia. Al acumular suficiente XP, el usuario sube de nivel (actualmente en Nivel 12 en el perfil de referencia).
- **Logros desbloqueables** con cuatro niveles de rareza: Common, Rare, Epic y Legendary.
- **Grid de achievements**: visualización compacta con lock visual sobre los no obtenidos y progreso parcial (ej. "21/30 días").
- **Logros recientes**: carrusel horizontal de los últimos achievements obtenidos.
- **Level Card**: tarjeta con el nivel actual, anillo de XP animado y desglose de XP ganado/bloqueado.
- **Tooltip de detalle**: al tocar un logro, aparece información completa sobre cómo desbloquearlo.

Ejemplos de logros: *First Flame* (7 días de racha), *Iron Will* (14 días), *Habit Hero* (30 días completos), *Legend* (100 días de racha), *Century Club* (100 hábitos totales).

### 2.9 Social — Competencia positiva entre amigos

La dimensión social de MindStreak convierte el progreso personal en una experiencia compartida:

- **Leaderboard semanal** con ranking de amigos ordenado por puntos, con podio visual para los tres primeros.
- **Tarjetas de amigos** con avatar, nivel, hábitos de la semana y racha actual.
- **Reto semanal (Weekly Challenge)** con un objetivo compartido para todo el grupo.
- **Feed de actividad**: registro de hitos y logros recientes de los amigos.
- **Grupos**: posibilidad de crear y unirse a grupos temáticos de hábitos.

### 2.10 Perfil — Vista de identidad del usuario

La pantalla de perfil consolida toda la información de progreso del usuario:

- **Hero Card** con avatar personalizado (emoji), nombre, username, universidad, nivel y barra de XP.
- **Stats Grid** con las cuatro métricas principales: racha actual, mejor racha, hábitos completados y nivel.
- **Accesos rápidos** a Achievements, Social y Reminders.
- **Sección de preferencias**: modo oscuro, notificaciones.
- **Sección de cuenta**: privacidad, upgrade a Pro, referidos (+50 XP).
- **Cierre de sesión**.

### 2.11 Notificaciones — Recordatorios personalizados

La pantalla de notificaciones permite al usuario controlar exactamente cuándo y cómo recibe recordatorios:

- **Toggle maestro** que activa/desactiva todas las notificaciones.
- **Tipos de alerta configurables**: Streak Alerts, Daily Summary, Achievements, Social Activity.
- **Recordatorios por hábito**: cada hábito tiene su propio toggle con la hora configurada en la creación.

### 2.12 Elementos que fomentan la consistencia

MindStreak combina múltiples mecanismos psicológicos para mantener al usuario comprometido:

- **Efecto de racha (Streak Effect)**: ver el contador crecer cada día genera aversión a la pérdida — romperlo duele más que mantenerlo.
- **Hitos progresivos**: los logros están diseñados para que siempre haya uno a corto alcance.
- **Recordatorios en el momento correcto**: cada hábito tiene su propia hora de notificación.
- **Frases motivacionales diarias**: refuerzo positivo contextual en la pantalla principal.
- **Presión social positiva**: ver el progreso de los amigos activa la motivación por comparación.
- **XP y subida de nivel**: recompensa cualquier acción, no solo la perfección.

---

## 3. Estado actual del proyecto

### Fase 1 — Estrategia ✅ Completada

| Entregable | Estado |
|---|---|
| Definición del problema | ✅ OK |
| Buyer Persona | ✅ OK |
| Propuesta de valor | ✅ OK |
| Alcance MVP (MoSCoW) | ✅ OK |

---

### Fase 2 — Diseño 🔄 En progreso

| Entregable | Estado | Responsable |
|---|---|---|
| User Flow | ✅ OK | Santiago |
| Wireframes / Prototipo | ✅ OK | Mariana |
| UI Kit / Guía de Estilos | ✅ OK | Mariana |

---

### Fase 3 — Arquitectura 🔄 Parcialmente en progreso

| Entregable | Estado |
|---|---|
| Stack tecnológico | ✅ OK |
| Modelo de datos (DER) | ⏳ Pendiente |
| Diseño de APIs / Webhooks | ⏳ Pendiente |
| Seguridad y Auth | 🔶 50% — Pendiente configuración de roles |

---

### Fase 4 — Desarrollo ⏸ No iniciado

| Entregable | Estado |
|---|---|
| Entorno de desarrollo (Setup) | ⏳ Pendiente — equipo completo |
| Implementación Core | 🔴 No iniciado |
| Integración de terceros | 🔴 No iniciado |

---

### Fase 5 — Calidad (QA) ⏸ No iniciado

| Entregable | Estado |
|---|---|
| Estrategia de testing | 🔴 No iniciado |
| Pruebas unitarias | 🔴 No iniciado |
| Pruebas de integración | 🔴 No iniciado |
| Pruebas de usuario (UAT) | 🔴 No iniciado |

---

### Fase 6 — Lanzamiento ⏸ No iniciado

| Entregable | Estado |
|---|---|
| Configuración de Play Store | 🔴 No iniciado |
| Assets de publicación | 🔴 No iniciado |
| Plan de distribución | 🔴 No iniciado |

---

### Fase 7 — Legal ⏸ No iniciado

| Entregable | Estado |
|---|---|
| Términos y condiciones | 🔴 No iniciado |
| Política de privacidad | 🔴 No iniciado |
| Cumplimiento GDPR / local | 🔴 No iniciado |

---

## 4. Seguimiento del proyecto

El seguimiento de MindStreak se realiza de forma estructurada y continua, integrando tanto el avance técnico como las decisiones de diseño, validación del problema y evolución del backlog.

Toda la trazabilidad del proyecto se documenta en el siguiente recurso:

🔗 **[Documentación diaria del proyecto](https://bit.ly/3R69DjK)**

Este documento consolida:

- Registro detallado de actividades por fase (estrategia, diseño, arquitectura).
- Decisiones clave de diseño (UX/UI, flujos de usuario, componentes).
- Definiciones técnicas (stack, arquitectura, backlog y planificación).
- Resultados de validación (encuestas, análisis de Pareto, 5 Whys).
- Evolución del problema, propuesta de valor y alcance del proyecto.
- Seguimiento de tareas, responsables y estado real del avance.

Este enfoque garantiza trazabilidad completa del proceso de diseño, permitiendo evidenciar cómo cada decisión tomada está directamente conectada con el problema identificado y los objetivos del proyecto.

Se recomienda utilizar este documento como fuente principal de referencia para revisiones académicas y validaciones del proyecto.

---

## 5. Enfoque técnico

### Stack tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM + Clean Architecture |
| Persistencia local | DataStore (Jetpack) |
| Navegación | Jetpack Navigation Compose |
| Estado | StateFlow + collectAsState |

### Separación de responsabilidades

El proyecto adopta una estructura modular por funcionalidad con las siguientes capas bien definidas:

```
com.example.mindstreak/
├── core/
│   ├── components/   ← Componentes UI reutilizables y sin estado
│   ├── navigation/   ← Grafo de navegación y rutas
│   └── theme/        ← Colores, tipografía y tema global
├── data/
│   ├── model/        ← Modelos de dominio (Habit, User, Achievement…)
│   ├── repository/   ← Capa de acceso a datos
│   └── local/        ← DataStore y serialización
└── feature/
    ├── home/         ← Pantalla principal
    ├── streak/       ← Sistema de rachas
    ├── statistics/   ← Estadísticas y gráficas
    ├── achievements/ ← Logros y gamificación
    ├── social/       ← Leaderboard y amigos
    ├── profile/      ← Perfil del usuario
    ├── create_habit/ ← Flujo de creación de hábito
    ├── habit_detail/ ← Detalle e historial de hábito
    ├── notifications/← Configuración de recordatorios
    ├── auth/         ← Autenticación
    └── onboarding/   ← Introducción a la app
```

### Componentes UI reutilizables (`core/components`)

Todos los elementos visuales reutilizables están extraídos a `core/components`, garantizando:

- **Cero lógica de negocio** dentro de los componentes: solo reciben parámetros y renderizan UI.
- **Reutilización real**: un mismo componente (ej. `StatCard`, `CustomToggle`) es consumido por múltiples pantallas sin duplicación.
- **Mantenibilidad**: cambiar el diseño de un componente se refleja automáticamente en todas las pantallas que lo usan.
- **Escalabilidad**: agregar nuevas pantallas no requiere recrear patrones UI ya existentes.

Para más detalle sobre qué componentes existen y cómo usarlos, consultar [`core/components/README_COMPONENTS.md`](app/src/main/java/com/example/mindstreak/core/components/README_COMPONENTS.md).

---

## 6. Próximos pasos

Con la fase de diseño completada y la arquitectura definida a nivel conceptual, los siguientes hitos se enfocan en la transición hacia construcción del POC y validación técnica:

1. **Finalizar el modelo de datos (DER)**  
   Definir de forma completa las entidades del sistema (usuarios, hábitos, registros diarios, rachas, logros y relaciones sociales), asegurando consistencia con los requisitos funcionales y reglas de negocio.

2. **Diseñar y documentar las APIs**  
   Especificar los endpoints necesarios para autenticación, gestión de hábitos, registro de cumplimiento, cálculo de rachas, notificaciones y dashboard.  
   Incluir contratos (request/response), validaciones y manejo de errores.

3. **Definir la arquitectura técnica detallada**  
   Pasar de arquitectura conceptual a diseño implementable:
   - Estructura backend (servicios, controladores, repositorios)
   - Integración con base de datos (Supabase/Firebase)
   - Flujo de notificaciones (FCM)
   - Manejo de estado y sincronización

4. **Implementar autenticación y seguridad**  
   Configurar el sistema de autenticación (OAuth2 / Firebase Auth), incluyendo:
   - Gestión de sesiones
   - Encriptación de contraseñas (bcrypt)
   - Reglas de acceso y protección de endpoints
   - Cumplimiento básico de protección de datos

5. **Configurar el entorno de desarrollo**  
   Unificar el setup del equipo:
   - Proyecto Android (Android Studio)
   - Backend y base de datos
   - Variables de entorno
   - Servicios externos (auth, notificaciones)

6. **Iniciar desarrollo del POC (Iteraciones 1 y 2)**  
   Implementar las funcionalidades core priorizadas:
   - Gestión de cuenta (registro, login, recuperación)
   - CRUD de hábitos
   - Registro diario y cálculo de rachas
   - Notificaciones y recordatorios
   - Dashboard de progreso y gamificación básica

7. **Definir e implementar estrategia de QA**  
   Establecer un enfoque de calidad alineado con los RNF:
   - Pruebas unitarias (ViewModels, lógica de negocio)
   - Pruebas de integración (flujos críticos)
   - Pruebas de usuario (usabilidad y experiencia)
   - Validación de rendimiento y seguridad básica

8. **Preparar validación con usuarios (UAT)**  
   Planear pruebas con estudiantes universitarios para evaluar:
   - Facilidad de uso (onboarding < 3 min)
   - Nivel de adopción
   - Percepción de valor (motivación, claridad, constancia)

9. **Ajuste y evolución hacia MVP**  
   Con base en resultados del POC:
   - Refinar funcionalidades existentes
   - Priorizar features de iteraciones 3–4 (recomendaciones y social)
   - Optimizar rendimiento, accesibilidad y escalabilidad

---

*Documento generado como parte del modulo de Capstone Design Project – MindStreak.  
La trazabilidad completa del proceso, decisiones y evolución del proyecto se encuentra documentada en: https://bit.ly/3R69DjK*
