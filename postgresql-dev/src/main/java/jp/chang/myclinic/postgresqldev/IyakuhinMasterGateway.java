package jp.chang.myclinic.postgresqldev;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.postgresql.Columns;
import jp.chang.myclinic.postgresql.IyakuhinMaster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class IyakuhinMasterGateway {

    //private static Logger logger = LoggerFactory.getLogger(IyakuhinMasterGateway.class);
    private Columns<IyakuhinMasterDTO> masterColumns = IyakuhinMaster.columns();

    private String searchIyakuhinByName_SQL =
            "select " + masterColumns.names() + " from iyakuhin_master " +
                    " where name like CONCAT('%', ?, '%') and " +
                    " valid_from <= ? and (valid_upto is null or valid_upto >= ?) " +
                    " order by yomi";

    public List<IyakuhinMasterDTO> searchIyakuhinByName(Connection conn, String text, LocalDate at)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(searchIyakuhinByName_SQL);
            stmt.setString(1, text);
            stmt.setObject(2, at);
            stmt.setObject(3, at);
            rs = stmt.executeQuery();
            List<IyakuhinMasterDTO> result = new ArrayList<>();
            while (rs.next()) {
                result.add(masterColumns.toDTO(rs, new IyakuhinMasterDTO()));
            }
            return result;
        } finally {
            if( rs != null ){
                rs.close();
            }
            if( stmt != null ){
                stmt.close();
            }
        }
    }

    private String findIyakuhinMaster_SQL = "select " + masterColumns.names() + " from iyakuhin_master " +
            " where iyakuhincode = ? and " +
            " valid_from <= ? " +
            " and (valid_upto is null or valid_upto >= ?) ";


    public Optional<IyakuhinMasterDTO> findIyakuhinMaster(Connection conn, int iyakuhincode, String sqldate)
            throws SQLException {
        LocalDate at = LocalDate.parse(sqldate);
        try(PreparedStatement stmt = conn.prepareStatement(findIyakuhinMaster_SQL)) {
            stmt.setInt(1, iyakuhincode);
            stmt.setObject(2, at);
            stmt.setObject(3, at);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(masterColumns.toDTO(rs, new IyakuhinMasterDTO()));
                } else {
                    return Optional.empty();
                }
            }
        }
    }


}
