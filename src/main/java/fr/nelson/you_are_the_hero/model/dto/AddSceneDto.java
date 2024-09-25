package fr.nelson.you_are_the_hero.dto;

import org.springframework.hateoas.RepresentationModel;

public class AddSceneDto extends RepresentationModel<AddSceneDto> {
    private String description;
    private String choice;

    public AddSceneDto(String description, String choice) {
        this.description = description;
        this.choice = choice;
    }

    public AddSceneDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
