package fr.nelson.you_are_the_hero.utility;

import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final SecureRandom random = new SecureRandom();

    @Value("${security.password.min.length}")
    private int minLength;

    public String generatePassword(int length, boolean isIncludeUpperCase, boolean isIncludeLower, boolean isIncludeDigits, boolean isIncludeSpecialChars){
        if (length < minLength) {
            throw new IllegalArgumentException("Password length must be at least " + minLength + ".");
        }

        StringBuilder pool = new StringBuilder();

        if (isIncludeUpperCase) {
            pool.append(UPPER_CASE);
        }
        if (isIncludeLower) {
            pool.append(LOWER_CASE);
        }
        if (isIncludeDigits) {
            pool.append(DIGITS);
        }
        if (isIncludeSpecialChars) {
            pool.append(SPECIAL_CHARS);
        }

        if (pool.isEmpty()) {
            throw new IllegalArgumentException("At least one character type must be selected.");
        }

        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(pool.length());
            password.append(pool.charAt(randomIndex));
        }

        return password.toString();
    }

    public String generatePassword(int length){
        if (length < minLength) {
            throw new IllegalArgumentException("Password length must be at least " + minLength + ".");
        }

        StringBuilder pool = new StringBuilder();

        pool.append(UPPER_CASE);
        pool.append(LOWER_CASE);
        pool.append(DIGITS);
        pool.append(SPECIAL_CHARS);

        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(pool.length());
            password.append(pool.charAt(randomIndex));
        }

        return password.toString();
    }

    public String generatePassword(){

        StringBuilder pool = new StringBuilder();

        pool.append(UPPER_CASE);
        pool.append(LOWER_CASE);
        pool.append(DIGITS);
        pool.append(SPECIAL_CHARS);

        StringBuilder password = new StringBuilder(minLength);

        for (int i = 0; i < minLength; i++) {
            int randomIndex = random.nextInt(pool.length());
            password.append(pool.charAt(randomIndex));
        }

        return password.toString();
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
}
