package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.ChoiceNotFoundException;
import fr.nelson.you_are_the_hero.exception.SceneNotFoundException;
import fr.nelson.you_are_the_hero.exception.SceneHasChildrenException;
import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.repository.SceneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SceneService {
    @Autowired
    private SceneRepository sceneRepository;
    @Autowired
    private UserService userService;

    public Scene addNewScene(String parentSceneId, AddSceneDto sceneToAdd) throws BadOwnerStoryException {
        Scene parentScene = getSceneById(parentSceneId);
        String currentUsername = validateAuthor(parentScene);

        Scene childScene = createChildScene(sceneToAdd.getDescription(), parentSceneId, currentUsername);

        return addChoiceToParentScene(parentScene, childScene.getId(), sceneToAdd.getChoice());
    }

    private Scene addChoiceToParentScene(Scene parentScene, String childSceneId, String choiceDescription) {
        Choice choice = new Choice(choiceDescription, childSceneId);
        parentScene.addChoice(choice);
        return sceneRepository.save(parentScene);
    }

    private Scene createChildScene(String description, String parentSceneId, String username){
        Scene newScene = new Scene(description, parentSceneId, username);
        return sceneRepository.save(newScene);
    }

    public Scene getSceneById(String id){
        Optional<Scene> sceneOptional = sceneRepository.findById(id);
        if (sceneOptional.isPresent()){
            return sceneOptional.get();
        } else {
            throw new SceneNotFoundException("Scene not found");
        }
    }

    public boolean hasChildScene(String id){
        return sceneRepository.existsByPreviousSceneId(id);
    }

    public void deleteSceneById(String id) throws BadOwnerStoryException {
        Scene parentScene = getParentScene(id);

        validateAuthor(parentScene);

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Scene ID cannot be null or empty.");
        }

        if(hasChildScene(id)) {
            throw new SceneHasChildrenException("Cannot delete scene with children.");
        }

        deleteChoiceFromScene(parentScene, id);

        try{
            sceneRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new SceneNotFoundException("Scene not found for ID: " + id);
        }
    }

    public void deleteChoiceFromScene(Scene parentScene, String sceneId){
        boolean choiceRemoved = parentScene.getChoices().removeIf(choice -> choice.getNextSceneId().equals(sceneId));
        if (!choiceRemoved) {
            throw new ChoiceNotFoundException("Choice not found for ID: " + sceneId);
        }

        sceneRepository.save(parentScene);
    }

    public Scene getParentScene(String childSceneId) {

        Scene childScene = sceneRepository.findById(childSceneId)
                .orElseThrow(() -> new SceneNotFoundException("Child scene not found"));

        return sceneRepository.findById(childScene.getPreviousSceneId())
                .orElseThrow(() -> new SceneNotFoundException("Parent scene not found"));
    }

    public String validateAuthor(Scene parentScene) throws BadOwnerStoryException {
        String currentUsername = userService.getCurrentUsername();

        if (currentUsername == null) {
            throw new BadOwnerStoryException("User is not authenticated.");
        }

        if(!parentScene.getAuthor().equals(currentUsername)){
            throw new BadOwnerStoryException("Your are not the author");
        }

        return currentUsername;
    }
}
