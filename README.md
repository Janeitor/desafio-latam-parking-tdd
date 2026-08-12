# Parking Stay Management

Backend educativo desarrollado en Java puro para modelar la gestión de
estadías de vehículos en un estacionamiento. El proyecto comenzó como un
ejercicio de TDD y evolucionó hacia una arquitectura en capas inspirada en
Domain-Driven Design (DDD), Clean Architecture y Ports and Adapters.

El código se mantiene independiente de frameworks, interfaces web y bases de
datos reales para concentrar el aprendizaje en el dominio, los casos de uso,
la inversión de dependencias y las pruebas automatizadas.

## Alcance del dominio

El contexto delimitado principal es **Parking Stay Management** (Gestión de
Estadías de Estacionamiento). Actualmente permite:

1. Registrar el ingreso de un vehículo.
2. Impedir que una patente tenga más de una estadía activa.
3. Registrar la salida del vehículo.
4. Validar que la salida no sea anterior al ingreso.
5. Calcular la duración de la estadía.
6. Calcular la tarifa correspondiente.
7. Guardar y consultar estadías mediante un contrato de repositorio.

El lenguaje ubicuo, los límites del contexto y la definición del agregado se
encuentran en
[docs/ubiquitous-language.md](docs/ubiquitous-language.md). Las reglas de
negocio y el alcance original también están documentados en
[PROJECT.md](PROJECT.md).

## Arquitectura

El proyecto separa las responsabilidades en tres capas:

```text
application ──> domain <── infrastructure
```

- `domain`: contiene el modelo y las reglas centrales del negocio. No depende
  de las otras capas ni de frameworks tecnológicos.
- `application`: contiene casos de uso que coordinan el dominio y dependen de
  contratos definidos en él.
- `infrastructure`: contiene detalles intercambiables, como la implementación
  en memoria del repositorio.

La dirección de las dependencias protege el núcleo del negocio. Los casos de
uso no conocen la clase de persistencia concreta y la reciben a través de la
interfaz `ParkingStayRepository`, aplicando inyección por constructor.

## Modelo de dominio

- `ParkingStay`: entidad y Aggregate Root que controla el ciclo de vida de una
  estadía.
- `ParkingStayId`: Value Object inmutable que representa la identidad única de
  cada estadía.
- `LicensePlate`: Value Object inmutable y auto-validado que representa la
  patente del vehículo.
- `ParkingFeeCalculator`: servicio de dominio que encapsula la política
  tarifaria.
- `ParkingStayRepository`: contrato puro que define las operaciones de
  almacenamiento requeridas por la aplicación.
- Excepciones de dominio: representan violaciones concretas de las reglas de
  negocio.

La igualdad de `ParkingStay` se basa en `ParkingStayId`, no en sus demás
atributos. De este modo, un vehículo puede tener diferentes estadías
históricas, cada una con identidad propia.

## Casos de uso

- `RegisterParkingEntryUseCase`: comprueba que no exista una estadía activa
  para la patente, crea la nueva entidad y solicita su almacenamiento.
- `CheckoutParkingStayUseCase`: obtiene la estadía activa, registra la salida,
  calcula su duración y tarifa, y guarda el estado actualizado.

Ambos casos de uso dependen de `ParkingStayRepository` mediante inyección por
constructor. El caso de salida recibe además `ParkingFeeCalculator` para
mantener separada la coordinación de la política tarifaria.

## Persistencia en memoria

`InMemoryParkingStayRepository` implementa el contrato del dominio mediante un
`ConcurrentHashMap`. Esta implementación permite ejecutar y probar los casos
de uso sin una base de datos.

Las estadías se almacenan por `ParkingStayId`, porque una misma patente puede
tener múltiples visitas históricas. Una estadía se considera activa cuando su
hora de salida todavía no ha sido registrada.

La implementación en memoria es un adaptador educativo y no representa una
persistencia permanente: sus datos se pierden al finalizar el proceso.

## Estructura del proyecto

```text
desafio-latam-parking-tdd/
├── docs/
│   └── ubiquitous-language.md
├── src/
│   ├── main/java/cl/desafiolatam/parking/
│   │   ├── application/
│   │   │   └── usecase/
│   │   │       ├── CheckoutParkingStayUseCase.java
│   │   │       └── RegisterParkingEntryUseCase.java
│   │   ├── domain/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   │   ├── LicensePlate.java
│   │   │   │   ├── ParkingStay.java
│   │   │   │   └── ParkingStayId.java
│   │   │   ├── repository/
│   │   │   │   └── ParkingStayRepository.java
│   │   │   └── service/
│   │   │       └── ParkingFeeCalculator.java
│   │   └── infrastructure/
│   │       └── persistence/
│   │           └── InMemoryParkingStayRepository.java
│   └── test/java/cl/desafiolatam/parking/
│       ├── application/usecase/
│       ├── domain/
│       └── infrastructure/persistence/
├── AGENTS.md
├── PROJECT.md
├── README.md
└── pom.xml
```

El directorio `target/` contiene artefactos generados por Maven y el informe
de cobertura. Está excluido del repositorio mediante `.gitignore`.

## Tecnologías

- Java 25 LTS
- Maven 3.9.16
- JUnit 5
- Mockito Core
- JaCoCo

No se utilizan Spring, JPA ni otras dependencias de frameworks en el código de
producción.

## Requisitos

- JDK 25 LTS
- Maven 3.9 o superior

Comprueba las versiones instaladas con:

```bash
java --version
mvn --version
```

## Compilar el proyecto

Desde la raíz del repositorio:

```bash
mvn clean compile
```

## Ejecutar las pruebas

```bash
mvn clean test
```

La suite actual contiene pruebas unitarias del dominio, los casos de uso y la
implementación del repositorio en memoria. Las pruebas no utilizan red, base de
datos ni frameworks externos.

## Generar el informe de cobertura

```bash
mvn clean test jacoco:report
```

El informe HTML se genera en:

```text
target/site/jacoco/index.html
```

En PowerShell puede abrirse con:

```powershell
Start-Process target\site\jacoco\index.html
```

El estado actual alcanza 100 % de cobertura de líneas y ramas en los paquetes
de producción. El resultado debe verificarse nuevamente después de cada
cambio, generando el informe con Maven.

## Proceso de desarrollo

El proyecto utiliza ciclos pequeños de TDD:

1. **RED:** escribir una prueba y comprobar que falla por la razón esperada.
2. **GREEN:** agregar la implementación mínima necesaria para hacerla pasar.
3. **REFACTOR:** mejorar el diseño sin alterar el comportamiento y manteniendo
   las pruebas en verde.

Los commits del repositorio registran la evolución desde el dominio inicial
hacia Value Objects, identidad de entidad, casos de uso y persistencia en
memoria.

## Fuera del alcance actual

- API REST o controladores web.
- Integración con el frontend TypeScript/Vite.
- Spring Boot y Spring Data.
- JPA u otro ORM.
- PostgreSQL u otra base de datos real.
- Docker.
- Autenticación y autorización.
- Procesamiento de pagos.
- Reservas anticipadas y gestión de disponibilidad de espacios.

Estas capacidades pertenecen a etapas posteriores y no forman parte del
núcleo Java puro implementado hasta este hito.
