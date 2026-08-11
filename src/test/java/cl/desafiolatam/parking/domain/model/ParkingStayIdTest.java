package cl.desafiolatam.parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class ParkingStayIdTest {

    @Test
    void shouldCreateParkingStayIdWithValidValue() {
        // Arrange
        UUID value = UUID.fromString(
                "20af45a2-0d6c-4dd0-95b8-a6e254de31bb");

        // Act
        ParkingStayId parkingStayId = new ParkingStayId(value);

        // Assert
        assertEquals(value, parkingStayId.value());
    }

    @Test
    void shouldRejectNullParkingStayId() {
        // Arrange
        UUID value = null;

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ParkingStayId(value));

        // Assert
        assertEquals(
                "Parking stay id cannot be null",
                exception.getMessage());
    }
}