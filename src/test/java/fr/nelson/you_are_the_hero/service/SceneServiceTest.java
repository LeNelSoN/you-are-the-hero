package fr.nelson.you_are_the_hero.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.repository.SceneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SceneServiceTest {

    @Mock
    private SceneRepository sceneRepository;

    @InjectMocks
    private SceneService sceneService;

    @Test
    void testAddNewScene_Success() {
        // Arrange
        String parentId = "parent123";
        AddSceneDto sceneToAdd = new AddSceneDto("New scene description", "New choice");
        Scene parentScene = new Scene("Parent scene", null);
        parentScene.setId(parentId);
        Scene childScene = new Scene("New scene description", parentId);
        childScene.setId("child456");

        when(sceneRepository.findById(parentId)).thenReturn(Optional.of(parentScene));
        when(sceneRepository.save(any(Scene.class))).thenReturn(childScene).thenReturn(parentScene);

        // Act
        Scene result = sceneService.addNewScene(parentId, sceneToAdd);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getChoices().size());
        assertEquals("New choice", result.getChoices().get(0).getDescription());
        assertEquals("child456", result.getChoices().get(0).getNextSceneId());
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
}