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
    private ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    public SerialDB(DB delegate) {
        this.delegate = delegate;
    }

    @Override
    public Supplier<Connection> getConnectionProvider() {
        return delegate.getConnectionProvider();
    }

    @Override
    public <T> T query(Proc<T> proc) {
        Future<T> future = executorService.submit(() -> delegate.query(proc));
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public <T> T tx(Proc<T> proc) {
        Future<T> future = executorService.submit(() -> delegate.tx(proc));
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

}
