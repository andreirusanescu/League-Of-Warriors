package accountInfo;

import entities.Character;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class Account {
    private Information playerInfo;
    private ArrayList<Character> characters;
    private Integer numberOfGames;

    public Account(ArrayList<Character> characters, int gamesNumber, Information information) {
        this.characters = characters;
        this.numberOfGames = gamesNumber;
        this.playerInfo = information;
    }

    public static class Information {
        private Credentials creds;
        private TreeSet<String> favouriteGames;
        private String name;
        private String country;
        public Information(Credentials creds, SortedSet<String> favouriteGames,
                           String name, String country) {
            this.creds = creds;
            this.favouriteGames = (TreeSet<String>) favouriteGames;
            this.name = name;
            this.country = country;
        }
        public Credentials getCreds() {
            return creds;
        }
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
