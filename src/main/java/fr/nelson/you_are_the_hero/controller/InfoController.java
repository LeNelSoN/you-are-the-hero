package fr.nelson.you_are_the_hero.controller;

import fr.nelson.you_are_the_hero.dto.InfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class InfoController {

    @GetMapping
    public ResponseEntity<InfoDto> getInfo() {
        InfoDto info = new InfoDto("Bonjour et bienvenue Hero !!!");
        return ResponseEntity.ok(info);
    }
}
