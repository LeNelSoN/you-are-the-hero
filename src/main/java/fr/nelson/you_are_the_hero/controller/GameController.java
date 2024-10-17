package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.model.dto.GameDto;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/game")
public class GameController {

    @Autowired
    SceneService sceneService;

    @GetMapping(path = "/scene/{sceneId}")
    public ResponseEntity<GameDto> play(@PathVariable String sceneId){
        Scene scene = sceneService.getSceneById(sceneId);
        GameDto gameDto = new GameDto(scene.getDescription());

        for(Choice choice: scene.getChoices()){
            gameDto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GameController.class).play(choice.getNextSceneId())).withRel("next").withTitle(choice.getDescription()).withType(HttpMethod.GET.name()));
        }

        return ResponseEntity.ok(gameDto);
    }
}
