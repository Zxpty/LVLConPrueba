# LVLConPrueba - Aplicación Android (Prueba Técnica)

¡Hola! 👋. He desarrollado esta aplicación enfocándome en las buenas prácticas, la escalabilidad y una experiencia de usuario fluida.

A continuación, detallo los aspectos clave del proyecto, mis decisiones técnicas y algunos puntos que encontré durante el desarrollo.

## 🛠 Arquitectura de Desarrollo

Para este proyecto he implementado una arquitectura **Clean Architecture** dividida en capas, lo que garantiza que el código sea testeable y fácil de mantener:

1.  **Capa de UI (Presentación):** Utiliza el patrón **MVVM (Model-View-ViewModel)**. Las vistas son reactivas y observan el estado expuesto por los ViewModels.
2.  **Capa de Domain (Dominio):** Contiene las reglas de negocio, modelos de dominio y las definiciones de los repositorios. Es una capa pura de Kotlin, sin dependencias de Android.
3.  **Capa de Data (Datos):** Se encarga de la persistencia (DataStore para la sesión) y la comunicación con la API (Retrofit). Implementa las interfaces de repositorio definidas en la capa de dominio.

**Inyección de Dependencias:** He utilizado **Hilt** para gestionar las dependencias de manera eficiente y desacoplada.

## 🎨 Desarrollo con Jetpack Compose

Toda la interfaz de usuario ha sido construida íntegramente con **Jetpack Compose**, el toolkit moderno de Android.

-   **Componentización:** He creado componentes reutilizables para mantener la coherencia visual y facilitar el mantenimiento.
-   **Navegación:** Implementada con `navigation-compose`, gestionando las transiciones entre pantallas de forma centralizada.
-   **Gestión de Estado:** Uso intensivo de `StateFlow` para asegurar que la UI sea reactiva y refleje fielmente el estado de la aplicación.

## 🔍 Observaciones y Desafíos (Identificación de Errores)

Durante el proceso de integración, identifiqué algunas discrepancias entre los recursos proporcionados que considero importante mencionar:

1.  **Discrepancia en el Login (Figma vs API):** En el diseño de Figma se indicaba el uso de un campo de **"Correo"**. Sin embargo, según la especificación de la API proporcionada, el acceso se realiza mediante un **"Usuario"**. He priorizado la funcionalidad de la API ajustando el formulario para utilizar el nombre de usuario.
2.  **Información de Empresa en Perfil y Password:** Al consumir el endpoint para obtener los datos del perfil del usuario, noté que la respuesta busca al usuario pero no trae información asociada a la **"Empresa"**. Debido a esto, no es posible determinar o mostrar en la app en qué empresa se encuentra el usuario actualmente basándose solo en ese endpoint, al igual que la contraseña solo es ingresada cuando nos logueamos.
3.  **Desafios dentro del Projecto:** Uno de los principales desafíos durante el desarrollo fue la implementación de las vistas y su integración con el backend. Lo que más me costó fue entender la estructura utilizada para la creación y actualización de datos, ya que se manejaba todo a través de un solo endpoint, una forma de trabajo con la que no tenía experiencia previa.
Otro reto importante fue el manejo de excepciones. En varios endpoints, los errores no estaban claramente diferenciados ni documentados, lo que hacía difícil identificar qué estaba fallando en las solicitudes enviadas al backend. Por ejemplo, cuando un usuario ingresaba una contraseña incorrecta, el backend respondía con un status 500 (Internal Server Error) en lugar de un código más específico. Esta misma situación se repetía en otros endpoints cuando faltaba información o se enviaban datos inválidos, lo que me obligaba a inferir qué campos eran obligatorios y cuál era la estructura correcta de la data esperada.

A pesar de estas dificultades, el proceso me permitió mejorar mi capacidad de análisis, depuración y adaptación a backend con validaciones poco explícitas, fortaleciendo mi experiencia en la integración frontend–backend.
## 🚀 Tecnologías Utilizadas

-   **Kotlin**
-   **Jetpack Compose** (UI)
-   **Hilt** (Inyección de Dependencias)
-   **Retrofit & OkHttp** (Comunicación con API)
-   **DataStore** (Almacenamiento de sesión y preferencias)
-   **ViewModel & Flow** (Arquitectura reactiva)

---
Espero que el proyecto cumpla con sus expectativas.
