package fr.nelson.you_are_the_hero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDetails> createErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler(AdminAlreadyExistException.class)
    public ResponseEntity<ErrorDetails> handleAdminAlreadyExistException(AdminAlreadyExistException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorDetails> handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UserAllreadyExistException.class)
    public ResponseEntity<ErrorDetails> handleUserAllreadyExistException(UserAllreadyExistException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(StoryNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleStoryNotFoundException(StoryNotFoundException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(SceneAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handleSceneAlreadyExistsException(SceneAlreadyExistsException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(SceneNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleSceneNotFoundException(SceneNotFoundException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorDetails> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest request) {
        return createErrorResponse(ex,HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(BadOwnerStoryException.class)
    public ResponseEntity<ErrorDetails> handleBadOwnerStoryException(BadOwnerStoryException ex, WebRequest request) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(SceneHasChildrenException.class)
    public ResponseEntity<ErrorDetails> handleSceneHasChildrenException(SceneHasChildrenException ex, WebRequest request) {
        return createErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralException(Exception ex, WebRequest request) {
        ErrorDetails errorResponse = new ErrorDetails(
                "An error occurred",
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
