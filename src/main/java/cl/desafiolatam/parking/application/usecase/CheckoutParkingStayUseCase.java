package cl.desafiolatam.parking.application.usecase;

import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.ActiveParkingStayNotFoundException;
import cl.desafiolatam.parking.domain.model.LicensePlate;
import cl.desafiolatam.parking.domain.model.ParkingStay;
import cl.desafiolatam.parking.domain.repository.ParkingStayRepository;
import cl.desafiolatam.parking.domain.service.ParkingFeeCalculator;

public class CheckoutParkingStayUseCase {

    private final ParkingStayRepository repository;
    private final ParkingFeeCalculator feeCalculator;

    public CheckoutParkingStayUseCase(
            ParkingStayRepository repository,
            ParkingFeeCalculator feeCalculator) {
        this.repository = repository;
        this.feeCalculator = feeCalculator;
    }

    public int execute(
            LicensePlate licensePlate,
            LocalDateTime exitTime) {
        ParkingStay parkingStay = repository
                .findActiveByLicensePlate(licensePlate)
                .orElseThrow(
                        ActiveParkingStayNotFoundException::new);

        parkingStay.close(exitTime);

        long durationInMinutes =
                parkingStay.calculateDurationInMinutes(exitTime);

        int fee =
                feeCalculator.calculateFee(durationInMinutes);

        repository.save(parkingStay);

        return fee;
    }
}