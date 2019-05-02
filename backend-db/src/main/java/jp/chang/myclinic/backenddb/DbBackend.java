package jp.chang.myclinic.backenddb;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class DbBackend {

    public interface QueryStatement<T> {
        T query(Backend backend) throws SQLException;
    }

    public interface ProcStatement {
        void execute(Backend backend) throws SQLException;
    }

    private DB db;
    private Query query;
    private TableSet ts;
    private Backend backend;

    public DbBackend(DB db, Function<Query, TableSet> tableSetCreator, SupportSet ss){
        this.db = db;
        this.query = new Query(db.getConnectionProvider());
        this.ts = tableSetCreator.apply(query);
        this.backend = new Backend(ts, query, ss);
    }

    public void setPracticeLogPublisher(Consumer<String> publisher){
        backend.setPracticeLogPublisher(publisher);
    }

    public void setHolineLogPublisher(Consumer<String> publisher){
        backend.setHotlineLogPublisher(publisher);
    }

    public TableSet getTableSet() {
        return ts;
    }

    public void proc(ProcStatement proc){
        db.query(() -> {
            proc.execute(backend);
            return null;
        });
    }

    public <T> T query(QueryStatement<T> proc){
        return db.query(() -> proc.query(backend));
    }

    public <T> T tx(QueryStatement<T> proc){
        return db.tx(() -> proc.query(backend));
    }

    public void txProc(ProcStatement proc){
        db.tx(() -> {
            proc.execute(backend);
            return null;
        });
    }

}
