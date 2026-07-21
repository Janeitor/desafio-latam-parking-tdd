package cl.desafiolatam.parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ParkingStayTest {

    @Test
    void shouldCalculateDurationInMinutes() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2026, 7, 21, 11, 30);
        ParkingStay parkingStay = new ParkingStay(entryTime);

        // Act
        long durationInMinutes = parkingStay.calculateDurationInMinutes(exitTime);

        // Assert
        assertEquals(90L, durationInMinutes);
    }
}