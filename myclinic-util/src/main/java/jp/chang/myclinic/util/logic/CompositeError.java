package jp.chang.myclinic.util.logic;

public class CompositeError<P> {

    //private static Logger logger = LoggerFactory.getLogger(CompositeError.class);
    private final P part;
    private final String message;

    public CompositeError(P part, String message) {
        this.part = part;
        this.message = message;
    }

    public P getPart() {
        return part;
    }

    public String getMessage() {
        return message;
    }
}
