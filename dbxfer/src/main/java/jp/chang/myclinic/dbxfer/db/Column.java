package jp.chang.myclinic.dbxfer.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class Column<T> {

    private String name;
    private boolean isAutoIncrement = false;

    public Column(String name){
        this.name = name;
    }

    String getColumnName(){
        return name;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    abstract T getValueFromResultSet(ResultSet rs);

    @FunctionalInterface
    interface SqlFunction<T, U> {
        U get(T t) throws SQLException;
    }

    T rsValue(SqlFunction<String, T> getter){
        try {
            return getter.get(getColumnName());
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @FunctionalInterface
    interface SqlRunner {
        void run() throws SQLException;
    }

    abstract void setParameter(PreparedStatement stmt, int index, T value);

    void stmtPrep(SqlRunner runner){
        try {
            runner.run();
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
