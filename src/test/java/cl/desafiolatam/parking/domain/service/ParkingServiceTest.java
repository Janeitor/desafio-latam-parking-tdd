package cl.desafiolatam.parking.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import cl.desafiolatam.parking.domain.exception.VehicleAlreadyParkedException;
import cl.desafiolatam.parking.domain.model.ParkingStay;
import cl.desafiolatam.parking.domain.port.ParkingStayRepository;

class ParkingServiceTest {

    @Test
    void shouldSaveAndReturnParkingStayWhenVehicleIsNotParked() {
        // Arrange
        ParkingStayRepository repository = mock(ParkingStayRepository.class);
        ParkingFeeCalculator feeCalculator = mock(ParkingFeeCalculator.class);
        ParkingService service = new ParkingService(repository, feeCalculator);
        String licensePlate = "ABCD12";
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 22, 10, 0);
        ParkingStay savedStay = new ParkingStay(entryTime);

        when(repository.findActiveByLicensePlate(licensePlate))
                .thenReturn(Optional.empty());
        when(repository.save(any(ParkingStay.class)))
                .thenReturn(savedStay);

        // Act
        ParkingStay result = service.registerEntry(licensePlate, entryTime);

        // Assert
        assertSame(savedStay, result);
        verify(repository).findActiveByLicensePlate(licensePlate);
        verify(repository).save(any(ParkingStay.class));
    }

    @Test
    void shouldStoreLicensePlateInNewParkingStay() {
        // Arrange
        ParkingStayRepository repository = mock(ParkingStayRepository.class);
        ParkingFeeCalculator feeCalculator = mock(ParkingFeeCalculator.class);
        ParkingService service = new ParkingService(repository, feeCalculator);
        String licensePlate = "ABCD12";
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 22, 10, 0);
        ParkingStay savedStay = new ParkingStay(entryTime);
        ArgumentCaptor<ParkingStay> parkingStayCaptor =
                ArgumentCaptor.forClass(ParkingStay.class);

        when(repository.findActiveByLicensePlate(licensePlate))
                .thenReturn(Optional.empty());
        when(repository.save(any(ParkingStay.class)))
                .thenReturn(savedStay);

        // Act
        service.registerEntry(licensePlate, entryTime);

        // Assert
        verify(repository).save(parkingStayCaptor.capture());
        assertEquals(
                licensePlate,
                parkingStayCaptor.getValue().getLicensePlate());
    }

    @Test
    void shouldRejectEntryWhenVehicleIsAlreadyParked() {
        // Arrange
        ParkingStayRepository repository = mock(ParkingStayRepository.class);
        ParkingFeeCalculator feeCalculator = mock(ParkingFeeCalculator.class);
        ParkingService service = new ParkingService(repository, feeCalculator);
        String licensePlate = "ABCD12";
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 22, 10, 0);
        ParkingStay activeStay = new ParkingStay(entryTime);

        when(repository.findActiveByLicensePlate(licensePlate))
                .thenReturn(Optional.of(activeStay));

        // Act
        VehicleAlreadyParkedException exception = assertThrows(
                VehicleAlreadyParkedException.class,
                () -> service.registerEntry(licensePlate, entryTime));

        // Assert
        assertEquals("Vehicle is already parked", exception.getMessage());
        verify(repository).findActiveByLicensePlate(licensePlate);
        verify(repository, never()).save(any(ParkingStay.class));
    }

    @Test
    void shouldCloseSaveAndReturnFeeWhenCheckingOutActiveStay() {
        // Arrange
        ParkingStayRepository repository = mock(ParkingStayRepository.class);
        ParkingFeeCalculator feeCalculator = mock(ParkingFeeCalculator.class);
        ParkingService service = new ParkingService(repository, feeCalculator);
        String licensePlate = "ABCD12";
        LocalDateTime entryTime = LocalDateTime.of(2026, 7, 22, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2026, 7, 22, 11, 30);
        ParkingStay activeStay = new ParkingStay(licensePlate, entryTime);

        when(repository.findActiveByLicensePlate(licensePlate))
                .thenReturn(Optional.of(activeStay));
        when(feeCalculator.calculateFee(90L))
                .thenReturn(1_500);
        when(repository.save(activeStay))
                .thenReturn(activeStay);

        // Act
        int fee = service.checkout(licensePlate, exitTime);

        // Assert
        assertEquals(1_500, fee);
        assertEquals(exitTime, activeStay.getExitTime());
        verify(repository).findActiveByLicensePlate(licensePlate);
        verify(feeCalculator).calculateFee(90L);
        verify(repository).save(activeStay);
    }
}