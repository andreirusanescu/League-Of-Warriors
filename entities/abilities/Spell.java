package entities.abilities;

public abstract class Spell {
    protected int damage;
    protected int cost;

    public Spell(int damage, int cost) {
        this.damage = damage;
        this.cost = cost;
    }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [damage = " + damage + ", cost = " + cost + "]";
    }
}
