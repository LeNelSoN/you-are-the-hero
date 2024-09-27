package fr.nelson.you_are_the_hero.model;

public class Choice {
    private String description;
    private String nextSceneId;

    public Choice() {
    }

    public Choice(String description, String nextSceneId) {
        this.description = description;
        this.nextSceneId = nextSceneId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNextSceneId() {
        return nextSceneId;
    }

    public void setNextSceneId(String nextSceneId) {
        this.nextSceneId = nextSceneId;
    }
}
