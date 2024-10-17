package fr.nelson.you_are_the_hero.model.dto.template;

import org.springframework.hateoas.RepresentationModel;

public class UserTemplateDto extends RepresentationModel<UserTemplateDto> {
    private String username;
    private String password;

    public UserTemplateDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
