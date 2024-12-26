package entities.characters;

import api.CharacterType;
import api.Information;
import entities.abilities.Spell;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public abstract class Character extends Entity {
    private int strength;
    private int charisma;
    private int dexterity;
    private final int maxLevel = 20;
    private final int experienceBound = 100;

    public Character(CharacterType characterType, String name, int experience, int level, int damage) {
        information = new Information.Builder().name(name).role(characterType)
                .experience(experience).level(level).health(100).blessing(100)
                .damage(damage).build();
        abilities = new ArrayList<Spell>();
        addAbilities();
    }

    public void setAttributes(int strength, int charisma, int dexterity) {
        this.strength = strength;
        this.charisma = charisma;
        this.dexterity = dexterity;
    }

    public abstract void receiveDamage(int damage);
    public abstract int getDamage();

    /**
     * Increases experience with @param experience
     */
    public void increaseExperience(int experience) {
        information.setExperience(information.getExperience() + experience);
        System.out.println("Increased experience to: " + GREEN + information.getExperience() + RESET);
    }

    /**
     * Chooses ability
     * @return the spell chosen
     */
    private Spell chooseAbility() {
        System.out.println("Choose ability:");
        for (int i = 0; i < abilities.size(); i++) {
            System.out.println((i + 1) + ". " + abilities.get(i));
        }

        Scanner sc = new Scanner(System.in);
        int option = 0;
        boolean chooseOption = false;
        while (!chooseOption) {
            try {
                option = sc.nextInt();
                if (option >= 1 && option <= abilities.size()) {
                    chooseOption = true;
                } else {
                    System.out.println("Introduce a valid number!");
                    sc.nextLine();
                }
            } catch (InputMismatchException e) {
                System.out.println("Introduce a valid number!");
                sc.nextLine();
            }
        }
        return abilities.isEmpty() ? null : abilities.get(option - 1);
    }

    /**
     * Chooses ability for the testing environment
     * @return the spell chosen
     */
    private Spell chooseAbility(boolean testing) {
        return abilities.isEmpty() ? null : abilities.get(0);
    }

    /**
     * Uses ability on @param target or
     * a default attack if the character does
     * not have enough blessing.
     * @param testing for the testing environment
     */
    @Override
    public void useAbility(Entity target, boolean testing) {
        if (target == this)
            return;
        Spell ability;
        if (testing)
            ability = chooseAbility(testing);
        else
            ability = chooseAbility();

        if (ability != null && ability.getCost() < getBlessing()) {
            /* Remove ability after using it */
            abilities.remove(ability);
            target.accept(ability);
        } else if (ability != null) {
            System.out.println("You do not have any abilities left");
            System.out.println("Default Attack");
            attackEnemy(target);
        }
    }

    /**
     * Increases all the attributes by 20%
     * when experience hits experienceBound.
     */
    public void increaseAttributes() {
        this.strength = this.strength + (int) (this.strength * 0.2);
        this.dexterity = this.dexterity + (int) (this.dexterity * 0.2);
        this.charisma = this.charisma + (int) (this.charisma * 0.2);
    }

    /**
     * Increases normal attack damage by 30%
     */
    public void increaseNormalDamage() {
        information.setDamage(information.getDamage() + (int) (0.3 * getNormalDamage()));
    }

    /**
     * Attack menu for Characters
     * Character attacks @param enemy
     */
    public void attack(Entity enemy, boolean testing) {
        System.out.println("Current health and blessing: "
                + getHealthBar() + " " + getBlessing());
        System.out.println("Enemy's health: " + enemy.getHealthBar());
        if (!testing) {
            int option = 0;
            Scanner sc = new Scanner(System.in);
            boolean chooseOption = false;
            while (!chooseOption) {
                /* No abilities left => just attack */
                if (abilities.isEmpty()) {
                    System.out.println("1. Normal Attack");
                    System.out.println("No abilities left");
                    chooseOption = true;
                    option = 1;
                    break;
                }
                System.out.println("1. Normal Attack\n" +
                                   "2. Choose Ability");
                try {
                    option = sc.nextInt();
                    if (option == 1 || option == 2) {
                        chooseOption = true;
                    } else {
                        System.out.println("Introduce 1 or 2!");
                        sc.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Introduce 1 or 2!");
                    sc.nextLine();
                }
            }
            if (option == 1) {
                attackEnemy(enemy);
            } else {
                useAbility(enemy, testing);
            }
        } else {
            if (abilities.isEmpty()) {
                attackEnemy(enemy);
            } else {
                useAbility(enemy, testing);
            }
        }

        if (!enemy.isAlive()) {
            System.out.println(BLUE + "Enemy is dead" + RESET);
            regenerateHealth(getHealthBar());
            System.out.println("Regenerated health: " + GREEN
                    + getHealthBar() + RESET);
            regenerateBlessing(getMaxBlessing());
            System.out.println("Regenerated blessing: " + GREEN
                    + getBlessing() + RESET);
            increaseExperience(new Random().nextInt(100));
            if (information.getExperience() >= this.experienceBound) {
                information.setExperience(information.getExperience() - experienceBound);
                increaseAttributes();
                increaseNormalDamage();
                System.out.println("Increased damage to " + BLUE
                        + getNormalDamage() + RESET);
            }
        } else {
            System.out.println("Enemy is on " + RED
                    + enemy.getHealthBar() + RESET + " life");
        }
    }

    public int getStrength() {
        return strength;
    }

    public int getCharisma() {
        return charisma;
    }

    public int getDexterity() {
        return dexterity;
    }

    public String getName() {
        return information.getName();
    }

    public int getExperience() {
        return information.getExperience();
    }

    public void setExperience(int experience) {
        information.setExperience(experience);
    }

    public int getLevel() {
        return information.getLevel();
    }

    public void setLevel(int level) {
        information.setLevel(level);
    }

    public int getExperienceBound() {
        return experienceBound;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getName() + ", experience "
                + getExperience() + ", level " + getLevel()  + ", strength " + getStrength()
                + ", charisma " + getCharisma() + ", dexterity " + getDexterity();
    }
}
