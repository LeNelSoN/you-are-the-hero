package fr.nelson.you_are_the_hero.model.dto;

public class GameDto extends AbstractHateoasDto<SceneDto> {
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
