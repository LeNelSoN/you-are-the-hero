package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.SceneAlreadyExistsException;
import fr.nelson.you_are_the_hero.exception.StoryNotFoundException;
import fr.nelson.you_are_the_hero.model.dto.StoryDto;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.model.dto.template.StoryTemplateDto;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.model.db.Story;
import fr.nelson.you_are_the_hero.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/story")
public class StoryController {
    @Autowired
    StoryService storyService;

    @GetMapping(path = "/template")
    public ResponseEntity<StoryTemplateDto> getTemplate(){
        StoryTemplateDto template = new StoryTemplateDto("Your Story title", "A description of your story");
        return ResponseEntity.ok(template);
    }

    @GetMapping
    public ResponseEntity<List<StoryDto>> getAllStory(){
        List<Story> allStory = storyService.getAllStory();
        List<StoryDto> storyDtoList = new ArrayList<>();
        for(Story story: allStory){
            StoryDto storyDto = new StoryDto(story.getTitle(), story.getDescription());
            storyDto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GameController.class).play(story.getFirstSceneId())).withRel("startStory").withType(HttpMethod.GET.name()));
            storyDtoList.add(storyDto);
        }
        return ResponseEntity.ok(storyDtoList);
    }

    @GetMapping(path = "/{storyId}")
    public ResponseEntity<?> getOneStory(@PathVariable String storyId){
        try{
            Story story = storyService.getStoryById(storyId);
            StoryDto storyDto = new StoryDto(story.getTitle(), story.getDescription());
            return ResponseEntity.ok(storyDto);
        } catch (StoryNotFoundException e) {
            return ResponseEntity.status(404).body("Sorry, story not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred on the server. Please try again later.");
        }

    }

    @PostMapping
    public ResponseEntity<MessageDto> createStory(@RequestBody Story story){
        Story newStory = storyService.createNewStory(story);
        MessageDto message = new MessageDto("Your story is ready, now add some scenes.");
        message.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StoryController.class).addFirstSceneToStory(newStory.getId(), null)).withRel("addFirstScene").withType(HttpMethod.POST.name()));
        message.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SceneController.class).getFirstSceneTemplate()).withRel("getFirstSceneTemplate").withType(HttpMethod.GET.name()));
        return ResponseEntity.ok(message);
    }

    @PostMapping(path = "/{storyId}/scene")
    public ResponseEntity<?> addFirstSceneToStory(@PathVariable String storyId, @RequestBody Scene scene){
        try{
            Story updatedStory = storyService.addSceneToStory(storyId, scene);
            StoryDto storyDto = new StoryDto(updatedStory.getTitle(), updatedStory.getDescription());
            storyDto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SceneController.class).getScene(updatedStory.getFirstSceneId())).withRel("getFirstScene").withType(HttpMethod.GET.name()));
            return ResponseEntity.ok(storyDto);
        } catch (SceneAlreadyExistsException e) {
            return ResponseEntity.status(409).body("The first scene already exists and cannot be overwritten.");
        } catch (StoryNotFoundException e){
            return ResponseEntity.status(404).body("Sorry, story not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred on the server. Please try again later.");
        }

    }

}
