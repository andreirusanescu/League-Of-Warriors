package entities.abilities;

import entities.characters.Entity;
import java.util.Random;

public class Ice extends Spell {
    public Ice() {
        super(randomDamage(), randomCost());
    }

    private static int randomDamage() {
        return 15 + new Random().nextInt(15);
    }

    private static int randomCost() {
        return 5 + new Random().nextInt(10);
    }

    @Override
    public String getIcon() {
        return "src/images/ice.png";
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
        entity.receiveDamage(getDamage());
    }
}
