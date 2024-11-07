package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.model.Choice;
import fr.nelson.you_are_the_hero.model.dto.AddSceneDto;
import fr.nelson.you_are_the_hero.model.dto.SceneDto;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.model.dto.template.AddSceneTemplateDto;
import fr.nelson.you_are_the_hero.model.dto.template.SceneTemplateDto;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
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
    @GetMapping(path = "/template/changeDescription")
    public ResponseEntity<SceneTemplateDto> getDescriptionTemplate(){
        SceneTemplateDto template = new SceneTemplateDto("A new description of your scene");
        return ResponseEntity.ok(template);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @GetMapping(path = "/template")
    public ResponseEntity<AddSceneTemplateDto> getTemplate(){
        AddSceneTemplateDto template = new AddSceneTemplateDto("A description of your scene", "The description of the choice");
        return ResponseEntity.ok(template);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PostMapping(path = "/{parentSceneId}")
    public ResponseEntity<SceneDto> addScene(@PathVariable String parentSceneId, @RequestBody AddSceneDto childScene) throws BadOwnerStoryException {
        Scene scene = sceneService.addNewScene(parentSceneId, childScene);
        SceneDto sceneDto = new SceneDto(scene.getDescription(), scene.getChoices());

        sceneDto = addNextSceneLink(sceneDto);

        if(scene.getPreviousSceneId() != null){
            sceneDto.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder
                            .methodOn(SceneController.class).getScene(scene.getPreviousSceneId()))
                            .withRel("previousScene")
                            .withType(HttpMethod.GET.name())
            );
        }

        return ResponseEntity.ok(sceneDto);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @GetMapping(path = "/{sceneId}")
    public ResponseEntity<SceneDto> getScene(@PathVariable String sceneId) throws BadOwnerStoryException {
        Scene scene = sceneService.getSceneById(sceneId);
        SceneDto sceneDto = new SceneDto(scene.getDescription(), scene.getChoices());

        sceneDto = addNextSceneLink(sceneDto);

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(SceneController.class).getTemplate())
                        .withRel("templateForAddScene")
                        .withType(HttpMethod.GET.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(SceneController.class).addScene(sceneId, null))
                        .withRel("addNextScene")
                        .withType(HttpMethod.POST.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).getDescriptionTemplate())
                .withRel("templateDescription")
                .withType(HttpMethod.GET.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                        .methodOn(SceneController.class).addScene(sceneId, null))
                        .withRel("changeDescription")
                        .withType(HttpMethod.PUT.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(SceneController.class).deleteScene(sceneId))
                        .withRel("deleteScene")
                        .withType(HttpMethod.DELETE.name()));

        if(scene.getPreviousSceneId() != null){
            sceneDto.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder
                            .methodOn(SceneController.class).getScene(scene.getPreviousSceneId()))
                            .withRel("previousScene")
                            .withType(HttpMethod.GET.name())
            );
        }

        return ResponseEntity.ok(sceneDto);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PutMapping(path = "/{sceneId}")
    public ResponseEntity<?> updateSceneDescription(@PathVariable String sceneId, @RequestBody SceneDto sceneRequest) throws BadOwnerStoryException {
        Scene updatedScene = sceneService.updateSceneDescriptionById(sceneId, sceneRequest);
        SceneDto sceneDto = new SceneDto(updatedScene.getDescription(), updatedScene.getChoices());

        sceneDto = addNextSceneLink(sceneDto);

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).getTemplate())
                .withRel("templateForAddScene")
                .withType(HttpMethod.GET.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).addScene(sceneId, null))
                .withRel("addNextScene")
                .withType(HttpMethod.POST.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).getDescriptionTemplate())
                .withRel("templateDescription")
                .withType(HttpMethod.GET.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).addScene(sceneId, null))
                .withRel("changeDescription")
                .withType(HttpMethod.PUT.name()));

        sceneDto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(SceneController.class).deleteScene(sceneId))
                .withRel("deleteScene")
                .withType(HttpMethod.DELETE.name()));

        if(updatedScene.getPreviousSceneId() != null){
            sceneDto.add(WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder
                                    .methodOn(SceneController.class).getScene(updatedScene.getPreviousSceneId()))
                    .withRel("previousScene")
                    .withType(HttpMethod.GET.name())
            );
        }

        return ResponseEntity.ok(sceneDto);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @DeleteMapping(path = "/{sceneId}")
    public ResponseEntity<MessageDto> deleteScene(@PathVariable String sceneId) throws BadOwnerStoryException {
        sceneService.deleteSceneById(sceneId);
        MessageDto messageDto = new MessageDto("Scene has been deleted");

        return ResponseEntity.ok(messageDto);
    }

    private static SceneDto addNextSceneLink(SceneDto sceneDto) throws BadOwnerStoryException {
        for(Choice choice: sceneDto.getChoices()){
            sceneDto.add(
                    WebMvcLinkBuilder
                            .linkTo(WebMvcLinkBuilder.methodOn(SceneController.class).getScene(choice.getNextSceneId()))
                            .withRel("nextScene")
                            .withTitle(choice.getDescription())
                            .withType(HttpMethod.GET.name())
            );
        }
        return sceneDto;
    }

}
