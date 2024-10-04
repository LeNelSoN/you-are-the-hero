package fr.nelson.you_are_the_hero.exception;

public class UserAllreadyExistException extends RuntimeException {
    public UserAllreadyExistException(String message) {
        super(message);
    }
}
