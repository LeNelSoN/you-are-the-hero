package fr.nelson.you_are_the_hero.exception;

public class SceneNotFoundException extends RuntimeException {
    public SceneNotFoundException(String message) {
        super(message);
    }
}
