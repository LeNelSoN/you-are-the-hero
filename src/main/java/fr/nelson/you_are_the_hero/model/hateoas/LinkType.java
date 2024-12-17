package fr.nelson.you_are_the_hero.model.hateoas;

import fr.nelson.you_are_the_hero.model.dto.template.*;
import org.springframework.http.HttpMethod;

public enum LinkType {
    AUTH_INFO("Authentication Information", "Receive the information to sign up and create an account.", HttpMethod.GET, null, "application/json", null, null),
    AUTH_USER("login","Authenticate user", "Authenticate the user with correct credentials and receive accessToken and refreshToken",HttpMethod.POST, "application/json", "application/json", null, new UserTemplateDto("Your username", "Your secured password")),
    REGISTER_USER("register","Register user", "Create new user for the application", HttpMethod.POST, "application/json", "application/json", null, new UserTemplateDto("Your username", "Your secured password")),
    GET_ALL_STORIES("Browse Available Stories", "Get all the stories.", HttpMethod.GET, null, "application/json", null, null),
    CREATE_STORY("Create New Story", "Create a new story with the correct parameters.", HttpMethod.POST, "application/json", "application/json", null, new StoryTemplateDto("Your Story title", "A description of your story")),
    ADD_FIRST_SCENE("Add first scene of the story", "If you are the author you can add the first scene of this story", HttpMethod.POST, "application/json", "application/json", "Authorization: Bearer <JWT with role: editor>", new SceneTemplateDto("A description of your scene")),
    ADD_SCENE("Add Next Scene", "Add a new scene to this actual scene", HttpMethod.POST,"application/json", "application/json","Authorization: Bearer <JWT with role: editor>", new AddSceneTemplateDto("A description of your scene", "The description of the choice")),
    NEXT_SCENE("Get Next Scene", "Receive the next scene of the story", HttpMethod.GET, "application/json", "application/json", "Authorization: Bearer <JWT with role: editor>", null),
    PREVIOUS_SCENE("Previous scene", "Receive the previous scene of this scene",  HttpMethod.GET, null, "application/json", "Authorization: Bearer <JWT with role: editor>", null),
    UPDATE_DESCRIPTION("Change description of a scene", "With the correct template change the description of the scene for the user", HttpMethod.PUT, "application/json", "application/json", "Authorization: Bearer <JWT with role: editor>", new SceneTemplateDto("A new description of your scene")),
    DELETE_SCENE("Delete description", "Delete Scene of a story", HttpMethod.DELETE, null, null, "Authorization: Bearer <JWT with role: editor>", null),
    UPDATE_CHOICE(null, "Modify the description of the choice", HttpMethod.PUT, null, "application/json", "Authorization: Bearer <JWT with role: editor>", null),
    NEXT_GAME_SCENE("Next Scene of the story", "Continue the story with the next scene", HttpMethod.GET, null, "application/json", null, null),
    START_STORY("Start to play this story", "Play this story in game mode", HttpMethod.GET, null, "application/json",  null, null);


    public final String REL;
    public final String TITLE;
    public final String DESCRIPTION;
    public final HttpMethod METHOD;
    public final String REQUEST_FORMAT;
    public final String RESPONSE_FORMAT;
    public final String REQUIRED_HEADERS;
    public final TemplateDTO REQUEST_BODY_TEMPLATE;

    LinkType(String TITLE, String DESCRIPTION, HttpMethod METHOD, String REQUEST_FORMAT, String RESPONSE_FORMAT, String REQUIRED_HEADERS, TemplateDTO REQUEST_BODY_TEMPLATE) {
        this.REL = "next";
        this.TITLE = TITLE;
        this.DESCRIPTION = DESCRIPTION;
        this.METHOD = METHOD;
        this.REQUEST_FORMAT = REQUEST_FORMAT;
        this.RESPONSE_FORMAT = RESPONSE_FORMAT;
        this.REQUIRED_HEADERS = REQUIRED_HEADERS;
        this.REQUEST_BODY_TEMPLATE = REQUEST_BODY_TEMPLATE;
    }

    LinkType(String REL, String TITLE, String DESCRIPTION, HttpMethod METHOD, String REQUEST_FORMAT, String RESPONSE_FORMAT, String REQUIRED_HEADERS, TemplateDTO REQUEST_BODY_TEMPLATE) {
        this.REL = REL;
        this.TITLE = TITLE;
        this.DESCRIPTION = DESCRIPTION;
        this.METHOD = METHOD;
        this.REQUEST_FORMAT = REQUEST_FORMAT;
        this.RESPONSE_FORMAT = RESPONSE_FORMAT;
        this.REQUIRED_HEADERS = REQUIRED_HEADERS;
        this.REQUEST_BODY_TEMPLATE = REQUEST_BODY_TEMPLATE;
    }


}
