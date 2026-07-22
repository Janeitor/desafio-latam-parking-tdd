package cl.desafiolatam.parking.domain.service;

import cl.desafiolatam.parking.domain.exception.InvalidParkingDurationException;

public class ParkingFeeCalculator {

    private static final long FREE_PERIOD_LIMIT_MINUTES = 15;
    private static final long BASE_FEE_LIMIT_MINUTES = 60;
    private static final long MAXIMUM_FEE_START_MINUTES = 481;
    private static final long MINUTES_PER_HOUR = 60;

    private static final int BASE_FEE = 1_000;
    private static final int ADDITIONAL_HOUR_FEE = 500;
    private static final int MAXIMUM_FEE = 5_000;

    public int calculateFee(long durationInMinutes) {
        if (durationInMinutes < 0) {
            throw new InvalidParkingDurationException();
        }

        if (durationInMinutes <= FREE_PERIOD_LIMIT_MINUTES) {
            return 0;
        }

        if (durationInMinutes <= BASE_FEE_LIMIT_MINUTES) {
            return BASE_FEE;
        }

        if (durationInMinutes >= MAXIMUM_FEE_START_MINUTES) {
            return MAXIMUM_FEE;
        }

        long additionalHours = (durationInMinutes - 1) / MINUTES_PER_HOUR;
        return BASE_FEE + (int) (additionalHours * ADDITIONAL_HOUR_FEE);
    }
}