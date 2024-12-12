import exceptions.AccountNotFoundException;

public class Test {
    public static void main(String[] args) {
        Game g  = new Game(true);
        try {
            System.out.println("Running in testing mode");
            g.run();
        } catch (AccountNotFoundException e) {
            System.out.println("Email or password are not correct, try again.");
        }
    }
}
