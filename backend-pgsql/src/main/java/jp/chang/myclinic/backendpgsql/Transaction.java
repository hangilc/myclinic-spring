package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {

    private Connection conn;

    public Transaction(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection(){
        return conn;
    }

    public interface SqlConsumer<T> {
        void accept(T t) throws SQLException;
    }

    public interface SqlMapper<T> {
        T toDTO(ResultSet rs) throws SQLException;
    }

    public <T> T selectOne(String sql, SqlConsumer<PreparedStatement> paramSetter, SqlMapper<T> mapper) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            paramSetter.accept(stmt);
            ResultSet rs = stmt.executeQuery();
            T dto = null;
            if( rs.next() ){
                dto = mapper.toDTO(rs);
            }
            stmt.close();
            return dto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
