package api;

import entities.characters.Character;
import entities.characters.Mage;
import entities.characters.Rogue;
import entities.characters.Warrior;

public class CharacterFactory {

    public static Character getCharacter(CharacterType type, String cname, int level, Integer experience) {
        switch (type) {
            case Mage:
                return new Mage(cname, level, experience);
            case Rogue:
                return new Rogue(cname, level, experience);
            case Warrior:
                return new Warrior(cname, level, experience);
        }
        return null;
    }
}
