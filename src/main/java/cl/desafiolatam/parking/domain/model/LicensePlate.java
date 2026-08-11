package cl.desafiolatam.parking.domain.model;

public record LicensePlate(String value) {

    public LicensePlate {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "License plate cannot be blank");
        }
    }
}