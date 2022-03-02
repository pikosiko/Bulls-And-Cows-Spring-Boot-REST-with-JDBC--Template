package com.model;

import java.util.Objects;

public class Game {

    private int id;
    private String answer;
    private int numberOfGuesses;
    private boolean isWon = false;

    //USE DEFAULT CONSTRUCTOR

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && numberOfGuesses == game.numberOfGuesses && isWon == game.isWon && Objects.equals(answer, game.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer, numberOfGuesses, isWon);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    public void setNumberOfGuesses(int numberOfGuesses) {
        this.numberOfGuesses = numberOfGuesses;
    }

    public boolean isWon() {
        return isWon;
    }

    public void setWon(boolean won) {
        isWon = won;
    }
}
