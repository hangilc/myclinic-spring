package jp.chang.myclinic.kouhatsukasan;

import jp.chang.myclinic.consts.DrugCategory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 3) {
            Map<String, YakuzaiEntry> yakuzaiMap = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                Path path = Paths.get(args[i]);
                try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("MS932"))) {
                    Iterable<CSVRecord> records = CSVFormat.RFC4180.withAllowMissingColumnNames().withFirstRecordAsHeader().parse(reader);
                    for (CSVRecord record : records) {
                        String kubun = record.get("区分");
                        String name = record.get("品名");
                        String yakkacode = record.get("薬価基準収載医薬品コード");
                        boolean isKouhatsu = "後発品".equals(record.get("診療報酬において加算等の算定対象となる後発医薬品"));
                        boolean isSenpatsu = "先発品".equals(record.get("先発医薬品"));
                        boolean hasKouhatsu = "○".equals(record.get("同一剤形・規格の後発医薬品がある先発医薬品"));
                        SenpatsuKouhatsuKubun senpatsuKouhatsuKubun = SenpatsuKouhatsuKubun.Other;
                        if (isKouhatsu) {
                            senpatsuKouhatsuKubun = SenpatsuKouhatsuKubun.Kouhatsu;
                        } else if (isSenpatsu && hasKouhatsu) {
                            senpatsuKouhatsuKubun = SenpatsuKouhatsuKubun.SenpatsuWithKouhatsu;
                        } else if (isSenpatsu && !hasKouhatsu) {
                            senpatsuKouhatsuKubun = SenpatsuKouhatsuKubun.SenpatuWithoutKouhatsu;
                        }
                        YakuzaiEntry entry = new YakuzaiEntry();
                        entry.kubun = kubun;
                        entry.name = name;
                        entry.yakkacode = yakkacode;
                        entry.senpatsuKouhatsuKubun = senpatsuKouhatsuKubun;
                        yakuzaiMap.put(yakkacode, entry);
                    }
                }
            }
            Map<String, Drug> drugMap1 = new HashMap<>();
            List<Drug> drugs1 = collectDrugs("2018-01-01", "2018-01-31");
            drugs1.forEach(drug -> extendDrugMap(drugMap1, drug));
            for(Map.Entry<String, Drug> entry: drugMap1.entrySet()){
                System.out.println(entry.getValue());
            }
        } else {
            System.err.println("Usage: kouhatsu TP-1.csv TP-2.csv TP-3.csv");
            System.exit(1);
        }
    }

    private void extendDrugMap(Map<String, Drug> map, Drug drug){
        Drug entry = map.get(drug.yakkacode);
        if( entry == null ){
            entry = drug;
        } else {
            entry = Drug.copy(entry);
            entry.amount += drug.amount;
        }
        map.put(drug.yakkacode, entry);
    }

    private List<Drug> collectDrugs(String dateFrom, String dateUpto){
        String sql = "select d.d_category as `category`, d.d_amount as amount, d.d_days as days, " +
                " m.yakkacode as yakkacode, m.name as name " +
                " from visit_drug d, visit v, iyakuhin_master_arch m " +
                " where d.visit_id = v.visit_id " +
                " and date(v.v_datetime) >= ? and date(v.v_datetime) <= ? " +
                " and m.iyakuhincode = d.d_iyakuhincode  " +
                " and m.valid_from <= date(v.v_datetime) " +
                " and (m.valid_upto = '0000-00-00' or m.valid_upto >= date(v.v_datetime)) ";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
                    Drug drug = new Drug();
                    drug.name = resultSet.getString("name");
                    drug.yakkacode = resultSet.getString("yakkacode");
                    int category = resultSet.getInt("category");
                    double amount = resultSet.getDouble("amount");
                    int days = resultSet.getInt("days");
                    if( DrugCategory.fromCode(category) == DrugCategory.Gaiyou ){
                        days = 1;
                    }
                    drug.amount = amount * days;
                    return drug;
                },
                dateFrom, dateUpto);
    }
}
