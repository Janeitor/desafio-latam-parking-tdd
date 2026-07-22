package cl.desafiolatam.parking.domain.service;

import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.model.ParkingStay;
import cl.desafiolatam.parking.domain.port.ParkingStayRepository;

public class ParkingService {

    private final ParkingStayRepository repository;

    public ParkingService(ParkingStayRepository repository) {
        this.repository = repository;
    }

    public ParkingStay registerEntry(
            String licensePlate,
            LocalDateTime entryTime) {
        repository.findActiveByLicensePlate(licensePlate);

        ParkingStay parkingStay = new ParkingStay(entryTime);
        return repository.save(parkingStay);
    }
}