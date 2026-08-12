package cl.desafiolatam.parking.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cl.desafiolatam.parking.domain.model.LicensePlate;
import cl.desafiolatam.parking.domain.model.ParkingStay;

class InMemoryParkingStayRepositoryTest {

    @Test
    void shouldSaveAndFindActiveParkingStayByLicensePlate() {
        // Arrange
        InMemoryParkingStayRepository repository =
                new InMemoryParkingStayRepository();
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        ParkingStay parkingStay = new ParkingStay(
                licensePlate,
                LocalDateTime.of(2026, 8, 12, 10, 0));

        // Act
        repository.save(parkingStay);
        Optional<ParkingStay> result =
                repository.findActiveByLicensePlate(licensePlate);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(parkingStay, result.get());
    }

    @Test
    void shouldReturnEmptyWhenLicensePlateIsNotFound() {
        // Arrange
        InMemoryParkingStayRepository repository =
                new InMemoryParkingStayRepository();
        LicensePlate licensePlate = new LicensePlate("ABCD12");

        // Act
        Optional<ParkingStay> result =
                repository.findActiveByLicensePlate(licensePlate);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotFindClosedParkingStayAsActive() {
        // Arrange
        InMemoryParkingStayRepository repository =
                new InMemoryParkingStayRepository();
        LicensePlate licensePlate = new LicensePlate("ABCD12");
        ParkingStay parkingStay = new ParkingStay(
                licensePlate,
                LocalDateTime.of(2026, 8, 12, 10, 0));

        parkingStay.close(
                LocalDateTime.of(2026, 8, 12, 11, 0));
        repository.save(parkingStay);

        // Act
        Optional<ParkingStay> result =
                repository.findActiveByLicensePlate(licensePlate);

        // Assert
        assertTrue(result.isEmpty());
    }
}