package fr.nelson.you_are_the_hero.service;

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

    public Scene addNewScene(String parentSceneId, AddSceneDto sceneToAdd){
        Scene parentScene = getSceneById(parentSceneId);
        Scene childScene = sceneRepository.save(new Scene(sceneToAdd.getDescription(), parentSceneId));
        Choice choice = new Choice(sceneToAdd.getChoice(), childScene.getId());
        parentScene.addChoice(choice);
        return sceneRepository.save(parentScene);
    }

    public Scene getSceneById(String id){
        Optional<Scene> sceneOptional = sceneRepository.findById(id);
        if (sceneOptional.isPresent()){
            return sceneOptional.get();
        } else {
            throw new RuntimeException("Scene not found");
        }
    }
}
