package fr.nelson.you_are_the_hero.dto.template;

import org.springframework.hateoas.RepresentationModel;

public class StoryTemplateDto extends RepresentationModel<StoryTemplateDto> {
    private String title;
    private String description;

    public StoryTemplateDto(String title, String description)  {
        this.title = title;
        this.description = description;
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
