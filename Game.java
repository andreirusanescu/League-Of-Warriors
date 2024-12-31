import accountInfo.Account;
import accountInfo.Credentials;
import api.CellEntityType;
import api.JsonInput;
import entities.characters.Character;
import entities.characters.Enemy;
import exceptions.AccountNotFoundException;
import exceptions.ImpossibleMove;
import exceptions.InvalidCommandException;
import grid.Grid;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private static Game gameInstance;
    private ArrayList<Account> accounts;
    private Grid grid;

    /* Used if the program is initialised in testing mode */
    private boolean testing;

    /* Current parameters */
    private Scanner input;
    private Account selectedAccount = null;
    private Character currentCharacter = null;

    /* Colors for output */
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    /**
     * @param testing used for testing mode
     */
    private Game(boolean testing) {
        this.testing = testing;
    }

    public static Game getInstance() {
        if (gameInstance == null) {
            gameInstance = new Game(false);
        }
        return gameInstance;
    }

    public void test() {
        testing = true;
    }

    public boolean isTesting() {
        return testing;
    }

    public Grid getGrid() {
        return grid;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }
    public void setCurrentCharacter(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public void setGrid(grid.Grid grid) {
        this.grid = grid;
    }

    public ArrayList<Account> getAccounts() {
        if (accounts == null) {
            accounts = JsonInput.deserializeAccounts();
        }
        return accounts;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }
    /**
     * Shows the list of directions for the
     * current cell or EXIT.
     * @return if correctly selected a cell
     */
    public boolean displayOptions() throws InvalidCommandException {
        System.out.println("Choose direction: W (WEST), E (EAST), N (NORTH), S (SOUTH)");
        System.out.println("Enter 'Q' to quit");
        try {
            char direction = input.next().charAt(0);
            input.nextLine();
            switch (direction) {
                case 'W':
                    grid.goWest();
                    return true;
                case 'E':
                    grid.goEast();
                    return true;
                case 'N':
                    grid.goNorth();
                    return true;
                case 'S':
                    grid.goSouth();
                    return true;
                case 'Q':
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    throw new InvalidCommandException("Enter a valid direction (just the character)!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter a valid direction (just the character)!");
        } catch (ImpossibleMove e) {
            System.out.println("Current coords: " + grid.getCurrentCell().getX()
                    + " " + grid.getCurrentCell().getY());
        }

        return false;
    }

    /**
     * Chooses character or EXIT the program entirely
     */
    private boolean chooseCharacter() {
        System.out.println("Choose character (choose the index of the character) or enter 'Q' to quit: ");
        try {
            String s = input.nextLine();
            if (s.equals("Q")) {
                System.out.println("Exiting...");
                System.exit(0);
            }
            int indexOfCharacter = Integer.parseInt(s);
            currentCharacter = selectedAccount.getCharacters().get(indexOfCharacter - 1);
            System.out.println("Character chosen: " + GREEN + currentCharacter.getName() + RESET);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(RED + "Enter a valid number!" + RESET);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(RED + "Invalid index. Please choose a valid index." + RESET);
        }
        return false;
    }

    /**
     * Fights enemy until one dies.
     * @return done if the user died (true)
     */
    public boolean fightEnemy() {
        System.out.println("Encountered an " + RED + "enemy" + RESET + "!\nAttacking...");
        boolean done = false;
        Enemy enemy = new Enemy(3 * currentCharacter.getNormalDamage() / 2, currentCharacter.getMaxHealth());
        while (true) {
            currentCharacter.attack(enemy, testing);
            if (enemy.isAlive())
                enemy.attack(currentCharacter, testing);

            /* Character is dead */
            if (!currentCharacter.isAlive()) {
                done = true;
                break;
            }

            /* Character won */
            if (!enemy.isAlive()) {
                break;
            }
        }

        /* Character is dead */
        if (done) {
            currentCharacter.regenerateHealth(currentCharacter.getMaxHealth());
            currentCharacter.regenerateBlessing(currentCharacter.getMaxBlessing());
            currentCharacter.getAbilities().removeAll(currentCharacter.getAbilities());
            currentCharacter.addAbilities();

            System.out.println(RED + "GAME OVER" + RESET);
            System.out.println("Start NEW GAME or EXIT");
            if (testing) {
                input.nextLine();
            }
            return done;
        }
        grid.getCurrentCell().setType(CellEntityType.PLAYER);
        return done;
    }

    /**
     * Called when character gets to
     * a SANCTUARY CELL
     */
    private void sanctuaryCell() {
        System.out.println(GREEN + "Sanctuary... Regenerating" + RESET);
        Random rand = new Random();
        currentCharacter.regenerateHealth(rand.nextInt(currentCharacter.getMaxHealth()));
        System.out.println("Regenerated health: " + GREEN + currentCharacter.getHealthBar() + RESET);
        currentCharacter.regenerateBlessing(rand.nextInt(currentCharacter.getMaxBlessing()));
        System.out.println("Regenerated blessing: " + GREEN + currentCharacter.getBlessing() + RESET);
        grid.getCurrentCell().setType(CellEntityType.PLAYER);
    }

    /**
     * Called when a character gets
     * to a PORTAL CELL
     */
    private void portalCell() {
        System.out.println(BLUE + "Portal..." + RESET);
        currentCharacter.increaseExperience(currentCharacter.getLevel() * 5);

        selectedAccount.setNumberOfGames(selectedAccount.getNumberOfGames() + 1);
        grid.getCurrentCell().setType(CellEntityType.PLAYER);
    }

    /**
     *  Loads all the data parsed from a JSON file
     *  and offers the user the possibility to
     *  log into their account and choose the
     *  character they want to play as.
     *  @throws AccountNotFoundException, if the user
     *  incorrectly introducer their data.
     */
    public void run() throws AccountNotFoundException {
        accounts = JsonInput.deserializeAccounts();
        String email;
        String password;

        if (!testing)
            input = new Scanner(System.in);
        else {
            try {
                input = new Scanner(new File("src/test.in"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (!testing) {
            System.out.print("Your email: ");
            email = input.nextLine();
            System.out.print("Your password: ");
        } else {
            email = input.nextLine();
        }
        password = input.nextLine();

        for (Account account : accounts) {
            Credentials creds = account.getPlayerInfo().getCreds();
            if (email.equals(creds.getEmail())
                    && password.equals(creds.getPassword())) {
                selectedAccount = account;
                break;
            }
        }

        if (selectedAccount == null) {
            throw new AccountNotFoundException();
        }

        // Main loop
        while (true) {
            int i = 1;
            for (Character character : selectedAccount.getCharacters()) {
                System.out.println(i + ". " + character);
                i++;
            }

            // Choose a character
            boolean done = false;
            while (!done) {
                done = chooseCharacter();
            }

            Random random = new Random();
            grid = Grid.generateGrid(random.nextInt(10),
                                     random.nextInt(10), testing);
            grid.setCurrentCharacter(currentCharacter);

            /* Tries to move in a direction */
            while (true) {
                System.out.println(grid);
                done = false;

                /* Tries to go in a direction */
                while (!done) {
                    try {
                        done = displayOptions();
                    } catch (InvalidCommandException e) {
                        System.out.println(e.getMessage());
                    }
                }

                /* ENEMY CELL */
                if (grid.getCurrentCell().getType()
                        == CellEntityType.ENEMY) {
                    done = fightEnemy();
                    if (done) break;

                    /* SANCTUARY CELL */
                } else if (grid.getCurrentCell().getType()
                            == CellEntityType.SANCTUARY) {
                   sanctuaryCell();

                    /* PORTAL CELL */
                } else if (grid.getCurrentCell().getType()
                            == CellEntityType.PORTAL) {
                    portalCell();
                    break;

                    /* VOID CELL */
                } else if (grid.getCurrentCell().getType()
                            == CellEntityType.VOID) {
                    System.out.println("Nothing here...");
                    grid.getCurrentCell().setType(CellEntityType.PLAYER);

                    /* VISITED CELL */
                } else if (grid.getCurrentCell().getType()
                            == CellEntityType.VISITED) {
                    System.out.println("Visited...");
                    grid.getCurrentCell().setType(CellEntityType.PLAYER);
                }
            }
        }
    }
}
