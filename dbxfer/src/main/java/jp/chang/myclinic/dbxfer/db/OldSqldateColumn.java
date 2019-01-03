package jp.chang.myclinic.dbxfer.db;

public class OldSqldateColumn extends Column {

    public OldSqldateColumn(String name) {
        super(name, String.class, OldSqldate.class);
    }

    @Override
    Object convertJdbcObjectToJavaObject(Object jdbcObject) {
        return new OldSqldate((String)jdbcObject);
    }

    @Override
    Object convertJavaObjectToJdbcObject(Object javaObject) {
        OldSqldate d = (OldSqldate) javaObject;
        return d.getRep();
    }
}
