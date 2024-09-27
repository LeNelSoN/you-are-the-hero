package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class InfoController {

    @GetMapping
    public ResponseEntity<MessageDto> getInfo() {
        String welcomeMsg = "Welcome, Traveler. You are about to embark on a journey through magical realms full of mysteries. As the Pilgrim, you'll explore forgotten kingdoms and uncover ancient secrets. Each world offers unique challenges. Your skills and spirit will guide you. The adventure begins now step into the unknown, where legends come to life and the fate of worlds is in your hands.";

        MessageDto message = new MessageDto(welcomeMsg);
        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).getInfo())
                        .withRel("authenticationInfo")
                        .withType(HttpMethod.GET.name())
        );

        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(StoryController.class).getAllStory())
                        .withRel("getAllStory")
                        .withType(HttpMethod.GET.name())
        );

        return ResponseEntity.ok(message);
    }
}
