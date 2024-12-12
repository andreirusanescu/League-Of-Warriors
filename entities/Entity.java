package entities;

import api.Battle;
import entities.abilities.*;

import java.util.ArrayList;
import java.util.Random;

public abstract class Entity implements Battle {
    ArrayList<Spell> abilities;
    private int healthBar;
    private final int maxHealth;
    private int blessing;
    private final int maxBlessing;

    /* Normal damage value */
    private int normalDamage;

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

    public Entity(int health, int blessing) {
        this.healthBar = health;
        this.blessing = blessing;
        this.maxHealth = health;
        this.maxBlessing = blessing;
        this.alive = true;
    }

    public abstract void receiveDamage(int damage);
    public abstract int getDamage();
    public abstract void useAbility(Entity target, boolean testing);

    /**
     * Getter and setter for default attack
     */
    public int getNormalDamage() {
        return normalDamage;
    }
    public void setNormalDamage(int damage) {
        this.normalDamage = damage;
    }

    /**
     * Regenerates healthBar with @param health
     */
    public void regenerateHealth(int health) {
        this.healthBar = Math.min(healthBar + health, maxHealth);
    }

    /**
     * Regenerates blessing with @param blessing
     */
    public void regenerateBlessing(int blessing) {
        this.blessing = Math.min(this.blessing + blessing, maxBlessing);
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
        this.healthBar = health;
    }

    public int getHealthBar() {
        return healthBar;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxBlessing() {
        return maxBlessing;
    }

    public int getBlessing() {
        return blessing;
    }

    public void setBlessing(int blessing) {
        this.blessing = blessing;
    }
}
