package accountInfo;

import patterns.Information;
import entities.characters.Character;

import java.util.ArrayList;

public class Account {
    private Information playerInfo;
    private ArrayList<Character> characters;
    private Integer numberOfGames;

    public Account(ArrayList<Character> characters, int gamesNumber, Information information) {
        this.characters = characters;
        this.numberOfGames = gamesNumber;
        this.playerInfo = information;
    }
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public Information getPlayerInfo() {
        return playerInfo;
    }

    public void setNumberOfGames(Integer numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public Integer getNumberOfGames() {
        return numberOfGames;
    }
}
