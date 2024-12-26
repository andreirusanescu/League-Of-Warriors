package entities.abilities;

import api.Information;
import api.Visitor;
import entities.characters.Entity;

public abstract class Spell implements Visitor<Entity> {
    protected Information information;

    public Spell(int damage, int cost) {
        this.information = new Information.Builder().damage(damage).blessing(cost).build();
    }

    public int getDamage() {
        return information.getDamage();
    }
    public int getCost() {
        return information.getBlessing();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " [damage = " + information.getDamage() + ", cost = " + information.getBlessing() + "]";
    }

    @Override
    public abstract void visit(Entity entity);
}
