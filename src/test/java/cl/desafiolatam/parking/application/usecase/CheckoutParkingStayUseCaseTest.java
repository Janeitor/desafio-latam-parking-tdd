package cl.desafiolatam.parking.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import cl.desafiolatam.parking.domain.exception.ActiveParkingStayNotFoundException;
import cl.desafiolatam.parking.domain.model.LicensePlate;
import cl.desafiolatam.parking.domain.model.ParkingStay;
import cl.desafiolatam.parking.domain.port.ParkingStayRepository;
import cl.desafiolatam.parking.domain.service.ParkingFeeCalculator;

class CheckoutParkingStayUseCaseTest {

    @Test
    void shouldCloseSaveAndReturnFeeWhenCheckingOutActiveStay() {
        // Arrange
        ParkingStayRepository repository =
                mock(ParkingStayRepository.class);
        ParkingFeeCalculator feeCalculator =
                mock(ParkingFeeCalculator.class);
        CheckoutParkingStayUseCase useCase =
                new CheckoutParkingStayUseCase(
                        repository,
                        feeCalculator);

        LicensePlate licensePlate = new LicensePlate("ABCD12");
        LocalDateTime entryTime =
                LocalDateTime.of(2026, 8, 11, 10, 0);
        LocalDateTime exitTime =
                LocalDateTime.of(2026, 8, 11, 11, 30);
        ParkingStay activeStay =
                new ParkingStay(licensePlate, entryTime);

        when(repository.findActiveByLicensePlate(licensePlate))
                .thenReturn(Optional.of(activeStay));
        when(feeCalculator.calculateFee(90L))
                .thenReturn(1_500);
        when(repository.save(activeStay))
                .thenReturn(activeStay);

        // Act
        int fee = useCase.execute(licensePlate, exitTime);

        // Assert
        assertEquals(1_500, fee);
        assertEquals(exitTime, activeStay.getExitTime());
        verify(repository).findActiveByLicensePlate(licensePlate);
        verify(feeCalculator).calculateFee(90L);
        verify(repository).save(activeStay);
    }

    @Test
    void shouldRejectCheckoutWhenActiveParkingStayDoesNotExist() {
        // Arrange
        ParkingStayRepository repository =
                mock(ParkingStayRepository.class);
        ParkingFeeCalculator feeCalculator =
                mock(ParkingFeeCalculator.class);
        CheckoutParkingStayUseCase useCase =
                new CheckoutParkingStayUseCase(
                        repository,
                        feeCalculator);

        LicensePlate licensePlate = new LicensePlate("ABCD12");
        LocalDateTime exitTime =
                LocalDateTime.of(2026, 8, 11, 11, 30);

        when(repository.findActiveByLicensePlate(licensePlate))
                .thenReturn(Optional.empty());

        // Act
        ActiveParkingStayNotFoundException exception = assertThrows(
                ActiveParkingStayNotFoundException.class,
                () -> useCase.execute(licensePlate, exitTime));

        // Assert
        assertEquals(
                "Active parking stay not found",
                exception.getMessage());
        verify(repository).findActiveByLicensePlate(licensePlate);
        verify(repository, never()).save(
                org.mockito.ArgumentMatchers.any(ParkingStay.class));
        verifyNoInteractions(feeCalculator);
    }
}