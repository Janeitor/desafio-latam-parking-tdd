package cl.desafiolatam.parking.domain.model;

import java.util.UUID;

public record ParkingStayId(UUID value) {

    public ParkingStayId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Parking stay id cannot be null");
        }
    }
}