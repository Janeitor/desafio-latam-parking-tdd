package cl.desafiolatam.parking.domain.service;

public class ParkingFeeCalculator {

    public int calculateFee(long durationInMinutes) {
        if (durationInMinutes <= 15) {
            return 0;
        }

        if (durationInMinutes <= 60) {
            return 1_000;
        }

        long additionalHours = (durationInMinutes - 1) / 60;
        return 1_000 + (int) (additionalHours * 500);
    }
}
