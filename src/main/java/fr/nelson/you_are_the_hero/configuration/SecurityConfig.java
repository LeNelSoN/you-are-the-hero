package fr.nelson.you_are_the_hero.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Désactiver la protection CSRF pour les tests d'API
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Nécessite une authentification pour tous les endpoints
                )
                .httpBasic();  // Utiliser HTTP Basic pour l'authentification

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withDefaultPasswordEncoder() // Créer un utilisateur en mémoire
                .username("user")  // Nom d'utilisateur
                .password("password")  // Mot de passe
                .roles("USER")  // Rôle de l'utilisateur
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}

