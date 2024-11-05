package fr.nelson.you_are_the_hero.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.SceneHasChildrenException;
import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.repository.SceneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SceneServiceTest {

    @Mock
    private SceneRepository sceneRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SceneService sceneService;

    @Test
    void testAddNewScene_Success() throws BadOwnerStoryException {
        // Arrange
        String author = "Jean Neige";
        String parentId = "parent123";
        AddSceneDto sceneToAdd = new AddSceneDto("New scene description", "New choice");
        Scene parentScene = new Scene("Parent scene", null, author);
        parentScene.setId(parentId);
        Scene childScene = new Scene("New scene description", parentId);
        childScene.setId("child456");

        when(sceneRepository.findById(parentId)).thenReturn(Optional.of(parentScene));
        when(sceneRepository.save(any(Scene.class))).thenReturn(childScene).thenReturn(parentScene);
        when(userService.getCurrentUsername()).thenReturn(author);

        // Act
        Scene result = sceneService.addNewScene(parentId, sceneToAdd);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getChoices().size());
        assertEquals("New choice", result.getChoices().get(0).getDescription());
        assertEquals("child456", result.getChoices().get(0).getNextSceneId());
        assertEquals("Jean Neige", result.getAuthor());
        verify(sceneRepository, times(2)).save(any(Scene.class));
    }

    @Test
    void testAddNewScene_ParentSceneNotFound() {
        // Arrange
        String parentId = "nonexistent";
        AddSceneDto sceneToAdd = new AddSceneDto("New scene description", "New choice");

        when(sceneRepository.findById(parentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sceneService.addNewScene(parentId, sceneToAdd));
    }

    @Test
    void testAddNewScene_WrongAuthor() {
        String author = "Jean Neige";
        String parentId = "parent123";

        AddSceneDto sceneToAdd = new AddSceneDto("New scene description", "New choice");

        Scene parentScene = new Scene("Parent scene", null, author);
        parentScene.setId(parentId);

        Scene childScene = new Scene("New scene description", parentId);
        childScene.setId("child456");

        when(sceneRepository.findById(parentId)).thenReturn(Optional.of(parentScene));
        when(userService.getCurrentUsername()).thenReturn("Cercei Lannister");

        assertThrows(BadOwnerStoryException.class, () -> sceneService.addNewScene(parentId, sceneToAdd));
        verify(sceneRepository, never()).save(any(Scene.class));
    }

    @Test
    void testGetSceneById_Success() {
        // Arrange
        String sceneId = "scene123";
        Scene expectedScene = new Scene("Test scene", null);
        expectedScene.setId(sceneId);

        when(sceneRepository.findById(sceneId)).thenReturn(Optional.of(expectedScene));

        // Act
        Scene result = sceneService.getSceneById(sceneId);

        // Assert
        assertNotNull(result);
        assertEquals(sceneId, result.getId());
        assertEquals("Test scene", result.getDescription());
    }

    @Test
    void testGetSceneById_SceneNotFound() {
        // Arrange
        String sceneId = "nonexistent";

        when(sceneRepository.findById(sceneId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sceneService.getSceneById(sceneId));
    }

    @Test
    void testDeleteSceneById_Success() throws BadOwnerStoryException {
        Scene parentScene = new Scene("Description", null,  "Jean Neige");
        parentScene.setId("parentSceneId1234");

        Scene childScene = new Scene("Child Description", "parentSceneId1234",  "Jean Neige");
        childScene.setId("childSceneId1234");

        parentScene.setChoices(new ArrayList<>(List.of(new Choice("Choice Description", "childSceneId1234"))));

        when(sceneRepository.findById("childSceneId1234")).thenReturn(Optional.of(childScene));
        when(sceneRepository.findById(childScene.getPreviousSceneId())).thenReturn(Optional.of(parentScene));
        when(userService.getCurrentUsername()).thenReturn("Jean Neige");
        when(sceneRepository.existsByPreviousSceneId("childSceneId1234")).thenReturn(false);

        sceneService.deleteSceneById("childSceneId1234");

        verify(sceneRepository).deleteById("childSceneId1234");
        assertFalse(
                parentScene.getChoices()
                    .stream()
                        .anyMatch(choice -> choice.getNextSceneId().equals("childSceneId1234"))
        );
    }

    @Test
    void testDeleteSceneById_withChildScene() throws BadOwnerStoryException {
        Scene parentScene = new Scene("Description", null,  "Jean Neige");
        parentScene.setId("parentSceneId1234");

        Scene childScene = new Scene("Child Description", "parentSceneId1234",  "Jean Neige");
        childScene.setId("childSceneId1234");

        parentScene.setChoices(new ArrayList<>(List.of(new Choice("Choice Description", "childSceneId1234"))));

        when(sceneRepository.findById("childSceneId1234")).thenReturn(Optional.of(childScene));
        when(sceneRepository.findById(childScene.getPreviousSceneId())).thenReturn(Optional.of(parentScene));
        when(userService.getCurrentUsername()).thenReturn("Jean Neige");
        when(sceneRepository.existsByPreviousSceneId("childSceneId1234")).thenReturn(true);

        assertThrows(SceneHasChildrenException.class,  () -> sceneService.deleteSceneById("childSceneId1234"));
    }

    @Test
    void testDeleteSceneById_WithWrongAuthor() throws BadOwnerStoryException {
        Scene parentScene = new Scene("Description", null,  "Jean Neige");
        parentScene.setId("parentSceneId1234");

        Scene childScene = new Scene("Child Description", "parentSceneId1234",  "Jean Neige");
        childScene.setId("childSceneId1234");

        parentScene.setChoices(new ArrayList<>(List.of(new Choice("Choice Description", "childSceneId1234"))));

        when(sceneRepository.findById("childSceneId1234")).thenReturn(Optional.of(childScene));
        when(sceneRepository.findById(childScene.getPreviousSceneId())).thenReturn(Optional.of(parentScene));
        when(userService.getCurrentUsername()).thenReturn("Sam Tarly");

        assertThrows(BadOwnerStoryException.class,  () -> sceneService.deleteSceneById("childSceneId1234"));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void testDeleteSceneById_WithInvalid(String id) {
        assertThrows(IllegalArgumentException.class,  () -> sceneService.deleteSceneById(id));
    }

}