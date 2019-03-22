package jp.chang.myclinic.backenddb;

import java.util.function.Function;

public class DbBackend {

    public static Backend createBackend(DB db, Function<Query, TableSet> tableSetCreator){
        Query query = new Query(db.getConnectionProvider());
        TableSet ts = tableSetCreator.apply(query);
        return new Backend(ts, query);
    }

}
