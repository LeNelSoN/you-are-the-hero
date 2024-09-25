package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.model.Story;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/game")
public class GameController {
    @GetMapping(path = "/start")
    public void startGame(){
        // Commence une nouvelle story au hasard
    }

    @PostMapping
    public ResponseEntity<Story> createStory(){

    }
}
