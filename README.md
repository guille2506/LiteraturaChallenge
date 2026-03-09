# LiteraturaChallenge

# 📚 BookExplorer

Aplicación de consola desarrollada en **Java 21** utilizando **Spring Boot** y **Maven** que permite consultar, almacenar y gestionar información de libros y autores obtenidos desde la API pública **Gutendex**.
Los datos recuperados se almacenan localmente en una base de datos **PostgreSQL** para poder realizar consultas posteriores.

---

# 🛠️ Tecnologías utilizadas

* Java 21
* Spring Boot
* Maven
* Spring Data JPA / Hibernate
* PostgreSQL
* Jackson Databind (para procesamiento de JSON)
* API Gutendex (https://gutendex.com)

---

# 📋 Funcionalidades del sistema

Excepto la primera opción, todas las demás utilizan los datos almacenados previamente en la base de datos local.

| Opción | Acción                                                   |
| ------ | -------------------------------------------------------- |
| 1      | Buscar un libro en la API y agregarlo a la base de datos |
| 2      | Consultar un libro almacenado mediante su título         |
| 3      | Mostrar todos los libros registrados                     |
| 4      | Listar todos los autores guardados                       |
| 5      | Buscar autores que estaban vivos en un año específico    |
| 6      | Mostrar libros según el idioma                           |
| 7      | Visualizar el Top 10 de libros más descargados           |
| 0      | Finalizar la aplicación                                  |

---

# ⚙️ Configuración del proyecto

## 1. Requisitos

Antes de ejecutar el proyecto debes tener instalado:

* **Java 21**
* **PostgreSQL**
* **Maven**

Además debes crear una base de datos en PostgreSQL llamada:

```
literalura
```

---

## 2. Variables de entorno

Para evitar exponer credenciales directamente en el código, la aplicación utiliza variables de entorno.

| Variable    | Descripción                                             |
| ----------- | ------------------------------------------------------- |
| DB_HOST     | Dirección del servidor de base de datos (ej: localhost) |
| DB_USER     | Usuario de PostgreSQL                                   |
| DB_PASSWORD | Contraseña del usuario                                  |

Estas variables pueden configurarse desde tu sistema operativo o directamente en tu **IDE**.

---

# ▶️ Ejecución de la aplicación

1. Clona el repositorio o descarga el proyecto.
2. Configura las variables de entorno con tus datos de PostgreSQL.
3. Ejecuta el proyecto usando **Maven** o desde tu **IDE**.
4. Utiliza el menú de la consola para interactuar con la aplicación.

---

# 📌 Consideraciones

* Si el autor del libro ya se encuentra registrado en la base de datos, el sistema reutiliza ese registro para evitar duplicados.
* Si un libro ya existe en la colección, la aplicación informa al usuario.
* Algunos libros pueden no tener idioma registrado y se almacenarán con valor `null`.
* La búsqueda por título no distingue entre mayúsculas y minúsculas.

---

# 📖 Fuente de datos

La información de los libros se obtiene desde la API pública:

https://gutendex.com
