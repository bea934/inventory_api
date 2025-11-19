# Inventory API

Inventario API es una aplicación full stack construida con Spring Boot que combina una API REST JSON y una interfaz HTML/Thymeleaf para administrar productos de forma sencilla. El proyecto ofrece CRUD completo, persistencia en una base H2 en memoria y pruebas automatizadas listas para ejecutarse.

## Características principales

- ✅ **CRUD completo** para productos (crear, listar, actualizar y eliminar) disponible tanto en la UI como en la API REST bajo `/api/products`.
- 🌐 **API REST** con respuestas JSON, validaciones y manejo centralizado de errores.
- 🖥️ **Interfaz web (Thymeleaf + Bootstrap)** bajo `/products`, lista para usarse en portafolios o demos.
- 🗄️ **Base de datos H2 en memoria**, accesible mediante la consola `/h2-console`.
- 🧪 **Pruebas automatizadas** para servicios, endpoints REST y controladores de vistas.

## Tecnologías utilizadas

| Tecnología | Descripción |
| --- | --- |
| ![Java](https://img.shields.io/badge/Java-25-007396?logo=openjdk&logoColor=white) | Lenguaje principal del backend |
| ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white) | Framework para la API REST y MVC |
| ![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36?logo=apachemaven&logoColor=white) | Gestión de dependencias y tareas |
| ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Templating-005F0F?logo=thymeleaf&logoColor=white) | Motor de plantillas para la UI |
| ![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?logo=bootstrap&logoColor=white) | Estilos responsivos para la UI |
| ![H2](https://img.shields.io/badge/H2-Database-4479A1?logo=h2&logoColor=white) | Base de datos en memoria |

## Requisitos previos

- Java 25
- Maven 3.9+
- Git

## Instalación y ejecución

```bash
git clone https://github.com/your-user/inventory_api.git
cd inventory_api
mvn spring-boot:run
```

> También puedes usar el wrapper del proyecto `./mvnw spring-boot:run` en lugar de Maven global.

## URLs de la aplicación

| Ruta | Descripción |
| --- | --- |
| `/` | Redirección automática hacia `/products` |
| `/products` | UI HTML con Thymeleaf y Bootstrap |
| `/api/products` | API REST JSON con CRUD completo |
| `/h2-console` | Consola web para la base de datos H2 |

## Estructura del proyecto

```
.
├── pom.xml
├── src
│   ├── main
│   │   ├── java/com/bahs/inventory_api
│   │   │   ├── controller
│   │   │   ├── dto
│   │   │   ├── entity
│   │   │   ├── exception
│   │   │   ├── repository
│   │   │   └── service
│   │   └── resources
│   │       ├── templates
│   │       └── application.properties
│   └── test/java/com/bahs/inventory_api
└── README.md
```

## REST API (CRUD)

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/api/products` | Obtiene todos los productos |
| `GET` | `/api/products/{id}` | Obtiene un producto por ID |
| `POST` | `/api/products` | Crea un producto (JSON) |
| `PUT` | `/api/products/{id}` | Actualiza un producto existente |
| `DELETE` | `/api/products/{id}` | Elimina un producto |

## Interfaz de usuario

La UI bajo `/products` ofrece:

- Listado con tarjetas responsive
- Formularios de creación y edición con validaciones
- Detalle de producto
- Flujo de eliminación por botón

Thymeleaf compone las vistas y Bootstrap 5 asegura el estilo responsivo.

## Base de datos H2

- Base en memoria creada al arrancar la aplicación
- Consola disponible en `/h2-console`
- URL por defecto: `jdbc:h2:mem:testdb`
- Usuario: `sa` (sin contraseña)

## Pruebas automatizadas

- **Service tests** (`ProductServiceTest`): validan la lógica de negocio y manejo de excepciones.
- **API tests** (`ProductControllerTest`): prueban todos los endpoints bajo `/api/products` usando MockMvc y JSON.
- **UI tests** (`ProductViewControllerTest`): validan el flujo de la interfaz Thymeleaf y sus modelos.

Ejecuta todas las pruebas con:

```bash
mvn test
```

## Capturas de pantalla

Incluye tus capturas para enriquecer el portafolio:

- ![Listado de productos](docs/ui-list.png)
- ![Detalle de producto](docs/ui-detail.png)
- ![Formulario de producto](docs/ui-form.png)

## Contribuciones

¡Las contribuciones son bienvenidas! Abre un issue o envía un pull request siguiendo las mejores prácticas de Git.

