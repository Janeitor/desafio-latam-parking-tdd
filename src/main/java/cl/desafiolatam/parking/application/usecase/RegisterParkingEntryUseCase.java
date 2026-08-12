package cl.desafiolatam.parking.application.usecase;

import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.VehicleAlreadyParkedException;
import cl.desafiolatam.parking.domain.model.LicensePlate;
import cl.desafiolatam.parking.domain.model.ParkingStay;
import cl.desafiolatam.parking.domain.port.ParkingStayRepository;

public class RegisterParkingEntryUseCase {

    private final ParkingStayRepository repository;

    public RegisterParkingEntryUseCase(
            ParkingStayRepository repository) {
        this.repository = repository;
    }

    public ParkingStay execute(
            LicensePlate licensePlate,
            LocalDateTime entryTime) {
        if (repository
                .findActiveByLicensePlate(licensePlate)
                .isPresent()) {
            throw new VehicleAlreadyParkedException();
        }

        ParkingStay parkingStay =
                new ParkingStay(licensePlate, entryTime);

        return repository.save(parkingStay);
    }
}