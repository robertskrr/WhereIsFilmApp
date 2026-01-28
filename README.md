# WhereIsFilm 🎬

**WhereIsFilm** es una aplicación de Android desarrollada en **Java** que permite consultar la disponibilidad de películas y series en diversas plataformas de streaming en España. 

La app ofrece una experiencia directa integrando búsqueda, visualización de resultados y acceso a los contenidos mediante enlaces oficiales.

## 🚀 Tecnologías y Herramientas
* **Lenguaje:** Java con **JDK 8**.
* **Entorno:** Android Studio.
* **API:** Streaming Availability.
* **Consumo de API:** **Retrofit 2** para gestionar peticiones REST.
* **Procesamiento de datos:** **GSON** para la conversión de JSON a objetos Java.
* **Carga de Imágenes:** **Glide** para la descarga y renderizado de pósters.
* **Almacenamiento Local:** **SharedPreferences** para gestionar el historial de búsquedas del usuario.

## 🛠️ Características Principales
* **Búsqueda Inteligente:** Implementa una lógica de **coincidencia exacta** que recorre los resultados de la API para encontrar el título específico que el usuario ha escrito.
* **Visualización de Resultados:**
    * Muestra el póster del audiovisual.
    * Genera una lista dinámica de plataformas disponibles con sus logos correspondientes.
    * Aplica colores temáticos a los nombres de las plataformas (Rojo para Netflix, Azul para Prime Video, etc.) para una mejor identidad visual.
* **Navegación Integrada:** Incluye una **WebView** propia para abrir los enlaces de streaming sin salir de la aplicación.
* **Historial de Búsquedas:** Guarda automáticamente cada búsqueda exitosa con marca de tiempo.
* **Multimedia:** Incorpora efectos sonoros mediante **MediaPlayer** al mostrar resultados exitosos.

