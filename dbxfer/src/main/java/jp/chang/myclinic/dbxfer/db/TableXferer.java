package jp.chang.myclinic.dbxfer.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TableXferer {

    //private static Logger logger = LoggerFactory.getLogger(TableXferer.class);

    private Connection connSrc;
    private Table src;
    private Connection connDst;
    private Table dst;
    private List<String> colNames = new ArrayList<>();

    @FunctionalInterface
    private interface Proc {
        void exec(ResultSet rs, PreparedStatement stmt, int index);
    }

    private List<Proc> procs = new ArrayList<>();

    public TableXferer(Connection connSrc, Table src, Connection connDst, Table dst) {
        this.connSrc = connSrc;
        this.src = src;
        this.connDst = connDst;
        this.dst = dst;
    }

    public <T, U> void addColumn(Column<T> srcColumn, Column<U> dstColumn, Function<T, U> conv){
        colNames.add(dstColumn.getColumnName());
        procs.add((rs, stmt, index) -> {
            T value = srcColumn.getValueFromResultSet(rs);
            dstColumn.setParameter(stmt, index, conv.apply(value));
        });
    }

    public <T> void addColumn(Column<T> srcColumn, Column<T> dstColumn){
        addColumn(srcColumn, dstColumn, Function.identity());
    }

    public void xfer() {
        try {
            Statement stmt = connSrc.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + src.getName());
            String names = String.join(",", colNames);
            String vars = colNames.stream().map(n -> "?").collect(Collectors.joining(","));
            PreparedStatement prep = connDst.prepareStatement("insert into " + dst.getName() +
                    "(" + names + ") values (" + vars + ")");
            while (rs.next()) {
                for (int i = 0; i < procs.size(); i++) {
                    Proc proc = procs.get(i);
                    proc.exec(rs, prep, i + 1);
                }
                prep.executeUpdate();
            }
            rs.close();
            stmt.close();
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
