package entities.abilities;

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
}
