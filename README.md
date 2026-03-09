# LiterAlura 📚

Aplicación de consola desarrollada en **Java + Spring Boot** que permite consultar libros desde la API pública **Gutendex**, almacenar la información en **PostgreSQL** y realizar diferentes consultas sobre libros y autores.

Este proyecto fue desarrollado como parte del challenge **LiterAlura** del programa **Oracle Next Education (ONE) + Alura Latam**.

---

# 🚀 Funcionalidades

La aplicación permite interactuar mediante un menú en consola:

1. Buscar libro por título
2. Listar libros registrados
3. Listar autores registrados
4. Listar autores vivos en un determinado año
5. Listar libros por idioma
6. Mostrar estadísticas de descargas
7. Top 10 libros más descargados
8. Buscar autor por nombre
9. Buscar autores por año de nacimiento

---

# 🧠 Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Jackson (JSON)
- Gutendex API
- Project Gutenberg Dataset

---

# 🏗 Arquitectura del proyecto

El proyecto sigue una arquitectura organizada por capas:


modelo/
repositorio/
servicio/
principal/


### modelo
Contiene las entidades del sistema:

- Autor
- Libro
- AutorApi
- LibroApi
- RespuestaLibros

### repositorio
Interfaces de acceso a datos con **Spring Data JPA**

- AutorRepository
- LibroRepository

### servicio
Contiene la lógica de integración con APIs externas.

- ConsumoAPI
- ConvierteDatos
- IConvierteDatos

### principal
Contiene el menú interactivo de la aplicación.

---

# 🔗 API utilizada

El proyecto consume la API pública:

https://gutendex.com/

Basada en el catálogo de **Project Gutenberg**, que contiene más de **70,000 libros de dominio público**.

---

# 🗄 Base de datos

Se utiliza **PostgreSQL** para almacenar:

- libros
- autores
- relación autor-libro

Relación implementada:

Autor 1 ---- N Libro

---

# ▶️ Ejecución del proyecto

### 1 Clonar el repositorio

```bash
git clone https://github.com/tu_usuario/literalura-springboot-api.git

2 Crear base de datos en PostgreSQL
literalura

3 Configurar application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

4 Ejecutar el proyecto

Ejecutar:

LiteraluraNuevoApplication.java

Aparecerá el menú en consola.
```

#📊 Ejemplo de funcionamiento
      
Menú principal

<img width="868" height="709" alt="image" src="https://github.com/user-attachments/assets/511c5737-acb3-4bdf-82c1-057a0505f2a4" />


Listar libros registrados

<img width="677" height="742" alt="image" src="https://github.com/user-attachments/assets/69c31aa8-dee5-42ad-ba10-cddfcaced1d1" />


Listar autores

<img width="534" height="737" alt="image" src="https://github.com/user-attachments/assets/8e183768-4f91-4530-b4af-19db48449236" />


Estadísticas de descargas

<img width="951" height="526" alt="image" src="https://github.com/user-attachments/assets/d29c1950-cc77-4206-9338-dc25da4b1155" />



Top 10 libros más descargados

<img width="1210" height="312" alt="image" src="https://github.com/user-attachments/assets/019e5be8-72f6-4a87-b8ce-07575a80885e" />



🧪 Libros recomendados para pruebas

Algunos títulos disponibles en Gutendex:

Pride and Prejudice

Emma

Frankenstein

Dracula

Moby Dick

The Odyssey

Don Quixote

Metamorphosis

The Adventures of Sherlock Holmes

Alice's Adventures in Wonderland

#👨‍💻 Autor

Cristian Figueroa

Proyecto desarrollado para el programa:

Oracle Next Education (ONE)
Alura Latam
