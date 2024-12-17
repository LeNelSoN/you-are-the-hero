package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.dto.SceneDto;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.model.dto.template.SceneTemplateDto;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.model.hateoas.LinkType;
import fr.nelson.you_are_the_hero.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/scene")
public class SceneController {
    @Autowired
    SceneService sceneService;

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @GetMapping(path = "/template/firstScene")
    public ResponseEntity<SceneTemplateDto> getFirstSceneTemplate(){
        SceneTemplateDto template = new SceneTemplateDto("A description of your scene");
        return ResponseEntity.ok(template);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PostMapping(path = "/{parentSceneId}")
    public ResponseEntity<SceneDto> addScene(@PathVariable String parentSceneId, @RequestBody AddSceneDto childScene) throws BadOwnerStoryException {
        Scene scene = sceneService.addNewScene(parentSceneId, childScene);
        SceneDto sceneDto = new SceneDto(scene.getDescription(), scene.getChoices());

        addNextSceneLink(sceneDto);
        addPreviousSceneLink(sceneDto, scene);

        return ResponseEntity.ok(sceneDto);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @GetMapping(path = "/{sceneId}")
    public ResponseEntity<SceneDto> getScene(@PathVariable String sceneId) throws BadOwnerStoryException {
        Scene scene = sceneService.getSceneById(sceneId);
        SceneDto sceneDto = new SceneDto(scene.getDescription(), scene.getChoices());

        addNextSceneLink(sceneDto);
        addPreviousSceneLink(sceneDto, scene);
        addActionLinks(sceneId, sceneDto);
        addUpdateChoiceDescriptionLink(sceneId, sceneDto);

        return ResponseEntity.ok(sceneDto);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PatchMapping(path = "/{sceneId}/choice/{nextSceneId}")
    public ResponseEntity<?> updateChoiceDescription(@PathVariable String sceneId, @PathVariable String nextSceneId, @RequestBody Choice requestChoice) throws BadOwnerStoryException {
        Scene scene = sceneService.getSceneById(sceneId);

        Scene updatedScene = sceneService.updateChoiceDescription(scene, nextSceneId, requestChoice);

        SceneDto sceneDto = new SceneDto(updatedScene.getDescription(), updatedScene.getChoices());

        addNextSceneLink(sceneDto);
        addPreviousSceneLink(sceneDto, updatedScene);
        addActionLinks(sceneId, sceneDto);

        return ResponseEntity.ok(sceneDto);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PutMapping(path = "/{sceneId}")
    public ResponseEntity<?> updateSceneDescription(@PathVariable String sceneId, @RequestBody SceneDto sceneRequest) throws BadOwnerStoryException {
        Scene updatedScene = sceneService.updateSceneDescriptionById(sceneId, sceneRequest);
        SceneDto sceneDto = new SceneDto(updatedScene.getDescription(), updatedScene.getChoices());

        addNextSceneLink(sceneDto);
        addPreviousSceneLink(sceneDto, updatedScene);
        addActionLinks(sceneId, sceneDto);
        addUpdateChoiceDescriptionLink(sceneId, sceneDto);

        return ResponseEntity.ok(sceneDto);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @DeleteMapping(path = "/{sceneId}")
    public ResponseEntity<MessageDto> deleteScene(@PathVariable String sceneId) throws BadOwnerStoryException {
        sceneService.deleteSceneById(sceneId);
        MessageDto messageDto = new MessageDto("Scene has been deleted");

        return ResponseEntity.ok(messageDto);
    }

    private static void addActionLinks(String sceneId, SceneDto sceneDto) throws BadOwnerStoryException {
        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).addScene(sceneId, null))
                .withRel(LinkType.ADD_SCENE.REL)
                .withType(LinkType.ADD_SCENE.METHOD.name()));

        sceneDto.addDocumentation(LinkType.ADD_SCENE);

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).updateSceneDescription(sceneId, null))
                .withRel(LinkType.UPDATE_DESCRIPTION.REL)
                .withType(LinkType.UPDATE_DESCRIPTION.METHOD.name()));

        sceneDto.addDocumentation(LinkType.UPDATE_DESCRIPTION);

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).deleteScene(sceneId))
                .withRel(LinkType.DELETE_SCENE.REL)
                .withType(LinkType.DELETE_SCENE.name()));

        sceneDto.addDocumentation(LinkType.DELETE_SCENE);
    }

    private static void addPreviousSceneLink(SceneDto sceneDto, Scene scene) throws BadOwnerStoryException {
        if(scene.getPreviousSceneId() != null){
            sceneDto.add(WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder
                                    .methodOn(SceneController.class).getScene(scene.getPreviousSceneId()))
                    .withRel(LinkType.PREVIOUS_SCENE.REL)
                    .withType(LinkType.PREVIOUS_SCENE.METHOD.name())
            );

            sceneDto.addDocumentation(LinkType.PREVIOUS_SCENE);
        }
    }

    private static void addNextSceneLink(SceneDto sceneDto) throws BadOwnerStoryException {
        for(Choice choice: sceneDto.getChoices()){
            sceneDto.add(
                    WebMvcLinkBuilder
                            .linkTo(WebMvcLinkBuilder.methodOn(SceneController.class).getScene(choice.getNextSceneId()))
                            .withRel(LinkType.NEXT_SCENE.REL)
                            .withTitle(LinkType.NEXT_SCENE.TITLE)
                            .withType(LinkType.NEXT_SCENE.METHOD.name())
            );
            sceneDto.addDocumentation(LinkType.NEXT_SCENE);

        }
    }

    private static void addUpdateChoiceDescriptionLink(String sceneId, SceneDto sceneDto) throws BadOwnerStoryException {
        for(Choice choice: sceneDto.getChoices()){
            sceneDto.add(
                    WebMvcLinkBuilder
                            .linkTo(WebMvcLinkBuilder.methodOn(SceneController.class).updateChoiceDescription(sceneId, choice.getNextSceneId(), null))
                            .withRel(LinkType.UPDATE_CHOICE.REL)
                            .withTitle(choice.getDescription())
                            .withType(LinkType.UPDATE_CHOICE.METHOD.name())
            );

            sceneDto.addDocumentation(LinkType.UPDATE_CHOICE);
        }
    }

}
