package entities.characters;

import api.*;
import entities.abilities.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class Entity implements Battle, Element<Entity> {
    ArrayList<Spell> abilities;
    protected Information information;
    private final int maxHealth;
    private final int maxBlessing;

    /* Immunity values */
    private boolean fire;
    private boolean ice;
    private boolean earth;

    /* State of entity's life */
    private boolean alive;

    /* Colors for output */
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";

    public Entity() {
        this.maxHealth = 100;
        this.maxBlessing = 100;
        this.alive = true;
    }

    public abstract void receiveDamage(int damage);
    public abstract int getDamage();
    public abstract void useAbility(Entity target, boolean testing);
    public abstract void accept(Visitor<Entity> visitor);

    /**
     * Getter and setter for default attack
     */
    public int getNormalDamage() {
        return information.getDamage();
    }

    /**
     * Regenerates healthBar with @param health
     */
    public void regenerateHealth(int health) {
        information.setHealth(Math.min(maxHealth, information.getHealth() + health));
    }

    /**
     * Regenerates blessing with @param blessing
     */
    public void regenerateBlessing(int blessing) {
        information.setBlessing(Math.min(maxBlessing, information.getBlessing() + blessing));
    }

    /**
     * Default attack
     */
    public void attackEnemy(Entity enemy) {
        enemy.receiveDamage(getNormalDamage());
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isImmuneToAbility(Spell ability) {
        if (ability instanceof Fire) {
            return isImmuneToFire();
        } else if (ability instanceof Ice) {
            return isImmuneToIce();
        }
        return isImmuneToEarth();
    }

    public boolean isImmuneToFire() {
        return fire;
    }

    public boolean isImmuneToIce() {
        return ice;
    }

    public boolean isImmuneToEarth() {
        return earth;
    }

    public void setImmunity(boolean fire, boolean ice, boolean earth) {
        this.fire = fire;
        this.ice = ice;
        this.earth = earth;
    }

    private Spell randomSpell() {
        Random rand = new Random();
        switch (rand.nextInt(3)) {
            case 0: return new Fire();
            case 1: return new Ice();
            default: return new Earth();
        }
    }

    /**
     * Adds one ability of each type
     * and 3 or fewer abilities in a
     * random fashion
     */
    public void addAbilities() {
        Random rand = new Random();
        int numAbilities = rand.nextInt(4);
        abilities.add(new Fire());
        abilities.add(new Ice());
        abilities.add(new Earth());
        for (int i = 0; i < numAbilities; i++) {
            abilities.add(randomSpell());
        }
    }

    public void setHealthBar(int health) {
        information.setHealth(health);
    }

    public int getHealthBar() {
        return information.getHealth();
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxBlessing() {
        return maxBlessing;
    }

    public int getBlessing() {
        return information.getHealth();
    }

    public void setBlessing(int blessing) {
        information.setBlessing(blessing);
    }

    public CharacterType getCharacterType() {
        return information.getRole();
    }
}
