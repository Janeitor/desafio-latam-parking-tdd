# Requisitos del proyecto

## Alcance del Hito 1

El proyecto modela el dominio puro de un estacionamiento y cubre dos casos de
uso relacionados:

1. Registrar la entrada de un vehículo al estacionamiento.
2. Registrar su salida y calcular el monto a pagar según la duración de la
   estadía.

El Hito 1 no incluye interfaz web, API REST, menú de consola, base de datos
real, autenticación ni procesamiento de pagos.

## Política tarifaria

La tarifa se calcula mediante el comportamiento planificado:

```java
public int calculateFee(long durationInMinutes)
```

### Unidad y moneda

- `durationInMinutes` representa minutos completos.
- El monto se expresa en pesos chilenos (CLP).
- El resultado es un `int` sin decimales.

### Tramos de cobro

- Desde 0 hasta 15 minutos inclusive, la estadía es gratuita.
- Desde 16 hasta 60 minutos inclusive, el valor es $1.000.
- Después de 60 minutos, se agregan $500 por cada hora adicional iniciada.
- Toda fracción de una hora adicional se cobra como una hora completa.
- El cobro máximo por la estadía completa es $5.000.
- La tarifa no se reinicia cada 24 horas.

### Duraciones inválidas

- Una duración de cero minutos es válida.
- Una duración negativa debe lanzar `InvalidParkingDurationException`.
- El mensaje interno planificado para la excepción es
  `Parking duration cannot be negative`.

### Ejemplos de aceptación

| Duración | Resultado esperado |
| ---: | ---: |
| 0 minutos | $0 |
| 15 minutos | $0 |
| 16 minutos | $1.000 |
| 60 minutos | $1.000 |
| 61 minutos | $1.500 |
| 120 minutos | $1.500 |
| 121 minutos | $2.000 |
| 180 minutos | $2.000 |
| 480 minutos | $4.500 |
| 481 minutos | $5.000 |
| 541 minutos | $5.000 |
| -1 minuto | `InvalidParkingDurationException` |

## Exclusiones de la tarifa

Dentro del Hito 1 no existen:

- Descuentos.
- Tarifas nocturnas.
- Diferencias por tipo de vehículo.
- Reinicios de tarifa cada 24 horas.

## Metodología de desarrollo

Cada comportamiento debe emerger mediante un ciclo pequeño de TDD:

1. RED: escribir una prueba de un solo comportamiento y verificar su fallo.
2. GREEN: implementar el codigo minimo para que la prueba pase.
3. REFACTOR: mejorar la estructura solo cuando exista una oportunidad concreta,
   manteniendo todas las pruebas en verde.
