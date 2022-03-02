package com.dao;

import com.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Repository
public class GameDaoImpl {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private static final List<Game> games = new ArrayList<>();


    //SHOWS ALL THE GAMES AND THEIR INFO IN THE DB
    public List<Game> getAll() {
        List<Game> games = new ArrayList<>();
        final String sql = "SELECT game_id, numberOfGuesses, answer, isWon FROM game;";
        games = jdbcTemplate.query(sql, new GameMapper());
        for (Game g: games){
            if(g.isWon() == false){
                g.setAnswer("****");
            }
        }
        return games;
    }


    @Transactional
    public void deleteGameById(int id) {
        final String DELETE_GAME = "DELETE FROM game "
                + "WHERE game_id = ?";
        jdbcTemplate.update(DELETE_GAME, id);

    }



    //WE GET THE GAME BY ID
    public Game getAGamebyID(int gameID){
        final String sql = "SELECT game_id, numberOfGuesses, answer, isWon" +" FROM game WHERE game_id=?;";
        Game game; //= new Game();
        game = jdbcTemplate.queryForObject(sql, new GameMapper(), gameID);
        if(game.isWon() == false){
            game.setAnswer("****");
        }
        return game;
    }


    //HELPER METHOD USED AFTER WE MAKE A COMPARISON
    public boolean update(Game game) {

        final String sql = "UPDATE game SET "
                + "numberOfGuesses = ?, "
                + "isWon = ?, "
                + "answer = ? "
                + "WHERE game_id = ?;";

        return jdbcTemplate.update(sql,
                game.getNumberOfGuesses(),
                game.isWon(),
                game.getAnswer(),
                game.getId()) > 0;
    }


    //GAME LOGIC AFTER THE POST REQUEST --
    // WE GET THE ID AND CREATE THE GAME OBJECT,
    // WE GET THE ANSWER FROM THE GAME AND THE USER,
    // TURN IT TO AN ARRAYLIST AND APPLY THE GAME LOGIC
    public String compareGuess(int gameID, String guess){

        int bulls = 0;
        int cows = 0;

        //SAFETY PRINT TO SEE THAT WE START WITH 0 System.out.println(bulls + " " +cows);
        //Get the game by the id given and get the answer
        Game game = getAGamebyID(gameID);
        String gameAns = game.getAnswer();

        //ARRAYLISTS USED TO COMPARE
        ArrayList<Integer> secretList = new ArrayList<Integer>();
        ArrayList<Integer> guessList = new ArrayList<Integer>();


        //TURN THE STRINGS INTO INT ARRAY LISTS
        for (int charsG = 0; charsG < 4; charsG++) {
            char userGuessChar = guess.charAt(charsG);
            guessList.add(Integer.parseInt(String.valueOf(userGuessChar)));
        }
        for (int charsS = 0; charsS < 4; charsS++) {
            char gameSecretAnswer = gameAns.charAt(charsS);
            secretList.add(Integer.parseInt(String.valueOf(gameSecretAnswer)));
        }

        /* SAFETY PRINTS TO MAKE SURE WE GET THE CORRECT LIST
        System.out.println(secretList);
        System.out.println(guessList);
*/
        //COMPARE THE ANSWER AND THE GUESS
        for (int elements = 0; elements < 4; elements++) {
            if (guessList.get(elements) == secretList.get(elements)) {
                bulls += 1;
            } else if (secretList.contains(guessList.get(elements))) {
                cows += 1;
            }
        }
        //STRING THAT WE PRESENT IN POSTMAN
        String bullCow = "b: " + bulls + " c: " + cows;

        //ADD ANOTHER GAME TRY NO MATTER WHAT THE OUTCOME IS
        game.setNumberOfGuesses((game.getNumberOfGuesses() + 1));

        //IF BULLS ARE 4 WE WIN ELSE WE GET RESET THE BULLS AND COWS TO 0 AND EMPTY THE ARRAYLIST
        if (bulls == 4) {
            game.setWon(true);
            System.out.println("You won!!! Number of tries: " + game.getNumberOfGuesses());
        } else {
            System.out.println(bulls + " Bulls and " + cows + " Cows");
            bulls = 0;
            cows = 0;
            guessList.clear();
        }

        //UPDATE THE GAME IN THE DATABASE
        update(game);

        //RETURN THE STRING WITH THE NUMBER OF BULLS AND COWS
        return bullCow;


    }


    //START NEW GAME --WE CREATE A NEW GAME AND GENERATE AN ANSWER
    //ID IS A BIT WEIRD BECAUSE I DELETED SOME ELEMENTS FROM THE DB AFTER I CREATED THEM, BUT THE ID KEPT GOING UP
    //FIXABLE BUT I HAVE TO FIGURE OUT HOW TO
    public Game addGame(Game game) {

        ArrayList<Integer> secretList = new ArrayList<>();

        String randomAnswer= "";

        Random rand = new Random();

        final String sql = "INSERT INTO game(numberOfGuesses, answer) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        //FILLS ARRAYLIST WITH RANDOMS
        int i = 0;
        while (i < 4) {
            int secretInt = rand.nextInt(10);
            if (secretList.contains(secretInt) == false) {
                secretList.add(secretInt);
                i += 1;
            }
        }
        //TURNS ARRAYLIST NUMBERS TO A STRING
        for(int j=0; j<4;j++){
            int item = secretList.get(j);
            // SAFETYY PRINT System.out.println(item);
            randomAnswer = randomAnswer + String.valueOf(item);
        }

        game.setAnswer(randomAnswer);
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, game.getNumberOfGuesses());
            statement.setString(2, game.getAnswer());
            return statement;

        }, keyHolder);

        game.setId(keyHolder.getKey().intValue());

        return game;
    }

    //CUSTOM MAPPER
    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setId(rs.getInt("game_id"));
            game.setNumberOfGuesses(rs.getInt("numberOfGuesses"));
            game.setAnswer(rs.getString("answer"));
            game.setWon(rs.getBoolean("isWon"));
            return game;
        }
    }
}
