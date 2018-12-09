package jp.chang.myclinic.dbxfer.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StringColumn extends Column{


    public StringColumn(String name) {
        super(name, String.class, String.class);
    }

}
