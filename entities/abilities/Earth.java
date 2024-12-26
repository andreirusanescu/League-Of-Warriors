package entities.abilities;

import entities.characters.Entity;
import java.util.Random;

public class Earth extends Spell {
    public Earth() {
        super(randomDamage(), randomCost());
    }

    private static int randomDamage() {
        return 15 + new Random().nextInt(20);
    }

    private static int randomCost() {
        return 5 + new Random().nextInt(15);
    }

    @Override
    public void visit(Entity entity) {
        switch (entity.getCharacterType()){
            case Enemy:
                if (entity.isImmuneToAbility(this)) {
                    System.out.println("You are immune to enemy's spells");
                    return;
                }
                break;
            default:
                if (entity.isImmuneToAbility(this)) {
                    System.out.println("Target is immune to your spells");
                    return;
                }
                break;
        }
        /* Not immune */
        entity.setBlessing(entity.getBlessing() - getCost());

        /* Initial damage + ability's damage */
        entity.receiveDamage(entity.getDamage() + getDamage());
    }
}
