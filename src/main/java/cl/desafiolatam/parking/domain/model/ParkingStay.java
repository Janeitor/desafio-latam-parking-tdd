package cl.desafiolatam.parking.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

import cl.desafiolatam.parking.domain.exception.InvalidExitTimeException;

public class ParkingStay {

    private final LocalDateTime entryTime;

    public ParkingStay(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public long calculateDurationInMinutes(LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new InvalidExitTimeException();
        }
        return Duration.between(entryTime, exitTime).toMinutes();
    }
}
