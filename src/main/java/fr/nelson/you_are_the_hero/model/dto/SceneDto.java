package fr.nelson.you_are_the_hero.model.dto;

import fr.nelson.you_are_the_hero.model.Choice;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class SceneDto extends RepresentationModel<SceneDto> {
    private String description;
    private List<Choice> choices;

    public SceneDto(String description, List<Choice> choices) {
        this.description = description;
        this.choices = choices;
    }

    public SceneDto() {
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
}
