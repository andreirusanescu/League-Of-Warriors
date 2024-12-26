package grid;

import api.CellEntityType;
import exceptions.ImpossibleMove;
import entities.characters.Character;

import java.util.ArrayList;
import java.util.Random;

public class Grid extends ArrayList<ArrayList<Cell>> {
    private final int rows;
    private final int cols;
    private Character currentCharacter;
    private Cell currentCell;
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    private Grid(int rows, int cols, boolean testing) {
        if (!testing) {
            rows = Math.max(rows, 5);
            cols = Math.max(cols, 5);
            this.rows = Math.min(rows, 10);
            this.cols = Math.min(cols, 10);
        } else {
            this.rows = 5;
            this.cols = 5;
        }

        for (int i = 0; i < this.rows; i++) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < this.cols; j++) {
                row.add(new Cell(i, j));
            }
            this.add(row);
        }
    }

    /**
     * Searches in a random fashion
     * for an empty cell
     * @return an empty cell
     */
    private Cell getEmptyCell() {
        Random rand = new Random();
        while (true) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);
            if (get(row).get(col).getType() == CellEntityType.VOID) {
                return get(row).get(col);
            }
        }
    }

    /**
     * Sets the cells for the @param grid
     */
    private static void setCells(Grid grid) {
        /* Set player cell */
        grid.currentCell = grid.getEmptyCell();
        grid.currentCell.setType(CellEntityType.PLAYER);

        /* Set portal cell */
        Cell portal = grid.getEmptyCell();
        portal.setType(CellEntityType.PORTAL);

        /* Set sanctuary cells */
        for (int i = 0; i < 2; i++) {
            Cell sanctuary = grid.getEmptyCell();
            sanctuary.setType(CellEntityType.SANCTUARY);
        }

        /* Set enemy cells */
        for (int i = 0; i < 4; i++) {
            Cell enemy = grid.getEmptyCell();
            enemy.setType(CellEntityType.ENEMY);
        }
    }

    /**
     * Sets cells for @param grid int the
     * @param testing environment
     */
    private static void setCells(Grid grid, boolean testing) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                grid.get(i).get(j).setType(CellEntityType.VOID);
            }
        }
        grid.currentCell = grid.get(0).get(0);
        grid.get(0).get(0).setType(CellEntityType.PLAYER);
        grid.get(0).get(3).setType(CellEntityType.SANCTUARY);
        grid.get(1).get(3).setType(CellEntityType.SANCTUARY);
        grid.get(2).get(0).setType(CellEntityType.SANCTUARY);
        grid.get(4).get(3).setType(CellEntityType.SANCTUARY);
        grid.get(3).get(4).setType(CellEntityType.ENEMY);
        grid.get(4).get(4).setType(CellEntityType.PORTAL);
    }

    /**
     * Sets the default parameters for
     * a grid and creates an instance
     * of it of @param rows and @param cols
     */
    public static Grid generateGrid(final int rows, final int cols, boolean testing) {
        Grid grid = new Grid(rows, cols, testing);
        Random rand = new Random();

        if (!testing) {
            setCells(grid);
        } else {
            setCells(grid, testing);
        }

        System.out.println("Generated grid of " + grid.getRows() + " x " + grid.getCols());
        return grid;
    }

    /**
     * Moves Character to the NORTH 1 position
     * @throws ImpossibleMove if COORD X is 0
     */
    public void goNorth() throws ImpossibleMove {
        if (currentCell.getX() == 0) {
            System.out.println("You can't go North");
            throw new ImpossibleMove("You can't go North");
        } else {
            currentCell.setType(CellEntityType.VISITED);
            currentCell = get(currentCell.getX() - 1).get(currentCell.getY());
            System.out.println("Moved North");
        }
    }

    /**
     * Moves Character to the SOUTH 1 position
     * @throws ImpossibleMove if COORD X is rows - 1
     */
    public void goSouth() throws ImpossibleMove {
        if (currentCell.getX() == rows - 1) {
            System.out.println("You can't go South");
            throw new ImpossibleMove("You can't go South");
        } else {
            currentCell.setType(CellEntityType.VISITED);
            currentCell = get(currentCell.getX() + 1).get(currentCell.getY());
            System.out.println("Moved South");
        }
    }

    /**
     * Moves Character to the WEST 1 position
     * @throws ImpossibleMove if COORD Y is 0
     */
    public void goWest() throws ImpossibleMove {
        if (currentCell.getY() == 0) {
            System.out.println("You can't go West");
            throw new ImpossibleMove("You can't go West");
        } else {
            currentCell.setType(CellEntityType.VISITED);
            currentCell = get(currentCell.getX()).get(currentCell.getY() - 1);
            System.out.println("Moved West");
        }
    }

    /**
     * Moves Character to the EAST 1 position
     * @throws ImpossibleMove if COORD Y is cols - 1
     */
    public void goEast() throws ImpossibleMove {
        if (currentCell.getY() == cols - 1) {
            System.out.println("You can't go East");
            throw new ImpossibleMove("You can't go East");
        } else {
            currentCell.setType(CellEntityType.VISITED);
            currentCell = get(currentCell.getX()).get(currentCell.getY() + 1);
            System.out.println("Moved East");
        }
    }

    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public Cell getCurrentCell() {
        return currentCell;
    }
    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }
    public Character getCurrentCharacter() {
        return currentCharacter;
    }
    public void setCurrentCharacter(Character character) {
        this.currentCharacter = character;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<Cell> row : this) {
            for (Cell cell : row) {
                switch (cell.getType()) {
                case PLAYER:
                    sb.append(CYAN).append(cell).append(RESET).append("  ");
                    break;
                case VISITED:
                    sb.append(YELLOW).append(cell).append(RESET).append("  ");
                    break;
                default:
                    sb.append(cell).append("  ");
                    break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
