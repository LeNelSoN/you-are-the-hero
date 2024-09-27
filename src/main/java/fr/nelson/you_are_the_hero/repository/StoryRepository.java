package fr.nelson.you_are_the_hero.repository;

import fr.nelson.you_are_the_hero.model.db.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends MongoRepository<Story, String> {
}
