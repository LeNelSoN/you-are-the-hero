package fr.nelson.you_are_the_hero.model.dto.message;

import fr.nelson.you_are_the_hero.model.dto.AbstractHateoasDto;

public class MessageDto extends AbstractHateoasDto<MessageDto> {
    private String message;

    public MessageDto() {
    }

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
