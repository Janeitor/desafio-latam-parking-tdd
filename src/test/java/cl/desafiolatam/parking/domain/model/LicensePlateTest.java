package cl.desafiolatam.parking.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LicensePlateTest {

    @Test
    void shouldCreateLicensePlateWithValidValue() {
        // Arrange
        String value = "ABCD12";

        // Act
        LicensePlate licensePlate = new LicensePlate(value);

        // Assert
        assertEquals(value, licensePlate.value());
    }

    @Test
    void shouldRejectNullLicensePlate() {
        // Arrange
        String value = null;

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LicensePlate(value));

        // Assert
        assertEquals("License plate cannot be blank", exception.getMessage());
    }

    @Test
    void shouldRejectBlankLicensePlate() {
        // Arrange
        String value = "   ";

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new LicensePlate(value));

        // Assert
        assertEquals("License plate cannot be blank", exception.getMessage());
    }
}