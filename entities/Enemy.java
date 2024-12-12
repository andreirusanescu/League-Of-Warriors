package entities;

import entities.abilities.Spell;

import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity {
    public Enemy(int damage) {
        super(100, 100);
        Random rand = new Random();
        setNormalDamage(rand.nextInt(damage));
        setImmunity(rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean());
        abilities = new ArrayList<Spell>();
        addAbilities();
    }

    @Override
    public void receiveDamage(int damage) {
        if (!new Random().nextBoolean()) {
            System.out.println("Enemy received " + RED + damage  + RESET + " damage");
            setHealthBar(getHealthBar() - damage);
            if (getHealthBar() < 0) {
                setHealthBar(0);
                setAlive(false);
            }
        } else {
            System.out.println("Enemy avoided the attack");
        }
    }

    @Override
    public int getDamage() {
        if (new Random().nextBoolean()) {
            return getNormalDamage() * 2;
        }
        return getNormalDamage();
    }

    private Spell chooseAbility() {
        return abilities.isEmpty() ? null : abilities.get(new Random().nextInt(abilities.size()));
    }

    private Spell chooseAbility(boolean testing) {
        return abilities.isEmpty() ? null : abilities.get(0);
    }

    @Override
    public void useAbility(Entity target, boolean testing) {
        if (target == this)
            return;
        Spell ability;
        if (!testing)
            ability = chooseAbility();
        else
            ability = chooseAbility(true);
        if (ability != null && ability.getCost() < getBlessing()) {
            if (target.isImmuneToAbility(ability)) {
                System.out.println("You are immune to enemy's spells");
                return;
            }
            // Not immune
            setBlessing(getBlessing() - ability.getCost());
            // Initial damage + ability's damage
            abilities.remove(ability);
            target.receiveDamage(getDamage() + ability.getDamage());
        } else if (ability != null) {
            System.out.println("Enemy does not have enough blessing");
            System.out.println("Default Attack");
            attackEnemy(target);
        } else {
            System.out.println("Enemy does not have any abilities left");
            System.out.println("Default Attack");
            attackEnemy(target);
        }
    }

    public void attack(Entity target, boolean testing) {
        useAbility(target, testing);
        if (target.isAlive()) {
            System.out.println("You are on " +  RED + target.getHealthBar() + RESET + " life");
        }
    }
}
