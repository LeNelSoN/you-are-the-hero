package fr.nelson.you_are_the_hero.service;
import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.SceneAlreadyExistsException;
import fr.nelson.you_are_the_hero.exception.StoryNotFoundException;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.model.db.Story;
import fr.nelson.you_are_the_hero.repository.SceneRepository;
import fr.nelson.you_are_the_hero.repository.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private SceneRepository sceneRepository;

    @InjectMocks
    private StoryService storyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewStory() {
        // Arrange
        Story story = new Story();
        story.setId("story1");

        when(storyRepository.save(story)).thenReturn(story);

        // Act
        Story result = storyService.createNewStory(story);

        // Assert
        assertNotNull(result);
        assertEquals("story1", result.getId());
        verify(storyRepository, times(1)).save(story);
    }

    @Test
    public void testAddSceneToStory_Success() throws SceneAlreadyExistsException, StoryNotFoundException, BadOwnerStoryException {
        // Arrange
        String storyId = "story1";
        Scene scene = new Scene();
        scene.setId("scene1");

        Story story = new Story();
        story.setId(storyId);
        story.setFirstSceneId(null);
        story.setCreatedBy("username");

        when(storyRepository.findByIdAndCreatedBy(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(story));
        when(sceneRepository.save(scene)).thenReturn(scene);
        when(storyRepository.save(story)).thenReturn(story);

        // Act
        Story result = storyService.addSceneToStory(storyId, scene, "username");

        // Assert
        assertNotNull(result);
        assertEquals(scene.getId(), result.getFirstSceneId());
        verify(storyRepository, times(1)).save(story);
        verify(sceneRepository, times(1)).save(scene);
    }

    @Test
    public void testAddSceneToStory_SceneAlreadyExists() {
        // Arrange
        String storyId = "story1";
        Scene scene = new Scene();
        scene.setId("scene1");

        Story story = new Story();
        story.setId(storyId);
        story.setFirstSceneId("existingScene");
        story.setCreatedBy("username");

        when(storyRepository.findByIdAndCreatedBy(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(story));

        // Act & Assert
        SceneAlreadyExistsException exception = assertThrows(SceneAlreadyExistsException.class, () -> {
            storyService.addSceneToStory(storyId, scene, "username");
        });

        // Vérif
        assertEquals("The first scene already exists", exception.getMessage());
        verify(storyRepository, never()).save(any());
    }

    @Test
    public void testAddSceneToStory_StoryNotFound() {
        // Arrange
        String storyId = "storyNotFound";
        Scene scene = new Scene();

        when(storyRepository.findByIdAndCreatedBy(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());

        // Act & Assert
        StoryNotFoundException exception = assertThrows(StoryNotFoundException.class, () -> {
            storyService.addSceneToStory(storyId, scene, "username");
        });

        // Vérif
        assertEquals("Story not found", exception.getMessage());
    }

    @Test
    public void testGetAllStory() {
        // Arrange
        Story story1 = new Story();
        Story story2 = new Story();
        when(storyRepository.findAll()).thenReturn(List.of(story1, story2));

        // Act
        List<Story> result = storyService.getAllStory();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(storyRepository, times(1)).findAll();
    }

    @Test
    public void testGetStoryById_Success() throws StoryNotFoundException {
        // Arrange
        String storyId = "story1";
        Story story = new Story();
        story.setId(storyId);

        when(storyRepository.findById(storyId)).thenReturn(Optional.of(story));

        // Act
        Story result = storyService.getStoryById(storyId);

        // Assert
        assertNotNull(result);
        assertEquals(storyId, result.getId());
        verify(storyRepository, times(1)).findById(storyId);
    }

    @Test
    public void testGetStoryById_StoryNotFound() {
        // Arrange
        String storyId = "storyNotFound";

        when(storyRepository.findById(storyId)).thenReturn(Optional.empty());

        // Act & Assert
        StoryNotFoundException exception = assertThrows(StoryNotFoundException.class, () -> {
            storyService.getStoryById(storyId);
        });

        // Vérif
        assertEquals("Story not found", exception.getMessage());
    }

    @Test
    public void testAddSceneToStory_WrongUser() {
        // Arrange
        String storyId = "storyNotFound";
        Scene scene = new Scene();

        when(storyRepository.findByIdAndCreatedBy(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());

        // Act & Assert
        StoryNotFoundException exception = assertThrows(StoryNotFoundException.class, () -> {
            storyService.addSceneToStory(storyId, scene, "username");
        });

        // Vérif
        assertEquals("Story not found", exception.getMessage());
    }
}

