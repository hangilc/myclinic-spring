package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.backend.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.function.Function;

public class DbBackend {

    public static Backend createBackend(DataSource ds, Function<Query, TableSet> tableSetCreator){
        DB db = new DB(ds);
        Query query = new Query(db.getConnectionProvider());
        TableSet ts = tableSetCreator.apply(query);
        DbPersistence persist = new DbPersistence(ts, query);
        return new Backend(persist);
    }

}
