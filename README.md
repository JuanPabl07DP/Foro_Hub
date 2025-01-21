# ForoHub

ForoHub es una API REST desarrollada con Spring Framework para gestionar un foro de discusión. Los usuarios pueden crear, leer, actualizar y eliminar tópicos (CRUD). La API está diseñada siguiendo las mejores prácticas del modelo REST, e incluye validaciones, autenticación/autorización y una base de datos relacional para la persistencia de la información.

## Características

- 📝 **Crear un nuevo tópico**: Permite a los usuarios crear un nuevo tópico en el foro.
- 📖 **Mostrar todos los tópicos creados**: Lista todos los tópicos existentes en el foro.
- 🔍 **Mostrar un tópico específico**: Obtiene los detalles de un tópico específico mediante su ID.
- ✏️ **Actualizar un tópico**: Permite a los usuarios actualizar la información de un tópico existente.
- 🗑️ **Eliminar un tópico**: Permite a los usuarios eliminar un tópico del foro.
- ✔️ **Validaciones de las reglas de negocio**: Asegura la integridad de los datos mediante validaciones.
- 🔒 **Autenticación y autorización de usuarios**: Protege los endpoints mediante autenticación y autorización.

## Tecnologías Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Hibernate
- H2 Database (para desarrollo y pruebas)
- MySQL (para producción)
- Maven

## Instalación y Ejecución

### Prerrequisitos

- JDK 17
- Maven
- MySQL (para entorno de producción)
