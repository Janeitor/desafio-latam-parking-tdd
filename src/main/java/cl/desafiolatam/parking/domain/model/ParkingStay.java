package cl.desafiolatam.parking.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.InvalidExitTimeException;

public class ParkingStay {

    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public ParkingStay(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public void close(LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new InvalidExitTimeException();
        }
        this.exitTime = exitTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public long calculateDurationInMinutes(LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new InvalidExitTimeException();
        }
        return Duration.between(entryTime, exitTime).toMinutes();
    }
}
