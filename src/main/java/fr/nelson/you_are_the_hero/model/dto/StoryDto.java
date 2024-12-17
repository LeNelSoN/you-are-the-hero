package fr.nelson.you_are_the_hero.model.dto;

import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "stories")
public class StoryDto extends AbstractHateoasDto<StoryDto> {
    private String title;
    private String description;

    public StoryDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public StoryDto() {
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

}
