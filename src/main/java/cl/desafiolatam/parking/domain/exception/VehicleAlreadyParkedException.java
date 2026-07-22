package cl.desafiolatam.parking.domain.exception;

public class VehicleAlreadyParkedException extends RuntimeException {

    public VehicleAlreadyParkedException() {
        super("Vehicle is already parked");
    }
}