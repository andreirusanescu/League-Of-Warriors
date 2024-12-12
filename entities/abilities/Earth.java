package entities.abilities;

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
}
