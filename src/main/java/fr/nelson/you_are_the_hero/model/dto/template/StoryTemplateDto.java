package fr.nelson.you_are_the_hero.model.dto.template;

public class StoryTemplateDto extends TemplateDTO<StoryTemplateDto> {
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

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
