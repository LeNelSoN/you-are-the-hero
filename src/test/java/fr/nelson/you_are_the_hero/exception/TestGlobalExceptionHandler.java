package fr.nelson.you_are_the_hero.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestGlobalExceptionHandler {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest mockWebRequest;

    @Test
    public void testHandleTokenExpiredException() throws Exception {
        String message = "Token has expired";
        TokenExpiredException tokenExpiredException = new TokenExpiredException(message);
        Mockito.when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/tokenExpiredException");

        ResponseEntity<?> responseEntity = globalExceptionHandler.handleTokenExpiredException(tokenExpiredException, mockWebRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/tokenExpiredException", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);

    }

    @Test
    public void testHandleUserAllreadyExistException() {
        String message = "User allready exists";
        UserAllreadyExistException userAllreadyExistException = new UserAllreadyExistException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/UserAllreadyExists");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleUserAllreadyExistException(userAllreadyExistException, mockWebRequest);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/UserAllreadyExists", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);

    }

    @Test
    public void testHandleStoryNotFoundException() {
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
    public void testHandleSceneAlreadyExistsException() {
        String message = "Scene already exists";
        SceneAlreadyExistsException sceneAlreadyExistsException = new SceneAlreadyExistsException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/SceneAlreadyExists");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleSceneAlreadyExistsException(sceneAlreadyExistsException, mockWebRequest);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/SceneAlreadyExists", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    public void testHandleInvalidTokenException() {
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
    public void testHandleInvalidCredentialsException() {
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
    public void testHandleBadOwnerStoryException() {
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

    @Test
    public void testHandleRuntimeExceptionTest() {
        String message = "Runtime Exception";
        RuntimeException runtimeException = new RuntimeException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/BadOwnerStory");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleRuntimeException(runtimeException, mockWebRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals(message, errorDetails.getMessage());
        assertEquals("uri=/test/BadOwnerStory", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    public void testHandleSceneNotFoundException() {
        String message = "Scene not found";
        SceneNotFoundException exception = new SceneNotFoundException(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/SceneNotFound");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleSceneNotFoundException(exception, mockWebRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals("Scene not found", errorDetails.getMessage());
        assertEquals("uri=/test/SceneNotFound", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }

    @Test
    public void testHandleGeneralException() {
        String message = "General Exception";
        Exception exception = new Exception(message);
        when(mockWebRequest.getDescription(false)).thenReturn("uri=/test/BadOwnerStory");

        ResponseEntity<?> responseEntity = globalExceptionHandler
                .handleGeneralException(exception, mockWebRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ErrorDetails.class, Objects.requireNonNull(responseEntity.getBody()).getClass());

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertEquals("An error occurred", errorDetails.getMessage());
        assertEquals("uri=/test/BadOwnerStory", errorDetails.getDetails());

        verify(mockWebRequest, times(1)).getDescription(false);
    }
}
