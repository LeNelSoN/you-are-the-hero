package fr.nelson.you_are_the_hero.repository;

import fr.nelson.you_are_the_hero.model.db.Scene;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SceneRepository extends MongoRepository<Scene, String> {
    boolean existsByPreviousSceneId(String previousSceneId);
}
