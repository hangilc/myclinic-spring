package jp.chang.myclinic.postgresqldev;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Mover {

    //private static Logger logger = LoggerFactory.getLogger(Mover.class);
    Connection mysqlConnection;
    Connection pgsqlConnection;
    String mysqlSourceTable;
    String pgsqlTargetTable;
    List<Column> columns = new ArrayList<>();

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

    void move() throws SQLException {
        Statement stmt = mysqlConnection.createStatement();
        ResultSet rset = stmt.executeQuery("select * from " + mysqlSourceTable);
        PreparedStatement pgsqlStmt = pgsqlConnection.prepareStatement(createSql());
        int n = 0;
        while( rset.next() ){
            for(int i=0;i<columns.size();i++){
                Column c = columns.get(i);
                c.setParam(pgsqlStmt, i+1, rset);
                pgsqlStmt.executeUpdate();
            }
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("%s %d\n", mysqlSourceTable, n);
            }
        }
        System.out.printf("visit %d\n", n);
        pgsqlStmt.close();
        rset.close();
        stmt.close();
    }

    private String createSql() {
        return String.format("insert into %s (%s) values (%s)", pgsqlTargetTable,
                columns.stream().map(Column::getName).collect(Collectors.joining(",")),
                columns.stream().map(c -> "?").collect(Collectors.joining(","))
        );
    }

}
