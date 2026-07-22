package cl.desafiolatam.parking.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ParkingFeeCalculatorTest {

    @Test
    void shouldReturnZeroFeeForZeroMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(0L);

        // Assert
        assertEquals(0, fee);
    }

    @Test
    void shouldReturnZeroFeeForFifteenMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(15L);

        // Assert
        assertEquals(0, fee);
    }

    @Test
    void shouldReturnBaseFeeForSixteenMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(16L);

        // Assert
        assertEquals(1_000, fee);
    }

    @Test
    void shouldReturnBaseFeeForSixtyMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(60L);

        // Assert
        assertEquals(1_000, fee);
    }

    @Test
    void shouldChargeOneAdditionalHourForSixtyOneMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(61L);

        // Assert
        assertEquals(1_500, fee);
    }
}
