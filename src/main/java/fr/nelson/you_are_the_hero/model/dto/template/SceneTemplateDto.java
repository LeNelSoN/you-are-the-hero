package fr.nelson.you_are_the_hero.dto.template;

import org.springframework.hateoas.RepresentationModel;

import java.util.Map;

public class SceneTemplateDto {
    private String description;

    public SceneTemplateDto(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
