package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Transactional
class PracticeLogJdbc {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(LocalDateTime createdAt, String kind, String body) {
        String sql = "insert into practice_log (created_at, kind, body) values (?, ?, ?::json) " +
                " returning practice_log_id";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, createdAt, kind, body);
        rs.next();
        return rs.getInt(1);
    }

}
