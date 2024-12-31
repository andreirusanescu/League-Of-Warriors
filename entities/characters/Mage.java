package entities.characters;

import patterns.Visitor;
import api.CharacterType;

public class Mage extends Character {
    public Mage(String name, int experience, int level) {
        super(CharacterType.Mage, name, experience, level, 30);
        setAttributes(8, 20, 12);
        setImmunity(false, true, false);
    }
    @Override
    public void receiveDamage(int damage) {
        double param = 0.5 + (getDexterity() * Math.random() - getStrength() * Math.random()) / 100.0;
        if (Math.random() < param) {
            damage /= 2;
        }

        setHealthBar(getHealthBar() - damage);
        System.out.println("You received " + RED + damage  + RESET + " damage");
        if (getHealthBar() <= 0) {
            System.out.println(RED + "Mage " + getName() + " died." + RESET);
            setHealthBar(0);
            setAlive(false);
        }
    }

    @Override
    public int getDamage() {
        int damage = getNormalDamage();
        double modifier = 0.5 + (getCharisma() * Math.random()) / 100.0;
        if (Math.random() < modifier) {
            damage *= 2;
        }
        return damage;
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    @Override
    public String getImagePath() {
        return "src/images/mage.png";
    }
}
