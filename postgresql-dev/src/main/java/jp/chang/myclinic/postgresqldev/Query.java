package jp.chang.myclinic.postgresqldev;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {

    private static Logger logger = LoggerFactory.getLogger(Query.class);

    public static void main(String[] args) throws Exception{
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/myclinic",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
        Query q = new Query();
        System.out.println(q.searchIyakuhinMasterByName(conn, "アムロジン", LocalDate.now()));
    }

    private Query(){

    }

    private String SQL_SearchIyakuhinMasterByName =
            "select * from iyakuhin_master m where m.name like CONCAT('%', ?, '%') and " +
            " m.valid_from <= ? and (m.valid_upto is null or m.valid_upto >= ?)";

    public List<IyakuhinMasterDTO> searchIyakuhinMasterByName(Connection conn, String text, LocalDate at) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(SQL_SearchIyakuhinMasterByName);
        stmt.setString(1, text);
        stmt.setObject(2, at);
        stmt.setObject(3, at);
        ResultSet rs = stmt.executeQuery();
        Map<String, Integer> indexMap = createIndexMap(rs.getMetaData());
        List<IyakuhinMasterDTO> list = new ArrayList<>();
        while( rs.next() ){
            list.add(mapIyakuhinMaster(rs, indexMap));
        }
        rs.close();
        stmt.close();
        return list;
    }


    public IyakuhinMasterDTO mapIyakuhinMaster(ResultSet rs, Map<String, Integer> indexMap) throws SQLException {
        IyakuhinMasterDTO dto = new IyakuhinMasterDTO();
        dto.iyakuhincode = rs.getInt(indexMap.get("iyakuhin_master.iyakuhincode"));
        dto.name = rs.getString(indexMap.get("iyakuhin_master.name"));
        dto.yomi = rs.getString(indexMap.get("iyakuhin_master.yomi"));
        dto.unit = rs.getString(indexMap.get("iyakuhin_master.unit"));
        dto.yakka = rs.getBigDecimal(indexMap.get("iyakuhin_master.yakka")).doubleValue();
        dto.madoku = rs.getString(indexMap.get("iyakuhin_master.madoku")).charAt(0);
        dto.kouhatsu = rs.getString(indexMap.get("iyakuhin_master.kouhatsu")).charAt(0);
        dto.zaikei = rs.getString(indexMap.get("iyakuhin_master.zaikei")).charAt(0);
        dto.validFrom = rs.getObject(indexMap.get("iyakuhin_master.valid_from"), LocalDate.class).toString();
        LocalDate validUpto = rs.getObject(indexMap.get("iyakuhin_master.valid_upto"), LocalDate.class);
        dto.validUpto = mapValidUpto(validUpto);
        return dto;
    }

    public Map<String, Integer> createIndexMap(ResultSetMetaData meta) throws SQLException {
        int n = meta.getColumnCount();
        Map<String, Integer> map = new HashMap<>();
        for(int i=1;i<=n;i++){
            String colname = meta.getColumnName(i);
            String tabname = meta.getTableName(i);
            map.put(tabname + "." + colname, i);
        }
        return map;
    }

    public String mapValidUpto(LocalDate validUpto){
        return validUpto == null ? "0000-00-00" : validUpto.toString();
    }

}
