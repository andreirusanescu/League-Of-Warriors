package patterns;

import accountInfo.Credentials;
import api.CharacterType;

import java.util.SortedSet;

public class Information {
    private String name;
    private Integer level;
    private Integer experience;
    private Integer health;
    private Integer blessing;
    private Integer damage;
    private Integer enemiesKilled;
    private CharacterType role;
    private Credentials credentials;
    private SortedSet<String> favoriteGames;
    private String country;

    private Information() {
    }
    public static class Builder {
        private String name;
        private Integer level;
        private Integer experience;
        private Integer health;
        private Integer blessing;
        private Integer damage;
        private Integer enemiesKilled;
        private CharacterType role;
        private Credentials credentials;
        private SortedSet<String> favoriteGames;
        private String country;

        public Builder() {

        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder level(Integer level) {
            this.level = level;
            return this;
        }
        public Builder experience(Integer experience) {
            this.experience = experience;
            return this;
        }
        public Builder health(Integer health) {
            this.health = health;
            return this;
        }
        public Builder blessing(Integer blessing) {
            this.blessing = blessing;
            return this;
        }
        public Builder damage(Integer damage) {
            this.damage = damage;
            return this;
        }
        public Builder enemiesKilled(Integer enemiesKilled) {
            this.enemiesKilled = enemiesKilled;
            return this;
        }
        public Builder role(CharacterType role) {
            this.role = role;
            return this;
        }
        public Builder credentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }
        public Builder favoriteGames(SortedSet<String> favoriteGames) {
            this.favoriteGames = favoriteGames;
            return this;
        }
        public Builder country(String country) {
            this.country = country;
            return this;
        }
        public Information build() {
            Information information = new Information();
            information.name = name;
            information.level = level;
            information.experience = experience;
            information.health = health;
            information.blessing = blessing;
            information.damage = damage;
            information.enemiesKilled = enemiesKilled;
            information.role = role;
            information.credentials = credentials;
            information.favoriteGames = favoriteGames;
            information.country = country;
            return information;
        }
    }
    public String getName() {
        return name;
    }
    public Integer getLevel() {
        return level;
    }
    public Integer getExperience() {
        return experience;
    }
    public Integer getHealth() {
        return health;
    }
    public Integer getBlessing() {
        return blessing;
    }
    public Integer getDamage() {
        return damage;
    }
    public Integer getEnemiesKilled() {
        return enemiesKilled;
    }
    public CharacterType getRole() {
        return role;
    }
    public SortedSet<String> getFavoriteGames() {
        return favoriteGames;
    }
    public String getCountry() {
        return country;
    }
    public Credentials getCreds() {
        return credentials;
    }
    public void setExperience(Integer experience) {
        this.experience = experience;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    public void setHealth(Integer health) {
        this.health = health;
    }
    public void setBlessing(Integer blessing) {
        this.blessing = blessing;
    }
    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public void setEnemiesKilled(Integer enemiesKilled) {
        this.enemiesKilled = enemiesKilled;
    }
}
