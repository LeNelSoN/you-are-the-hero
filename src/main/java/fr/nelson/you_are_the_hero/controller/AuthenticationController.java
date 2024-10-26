package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.InvalidCredentialsException;
import fr.nelson.you_are_the_hero.exception.AdminAlreadyExistException;
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
import org.springframework.http.HttpStatus;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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
    public  ResponseEntity<MessageDto> getInfo() throws InvalidCredentialsException, BadRequestException {
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
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDto authRequest) throws InvalidCredentialsException, BadRequestException {

            return ResponseEntity.ok(authenticationService.
                    authenticateUser(authRequest.getUsername(), authRequest.getPassword()));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequestDto authRequestDto) throws InvalidCredentialsException, BadRequestException {

            AppUser registredUser = userService.saveUser(authRequestDto);
            UserDto userDto = new UserDto(registredUser.getUsername());
            userDto.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticateUser(null))
                    .withRel("authUser")
                    .withType(HttpMethod.POST.name())
            );
            return ResponseEntity.ok(userDto);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        MessageDto messageDto = new MessageDto();
        messageDto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticateUser(null))
                .withRel("authUser")
                .withType(HttpMethod.POST.name()));

            AuthResponseDto newAccessToken = authenticationService.refreshAccessToken(refreshTokenDto.getToken());
            return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(@RequestBody UserDto userDto){
        MessageDto messageDto = new MessageDto();
        try{
            AppUser appUser = userService.createAdminUser(userDto);
            messageDto.setMessage(appUser.getUsername() + " is now an admin");
            return ResponseEntity.ok(messageDto);
        } catch (AdminAlreadyExistException e) {
            messageDto.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(messageDto);
        } catch (UsernameNotFoundException e) {
            messageDto.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageDto);
        }

    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasRole('ROLE_PLAYER')")
    public ResponseEntity<?> deleteUser(@PathVariable String username, Authentication authentication) throws BadRequestException {
        this.userService.deleteByUsername(username, (User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
