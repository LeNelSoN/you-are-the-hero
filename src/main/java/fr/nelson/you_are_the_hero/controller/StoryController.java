package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.SceneAlreadyExistsException;
import fr.nelson.you_are_the_hero.exception.StoryNotFoundException;
import fr.nelson.you_are_the_hero.model.dto.StoryDto;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.model.db.Scene;
import fr.nelson.you_are_the_hero.model.db.Story;
import fr.nelson.you_are_the_hero.model.hateoas.LinkType;
import fr.nelson.you_are_the_hero.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/story")
public class StoryController {
    @Autowired
    StoryService storyService;

    @GetMapping
    public ResponseEntity<List<StoryDto>> getAllStory() throws StoryNotFoundException, SceneAlreadyExistsException, BadOwnerStoryException {
        List<Story> allStory = storyService.getAllStory();
        List<StoryDto> storyDtoList = new ArrayList<>();
        for(Story story: allStory){
            StoryDto storyDto = new StoryDto(story.getTitle(), story.getDescription());
            if(story.getFirstSceneId() != null) {
                storyDto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GameController.class).play(story.getFirstSceneId())).withRel("startStory").withType(HttpMethod.GET.name()));
            } else {
                storyDto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StoryController.class).addFirstSceneToStory(story.getId(), null, null)).withRel("addFirstScene").withType(HttpMethod.POST.name()));
            }
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

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PostMapping
    public ResponseEntity<MessageDto> createStory(@RequestBody Story story, Authentication authentication) throws StoryNotFoundException, SceneAlreadyExistsException, BadOwnerStoryException {
        User user = (User) authentication.getPrincipal();
        story.setCreatedBy(user.getUsername());
        Story newStory = storyService.createNewStory(story);
        MessageDto message = new MessageDto("Your story is ready, now add some scenes.");
        message.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(StoryController.class)
                                .addFirstSceneToStory(newStory.getId(), null, authentication))
                        .withRel(LinkType.ADD_FIRST_SCENE.REL)
                        .withType(LinkType.ADD_FIRST_SCENE.METHOD.name()));

        message.addDocumentation(LinkType.ADD_FIRST_SCENE);

        return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PostMapping(path = "/{storyId}/scene")
    public ResponseEntity<?> addFirstSceneToStory(@PathVariable String storyId, @RequestBody Scene scene, Authentication authentication) throws StoryNotFoundException, SceneAlreadyExistsException, BadOwnerStoryException {

            User user = (User) authentication.getPrincipal();
            Story updatedStory = storyService.addSceneToStory(storyId, scene, user.getUsername());
            StoryDto storyDto = new StoryDto(updatedStory.getTitle(), updatedStory.getDescription());
            storyDto.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SceneController.class).getScene(updatedStory.getFirstSceneId())).withRel("getFirstScene").withType(HttpMethod.GET.name()));
            return ResponseEntity.ok(storyDto);

    }

}
