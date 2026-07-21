package cl.desafiolatam.parking.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingStay {

    private final LocalDateTime entryTime;

    public ParkingStay(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public long calculateDurationInMinutes(LocalDateTime exitTime) {
        return Duration.between(entryTime, exitTime).toMinutes();
    }
}
