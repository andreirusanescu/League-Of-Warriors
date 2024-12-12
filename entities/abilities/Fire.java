package entities.abilities;

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
}
