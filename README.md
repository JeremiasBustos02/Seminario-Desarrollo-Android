# 🎮 Byte - Video Game Browser

Aplicación Android para explorar y filtrar videojuegos utilizando la API de RAWG.

## 📱 Características Implementadas

### Enunciado Principal ✅
- ✅ Lista de videojuegos con nombre e imagen
- ✅ Pantalla de filtrado independiente
- ✅ Filtros por plataforma, género, año y calificación
- ✅ Indicadores de carga durante consultas
- ✅ Manejo de errores con mensajes en pantalla

### Enunciados Secundarios
- ✅ **b) Guardar filtros de búsqueda**: Los filtros se persisten con SharedPreferences
- ✅ **f) UI adaptativa**: Soporte para modo claro/oscuro automático

## 🏗️ Arquitectura

La aplicación sigue el patrón **MVVM** con **Repository Pattern**:
**Flujo de datos:**

- Activity (Vista) → observa StateFlows
- ViewModel (Lógica de presentación) → llama funciones
- Repository (Abstracción de datos) → intermedia
- DataSource (Llamadas a API) → ejecuta requests
- RAWG API → provee datos

**¿Qué hace cada capa?**

- Activity: Muestra UI y observa cambios
- ViewModel: Maneja lógica de presentación y estados
- Repository: Abstrae la fuente de datos
- DataSource: Hace las llamadas reales a Retrofit
- API: Interfaz que define los endpoints

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI**: ViewBinding + Material Design 3
- **Arquitectura**: MVVM
- **Inyección de dependencias**: Dagger Hilt
- **Networking**: Retrofit + Gson
- **Asincronía**: Coroutines + StateFlow
- **Carga de imágenes**: Glide
- **Persistencia**: SharedPreferences

## 📦 Estructura del Proyecto

### Descripción por carpeta:

**data/api** - Define contratos de la API con anotaciones de Retrofit

**data/datasource** - Implementa las llamadas HTTP y transforma datos

**data/model** - Clases de datos (POJOs/DTOs)

**data/repository** - Patrón Repository, abstrae el origen de datos

**di** - Configuración de Dagger Hilt

**ui/main** - Pantalla principal con listado de juegos

**ui/filter** - Pantalla para aplicar filtros

## 🚀 Configuración

1. Clona el repositorio
2. Obtén una API Key de [RAWG](https://rawg.io/apidocs)
3. Cambia la clave en `build.gradle.kts (module)`:
```properties
buildConfigField("String", "apiKey", "\"TU_API_KEY\"")
```

## 👨‍💻 Autor
**Jeremías Emanuel Bustos** - jeremias.e.bustos@gmail.com

Desarrollado como proyecto del Seminario de Android - UNICEN
