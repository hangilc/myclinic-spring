package jp.chang.myclinic.backendsqlite;

import jp.chang.myclinic.backenddb.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqliteDialect implements Dialect {

    private static SqliteDialect INSTANCE = new SqliteDialect();

    public static SqliteDialect getInstance(){
        return INSTANCE;
    }

    @Override
    public String isValidAt(String validFrom, String validUpto, String at) {
        return String.format("(%s <= date(%s) and (%s = '0000-00-00' or %s >= date(%s)))",
                validFrom, at, validUpto, validUpto, at);
    }

    @Override
    public String isValidUptoUnbound(String validUpto) {
        return String.format("(%s = '0000-00-00')", validUpto);
    }
}
