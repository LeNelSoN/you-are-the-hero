package fr.nelson.you_are_the_hero.model.db;

import fr.nelson.you_are_the_hero.model.Choice;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Scene {

    @Id
    private String id;
    private String previousSceneId;
    private String description;
    private List<Choice> choices = new ArrayList<>();

    public Scene() {
    }

    public Scene(String description) {
        this.description = description;
    }

    public Scene(String description, String previousSceneId) {
        this.description = description;
        this.previousSceneId = previousSceneId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreviousSceneId() {
        return previousSceneId;
    }

    public void setPreviousSceneId(String previousSceneId) {
        this.previousSceneId = previousSceneId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addChoice(Choice choice) {
        this.choices.add(choice);
    }
}

