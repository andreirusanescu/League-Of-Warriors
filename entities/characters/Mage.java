package entities.characters;

import entities.Character;

public class Mage extends Character {
    public Mage(String name, int experience, int level) {
        super(name, experience, level);
        setAttributes(8, 20, 12);
        setImmunity(false, true, false);
        setNormalDamage(30);
    }
    @Override
    public void receiveDamage(int damage) {
        double param = 0.5 + (getDexterity() * Math.random() - getStrength() * Math.random()) / 100.0;
        if (Math.random() < param) {
            damage /= 2;
        }

        setHealthBar(getHealthBar() - damage);
        System.out.println("You received " + RED + damage  + RESET + " damage");
        if (getHealthBar() < 0) {
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
}
