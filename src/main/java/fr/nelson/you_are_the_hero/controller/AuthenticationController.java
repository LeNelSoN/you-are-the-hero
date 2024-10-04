package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.InvalidTokenException;
import fr.nelson.you_are_the_hero.exception.TokenExpiredException;
import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.dto.AuthRequestDto;
import fr.nelson.you_are_the_hero.model.dto.AuthResponseDto;
import fr.nelson.you_are_the_hero.model.dto.RefreshTokenDto;
import fr.nelson.you_are_the_hero.model.dto.UserDto;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.model.dto.template.UserTemplateDto;
import fr.nelson.you_are_the_hero.service.AuthenticationService;
import fr.nelson.you_are_the_hero.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        MessageDto messageDto = new MessageDto();
        messageDto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticateUser(null))
                .withRel("authUser")
                .withType(HttpMethod.POST.name()));
        try{
            AuthResponseDto newAccessToken = authenticationService.refreshAccessToken(refreshTokenDto.getToken());
            return ResponseEntity.ok(newAccessToken);
        } catch (InvalidTokenException e) {
            messageDto.setMessage("Invalid refresh token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageDto);
        } catch (TokenExpiredException e) {
            messageDto.setMessage("Token expired: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageDto);
        } catch (UsernameNotFoundException e) {
            messageDto.setMessage("User not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred : " + e.getMessage());
        }

    }

}
