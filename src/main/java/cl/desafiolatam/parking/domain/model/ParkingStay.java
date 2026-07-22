package cl.desafiolatam.parking.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.InvalidExitTimeException;

public class ParkingStay {

    private final String licensePlate;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public ParkingStay(LocalDateTime entryTime) {
        this(null, entryTime);
    }

    public ParkingStay(
            String licensePlate,
            LocalDateTime entryTime) {
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void close(LocalDateTime exitTime) {
        validateExitTime(exitTime);
        this.exitTime = exitTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public long calculateDurationInMinutes(LocalDateTime exitTime) {
        validateExitTime(exitTime);
        return Duration.between(entryTime, exitTime).toMinutes();
    }

    private void validateExitTime(LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new InvalidExitTimeException();
        }
    }
}
