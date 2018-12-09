package jp.chang.myclinic.dbxfer.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TableXferer {

    private Connection srcConn;
    private Connection dstConn;
    private ConverterMap converterMap;

    public TableXferer(Connection srcConn, Connection dstConn, ConverterMap converterMap) {
        this.srcConn = srcConn;
        this.dstConn = dstConn;
        this.converterMap = converterMap;
    }

    private class ColRel {
        Column srcCol;
        Column dstCol;

        ColRel(Column srcCol, Column dstCol){
            this.srcCol = srcCol;
            this.dstCol = dstCol;
        }
    }

    public <E extends Enum<E>> void xfer(Table<E> src, Table<E> dst) {
        try {
            Statement stmt = srcConn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + src.getTableName());
            List<Column> srcColumns = src.getColumns();
            List<Column> dstColumns = dst.getColumns();
            String names = srcColumns.stream().map(Column::getColumnName).collect(Collectors.joining(","));
            String vars = dstColumns.stream().map(n -> "?").collect(Collectors.joining(","));
            PreparedStatement prep = dstConn.prepareStatement("insert into " + dst.getTableName() +
                    "(" + names + ") values (" + vars + ")");
            List<ColRel> rels = new ArrayList<>();
            for(E e: src.listColumnEnums()){
                Column sc = src.getColumn(e);
                Column dc = dst.getColumn(e);
                if( sc != null && dc != null ){
                    rels.add(new ColRel(sc, dc));
                }
            }
            int nb = 0;
            while (rs.next()) {
                int index = 1;
                for(ColRel r: rels){
                    Column sc = r.srcCol;
                    Column dc = r.dstCol;
                    Object jdbcObj = sc.getResultSetObject(rs);
                    Object dstJdbcObj;
                    if( sc.getJdbcType() == dc.getJdbcType() ){
                        dstJdbcObj = jdbcObj;
                    } else {
                        Object javaObj = sc.convertJdbcObjectToJavaObject(jdbcObj);
                        Function<Object, Object> conv = converterMap.getConverter(sc.getJavaType(), dc.getJavaType());
                        Object dstJavaObj = conv.apply(javaObj);
                        dstJdbcObj = dc.convertJavaObjectToJdbcObject(dstJavaObj);
                    }
                    dc.setParam(prep, index++, dstJdbcObj);
                }
                prep.addBatch();
                nb += 1;
                if( nb >= 100 ){
                    prep.executeBatch();
                }
            }
            if( nb > 0 ){
                prep.executeBatch();
            }
            rs.close();
            stmt.close();
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
