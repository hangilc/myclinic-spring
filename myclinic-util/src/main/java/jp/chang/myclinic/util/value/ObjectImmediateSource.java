package jp.chang.myclinic.util.value;

public class ObjectImmediateSource<T> implements ObjectSource<T> {

    //private static Logger logger = LoggerFactory.getLogger(ObjectImmediateSource.class);
    private T value;

    public ObjectImmediateSource(T value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public void clear() {
        this.value = null;
    }

    @Override
    public T getValue(String name, ErrorMessages em) {
        return value;
    }
}
