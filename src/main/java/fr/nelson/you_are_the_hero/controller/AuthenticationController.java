package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.exception.InvalidCredentialsException;
import fr.nelson.you_are_the_hero.exception.AdminAlreadyExistException;
import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.dto.AuthRequestDto;
import fr.nelson.you_are_the_hero.model.dto.AuthResponseDto;
import fr.nelson.you_are_the_hero.model.dto.RefreshTokenDto;
import fr.nelson.you_are_the_hero.model.dto.UserDto;
import fr.nelson.you_are_the_hero.model.dto.message.MessageDto;
import fr.nelson.you_are_the_hero.model.hateoas.LinkType;
import fr.nelson.you_are_the_hero.service.AuthenticationService;
import fr.nelson.you_are_the_hero.service.UserService;
import org.springframework.http.HttpStatus;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticateUser(null))
                        .withRel(LinkType.AUTH_USER.REL)
                        .withType(LinkType.AUTH_USER.METHOD.name())
        );
        message.addDocumentation(LinkType.AUTH_USER);

        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).registerUser(null))
                        .withRel(LinkType.REGISTER_USER.REL)
                        .withType(LinkType.REGISTER_USER.METHOD.name())
        );
        message.addDocumentation(LinkType.REGISTER_USER);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticateUser(@RequestBody AuthRequestDto authRequest) throws InvalidCredentialsException, BadRequestException {
        return ResponseEntity.ok(authenticationService.
                authenticateUser(authRequest.getUsername(), authRequest.getPassword()));
    }
    
    @PostMapping("/register")
    public ResponseEntity<MessageDto> registerUser(@RequestBody AuthRequestDto authRequestDto) throws InvalidCredentialsException, BadRequestException {
        AppUser registeredUser = userService.saveUser(authRequestDto);
        MessageDto message = new MessageDto("Your account has been successfully created, " + registeredUser.getUsername() + ", you can now log in.");

        message.add(
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(AuthenticationController.class).authenticateUser(null))
                        .withRel(LinkType.AUTH_USER.REL)
                        .withType(LinkType.AUTH_USER.METHOD.name())
        );
        message.addDocumentation(LinkType.AUTH_USER);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
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

    @PatchMapping("/editor/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AppUser> updateUserRoleToEditor(@PathVariable String username) throws BadRequestException {
        AppUser promotedUser = this.userService.promoteUserToEditor(username);
        return ResponseEntity.ok(promotedUser);
    }

    @DeleteMapping("/users/{username}")
    @PreAuthorize("hasRole('ROLE_PLAYER')")
    public ResponseEntity<?> deleteUser(@PathVariable String username, Authentication authentication) throws BadRequestException {
        this.userService.deleteByUsername(username, (User) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
