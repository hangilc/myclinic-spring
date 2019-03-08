package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Query {

    public interface SqlConsumer<T> {
        void accept(T t) throws SQLException;
    }

    public interface SqlMapper<T> {
        T toDTO(ResultSet rs) throws SQLException;
    }

    public static <T> T get(Connection conn, String sql, SqlConsumer<PreparedStatement> paramSetter,
                            SqlMapper<T> mapper) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            paramSetter.accept(stmt);
            ResultSet rs = stmt.executeQuery();
            T dto = null;
            if (rs.next()) {
                dto = mapper.toDTO(rs);
            }
            stmt.close();
            return dto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> select(Connection conn, String sql, SqlConsumer<PreparedStatement> paramSetter,
                                     SqlMapper<T> mapper) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            paramSetter.accept(stmt);
            ResultSet rs = stmt.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                T t = mapper.toDTO(rs);
                result.add(t);
            }
            stmt.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public class Pair<T, U> {
        public T first;
        public U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    public class Tuple<P, Q, R> {
        public P first;
        public Q second;
        public R third;

        public Tuple(P first, Q second, R third){
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

}
