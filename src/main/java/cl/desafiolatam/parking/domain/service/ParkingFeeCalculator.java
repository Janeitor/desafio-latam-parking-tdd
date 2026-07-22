package cl.desafiolatam.parking.domain.service;

public class ParkingFeeCalculator {

    public int calculateFee(long durationInMinutes) {
        if (durationInMinutes <= 15) {
            return 0;
        }

        if (durationInMinutes <= 60) {
            return 1_000;
        }

        return 1_500;
    }
}
