package com.controller;


import com.dao.GameDaoImpl;
import com.model.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/game")
public class Controller {

    private final GameDaoImpl dao;

    public Controller(GameDaoImpl dao) {
        this.dao = dao;
    }

    @PostMapping
    @RequestMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game begin(@RequestBody Game game){
       return dao.addGame(game);
    }


    //USER POSTS A GUESS, THEN THE GAME COMPARES THE GUESS WITH THE ANSWER
    @PostMapping
    @RequestMapping("/guess")
    public String[] getGuess(int gameID, String userGuess){
       String result = dao.compareGuess(gameID, userGuess);
       return new String[]{result};
    }

    //TEST MESSAGE TO SEE THAT WE ARE CONNECTED
    @GetMapping
    public String[] HelloWorld(){
        String[] result = {"Hello", "World"};
        return result;
    }

    //GETS A SINGLE GAME OBJECT -- ID STARTS AFTER 10 SO CARE
    @GetMapping("/get/{game_id}")
    public ResponseEntity<Game> findById(@PathVariable int game_id) {
        Game result = dao.getAGamebyID(game_id);
        if (result == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }


    //WE GET A LIST OF ALL THE GAMES/ HIDDEN GAME ANSWER NOT ADDED
    @GetMapping
    @RequestMapping("/getall")
    public List<Game> all() {
        return dao.getAll();
    }

}
