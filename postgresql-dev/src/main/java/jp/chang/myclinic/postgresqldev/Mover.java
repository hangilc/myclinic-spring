package jp.chang.myclinic.postgresqldev;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class Mover {

    //private static Logger logger = LoggerFactory.getLogger(Mover.class);
    private Connection mysqlConnection;
    private Connection pgsqlConnection;
    private String mysqlSourceTable;
    private String pgsqlTargetTable;
    private List<Column> columns = new ArrayList<>();
    private List<Runnable> postMoveProcs = new ArrayList<>();
    private Supplier<String> mysqlQuerySupplier = null;

    Mover(Connection mysqlConnection, Connection pgsqlConnection, String mysqlSourceTable,
          String pgsqlTargetTable) {
        this.mysqlConnection = mysqlConnection;
        this.pgsqlConnection = pgsqlConnection;
        this.mysqlSourceTable = mysqlSourceTable;
        this.pgsqlTargetTable = pgsqlTargetTable;
    }

    void addColumn(Column col) {
        columns.add(col);
    }

    void addDateColumn(String name){
        addDateColumn(name, name);
    }

    void addDateColumn(String mysqlName, String pgsqlName){
        addColumn(new Column(pgsqlName) {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setDate(index, rs.getDate(mysqlName));
            }
        });
    }

    void addNullableDateColumn(String name){
        addNullableDateColumn(name, name);
    }

    void addNullableDateColumn(String mysqlName, String pgsqlName){
        addColumn(new Column(pgsqlName) {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setDate(index, convertValidUpto(rs.getString(mysqlName)));
            }
        });
    }

    void addValidFromColumn(){
        addColumn(new Column("valid_from") {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setDate(index, Date.valueOf(rs.getString("valid_from")));
            }
        });
    }

    void addValidUptoColumn(){
        addNullableDateColumn("valid_upto");
    }

    void addTimestamp(String name){
        addTimestamp(name, name);
    }

    void addTimestamp(String mysqlName, String pgsqlName){
        addColumn(new Column(pgsqlName) {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setTimestamp(index, rs.getTimestamp(mysqlName));
            }
        });
    }

    void addDecimalColumn(String name) {
        addDecimalColumn(name, name);
    }

    void addDecimalColumn(String mysqlName, String pgsqlName) {
        addColumn(new Column(pgsqlName) {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setBigDecimal(index, rs.getBigDecimal(mysqlName));
            }
        });
    }

    void addStringColumn(String name) {
        addStringColumn(name, name);
    }

    void addStringColumn(String mysqlName, String pgsqlName) {
        addColumn(new Column(pgsqlName) {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setString(index, rs.getString(mysqlName));
            }
        });
    }

    void addIntColumn(String name) {
        addIntColumn(name, name);
    }

    void addIntColumn(String mysqlName, String pgsqlName) {
        addColumn(new Column(pgsqlName) {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setInt(index, rs.getInt(mysqlName));
            }
        });
    }

    void addSerialColumn(String name) {
        addSerialColumn(name, name);
    }

    void addSerialColumn(String mysqlName, String pgsqlColumnName) {
        class Local {
            private int maxIndex = 0;

            private void handleIndex(int idx) {
                if (idx > maxIndex) {
                    maxIndex = idx;
                }
            }
        }
        Local local = new Local();
        postMoveProcs.add(() -> {
            String sql = String.format("alter table %s alter column %s restart with %d",
                    pgsqlTargetTable, pgsqlColumnName, local.maxIndex + 1);
            try {
                Statement seqStmt = pgsqlConnection.createStatement();
                seqStmt.executeUpdate(sql);
                seqStmt.close();
                System.out.printf("%s sequence restarts with %d.\n", pgsqlColumnName, local.maxIndex + 1);
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Failed to reset sequence.");
            }
        });
        addColumn(new Column(pgsqlColumnName) {
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                int idx = rs.getInt(mysqlName);
                local.handleIndex(idx);
                stmt.setInt(index, idx);
            }
        });
    }

    void setMySqlQuerySupplier(Supplier<String> querySupplier){
        this.mysqlQuerySupplier = querySupplier;
    }

    private String createMySqlQuery(){
        if( mysqlQuerySupplier != null ){
            return mysqlQuerySupplier.get();
        } else {
            return "select * from " + mysqlSourceTable;
        }
    }

    void move() throws SQLException {
        Statement stmt = mysqlConnection.createStatement();
        ResultSet rset = stmt.executeQuery(createMySqlQuery());
        PreparedStatement pgsqlStmt = pgsqlConnection.prepareStatement(createSql());
        int n = 0;
        while (rset.next()) {
            for (int i = 0; i < columns.size(); i++) {
                Column c = columns.get(i);
                c.setParam(pgsqlStmt, i + 1, rset);
            }
            pgsqlStmt.executeUpdate();
            n += 1;
            if (n % 1000 == 0) {
                System.out.printf("%s %d\n", mysqlSourceTable, n);
            }
        }
        System.out.printf("%s %d\n", pgsqlTargetTable, n);
        pgsqlStmt.close();
        rset.close();
        stmt.close();
        postMoveProcs.forEach(Runnable::run);
    }

    private String createSql() {
        return String.format("insert into %s (%s) values (%s)", pgsqlTargetTable,
                columns.stream().map(Column::getName).collect(Collectors.joining(",")),
                columns.stream().map(c -> "?").collect(Collectors.joining(","))
        );
    }

    private static Date convertValidUpto(String sqldate){
        if( sqldate == null ){
            return null;
        } else if( "0000-00-00".equals(sqldate) ){
            return null;
        } else {
            return Date.valueOf(sqldate);
        }
    }

}
