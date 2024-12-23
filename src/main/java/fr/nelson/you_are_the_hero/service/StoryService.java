package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.SceneAlreadyExistsException;
import fr.nelson.you_are_the_hero.exception.StoryNotFoundException;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.model.db.Story;
import fr.nelson.you_are_the_hero.repository.SceneRepository;
import fr.nelson.you_are_the_hero.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private SceneRepository sceneRepository;

    public Story createNewStory(Story story){
        return storyRepository.save(story);
    }

    public Story addSceneToStory(String storyId, Scene scene, String username) throws SceneAlreadyExistsException, StoryNotFoundException, BadOwnerStoryException {
        if (storyId == null || storyId.isEmpty()) {
            throw new IllegalArgumentException("Story ID cannot be null or empty");
        }

        if (scene == null) {
            throw new IllegalArgumentException("Scene cannot be null");
        }

        Story story = storyRepository.findByIdAndCreatedBy(storyId, username)
                .orElseThrow(() -> new StoryNotFoundException("Story not found"));

        return saveSceneToStory(scene, username, story);

    }

    public List<Story> getAllStory(){
        return storyRepository.findAll();
    }

    public Story getStoryById(String storyId) throws StoryNotFoundException {
        if (storyId == null || storyId.isEmpty()) {
            throw new IllegalArgumentException("Story ID cannot be null or empty");
        }

        Optional<Story> optionalStory = storyRepository.findById(storyId);
        if(optionalStory.isPresent()){
            return optionalStory.get();
        } else {
            throw new StoryNotFoundException("Story not found");
        }
    }

    private Story saveSceneToStory(Scene scene, String username, Story story) throws BadOwnerStoryException, SceneAlreadyExistsException {
        if(!story.getCreatedBy().equals(username)){
            throw new BadOwnerStoryException("The user can't modify this story");
        }

        if(story.getFirstSceneId() != null){
            throw new SceneAlreadyExistsException("The first scene already exists");
        }

        scene.setAuthor(username);
        Scene savedScene = sceneRepository.save(scene);
        story.setFirstSceneId(savedScene.getId());
        return storyRepository.save(story);

    }
}
