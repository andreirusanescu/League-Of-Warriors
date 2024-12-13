package grid;

import api.CellEntityType;

public class Cell {
    private int x, y;
    private CellEntityType entityType;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.entityType = CellEntityType.VOID;
    }

    public void setType(CellEntityType type) {
        this.entityType = type;
    }

    public CellEntityType getType() {
        return this.entityType;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        switch (entityType) {
            case PLAYER: return "P";
            case VISITED: return "V";
            default: return "N";
        }
    }
}
