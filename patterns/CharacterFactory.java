package patterns;

import api.CharacterType;
import entities.characters.Character;
import entities.characters.Mage;
import entities.characters.Rogue;
import entities.characters.Warrior;

public class CharacterFactory {
    private CharacterFactory() {
    }
    public static Character getCharacter(CharacterType type, String cname, int level, Integer experience) {
        switch (type) {
            case Mage:
                return new Mage(cname, experience, level);
            case Rogue:
                return new Rogue(cname, experience, level);
            case Warrior:
                return new Warrior(cname, experience, level);
        }
        return null;
    }
}
