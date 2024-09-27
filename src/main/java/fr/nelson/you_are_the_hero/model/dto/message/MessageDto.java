package fr.nelson.you_are_the_hero.model.dto.message;

import org.springframework.hateoas.RepresentationModel;

public class MessageDto extends RepresentationModel<MessageDto> {
    private String message;

    public MessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
