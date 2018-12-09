package jp.chang.myclinic.dbxfer.db;

public class SqldateColumn extends Column {

    public SqldateColumn(String name) {
        super(name, String.class, Sqldate.class);
    }

    @Override
    Object convertJdbcObjectToJavaObject(Object jdbcObject) {
        return new Sqldate((String) jdbcObject);
    }

    @Override
    Object convertJavaObjectToJdbcObject(Object javaObject) {
        Sqldate d = (Sqldate)javaObject;
        return d.getRep();
    }
}
