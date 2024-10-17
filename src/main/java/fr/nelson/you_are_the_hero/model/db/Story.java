package fr.nelson.you_are_the_hero.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stories")
public class Story {
    @Id
    private String id;
    private String title;
    private String description;
    private String firstSceneId;

    public Story() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstSceneId() {
        return firstSceneId;
    }

    public void setFirstSceneId(String firstSceneId) {
        this.firstSceneId = firstSceneId;
    }

}
