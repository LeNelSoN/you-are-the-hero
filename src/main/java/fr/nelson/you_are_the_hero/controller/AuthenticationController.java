package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.dto.AuthRequestDto;
import fr.nelson.you_are_the_hero.model.dto.UserDto;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.model.dto.template.StoryTemplateDto;
import fr.nelson.you_are_the_hero.model.dto.template.UserTemplateDto;
import fr.nelson.you_are_the_hero.service.AuthenticationService;
import fr.nelson.you_are_the_hero.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public  ResponseEntity<MessageDto> getInfo(){
        MessageDto message = new MessageDto("Links for account creation and authentication");

        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).getTemplate())
                        .withRel("userTemplate")
                        .withType(HttpMethod.GET.name())
        );

        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticateUser(null))
                        .withRel("authUser")
                        .withType(HttpMethod.POST.name())
        );

        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).registerUser(null))
                        .withRel("register")
                        .withType(HttpMethod.POST.name())
        );

        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/template")
    public ResponseEntity<UserTemplateDto> getTemplate(){
        UserTemplateDto template = new UserTemplateDto("Your Story username", "Your protected password");
        return ResponseEntity.ok(template);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDto authRequest) {
        try {
            return ResponseEntity.ok(authenticationService.authenticateUser(authRequest.getUsername(), authRequest.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password" + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequestDto authRequestDto) {

        try{
            AppUser registredUser = userService.saveUser(authRequestDto);
            UserDto userDto = new UserDto(registredUser.getUsername());
            userDto.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticateUser(null))
                    .withRel("authUser")
                    .withType(HttpMethod.POST.name())
            );
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("An error occurred while registering the user: " + e.getMessage());
        }
    }

}
