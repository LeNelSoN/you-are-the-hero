package fr.nelson.you_are_the_hero.model.dto;

import fr.nelson.you_are_the_hero.utility.ApiDocumentation.ApiDocumentationBuilder;
import fr.nelson.you_are_the_hero.model.hateoas.LinkType;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHateoasDto<T extends AbstractHateoasDto<T>> extends RepresentationModel<T> {
    private Map<String, Map<String, String>> linkDocumentation = new HashMap<>();

    public void addDocumentation(LinkType linkType) {
        linkDocumentation.put(linkType.REL, new ApiDocumentationBuilder(linkType).build());
    }

    public Map<String, Map<String, String>> getLinkDocumentation() {
        return linkDocumentation;
    }
}