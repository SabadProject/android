package farayan.commons.Exceptions;

public class Rexception extends RuntimeException {
    public Rexception(Throwable throwable, String format, Object... args) {
        super(format == null ? "" : String.format(format, args), throwable);
    }
}
