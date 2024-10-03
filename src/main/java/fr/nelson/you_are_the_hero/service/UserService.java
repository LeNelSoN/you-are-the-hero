package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.dto.AuthRequestDto;
import fr.nelson.you_are_the_hero.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import static fr.nelson.you_are_the_hero.model.db.Role.PLAYER;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);

        if(appUser == null) {
            throw new UsernameNotFoundException("User Not Found");
        }

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .build();
    }

    public AppUser saveUser(AuthRequestDto authRequestDto) throws Exception {
        if (appUserRepository.existsByUsername(authRequestDto.getUsername())) {
            throw new Exception("user allready exist");
        }
        AppUser newUser = new AppUser();
        newUser.setUsername(authRequestDto.getUsername());
        newUser.setPassword(authRequestDto.getPassword());
        newUser.setRole(PLAYER.name());
        newUser.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
        return appUserRepository.save(newUser);
    }

}
