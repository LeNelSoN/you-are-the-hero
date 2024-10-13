package fr.nelson.you_are_the_hero.exception;

public class AdminAlreadyExistException extends RuntimeException {
    public AdminAlreadyExistException(String message) {
        super(message);
    }
}
