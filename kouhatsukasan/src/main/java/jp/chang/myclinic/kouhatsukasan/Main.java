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
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Map<String, YakuzaiEntry> yakuzaiMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 4) {
            for (int i = 0; i < 3; i++) {
                Path path = Paths.get(args[i]);
                try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("MS932"))) {
                    Iterable<CSVRecord> records = CSVFormat.RFC4180.withAllowMissingColumnNames().withFirstRecordAsHeader().parse(reader);
                    for (CSVRecord record : records) {
                        String name = record.get("品名");
                        String yakkacode = record.get("薬価基準収載医薬品コード");
                        boolean isKouhatsu = "後発品".equals(record.get("診療報酬において加算等の算定対象となる後発医薬品"));
                        boolean isSenpatsu = "先発品".equals(record.get("先発医薬品"));
                        boolean hasKouhatsu = "○".equals(record.get("同一剤形・規格の後発医薬品がある先発医薬品"));
                        Kubun kubun = Kubun.Other;
                        if (isKouhatsu) {
                            kubun = Kubun.Kouhatsu;
                        } else if( isSenpatsu ){
                            if( hasKouhatsu ){
                                kubun = Kubun.SenpatsuWithKouhatsu;
                            } else {
                                kubun = Kubun.SenpatsuWithoutKouhatsu;
                            }
                        }
                        YakuzaiEntry entry = new YakuzaiEntry();
                        entry.kubun = kubun;
                        entry.name = name;
                        entry.yakkacode = yakkacode;
                        yakuzaiMap.put(yakkacode, entry);
                    }
                }
            }
            int year = Integer.parseInt(args[3]);
            List<Drug> drugs1 = collectDrugs(year, 1);
            Map<Kubun, Double> summary1 = new HashMap<>();
            extendSummary(summary1, drugs1);
            List<Drug> drugs2 = collectDrugs(year, 2);
            Map<Kubun, Double> summary2 = new HashMap<>();
            extendSummary(summary2, drugs2);
            List<Drug> drugs3 = collectDrugs(year, 3);
            Map<Kubun, Double> summary3 = new HashMap<>();
            extendSummary(summary3, drugs3);
            Map<Kubun, Double> grandSum = grandSum(List.of(summary1, summary2, summary3));
            report(summary1, summary2, summary3, grandSum);
            reportPossibleKouhatsu(List.of(drugs1, drugs2, drugs3));
        } else {
            System.err.println("Usage: kouhatsu TP-1.csv TP-2.csv TP-3.csv year");
            System.exit(1);
        }
    }

    private void reportPossibleKouhatsu(List<List<Drug>> drugsList){
        Map<String, Drug> map = new HashMap<>();
        drugsList.forEach(drugs -> {
            drugs.forEach(drug -> {
                if( drug.kubun == Kubun.SenpatsuWithKouhatsu ){
                    Drug bind = map.get(drug.yakkacode);
                    if( bind == null ){
                        bind = drug;
                    } else {
                        bind = Drug.copy(bind);
                        bind.amount += drug.amount;
                    }
                    map.put(drug.yakkacode, bind);
                }
            });
        });
        List<Drug> pks = new ArrayList<>(map.values());
        pks.sort((a, b) -> Double.compare(b.amount, a.amount));
        System.out.println("== Possible Kouhatsu ==");
        for(Drug d: pks.subList(0, 20)){
            String name = d.name;
            int slen = 12;
            if( name.length() > slen ){
                name = name.substring(0, slen);
            } else {
                name = name + String.join("", Collections.nCopies(slen - name.length(), "　"));
            }
            System.out.printf("  %s: %,10.0f\n", name, d.amount);
        }
        System.out.println();
    }

    private void report(Map<Kubun, Double> sum1, Map<Kubun, Double> sum2, Map<Kubun, Double> sum3,
                        Map<Kubun, Double> grand){
        System.out.println("   Jan         Feb         Mar         Sum    ");
        System.out.println("==========  ==========  ==========  ==========");
        System.out.printf("%,10.0f  %,10.0f  %,10.0f  %,10.0f\n",
                yakuzaiTotal(sum1), yakuzaiTotal(sum2), yakuzaiTotal(sum3),
                yakuzaiTotal(grand));
        System.out.printf("%,10.0f  %,10.0f  %,10.0f  %,10.0f\n",
                yakuzaiPossibleKouhatsu(sum1), yakuzaiPossibleKouhatsu(sum2), yakuzaiPossibleKouhatsu(sum3),
                yakuzaiPossibleKouhatsu(grand));
        System.out.printf("%,10.0f  %,10.0f  %,10.0f  %,10.0f\n",
                yakuzaiKouhatsu(sum1), yakuzaiKouhatsu(sum2), yakuzaiKouhatsu(sum3),
                yakuzaiKouhatsu(grand));
        System.out.printf("%10.1f  %10.1f  %10.1f  %10.1f\n",
                replaceRate(sum1) * 100, replaceRate(sum2) * 100, replaceRate(sum3) * 100,
                replaceRate(grand) * 100);
        System.out.printf("%10.1f  %10.1f  %10.1f  %10.1f\n",
                cutoffRate(sum1) * 100, cutoffRate(sum2) * 100, cutoffRate(sum3) * 100,
                cutoffRate(grand) * 100);
        System.out.println();
    }

    private double yakuzaiTotal(Map<Kubun, Double> map){
        return map.get(Kubun.Kouhatsu) + map.get(Kubun.SenpatsuWithKouhatsu) +
                map.get(Kubun.SenpatsuWithoutKouhatsu);
    }

    private double yakuzaiPossibleKouhatsu(Map<Kubun, Double> map){
        return map.get(Kubun.Kouhatsu) + map.get(Kubun.SenpatsuWithKouhatsu);
    }

    private double yakuzaiKouhatsu(Map<Kubun, Double> map){
        return map.get(Kubun.Kouhatsu);
    }

    private Map<Kubun, Double> grandSum(List<Map<Kubun, Double>> maps){
        Map<Kubun, Double> grandSum = new HashMap<>();
        grandSum.put(Kubun.Kouhatsu, 0.0);
        grandSum.put(Kubun.SenpatsuWithKouhatsu, 0.0);
        grandSum.put(Kubun.SenpatsuWithoutKouhatsu, 0.0);
        maps.forEach(map -> {
            for(Kubun kubun: grandSum.keySet()){
                grandSum.compute(kubun, (k, v) -> v + map.get(k));
            }
        });
        return grandSum;
    }

    private double replaceRate(Map<Kubun, Double> map){
        double kouhatsu = map.get(Kubun.Kouhatsu);
        double senpatsuWithKouhatsu = map.get(Kubun.SenpatsuWithKouhatsu);
        return kouhatsu / (kouhatsu + senpatsuWithKouhatsu);
    }

    private double cutoffRate(Map<Kubun, Double> map){
        double kouhatsu = map.get(Kubun.Kouhatsu);
        double senpatsuWithKouhatsu = map.get(Kubun.SenpatsuWithKouhatsu);
        double senpatsuWithoutKouhatsu = map.get(Kubun.SenpatsuWithoutKouhatsu);
        return (kouhatsu + senpatsuWithKouhatsu) / (kouhatsu + senpatsuWithKouhatsu + senpatsuWithoutKouhatsu);

    }

    private List<Drug> collectDrugs(int year, int month){
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate upto = start.plusMonths(1).minusDays(1);
        return collectDrugs(start.toString(), upto.toString());
    }

    private void extendSummary(Map<Kubun, Double> summary, Collection<Drug> drugs){
        for(Drug drug: drugs){
            Kubun kubun = drug.kubun;
            if( kubun != Kubun.Other ){
                summary.compute(kubun, (k, v) -> {
                    if( v == null ){
                        v = 0.0;
                    }
                    return v + drug.amount;
                });
            }
        }
    }

    private Kubun resolveKubun(String yakkacode){
        YakuzaiEntry entry = yakuzaiMap.get(yakkacode);
        return entry == null ? Kubun.Other : entry.kubun;
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
                    drug.kubun = resolveKubun(drug.yakkacode);
                    return drug;
                },
                dateFrom, dateUpto);
    }
}
