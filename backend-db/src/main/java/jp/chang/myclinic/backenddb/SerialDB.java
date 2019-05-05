package jp.chang.myclinic.backenddb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class SerialDB implements DB{

    private DB delegate;

    public SerialDB(DB delegate) {
        this.delegate = delegate;
    }

    @Override
    public Supplier<Connection> getConnectionProvider() {
        return delegate.getConnectionProvider();
    }

    @Override
    public <T> T query(Proc<T> proc) {
        synchronized(SerialDB.class){
            return delegate.query(proc);
        }
    }

    @Override
    public <T> T tx(Proc<T> proc) {
        synchronized(SerialDB.class){
            return delegate.tx(proc);
        }
    }

}
