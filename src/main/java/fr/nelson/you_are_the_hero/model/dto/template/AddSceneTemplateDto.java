package fr.nelson.you_are_the_hero.model.dto.template;

public class AddSceneTemplateDto extends TemplateDTO<AddSceneTemplateDto>{
    private String description;
    private String choice;

    public AddSceneTemplateDto(String description, String choice) {
        this.description = description;
        this.choice = choice;
    }

    public AddSceneTemplateDto() {
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

    @Override
    public String toString() {
        return "{" +
                "description='" + description + '\'' +
                ", choice='" + choice + '\'' +
                '}';
    }
}
