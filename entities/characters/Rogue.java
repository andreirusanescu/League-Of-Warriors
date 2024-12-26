package entities.characters;

import api.CharacterType;
import api.Visitor;

public class Rogue extends Character {
    public Rogue(String name, int experience, int level) {
        super(CharacterType.Rogue, name, experience, level, 30);
        setAttributes(10, 10, 20);
        setImmunity(false, false, true);
    }

    @Override
    public void receiveDamage(int damage) {
        double param = 0.5 + (getCharisma() * Math.random() - getStrength() * Math.random()) / 100.0;
        if (Math.random() < param) {
            damage /= 2;
        }

        setHealthBar(getHealthBar() - damage);
        System.out.println("You received " + RED + damage + RESET + " damage");
        if (getHealthBar() < 0) {
            System.out.println(RED + "Rogue " + getName() + " died." + RESET);
            setHealthBar(0);
            setAlive(false);
        }
    }

    @Override
    public int getDamage() {
        int damage = getNormalDamage();
        double modifier = 0.5 + (getDexterity() * Math.random()) / 100.0;
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
