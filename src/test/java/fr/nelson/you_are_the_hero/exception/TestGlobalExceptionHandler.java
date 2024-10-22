package fr.nelson.you_are_the_hero.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestGlobalExceptionHandler {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest mockWebRequest;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        mockWebRequest = mock(WebRequest.class);
    }

    @Test
    public void handleTokenExpiredExceptionTest() {
        String message = "Token has expired";
        TokenExpiredException tokenExpiredException = new TokenExpiredException(message);
        Mockito.when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/tokenExpiredException");

        ResponseEntity<?> responseEntity = globalExceptionHandler.handleTokenExpiredException(tokenExpiredException, mockWebRequest);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/tokenExpiredException", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);

    }

    @Test
    public void UserAllreadyExistExceptionTest() {
        String message = "User allready exists";
        UserAllreadyExistException userAllreadyExistException = new UserAllreadyExistException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/UserAllreadyExists");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleUserAllreadyExistException(userAllreadyExistException, mockWebRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/UserAllreadyExists", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);

    }

    @Test
    public void StoryNotFoundExceptionTest() {
        String message = "Story not found";
        StoryNotFoundException storyNotFoundException = new StoryNotFoundException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/StoryNotFound");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleStoryNotFoundException(storyNotFoundException, mockWebRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/StoryNotFound", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    public void SceneAlreadyExistsExceptionTest() {
        String message = "Scene already exists";
        SceneAlreadyExistsException sceneAlreadyExistsException = new SceneAlreadyExistsException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/SceneAlreadyExists");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleSceneAlreadyExistsException(sceneAlreadyExistsException, mockWebRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/SceneAlreadyExists", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    public void InvalidTokenExceptionTest() {
        String message = "Invalid Token";
        InvalidTokenException invalidTokenException = new InvalidTokenException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/InvalidToken");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleInvalidTokenException(invalidTokenException, mockWebRequest);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/InvalidToken", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    public void InvalidCredentialsExceptionTest() {
        String message = "Invalid Credentials";
        InvalidCredentialsException invalidCredentialsException = new InvalidCredentialsException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/InvalidCredentials");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleInvalidCredentialsException(invalidCredentialsException, mockWebRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/InvalidCredentials", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    public void BadOwnerStoryExceptionTest() {
        String message = "Bad Owner Story";
        BadOwnerStoryException badOwnerStoryException = new BadOwnerStoryException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/BadOwnerStory");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleBadOwnerStoryException(badOwnerStoryException, mockWebRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/BadOwnerStory", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

}
