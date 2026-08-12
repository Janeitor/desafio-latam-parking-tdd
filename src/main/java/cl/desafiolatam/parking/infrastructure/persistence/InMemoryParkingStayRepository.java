package cl.desafiolatam.parking.infrastructure.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import cl.desafiolatam.parking.domain.model.LicensePlate;
import cl.desafiolatam.parking.domain.model.ParkingStay;
import cl.desafiolatam.parking.domain.model.ParkingStayId;
import cl.desafiolatam.parking.domain.repository.ParkingStayRepository;

public class InMemoryParkingStayRepository
        implements ParkingStayRepository {

    private final Map<ParkingStayId, ParkingStay> storage =
            new ConcurrentHashMap<>();

    @Override
    public ParkingStay save(ParkingStay parkingStay) {
        storage.put(parkingStay.getId(), parkingStay);
        return parkingStay;
    }

    @Override
    public Optional<ParkingStay> findActiveByLicensePlate(
            LicensePlate licensePlate) {
        return storage.values()
                .stream()
                .filter(parkingStay ->
                        parkingStay.getLicensePlate()
                                .equals(licensePlate))
                .filter(parkingStay ->
                        parkingStay.getExitTime() == null)
                .findFirst();
    }
}