package fr.nelson.you_are_the_hero.model.dto;

import org.springframework.hateoas.RepresentationModel;

public class UserDto  extends RepresentationModel<StoryDto> {
    String username;

    public UserDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
