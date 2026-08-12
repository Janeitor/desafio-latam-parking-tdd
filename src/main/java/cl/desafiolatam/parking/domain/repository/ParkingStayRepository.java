package cl.desafiolatam.parking.domain.repository;

import java.util.Optional;

import cl.desafiolatam.parking.domain.model.LicensePlate;

import cl.desafiolatam.parking.domain.model.ParkingStay;

public interface ParkingStayRepository {

    Optional<ParkingStay> findActiveByLicensePlate(LicensePlate licensePlate);

    ParkingStay save(ParkingStay parkingStay);
}