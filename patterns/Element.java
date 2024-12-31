package patterns;

import entities.characters.Entity;

public interface Element <T extends Entity> {
    void accept(Visitor<T> visitor);
}
