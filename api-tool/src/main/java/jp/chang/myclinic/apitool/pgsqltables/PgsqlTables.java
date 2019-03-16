package jp.chang.myclinic.apitool.pgsqltables;

import com.github.javaparser.ast.CompilationUnit;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import jp.chang.myclinic.apitool.PgsqlConnectionProvider;
import jp.chang.myclinic.apitool.lib.gentablebase.DatabaseSpecifics;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;
import jp.chang.myclinic.apitool.lib.gentablebase.TableBaseGenerator;
import jp.chang.myclinic.apitool.lib.gentablebase.TableLister;
import picocli.CommandLine.Command;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Command(name = "pgsql-tables", description = "creates TableBase.java files in backend-pgsql")
public class PgsqlTables implements Runnable {

    private DatabaseSpecifics dbSpecs = new PgsqlSpecifics();
    private String basePackage = "jp.chang.myclinic.backendpgsql";

    @Override
    public void run(){
        Formatter formatter = new Formatter();
        try( Connection conn = PgsqlConnectionProvider.get() ){
            TableLister tableLister = new TableLister(dbSpecs);
            List<Table> tables = tableLister.listTables(conn);
            for(Table table: tables){
//                if( !table.getName().equals("patient") ){
//                    continue;
//                }
                TableBaseGenerator gen = new TableBaseGenerator(table, dbSpecs);
                gen.setBasePackage(basePackage);
                CompilationUnit unit = gen.generate();
                String src = formatter.formatSource(unit.toString());
                System.out.println(src);
            }
        } catch(SQLException | FormatterException e){
            throw new RuntimeException(e);
        }
    }

//    @Inject
//    @Named("pgsql")
//    private Connection conn;
//    @Inject
//    private Helper helper;
//    @Inject @Named("backend-pgsql-dir")
//    private Path backendPgsqlDir;
//    @Inject
//    private TableCodeGenerator tableGen;
//
//    private DatabaseMetaData meta;
//
//    private String formatSource(Formatter formatter, String source){
//        try {
//            return formatter.formatSource(source);
//        } catch (FormatterException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            this.meta = conn.getMetaData();
//            Map<String, List<String>> typeMap = new HashMap<>();
//            List<Table> tables = listTables();
//            Formatter formatter = new Formatter();
//            Path tableBaseDir = backendPgsqlDir.resolve("tablebase");
//            Path tableDir = backendPgsqlDir.resolve("table");
//            tables.forEach(table -> {
//                if (!table.getName().equals("patient")) {
//                    return;
//                }
//                Class<?> classDTO = TableToDTOMap.mapToDTO(table.getName());
//                SourceCodeGenerator gen = new SourceCodeGenerator();
//                CompilationUnit unit = gen.create(table, classDTO);
//                String src = formatSource(formatter, unit.toString());
//                System.out.println(src);
//                //gen.save(tableBaseDir, src);
////                {
////                    String src = formatSource(formatter, tableGen.generate(table).toString());
////                    System.out.println(src);
////                }
//            });
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private List<Table> listTables() throws SQLException {
//        List<Table> tables = new ArrayList<>();
//        for (String table : listTableNames()) {
//            List<Column> columns = listColumns(table);
//            tables.add(new Table(table, columns));
//        }
//        return tables;
//    }
//
//    private List<String> listTableNames() throws SQLException {
//        ResultSet rs = meta.getTables(null, "public", "%", new String[]{"TABLE"});
//        List<String> tables = new ArrayList<>();
//        while (rs.next()) {
//            tables.add(rs.getString("TABLE_NAME"));
//        }
//        rs.close();
//        return tables;
//    }
//
////  date
////          [byoumei_master:valid_from, byoumei_master:valid_upto, disease:start_date, disease:end_date, intraclinic_comment:created_at, intraclinic_post:created_at, iyakuhin_master:valid_from, iyakuhin_master:valid_upto, kizai_master:valid_from, kizai_master:valid_upto, kouhi:valid_from, kouhi:valid_upto, koukikourei:valid_from, koukikourei:valid_upto, patient:birthday, presc_example:master_valid_from, roujin:valid_from, roujin:valid_upto, shahokokuho:valid_from, shahokokuho:valid_upto, shinryou_master:valid_from, shinryou_master:valid_upto]
////  jsonb
////          [practice_log:body]
////  int2
////          [charge:charge, conduct:kind, drug:days, drug:category, drug:prescribed, koukikourei:futan_wari, payment:amount, pharma_queue:pharma_state, presc_example:days, presc_example:category, roujin:futan_wari, shahokokuho:honnin, shahokokuho:kourei, wqueue:wait_state]
////  int4
////          [byoumei_master:shoubyoumeicode, charge:visit_id, conduct:conduct_id, conduct:visit_id, conduct_drug:conduct_drug_id, conduct_drug:conduct_id, conduct_drug:iyakuhincode, conduct_kizai:conduct_kizai_id, conduct_kizai:conduct_id, conduct_kizai:kizaicode, conduct_shinryou:conduct_shinryou_id, conduct_shinryou:conduct_id, conduct_shinryou:shinryoucode, disease:disease_id, disease:patient_id, disease:shoubyoumeicode, disease_adj:disease_adj_id, disease_adj:disease_id, disease_adj:shuushokugocode, drug:drug_id, drug:visit_id, drug:iyakuhincode, drug_attr:drug_id, gazou_label:conduct_id, hotline:hotline_id, intraclinic_comment:comment_id, intraclinic_comment:post_id, intraclinic_post:post_id, intraclinic_tag:tag_id, intraclinic_tag_post:tag_id, intraclinic_tag_post:post_id, iyakuhin_master:iyakuhincode, kizai_master:kizaicode, kouhi:kouhi_id, kouhi:patient_id, kouhi:futansha, kouhi:jukyuusha, koukikourei:koukikourei_id, koukikourei:patient_id, koukikourei:hokensha_bangou, koukikourei:hihokensha_bangou, patient:patient_id, payment:visit_id, pharma_drug:iyakuhincode, pharma_queue:visit_id, practice_log:practice_log_id, presc_example:presc_example_id, presc_example:iyakuhincode, roujin:roujin_id, roujin:patient_id, roujin:shichouson, roujin:jukyuusha, shahokokuho:shahokokuho_id, shahokokuho:patient_id, shahokokuho:hokensha_bangou, shinryou:shinryou_id, shinryou:visit_id, shinryou:shinryoucode, shinryou_attr:shinryou_id, shinryou_master:shinryoucode, shouki:visit_id, shuushokugo_master:shuushokugocode, text:text_id, text:visit_id, visit:visit_id, visit:patient_id, visit:shahokokuho_id, visit:roujin_id, visit:koukikourei_id, visit:kouhi_1_id, visit:kouhi_2_id, visit:kouhi_3_id, wqueue:visit_id]
////  varchar
////          [byoumei_master:name, drug:usage, gazou_label:label, hotline:sender, hotline:recipient, intraclinic_comment:name, intraclinic_comment:content, intraclinic_tag:name, iyakuhin_master:name, iyakuhin_master:yomi, iyakuhin_master:unit, kizai_master:name, kizai_master:yomi, kizai_master:unit, patient:last_name, patient:first_name, patient:last_name_yomi, patient:first_name_yomi, patient:address, patient:phone, practice_log:kind, presc_example:usage, presc_example:comment, shahokokuho:hihokensha_kigou, shahokokuho:hihokensha_bangou, shinryou_master:name, shinryou_master:shuukeisaki, shuushokugo_master:name]
////  bpchar
////          [disease:end_reason, iyakuhin_master:madoku, iyakuhin_master:kouhatsu, iyakuhin_master:zaikei, patient:sex, shinryou_master:tensuu_shikibetsu, shinryou_master:houkatsukensa, shinryou_master:kensagroup]
////  numeric
////          [conduct_drug:amount, conduct_kizai:amount, drug:amount, iyakuhin_master:yakka, kizai_master:kingaku, presc_example:amount, shinryou_master:tensuu]
////  text
////          [drug_attr:tekiyou, hotline:message, intraclinic_post:content, pharma_drug:description, pharma_drug:side_effect, shinryou_attr:tekiyou, shouki:shouki, text:content]
////  timestamp
////          [hotline:posted_at, payment:paytime, practice_log:created_at, visit:visited_at]
//
//// JDBC Types CHAR(1), NUMERIC(2), INTEGER(4), SMALLINT(5), OTHER(1111 -> jsonb), DATE(91), VARCHAR(12), TIMESTAMP(93)
//
//    private Map<Integer, Class<?>> sqlTypeMap = new HashMap<>();
//
//    {
//        sqlTypeMap.put(Types.CHAR, String.class);
//        sqlTypeMap.put(Types.NUMERIC, BigDecimal.class);
//        sqlTypeMap.put(Types.INTEGER, Integer.class);
//        sqlTypeMap.put(Types.SMALLINT, Integer.class);
//        sqlTypeMap.put(Types.DATE, LocalDate.class);
//        sqlTypeMap.put(Types.VARCHAR, String.class);
//        sqlTypeMap.put(Types.TIMESTAMP, LocalDateTime.class);
//    }
//
//    private List<Column> listColumns(String table) throws SQLException {
//        List<Column> cols = new ArrayList<>();
//        ResultSet rs = meta.getColumns(null, "public", table, "%");
//        while (rs.next()) {
//            String name = rs.getString("COLUMN_NAME");
//            boolean isAutoIncrement = rs.getString("IS_AUTOINCREMENT").equals("YES");
//            String pgsqlTypeName = rs.getString("TYPE_NAME");
//            int dataType = rs.getInt("DATA_TYPE");
//            Class<?> jdbcClass = null;
//            if (dataType == Types.OTHER && pgsqlTypeName.equals("jsonb")) {
//                jdbcClass = String.class;
//            } else {
//                jdbcClass = sqlTypeMap.get(dataType);
//            }
//            if (jdbcClass == null) {
//                String msg = String.format("Cannot handle sql type (%s:%s %s %d[%s]).", table, name,
//                        pgsqlTypeName, dataType, JDBCType.valueOf(dataType).getName());
//                throw new RuntimeException(msg);
//            }
//            String dtoFieldName = DTOFieldNameResolver.resolve(table, name);
//            cols.add(new Column(name, isAutoIncrement, jdbcClass, dtoFieldName));
//        }
//        rs.close();
//        rs = meta.getPrimaryKeys(null, "public", table);
//        while (rs.next()) {
//            String name = rs.getString("COLUMN_NAME");
//            for (Column c : cols) {
//                if (c.getName().equals(name)) {
//                    c.setPrimary(true);
//                    break;
//                }
//            }
//        }
//        rs.close();
//        return cols;
//    }

}
