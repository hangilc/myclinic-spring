package jp.chang.myclinic.apitool.pgsqltables;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import jp.chang.myclinic.apitool.Main;
import picocli.CommandLine.Command;

import java.sql.*;
import java.util.*;

@Command(name = "pgsql-tables")
public class PgsqlTables implements Runnable {

    @Inject
    @Named("pgsql")
    private Connection conn;

    private DatabaseMetaData meta;

    @Override
    public void run() {
        try {
            this.meta = conn.getMetaData();
            Map<String, List<String>> typeMap = new HashMap<>();
            for (Table table : listTables()) {
                String tableName = table.getName();
                table.getColumns().forEach(c -> {
                    String type = c.getType();
                    String item = String.format("%s:%s", tableName, c.getName());
                    if (typeMap.containsKey(type)) {
                        typeMap.get(type).add(item);
                    } else {
                        List<String> value = new ArrayList<>();
                        value.add(item);
                        typeMap.put(type, value);
                    }
                });
            }
            for (String type : typeMap.keySet()) {
                System.out.println(type.toUpperCase());
                System.out.printf("  %s\n", typeMap.get(type).toString());
            }
//            DATE
//                    [byoumei_master:valid_from, byoumei_master:valid_upto, disease:start_date, disease:end_date, intraclinic_comment:created_at, intraclinic_post:created_at, iyakuhin_master:valid_from, iyakuhin_master:valid_upto, kizai_master:valid_from, kizai_master:valid_upto, kouhi:valid_from, kouhi:valid_upto, koukikourei:valid_from, koukikourei:valid_upto, patient:birthday, presc_example:master_valid_from, roujin:valid_from, roujin:valid_upto, shahokokuho:valid_from, shahokokuho:valid_upto, shinryou_master:valid_from, shinryou_master:valid_upto]
//            JSONB
//                    [practice_log:body]
//            INT2
//                    [charge:charge, conduct:kind, drug:days, drug:category, drug:prescribed, koukikourei:futan_wari, payment:amount, pharma_queue:pharma_state, presc_example:days, presc_example:category, roujin:futan_wari, shahokokuho:honnin, shahokokuho:kourei, wqueue:wait_state]
//            INT4
//                    [byoumei_master:shoubyoumeicode, charge:visit_id, conduct:conduct_id, conduct:visit_id, conduct_drug:conduct_drug_id, conduct_drug:conduct_id, conduct_drug:iyakuhincode, conduct_kizai:conduct_kizai_id, conduct_kizai:conduct_id, conduct_kizai:kizaicode, conduct_shinryou:conduct_shinryou_id, conduct_shinryou:conduct_id, conduct_shinryou:shinryoucode, disease:disease_id, disease:patient_id, disease:shoubyoumeicode, disease_adj:disease_adj_id, disease_adj:disease_id, disease_adj:shuushokugocode, drug:drug_id, drug:visit_id, drug:iyakuhincode, drug_attr:drug_id, gazou_label:conduct_id, hotline:hotline_id, intraclinic_comment:comment_id, intraclinic_comment:post_id, intraclinic_post:post_id, intraclinic_tag:tag_id, intraclinic_tag_post:tag_id, intraclinic_tag_post:post_id, iyakuhin_master:iyakuhincode, kizai_master:kizaicode, kouhi:kouhi_id, kouhi:patient_id, kouhi:futansha, kouhi:jukyuusha, koukikourei:koukikourei_id, koukikourei:patient_id, koukikourei:hokensha_bangou, koukikourei:hihokensha_bangou, patient:patient_id, payment:visit_id, pharma_drug:iyakuhincode, pharma_queue:visit_id, practice_log:practice_log_id, presc_example:presc_example_id, presc_example:iyakuhincode, roujin:roujin_id, roujin:patient_id, roujin:shichouson, roujin:jukyuusha, shahokokuho:shahokokuho_id, shahokokuho:patient_id, shahokokuho:hokensha_bangou, shinryou:shinryou_id, shinryou:visit_id, shinryou:shinryoucode, shinryou_attr:shinryou_id, shinryou_master:shinryoucode, shouki:visit_id, shuushokugo_master:shuushokugocode, text:text_id, text:visit_id, visit:visit_id, visit:patient_id, visit:shahokokuho_id, visit:roujin_id, visit:koukikourei_id, visit:kouhi_1_id, visit:kouhi_2_id, visit:kouhi_3_id, wqueue:visit_id]
//            VARCHAR
//                    [byoumei_master:name, drug:usage, gazou_label:label, hotline:sender, hotline:recipient, intraclinic_comment:name, intraclinic_comment:content, intraclinic_tag:name, iyakuhin_master:name, iyakuhin_master:yomi, iyakuhin_master:unit, kizai_master:name, kizai_master:yomi, kizai_master:unit, patient:last_name, patient:first_name, patient:last_name_yomi, patient:first_name_yomi, patient:address, patient:phone, practice_log:kind, presc_example:usage, presc_example:comment, shahokokuho:hihokensha_kigou, shahokokuho:hihokensha_bangou, shinryou_master:name, shinryou_master:shuukeisaki, shuushokugo_master:name]
//            BPCHAR
//                    [disease:end_reason, iyakuhin_master:madoku, iyakuhin_master:kouhatsu, iyakuhin_master:zaikei, patient:sex, shinryou_master:tensuu_shikibetsu, shinryou_master:houkatsukensa, shinryou_master:kensagroup]
//            NUMERIC
//                    [conduct_drug:amount, conduct_kizai:amount, drug:amount, iyakuhin_master:yakka, kizai_master:kingaku, presc_example:amount, shinryou_master:tensuu]
//            TEXT
//                    [drug_attr:tekiyou, hotline:message, intraclinic_post:content, pharma_drug:description, pharma_drug:side_effect, shinryou_attr:tekiyou, shouki:shouki, text:content]
//            TIMESTAMP
//                    [hotline:posted_at, payment:paytime, practice_log:created_at, visit:visited_at]
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Table> listTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        for (String table : listTableNames()) {
            List<Column> columns = listColumns(table);
            tables.add(new Table(table, columns));
        }
        return tables;
    }

    private List<String> listTableNames() throws SQLException {
        ResultSet rs = meta.getTables(null, "public", "%", new String[]{"TABLE"});
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        rs.close();
        return tables;
    }

    private List<Column> listColumns(String table) throws SQLException {
        List<Column> cols = new ArrayList<>();
        ResultSet rs = meta.getColumns(null, "public", table, "%");
        while (rs.next()) {
            String name = rs.getString("COLUMN_NAME");
            boolean isAutoIncrement = rs.getString("IS_AUTOINCREMENT").equals("YES");
            String type = rs.getString("TYPE_NAME");
            cols.add(new Column(name, isAutoIncrement, type));
        }
        rs.close();
        rs = meta.getPrimaryKeys(null, "public", table);
        while (rs.next()) {
            String name = rs.getString("COLUMN_NAME");
            for (Column c : cols) {
                if (c.getName().equals(name)) {
                    c.setPrimary(true);
                    break;
                }
            }
        }
        rs.close();
        return cols;
    }

}
