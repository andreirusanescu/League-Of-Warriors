package entities.abilities;

import entities.characters.Entity;
import java.util.Random;

public class Fire extends Spell {
    public Fire() {
        super(randomDamage(), randomCost());
    }

    private static int randomDamage() {
        return 30 + new Random().nextInt(20);
    }

    private static int randomCost() {
        return 10 + new Random().nextInt(15);
    }

    @Override
    public String getIcon() {
        return "src/images/fire.png";
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
