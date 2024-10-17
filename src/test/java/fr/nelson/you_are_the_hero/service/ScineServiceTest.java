package fr.nelson.you_are_the_hero.service;
import static org.junit.jupiter.api.Assertions.*;

import fr.nelson.you_are_the_hero.repository.SceneRepository;
import static org.mockito.Mockito.*;

import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.db.Scene;

import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ScineServiceTest {
    private SceneService sceneService;
    private SceneRepository sceneRepository;

    @BeforeEach
    void setUp() {
        sceneRepository = mock(SceneRepository.class);
        sceneService = new SceneService();
        ReflectionTestUtils.setField(sceneService, "sceneRepository", sceneRepository);
    }

    @Test
    void testAddNewScene() {
        // Arrange
        String parentSceneId = "parent123";
        AddSceneDto sceneToAdd = new AddSceneDto("New scene description", "New choice");
        Scene parentScene = new Scene("Parent scene", null);
        parentScene.setId(parentSceneId);
        Scene childScene = new Scene("New scene description", parentSceneId);
        childScene.setId("child456");

        when(sceneRepository.findById(parentSceneId)).thenReturn(Optional.of(parentScene));
        when(sceneRepository.save(any(Scene.class))).thenReturn(childScene).thenReturn(parentScene);

        // Act
        Scene result = sceneService.addNewScene(parentSceneId, sceneToAdd);

        // Assert
        assertNotNull(result);
        assertEquals(parentSceneId, result.getId());
        assertEquals(1, result.getChoices().size());
        assertEquals("New choice", result.getChoices().get(0).getDescription());
        assertEquals("child456", result.getChoices().get(0).getNextSceneId());
        verify(sceneRepository, times(2)).save(any(Scene.class));
    }

    @Test
    void testGetSceneById() {
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
    void testGetSceneByIdNotFound() {
        // Arrange
        String sceneId = "nonexistent";
        when(sceneRepository.findById(sceneId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> sceneService.getSceneById(sceneId));
    }
}
