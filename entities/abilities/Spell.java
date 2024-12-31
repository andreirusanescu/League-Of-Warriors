package entities.abilities;

import patterns.Information;
import patterns.Visitor;
import entities.characters.Entity;

public abstract class Spell implements Visitor<Entity> {
    protected Information information;

    public Spell(int damage, int cost) {
        this.information = new Information.Builder().name(getClass().getSimpleName()).damage(damage).blessing(cost).build();
    }

    public int getDamage() {
        return information.getDamage();
    }
    public int getCost() {
        return information.getBlessing();
    }
    public abstract String getIcon();

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " [damage = " + information.getDamage() + ", cost = " + information.getBlessing() + "]";
    }

    public String getInformation() {
        return "<html>Name: " + information.getName()
                + "<br>Mana cost: " + information.getBlessing()
                + "<br>Damage: " + information.getDamage()
                + "</html>";
    }

}
