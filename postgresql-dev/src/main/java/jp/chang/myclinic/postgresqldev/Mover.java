package jp.chang.myclinic.postgresqldev;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Mover {

    //private static Logger logger = LoggerFactory.getLogger(Mover.class);
    private Connection mysqlConnection;
    private Connection pgsqlConnection;
    private String mysqlSourceTable;
    private String pgsqlTargetTable;
    private List<Column> columns = new ArrayList<>();
    private List<Runnable> postMoveProcs = new ArrayList<>();

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

    void addIntColumn(String name){
        addColumn(new Column(name){
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                stmt.setInt(index, rs.getInt(name));
            }
        });
    }

    void addSerialColumn(String name, String pgsqlColumnName){
        class Local {
            private int maxIndex = 0;

            private void handleIndex(int idx){
                if( idx > maxIndex ){
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
            } catch(SQLException ex){
                ex.printStackTrace();
                throw new RuntimeException("Failed to reset sequence.");
            }
        });
        addColumn(new Column(name){
            @Override
            void setParam(PreparedStatement stmt, int index, ResultSet rs) throws SQLException {
                int idx = rs.getInt(name);
                local.handleIndex(idx);
                stmt.setInt(index, idx);
            }
        });
    }

    void move() throws SQLException {
        Statement stmt = mysqlConnection.createStatement();
        ResultSet rset = stmt.executeQuery("select * from " + mysqlSourceTable);
        PreparedStatement pgsqlStmt = pgsqlConnection.prepareStatement(createSql());
        int n = 0;
        while( rset.next() ){
            for(int i=0;i<columns.size();i++){
                Column c = columns.get(i);
                c.setParam(pgsqlStmt, i+1, rset);
            }
            pgsqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("%s %d\n", mysqlSourceTable, n);
            }
        }
        System.out.printf("visit %d\n", n);
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

}
