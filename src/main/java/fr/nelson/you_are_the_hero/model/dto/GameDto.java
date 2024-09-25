package fr.nelson.you_are_the_hero.dto;

import org.springframework.hateoas.RepresentationModel;

public class GameDto extends RepresentationModel<SceneDto> {
    private String description;

    public GameDto(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
