package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.exception.AdminAlreadyExistException;
import fr.nelson.you_are_the_hero.exception.UserAllreadyExistException;
import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.db.Role;
import fr.nelson.you_are_the_hero.model.dto.AuthRequestDto;
import fr.nelson.you_are_the_hero.model.dto.UserDto;
import fr.nelson.you_are_the_hero.repository.AppUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static fr.nelson.you_are_the_hero.model.db.Role.PLAYER;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = findAppUserByUsername(username);

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole().name())
                .build();
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return StringUtils.defaultString(authentication != null && authentication.isAuthenticated() ? authentication.getName() : null);
    }

    public AppUser saveUser(AuthRequestDto authRequestDto) throws UserAllreadyExistException {
        if (appUserRepository.existsByUsername(authRequestDto.getUsername())) {
            throw new UserAllreadyExistException("user allready exist");
        }
        AppUser newUser = new AppUser();
        newUser.setUsername(authRequestDto.getUsername());
        newUser.setPassword(authRequestDto.getPassword());
        newUser.setRole(PLAYER);
        newUser.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
        return appUserRepository.save(newUser);
    }

    public AppUser findAppUserByUsername(String username){
        AppUser appUser = appUserRepository.findByUsername(username);

        if(appUser == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return appUser;
    }

    public AppUser createAdminUser(UserDto UserDto) throws AdminAlreadyExistException, UsernameNotFoundException{
        if(isAlreadyAnAdmin()){
            throw new AdminAlreadyExistException("Admin allready exist");
        }

        AppUser appUser = appUserRepository.findByUsername(UserDto.getUsername());

        if(appUser == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        appUser.setRole(Role.ADMIN);

        return appUserRepository.save(appUser);
    }

    public boolean isAlreadyAnAdmin() {
        return appUserRepository.existsByRole(Role.ADMIN);
    }

    public void deleteByUsername(String username, User userConnected) throws BadRequestException {
        if(StringUtils.isEmpty(username) || Objects.isNull(userConnected) || StringUtils.isEmpty(userConnected.getUsername())) {
            throw new BadRequestException("EMPTY_PARAMETER");
        }
        AppUser user = this.appUserRepository.findByUsername(userConnected.getUsername());
        if(user.getUsername().equals(username) || Role.ADMIN.name().equals(user.getRole().name())) {
            user.setDeleted(true);
            this.appUserRepository.save(user);
        }
    }

    public AppUser promoteUserToEditor(String username) throws BadRequestException {
        if(StringUtils.isEmpty(username)) {
            throw new BadRequestException("EMPTY_PARAMETER");
        }

        AppUser userToPromote = findAppUserByUsername(username);
        if (userToPromote.getRole() == Role.EDITOR) {
            throw new BadRequestException("USER_ALREADY_EDITOR");
        }

        userToPromote.setRole(Role.EDITOR);
        return appUserRepository.save(userToPromote);
    }

}
