import exceptions.AccountNotFoundException;

public class Main {
    public static void main(String[] args) {
        Game g  = new Game(false);
        try {
            g.run();
        } catch (AccountNotFoundException e) {
            System.out.println("Email or password are not correct, try again.");
        }
    }
}