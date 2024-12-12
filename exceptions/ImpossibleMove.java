package exceptions;

public class ImpossibleMove extends ArrayIndexOutOfBoundsException {
    public ImpossibleMove(String message) {
        super(message);
    }
}
