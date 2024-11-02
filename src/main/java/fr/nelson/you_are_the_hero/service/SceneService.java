package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.SceneNotFoundException;
import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.repository.SceneRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        String currentUsername = userService.getCurrentUsername();

        if(!parentScene.getAuthor().equals(currentUsername)){
            throw new BadOwnerStoryException("Your are not the author");
        }

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
}
