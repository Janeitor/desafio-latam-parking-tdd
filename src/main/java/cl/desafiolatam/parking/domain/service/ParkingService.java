package cl.desafiolatam.parking.domain.service;

import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.VehicleAlreadyParkedException;
import cl.desafiolatam.parking.domain.model.ParkingStay;
import cl.desafiolatam.parking.domain.port.ParkingStayRepository;

public class ParkingService {

    private final ParkingStayRepository repository;
    private final ParkingFeeCalculator feeCalculator;

    public ParkingService(
            ParkingStayRepository repository,
            ParkingFeeCalculator feeCalculator) {
        this.repository = repository;
        this.feeCalculator = feeCalculator;
    }

    public ParkingStay registerEntry(
            String licensePlate,
            LocalDateTime entryTime) {
        if (repository.findActiveByLicensePlate(licensePlate).isPresent()) {
            throw new VehicleAlreadyParkedException();
        }

        ParkingStay parkingStay = new ParkingStay(licensePlate, entryTime);
        return repository.save(parkingStay);
    }

    public int checkout(
            String licensePlate,
            LocalDateTime exitTime) {
        ParkingStay parkingStay = repository
                .findActiveByLicensePlate(licensePlate)
                .orElseThrow();

        parkingStay.close(exitTime);

        long durationInMinutes =
                parkingStay.calculateDurationInMinutes(exitTime);
        int fee = feeCalculator.calculateFee(durationInMinutes);

        repository.save(parkingStay);
        return fee;
    }
}