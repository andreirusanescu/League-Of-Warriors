package entities.characters;

import api.CharacterType;
import api.Visitor;

public class Warrior extends Character {
    public Warrior(String name, int experience, int level) {
        super(CharacterType.Warrior, name, experience, level, 35);
        setAttributes(20, 12, 8);
        setImmunity(true, false, false);
    }

    @Override
    public void receiveDamage(int damage) {
        double param = 0.5 + (getDexterity() * Math.random() - getCharisma() * Math.random()) / 100.0;
        if (Math.random() < param) {
            damage /= 2;
        }

        setHealthBar(getHealthBar() - damage);
        System.out.println("You received " + RED + damage + RESET + " damage");
        if (getHealthBar() < 0) {
            System.out.println(RED + "Warrior " + getName() + " died." + RESET);
            setHealthBar(0);
            setAlive(false);
        }
    }

    @Override
    public int getDamage() {
        int damage = getNormalDamage();
        double modifier = 0.5 + (getStrength() * Math.random()) / 100.0;
        if (Math.random() < modifier) {
            damage *= 2;
        }
        return damage;
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }
}
