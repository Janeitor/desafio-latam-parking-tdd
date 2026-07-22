package cl.desafiolatam.parking.domain.exception;

public class InvalidParkingDurationException extends RuntimeException {

    public InvalidParkingDurationException() {
        super("Parking duration cannot be negative");
    }
}