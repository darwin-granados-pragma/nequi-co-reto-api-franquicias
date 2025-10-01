# **api-franchise**

**Descripción:**  
Este proyecto es una API desarrollada con Spring Webflux que permite gestionar una red de franquicias, sigue buenas prácticas de desarrollo como clean code, SOLID, kiss y dry. Además, se ha logrado una cobertura de pruebas del 100% con Jacoco en la logica de negocio.

---

## **Herramientas Utilizadas**

- **Lenguaje:** Java 17
- **Framework:** Spring
- **Gestor de Dependencias:** Gradle (versión 8.14.3)
- **Base de Datos:** PostgreSQL
- **Contenedorización:** Docker
- **Documentación API:** Swagger UI ([http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html))
- **Pruebas Unitarias:** JUnit 5, Mockito
- **Cobertura de la logica de negocio:** Jacoco (100% de cobertura)

---

## **Requisitos Previos**

Para ejecutar este proyecto, asegúrate de tener instalado lo siguiente:

1. **Java 17**  
   - Descarga e instala desde [OpenJDK](https://openjdk.org/).

2. **Gradle 8.14.3**
   - Descarga e instala desde [Gradle](https://gradle.org/install/).

3. **Crear base de datos en postgreSQL**

---

## **Configuración Inicial**

1. **Clona el Repositorio**
    - git clone https://github.com/darwin-granados-pragma/nequi-co-reto-api-franquicias.git
    - cd nequi-co-reto-api-franquicias

2. **Configura el archivo .env**
    - Copia el contenido archivo .env.example a .env:
        - cp .env.example .env
    - Edita el archivo .env con las variables necesarias para el entorno

3. **Configura el BootRun y despliega**
   - Agrega en "Environment variables" la ubicación de tu .env
   - Inicia la aplicación

4. **Verifica la Documentación de OpenAPI**
   Una vez que el proyecto esté en funcionamiento, accede a la documentación de la API en:
    - http://localhost:8080/swagger-ui/index.html

---

## **Cobertura de Pruebas**
Se ha implementado Jacoco para medir la cobertura de pruebas. El informe indica una cobertura del 100%.
- Para generar el informe localmente:
    - gradle :usecase:jacocoTestReport
- El informe estará disponible en:
    - domain/usecase/build/reports/jacocoHtml/index.html

---

## Proyecto Base Implementando Clean Architecture

### Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyecto y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture - Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

## Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

### Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

### Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

### Infrastructure

#### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

#### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

#### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

### Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de uso (UseCases) de forma automática, inyectando en estas instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el unico módulo del proyecto donde encontraremos la función public static void main(String[] args).

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**
