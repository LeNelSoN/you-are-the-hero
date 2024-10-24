package fr.nelson.you_are_the_hero.utility;
import fr.nelson.you_are_the_hero.exception.PasswordTooShortException;
import fr.nelson.you_are_the_hero.exception.PasswordTooWeakException;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    @Value("${security.password.min.lenght}")
    private int minLength;

    private static final String WEAK = "Weak";
    private static final String MEDIUM = "Medium";
    private static final String STRONG = "Strong";
    private static final String UNKNOWN = "Unknown";


    public String validatePasswordStrength(String password) {
        int score = 0;

        if (password.length() < minLength) {
            throw new PasswordTooShortException("Password must be at least " + minLength + " characters long.");
        }

        if (UPPER_CASE_PATTERN.matcher(password).find()) {
            score++;
        }

        if (LOWER_CASE_PATTERN.matcher(password).find()) {
            score++;
        }

        if (DIGIT_PATTERN.matcher(password).find()) {
            score++;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            score++;
        }

        return getPasswordLevel(score);
    }

    private String getPasswordLevel(int score) {
        switch (score) {
            case 0:
            case 1:
                throw new PasswordTooWeakException("Password_Too_Weak");
            case 2:
                return WEAK;
            case 3:
                return MEDIUM;
            case 4:
                return STRONG;
            default:
                return UNKNOWN;
        }
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
}
