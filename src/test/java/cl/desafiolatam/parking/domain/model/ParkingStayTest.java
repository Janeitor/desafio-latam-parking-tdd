package cl.desafiolatam.parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.InvalidExitTimeException;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ParkingStayTest {

    @Test
    void shouldCalculateDurationInMinutes() {
        // Arrange
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2026, 7, 21, 11, 30);
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        ParkingStay parkingStay = new ParkingStay(licensePlate, entryTime);

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
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        ParkingStay parkingStay = new ParkingStay(licensePlate, entryTime);

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
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        ParkingStay parkingStay = new ParkingStay(licensePlate, entryTime);

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
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        ParkingStay parkingStay = new ParkingStay(licensePlate, entryTime);

        // Act
        InvalidExitTimeException exception = assertThrows(
                InvalidExitTimeException.class,
                () -> parkingStay.close(exitTime));

        // Assert
        assertEquals("Exit time cannot be before entry time", exception.getMessage());
    }

    @Test
    void shouldStoreLicensePlateValueObject() {
        // Arrange
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);

        // Act
        ParkingStay parkingStay = new ParkingStay(licensePlate, entryTime);

        // Assert
        assertEquals(licensePlate, parkingStay.getLicensePlate());
    }

    @Test
    void shouldStoreUniqueIdentity() {
        // Arrange
        ParkingStayId id = new ParkingStayId(
                UUID.fromString(
                        "20af45a2-0d6c-4dd0-95b8-a6e254de31bb"));
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);

        // Act
        ParkingStay parkingStay = new ParkingStay(
                id,
                licensePlate,
                entryTime);

        // Assert
        assertEquals(id, parkingStay.getId());
    }

    @Test
    void shouldGenerateDifferentIdentitiesForSeparateStaysOfSameVehicle() {
        // Arrange
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        LocalDateTime firstEntryTime = LocalDateTime.of(2026, 7, 21, 10, 0);
        LocalDateTime secondEntryTime = LocalDateTime.of(2026, 7, 22, 10, 0);

        // Act
        ParkingStay firstParkingStay = new ParkingStay(licensePlate, firstEntryTime);
        ParkingStay secondParkingStay = new ParkingStay(licensePlate, secondEntryTime);

        // Assert
        assertNotEquals(
                firstParkingStay.getId(),
                secondParkingStay.getId());
    }

    @Test
    void shouldConsiderParkingStaysWithSameIdAsSameEntity() {
        // Arrange
        ParkingStayId id = new ParkingStayId(
                UUID.fromString(
                        "20af45a2-0d6c-4dd0-95b8-a6e254de31bb"));
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 21, 10, 0);

        ParkingStay firstParkingStay = new ParkingStay(
                id,
                licensePlate,
                entryTime);
        ParkingStay secondParkingStay = new ParkingStay(
                id,
                licensePlate,
                entryTime);

        // Act
        boolean sameEntity = firstParkingStay.equals(secondParkingStay);

        // Assert
        assertEquals(true, sameEntity);
    }

}
