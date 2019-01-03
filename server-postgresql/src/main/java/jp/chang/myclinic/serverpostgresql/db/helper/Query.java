package jp.chang.myclinic.serverpostgresql.db.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Query<T> {

    private Table<T> table;

    public Query(Table<T> table) {
        this.table = table;
    }

    public List<T> selectAll(Connection conn, String sql, Consumer<PreparedStatement> paramSetter,
                             Supplier<T> dataSupplier) {
        try {
            List<String> names = table.getColumns().stream().map(Column::getName).collect(Collectors.toList());
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT " + String.join(",", names) + " " + sql
            );
            paramSetter.accept(stmt);
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            while( rs.next() ){
                T data = dataSupplier.get();
                table.getColumns().forEach(c -> c.stuffToData(data));
                result.add(data);
            }
            return result;
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
