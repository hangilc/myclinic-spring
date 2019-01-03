package jp.chang.myclinic.dbxfer.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Column {

    private String name;
    private Class<?> jdbcType;
    private Class<?> javaType;

    public Column(String name, Class<?> jdbcType, Class<?> javaType){
        this.name = name;
        this.jdbcType = jdbcType;
        this.javaType = javaType;
    }

    String getColumnName(){
        return name;
    }

    public Class<?> getJdbcType(){
        return jdbcType;
    }

    public Class<?> getJavaType(){
        return javaType;
    }

    Object getJavaObject(ResultSet rs){
        return convertJdbcObjectToJavaObject(getResultSetObject(rs));
    }

    void setParamByJavaObject(PreparedStatement stmt, int index, Object obj){
        setParam(stmt, index, convertJavaObjectToJdbcObject(obj));
    }

    Object getResultSetObject(ResultSet rs){
        try {
            return rs.getObject(name, jdbcType);
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    void setParam(PreparedStatement stmt, int index, Object value){
        try {
            stmt.setObject(index, value);
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    Object convertJdbcObjectToJavaObject(Object jdbcObject){
        return jdbcObject;
    }

    Object convertJavaObjectToJdbcObject(Object javaObject){
        return javaObject;
    }

}
