# Lenguaje ubicuo y contexto delimitado

## Propósito

Este documento define el lenguaje compartido y los límites del
subdominio principal del backend de estacionamientos.

Los conceptos se describen en español para facilitar la comunicación
del negocio. Sus nombres equivalentes en inglés se utilizarán de forma
consistente en el código Java.

## Subdominio principal

### Gestión de Estadías de Estacionamiento

Nombre en inglés:

`Parking Stay Management`

Su propósito es administrar el ingreso y la salida de vehículos,
evitar estadías activas duplicadas para una misma patente y calcular
la tarifa correspondiente al tiempo permanecido en el
estacionamiento.

## Lenguaje ubicuo

### Vehículo — `Vehicle`

Medio de transporte que ingresa al estacionamiento.

Dentro del contexto actual, el vehículo se reconoce exclusivamente
mediante su patente. No se registran su marca, modelo, color,
propietario ni tipo.

### Patente — `LicensePlate`

Identificador utilizado para reconocer un vehículo y localizar su
estadía activa.

Dentro del alcance actual, dos patentes iguales representan el mismo
vehículo. La normalización y validación de su formato se analizarán
durante el modelado táctico.

### Estadía de estacionamiento — `ParkingStay`

Periodo durante el cual un vehículo permanece dentro del
estacionamiento.

Comienza con el ingreso del vehículo y termina cuando se registra su
salida. Contiene la patente, la fecha y hora de ingreso y,
opcionalmente, la fecha y hora de salida.

### Estadía activa — `ActiveParkingStay`

Estadía que posee una fecha y hora de ingreso, pero todavía no tiene
registrada una salida.

Una misma patente no puede tener más de una estadía activa
simultáneamente.

`ActiveParkingStay` representa un concepto del negocio y no implica
necesariamente la creación de una clase Java independiente.

### Ingreso — `Entry`

Acción que inicia una nueva estadía para un vehículo.

El ingreso debe rechazarse cuando la patente ya posee una estadía
activa.

El ingreso no representa una reserva anticipada.

### Salida — `Checkout`

Acción que finaliza una estadía activa mediante el registro de la
fecha y hora de salida.

Durante la salida se calcula la duración real de la estadía y la
tarifa correspondiente.

La salida debe rechazarse cuando no existe una estadía activa para la
patente indicada.

### Duración de la estadía — `ParkingDuration`

Cantidad de minutos completos transcurridos entre la fecha y hora de
ingreso y la fecha y hora de salida.

La duración nunca puede ser negativa.

### Tarifa de estacionamiento — `ParkingFee`

Monto expresado en pesos chilenos que corresponde a una estadía,
calculado a partir de su duración y de la política tarifaria vigente.

La tarifa representa un monto calculado. No representa un pago
procesado.

### Política tarifaria — `ParkingFeePolicy`

Conjunto de reglas utilizadas para calcular la tarifa de una estadía.

La política actual establece:

- Desde 0 hasta 15 minutos inclusive, la estadía es gratuita.
- Desde 16 hasta 60 minutos inclusive, la tarifa es de $1.000.
- Después de 60 minutos, se agregan $500 por cada hora adicional
  iniciada.
- La tarifa máxima de una estadía es de $5.000.
- Una duración negativa es inválida.

## Contexto delimitado

### Nombre

`Parking Stay Management`

### Responsabilidades incluidas

El contexto es responsable de:

- Registrar el ingreso de un vehículo.
- Identificar el vehículo mediante su patente.
- Evitar estadías activas duplicadas para una misma patente.
- Buscar una estadía activa.
- Registrar la salida de un vehículo.
- Validar que la salida no sea anterior al ingreso.
- Calcular la duración en minutos completos.
- Calcular la tarifa según la política vigente.
- Solicitar el almacenamiento de la estadía mediante un contrato.

### Responsabilidades excluidas

El contexto no es responsable de:

- Gestionar reservas anticipadas.
- Administrar la disponibilidad o capacidad del estacionamiento.
- Mantener un catálogo de estacionamientos.
- Procesar pagos.
- Emitir boletas o facturas.
- Administrar clientes o cuentas de usuario.
- Autenticar usuarios.
- Enviar notificaciones.
- Gestionar sensores o barreras físicas.
- Exponer una API REST.
- Administrar una base de datos real.
- Implementar una interfaz web.

### Inicio y término del contexto

El contexto comienza cuando se solicita registrar el ingreso de un
vehículo.

El contexto termina cuando la estadía se cierra y se calcula la tarifa
correspondiente.

El cálculo de la tarifa pertenece al contexto. El cobro o
procesamiento del pago queda fuera de sus límites.

## Reglas de consistencia reconocidas

- Una patente solo puede tener una estadía activa.
- Una estadía está activa mientras no tenga hora de salida.
- La hora de salida no puede ser anterior a la hora de ingreso.
- La duración se calcula utilizando minutos completos.
- Una duración negativa es inválida.
- La tarifa se calcula mediante la política tarifaria vigente.
- Cerrar una estadía no equivale a procesar su pago.

## Contextos relacionados

Los siguientes contextos pertenecen al negocio general de
estacionamientos, pero no se implementan dentro de este subdominio:

### Reservas

Administra solicitudes anticipadas para utilizar un estacionamiento
en una fecha futura.

### Disponibilidad

Administra la capacidad, ocupación y cantidad de espacios disponibles.

### Pagos

Procesa el cobro de una tarifa previamente calculada.

### Catálogo de estacionamientos

Administra nombres, direcciones, horarios, servicios y
características de los estacionamientos.

## Términos que no deben confundirse

- Una `Reservation` no es una `ParkingStay`.
- Una `ParkingStay` comienza con el ingreso real del vehículo.
- Un `Entry` no es una reserva anticipada.
- Un `Checkout` cierra una estadía, pero no procesa un pago.
- Un `ParkingFee` es un monto calculado, no una transacción.
- La ausencia de una estadía activa no representa disponibilidad de
  espacios.


## Aggregate Root

`ParkingStay` is the aggregate root of the Parking Stay Management context.

It protects the lifecycle and consistency of a parking stay by:

- Owning its unique `ParkingStayId`.
- Associating the stay with a valid `LicensePlate`.
- Controlling when the stay is closed.
- Rejecting exit times earlier than the entry time.
- Calculating the parking duration from its own state.

Rules that require information outside a single parking stay, such as detecting
another active stay for the same vehicle, are coordinated by a domain service
through the `ParkingStayRepository` contract.

Fee calculation remains outside the aggregate because it represents a pricing
policy that can change independently from the parking stay lifecycle.