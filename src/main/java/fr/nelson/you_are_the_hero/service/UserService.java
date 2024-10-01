package fr.nelson.you_are_the_hero.service;

import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public AppUser saveUser(AppUser appUser) throws Exception {
        AppUser existingUser = appUserRepository.findByUsername(appUser.getUsername());
        if (existingUser != null) {
            throw new Exception("user allready exist");
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    public AppUser findAppUserByUsername(String username){
        return appUserRepository.findByUsername(username);
    }

}
