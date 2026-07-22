package cl.desafiolatam.parking.domain.service;

import cl.desafiolatam.parking.domain.exception.InvalidParkingDurationException;

public class ParkingFeeCalculator {

    public int calculateFee(long durationInMinutes) {
        if (durationInMinutes < 0) {
            throw new InvalidParkingDurationException();
        }

        if (durationInMinutes <= 15) {
            return 0;
        }

        if (durationInMinutes <= 60) {
            return 1_000;
        }

        if (durationInMinutes >= 481) {
            return 5_000;
        }

        long additionalHours = (durationInMinutes - 1) / 60;
        return 1_000 + (int) (additionalHours * 500);
    }
}
