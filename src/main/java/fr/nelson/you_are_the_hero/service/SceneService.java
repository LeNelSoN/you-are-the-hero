package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.SceneNotFoundException;
import fr.nelson.you_are_the_hero.exception.SceneHasChildrenException;
import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.model.dto.SceneDto;
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

        userService.validateAuthor(parentScene.getAuthor());

        String currentUsername = userService.getCurrentUsername();

        Scene childScene = createChildScene(sceneToAdd.getDescription(), parentSceneId, currentUsername);

        return addChoiceToParentScene(parentScene, childScene.getId(), sceneToAdd.getChoice());
    }

    public Scene getSceneById(String id){
        Optional<Scene> sceneOptional = sceneRepository.findById(id);
        if (sceneOptional.isPresent()){
            return sceneOptional.get();
        } else {
            throw new SceneNotFoundException("Scene not found");
        }
    }

    public Scene updateSceneDescriptionById(String id, SceneDto sceneDto) throws BadOwnerStoryException {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Scene ID cannot be null or empty.");
        }

        if (sceneDto == null || sceneDto.getDescription() == null || sceneDto.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }

        Scene sceneToUpdate = getSceneById(id);
        userService.validateAuthor(sceneToUpdate.getAuthor());

        sceneToUpdate.setDescription(sceneDto.getDescription());
        return sceneRepository.save(sceneToUpdate);
    }

    public void deleteSceneById(String id) throws BadOwnerStoryException {

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Scene ID cannot be null or empty.");
        }

        Scene parentScene = getParentScene(id);

        userService.validateAuthor(parentScene.getAuthor());

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

    public Scene updateChoiceDescription(Scene scene, String nextSceneId, Choice requestChoice) throws BadOwnerStoryException {
        userService.validateAuthor(scene.getAuthor());
        Choice choice = getChoiceWithNextSceneId(scene, nextSceneId);
        choice.setDescription(requestChoice.getDescription());

        return sceneRepository.save(scene);
    }

    private Scene getParentScene(String childSceneId) {

        Scene childScene = sceneRepository.findById(childSceneId)
                .orElseThrow(() -> new SceneNotFoundException("Child scene not found"));

        return sceneRepository.findById(childScene.getPreviousSceneId())
                .orElseThrow(() -> new SceneNotFoundException("Parent scene not found"));
    }

    private void deleteChoiceFromScene(Scene parentScene, String sceneId){

        boolean choiceRemoved = parentScene.getChoices().removeIf(choice -> choice.getNextSceneId().equals(sceneId));

        if (choiceRemoved) {
            sceneRepository.save(parentScene);
        }

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

    private boolean hasChildScene(String id){
        return sceneRepository.existsByPreviousSceneId(id);
    }

    private Choice getChoiceWithNextSceneId(Scene scene, String nextSceneId) {
        return scene.getChoices().stream()
                .filter(choiceElm -> choiceElm.getNextSceneId().equals(nextSceneId))
                .findFirst()
                .orElseThrow(() -> new SceneNotFoundException("Scene with this choice doesn't exist"));
    }

}
