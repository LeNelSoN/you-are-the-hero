package fr.nelson.you_are_the_hero.utility;

import fr.nelson.you_are_the_hero.model.dto.template.TemplateDTO;
import fr.nelson.you_are_the_hero.model.hateoas.LinkType;

import java.util.HashMap;
import java.util.Map;

public class ApiDocumentation {

    private String title;
    private String description;
    private String requiredHeaders;
    private String requestFormat;
    private TemplateDTO requestBodyTemplate;
    private String responseFormat;

    private ApiDocumentation() {}

    public ApiDocumentation(String title, String description, String requiredHeaders, String requestFormat, TemplateDTO requestBodyTemplate, String responseFormat) {
        this.title = title;
        this.description = description;
        this.requiredHeaders = requiredHeaders;
        this.requestFormat = requestFormat;
        this.requestBodyTemplate = requestBodyTemplate;
        this.responseFormat = responseFormat;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequiredHeaders() {
        return requiredHeaders;
    }

    public String getRequestFormat() {
        return requestFormat;
    }

    public TemplateDTO getRequestBodyTemplate() {
        return requestBodyTemplate;
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    public static class ApiDocumentationBuilder {

        private ApiDocumentation documentation;

        public ApiDocumentationBuilder() {
            documentation = new ApiDocumentation();
        }

        public ApiDocumentationBuilder(LinkType linkType) {
            documentation = new ApiDocumentation(linkType.TITLE, linkType.DESCRIPTION, linkType.REQUIRED_HEADERS, linkType.REQUEST_FORMAT, linkType.REQUEST_BODY_TEMPLATE, linkType.RESPONSE_FORMAT);
        }

        public Map<String, String> build() {
            Map<String, String> actionMap = new HashMap<>();
            if (documentation.title != null) {
                actionMap.put("title", documentation.title);
            }
            if (documentation.description != null){
                actionMap.put("description", documentation.description);
            }
            if (documentation.requiredHeaders != null){
                actionMap.put("required_headers", documentation.requiredHeaders);
            }
            if (documentation.requestFormat != null) {
                actionMap.put("request_format", documentation.requestFormat);
            }
            if (documentation.requestBodyTemplate != null) {
                actionMap.put("request_body_template", documentation.requestBodyTemplate.toString());
            }
            if (documentation.requestFormat != null) {
                actionMap.put("response_format", documentation.responseFormat);
            }
            return actionMap;
        }
    }
}
