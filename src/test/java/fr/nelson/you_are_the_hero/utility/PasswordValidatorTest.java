package fr.nelson.you_are_the_hero.utility;

import fr.nelson.you_are_the_hero.exception.PasswordTooShortException;
import fr.nelson.you_are_the_hero.exception.PasswordTooWeakException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorTest {

    @InjectMocks
    PasswordValidator passwordValidator;

    final int MIN_LENGTH_PASSWORD = 8;

    @BeforeEach
    void setUp() {
        passwordValidator.setMinLength(MIN_LENGTH_PASSWORD);
    }

    @Test
    void testValidatePasswordStrength_StrongPassword() {
        String password = "StrongP@ss1";
        String result = passwordValidator.validatePasswordStrength(password);
        assertEquals("Strong", result);
    }

    @Test
    void testValidatePasswordStrength_MediumPassword() {
        String password = "medium1!";
        String result = passwordValidator.validatePasswordStrength(password);
        assertEquals("Medium", result);
    }

    @Test
    void testValidatePasswordStrength_WeakPassword() {
        String password = "weakp@ss";
        String result = passwordValidator.validatePasswordStrength(password);
        assertEquals("Weak", result);
    }

    @Test
    void testValidatePasswordStrength_TooShortPassword() {
        String password = "short";
        PasswordTooShortException exception = assertThrows(
                PasswordTooShortException.class,
                () -> passwordValidator.validatePasswordStrength(password)
        );
        assertEquals("Password must be at least 8 characters long.", exception.getMessage());
    }

    @Test
    void testValidatePasswordStrength_TooWeakPassword() {
        String password = "12345678";
        PasswordTooWeakException exception = assertThrows(
                PasswordTooWeakException.class,
                () -> passwordValidator.validatePasswordStrength(password)
        );
        assertEquals("Password_Too_Weak", exception.getMessage());
    }
}
