package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.BadOwnerStoryException;
import fr.nelson.you_are_the_hero.exception.InvalidCredentialsException;
import fr.nelson.you_are_the_hero.exception.SceneAlreadyExistsException;
import fr.nelson.you_are_the_hero.exception.StoryNotFoundException;
import fr.nelson.you_are_the_hero.model.db.Role;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.service.AuthenticationService;
import fr.nelson.you_are_the_hero.model.hateoas.LinkType;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class InfoController {

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<MessageDto> getInfo(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) throws InvalidCredentialsException, BadRequestException, StoryNotFoundException, SceneAlreadyExistsException, BadOwnerStoryException {
        String welcomeMsg = "Welcome, Traveler. You are about to embark on a journey through magical realms full of mysteries. As the Pilgrim, you'll explore forgotten kingdoms and uncover ancient secrets. Each world offers unique challenges. Your skills and spirit will guide you. The adventure begins now step into the unknown, where legends come to life and the fate of worlds is in your hands.";

        MessageDto message = new MessageDto(welcomeMsg);
        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).getInfo())
                        .withRel(LinkType.AUTH_INFO.REL)
                        .withType(LinkType.AUTH_INFO.METHOD.name())
        );
        message.addDocumentation(LinkType.AUTH_INFO);

        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(StoryController.class).getAllStory())
                        .withRel(LinkType.GET_ALL_STORIES.REL)
                        .withType(LinkType.GET_ALL_STORIES.METHOD.name())
        );
        message.addDocumentation(LinkType.GET_ALL_STORIES);

        if(authenticationService.hasRole(Role.EDITOR)){
            message.add(
                    WebMvcLinkBuilder
                            .linkTo(WebMvcLinkBuilder.methodOn(StoryController.class).createStory(null, null))
                            .withRel(LinkType.CREATE_STORY.REL)
                            .withType(LinkType.CREATE_STORY.METHOD.name())
            );
            message.addDocumentation(LinkType.CREATE_STORY);
        }
        return ResponseEntity.ok(message);
    }
}
