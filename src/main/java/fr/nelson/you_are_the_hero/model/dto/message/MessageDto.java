package fr.nelson.you_are_the_hero.dto.message;

import org.springframework.hateoas.RepresentationModel;

public class MessageDto extends RepresentationModel<MessageDto> {
    private String messageFR;
    private String messageEN;

    public MessageDto(String messageFR, String messageEN) {
        this.messageFR = messageFR;
        this.messageEN = messageEN;
    }

    public String getMessageFR() {
        return messageFR;
    }

    public void setMessageFR(String messageFR) {
        this.messageFR = messageFR;
    }

    public String getMessageEN() {
        return messageEN;
    }

    public void setMessageEN(String messageEN) {
        this.messageEN = messageEN;
    }
}
