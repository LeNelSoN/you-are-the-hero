package fr.nelson.you_are_the_hero.repository;

import fr.nelson.you_are_the_hero.model.db.AppUser;
import fr.nelson.you_are_the_hero.model.db.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends MongoRepository<AppUser, String> {
    AppUser findByUsername(String username);
    boolean existsByRole(Role role);
    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}

