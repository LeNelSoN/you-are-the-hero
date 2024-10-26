package fr.nelson.you_are_the_hero.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PasswordGeneratorTest {
    @InjectMocks
    PasswordGenerator passwordGenerator;

    final int MIN_LENGTH_PASSWORD = 8;

    @BeforeEach
    void setUp() {
        passwordGenerator.setMinLength(MIN_LENGTH_PASSWORD);
    }

    @Test
    void testGeneratePassword_ValidParameters() {
        String password = passwordGenerator.generatePassword(12, true, true, true, true);
        assertNotNull(password);
        assertEquals(12, password.length());
        assertTrue(password.matches(".*[A-Z].*"));
        assertTrue(password.matches(".*[a-z].*"));
        assertTrue(password.matches(".*\\d.*"));
        assertTrue(password.matches(".*[^a-zA-Z0-9].*"));
    }

    @Test
    void testGeneratePassword_withMethodWithNoParameters() {
        String password = passwordGenerator.generatePassword();
        assertNotNull(password);
        assertEquals(8, password.length());
        assertTrue(password.matches(".*[A-Z].*"));
        assertTrue(password.matches(".*[a-z].*"));
        assertTrue(password.matches(".*\\d.*"));
        assertTrue(password.matches(".*[^a-zA-Z0-9].*"));
    }

    @Test
    void testGeneratePassword_withMethodWithLength() {
        String password = passwordGenerator.generatePassword(12);
        assertNotNull(password);
        assertEquals(12, password.length());
        assertTrue(password.matches(".*[A-Z].*"));
        assertTrue(password.matches(".*[a-z].*"));
        assertTrue(password.matches(".*\\d.*"));
        assertTrue(password.matches(".*[^a-zA-Z0-9].*"));
    }

    @Test
    void testGeneratePassword_TooShort() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> passwordGenerator.generatePassword(5, true, true, true, true)
        );
        assertEquals("Password length must be at least 8.", exception.getMessage());
    }

    @Test
    void testGeneratePassword_NoCharacterTypes() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> passwordGenerator.generatePassword(10, false, false, false, false)
        );
        assertEquals("At least one character type must be selected.", exception.getMessage());
    }

}
