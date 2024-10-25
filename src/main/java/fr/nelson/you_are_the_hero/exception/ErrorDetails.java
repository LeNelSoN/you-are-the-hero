package fr.nelson.you_are_the_hero.exception;

import java.util.Date;

public class ErrorDetails {

    private String message;
    private String details;
    private Date timestamp;

    public ErrorDetails(String message,String details){
        this.timestamp=new Date();
        this.message=message;
        this.details=details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
