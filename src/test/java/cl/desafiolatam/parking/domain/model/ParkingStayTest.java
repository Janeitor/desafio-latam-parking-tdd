package cl.desafiolatam.parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.InvalidExitTimeException;

import org.junit.jupiter.api.Test;

class ParkingStayTest {

    @Test
    void shouldCalculateDurationInMinutes() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2026, 7, 21, 11, 30);
        ParkingStay parkingStay = new ParkingStay("ABCD12", entryTime);

        // Act
        long durationInMinutes = parkingStay.calculateDurationInMinutes(exitTime);

        // Assert
        assertEquals(90L, durationInMinutes);
    }

    @Test
    void shouldRejectExitTimeBeforeEntryTime() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2026, 7, 21, 9, 59);
        ParkingStay parkingStay = new ParkingStay("ABCD12", entryTime);

        // Act
        InvalidExitTimeException exception = assertThrows(
                InvalidExitTimeException.class,
                () -> parkingStay.calculateDurationInMinutes(exitTime));

        // Assert
        assertEquals("Exit time cannot be before entry time", exception.getMessage());
    }

    @Test
    void shouldStoreExitTimeWhenClosingParkingStay() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2026, 7, 21, 11, 30);
        ParkingStay parkingStay = new ParkingStay("ABCD12", entryTime);

        // Act
        parkingStay.close(exitTime);

        // Assert
        assertEquals(exitTime, parkingStay.getExitTime());
    }

    @Test
    void shouldRejectExitTimeBeforeEntryWhenClosingParkingStay() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2026, 7, 21, 9, 59);
        ParkingStay parkingStay = new ParkingStay("ABCD12", entryTime);

        // Act
        InvalidExitTimeException exception = assertThrows(
                InvalidExitTimeException.class,
                () -> parkingStay.close(exitTime));

        // Assert
        assertEquals("Exit time cannot be before entry time", exception.getMessage());
    }
}
