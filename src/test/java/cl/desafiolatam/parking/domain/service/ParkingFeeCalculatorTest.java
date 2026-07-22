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

    @Test
    void shouldChargeOneAdditionalHourForOneHundredTwentyMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(120L);

        // Assert
        assertEquals(1_500, fee);
    }

    @Test
    void shouldChargeTwoAdditionalHoursForOneHundredTwentyOneMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(121L);

        // Assert
        assertEquals(2_000, fee);
    }

    @Test
    void shouldChargeTwoAdditionalHoursForOneHundredEightyMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(180L);

        // Assert
        assertEquals(2_000, fee);
    }

    @Test
    void shouldChargeFourThousandFiveHundredForFourHundredEightyMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(480L);

        // Assert
        assertEquals(4_500, fee);
    }

    @Test
    void shouldReachMaximumFeeAtFourHundredEightyOneMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(481L);

        // Assert
        assertEquals(5_000, fee);
    }

    @Test
    void shouldCapFeeAtFiveThousandForFiveHundredFortyOneMinutes() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int fee = calculator.calculateFee(541L);

        // Assert
        assertEquals(5_000, fee);
    }
}
