package jp.chang.myclinic.pharma;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.drawer.DrawerColor;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.dto.*;

import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by hangil on 2017/06/14.
 */
public class DrugBagDataCreator {

    private DrugDTO drug;
    private IyakuhinMasterDTO master;
    private PatientDTO patient;
    private PharmaDrugDTO pharmaDrug;
    private DrugCategory category;
    private static Pattern timesPattern = Pattern.compile("分([0-9０-９]+)(.*)");
    private static Pattern unevenPattern = Pattern.compile(".*[(（]([- 　0-9０-９.．ー－]+)[)）](.*)");
    private static Pattern mealsPattern = Pattern.compile("([朝昼夕]+)食(.)");
    private static NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private static Map<Integer, Double> powderDrugMap = new HashMap<>();

    static {
        powderDrugMap.put(620000007, 0.5);  // アトミフェンＤＳ
        powderDrugMap.put(620721001, 1.0);  // シーピーＧ
        powderDrugMap.put(613180070, 1.0);  // シーピーＧ
        powderDrugMap.put(620420001, 1.0);  // ビオフェルミン
        powderDrugMap.put(612370050, 1.0);  // ビオフェルミン
        powderDrugMap.put(620160501, 1.0);  // ＰＬ
        powderDrugMap.put(611180001, 1.0);  // ＰＬ
        powderDrugMap.put(620161301, 1.0);  // 幼児用ＰＬ
        powderDrugMap.put(620459001, 0.67);  // マーズレンＳ
        powderDrugMap.put(612320261, 0.67);  // マーズレンＳ
        powderDrugMap.put(613940051, 1.0);  // ウラリットーＵ
        powderDrugMap.put(620491801, 0.5);  // アローゼン
        powderDrugMap.put(612350003, 0.5);  // アローゼン
        powderDrugMap.put(610462036, 1.0);  // コデイン散
        powderDrugMap.put(620392528, 1.0);  // コデイン散
    }

    public DrugBagDataCreator(DrugFullDTO drugFull, PatientDTO patient, PharmaDrugDTO pharmaDrug){
        this.drug = drugFull.drug;
        this.master = drugFull.master;
        this.patient = patient;
        this.pharmaDrug = pharmaDrug;
        category = DrugCategory.fromCode(drug.category);
    }

    public DrugBagDrawerData createData(){
        DrugBagDrawerData data = new DrugBagDrawerData();
        data.color = getColor();
        data.title = getTitle();
        data.patientName = patient.lastName + " " + patient.firstName;
        data.patientNameYomi = patient.lastNameYomi + " " + patient.firstNameYomi;
        data.instructions = composeInstructions();
        data.drugName = composeDrugName();
        data.drugDescription = composeDescription();
        return data;
    }

    private DrawerColor getColor(){
        if( category == null ){
            return new DrawerColor(0, 0, 0);
        }
        switch(category){
            case Naifuku: return new DrawerColor(0, 0, 255);
            case Tonpuku: return new DrawerColor(0, 255, 0);
            case Gaiyou: return new DrawerColor(255, 0, 0);
            default: return new DrawerColor(0, 0, 0);
        }
    }

    private String getTitle(){
        if( category == null ){
            return "";
        }
        switch(category){
            case Naifuku: return "内服薬";
            case Tonpuku: return "頓服薬";
            case Gaiyou: return "外用薬";
            default: return "おくすり";
        }
    }

    private List<String> composeInstructions(){
        if( category == null ){
            return Collections.emptyList();
        }
        switch(category){
            case Naifuku: return naifukuInstructions();
            case Tonpuku: return tonpukuInstructions();
            case Gaiyou: return gaiyouInstructions();
            default: return Collections.emptyList();
        }
    }

    private List<String> gaiyouInstructions() {
        List<String> instrs = new ArrayList<>();
        instrs.add(drug.usage);
        return instrs;
    }

    private List<String> tonpukuInstructions() {
        List<String> instrs = new ArrayList<>();
        instrs.add("１回" + dosageRep(1, 1) + " " + numberToKanjiString(drug.days) + "回分");
        instrs.add(drug.usage);
        return instrs;
    }

    private List<String> naifukuInstructions(){
        Matcher matcher = timesPattern.matcher(drug.usage);
        if( matcher.matches() ){
            String restUsage = matcher.group(2);
            int times = Integer.parseInt(kanjiStringToDigitString(matcher.group(1)));
            return naifukuWithTimes(times, trim(restUsage));
        } else {
            return naifukuWithoutTimes();
        }
    }

    private List<String> naifukuWithTimes(int times, String usage) {
        List<String> instrs = new ArrayList<>();
        instrs.add(
             "１日" + numberStringToKanjiString("" + times) + "回 " +
                     numberStringToKanjiString("" + drug.days) + "日分"
        );
        Matcher matcher = unevenPattern.matcher(usage);
        if( matcher.matches() ){
            List<Double> weights = parseUnevenWeights(matcher.group(1));
            if( times != weights.size() ){
                throw new RuntimeException("invalid uneven prescription (times and weights does not match)");
            }
            String restUsage = trim(matcher.group(2));
            List<String> timings = extractTimingParts(restUsage);
            if( timings.size() != weights.size() ){
                System.out.println(weights);
                System.out.println(timings);
                throw new RuntimeException("invalid uneven prescription (timings and weights does not match)");
            }
            naifukuUneven(instrs, times, weights, timings);
        } else {
            naifukuEven(instrs, times, usage);
        }
        return instrs;
    }

    private void naifukuUneven(List<String> instrs, int times, List<Double> weights, List<String> timings){
        double totalWeights = weights.stream().mapToDouble(w -> w).sum();
        List<String> parts = new ArrayList<>();
        for(int i=0;i<weights.size();i++){
            double weight = weights.get(i);
            String timing = timings.get(i);
            parts.add(timing + dosageRep(weight, totalWeights));
        }
        instrs.add(parts.stream().collect(Collectors.joining("、")));
    }

    private List<String> extractTimingParts(String usage){
        List<String> parts = new ArrayList<>();
        for(String part: usage.split("[,、]")){
            part = part.trim();
            if( part.startsWith("毎食") ){
                String suffix = part.substring(2, 3);
                parts.add("朝食" + suffix);
                parts.add("昼食" + suffix);
                parts.add("夕食" + suffix);
            } else {
                Matcher matcher = mealsPattern.matcher(usage);
                if( matcher.matches() ){
                    String suffix = matcher.group(2);
                    matcher.group(1).chars().forEach(ch -> {
                        parts.add(String.format("%c食%s", ch, suffix));
                    });
                } else {
                    parts.add(part);
                }
            }
        }
        return parts;
    }

    private List<Double> parseUnevenWeights(String src){
        StringBuilder sb = new StringBuilder();
        src.chars().forEach(ch -> {
            if( ch == '-' || ch == 'ー' || ch == '－' ){
                sb.append('-');
            } else if( ch == ' ' || ch == '\t' ){
                // nop
            } else if( ch == '.' || ch == '．' ){
                sb.append('.');
            } else {
                ch = kanjiToDigit(ch);
                if( ch >= '0' && ch <= '9' ){
                    sb.appendCodePoint(ch);
                } else {
                    throw new Error("invalid char in uneven weights");
                }
            }
        });
        //System.out.println("uneven part: " + sb.toString());
        return Arrays.stream(sb.toString().split("-")).map(Double::parseDouble).collect(Collectors.toList());
    }

    private void naifukuEven(List<String> instrs, int times, String usage) {
        instrs.add("１回" + dosageRep(1, times));
        instrs.add(usage);
    }

    private List<String> naifukuWithoutTimes() {
        return Arrays.asList("１日" + dosageRep() + " " +
                numberStringToKanjiString("" + drug.days) + "日分",
                drug.usage);
    }

    private String composeDrugName(){
        String name = master.name;
        Double powderDose = powderDrugMap.get(drug.iyakuhincode);
        if( powderDose != null ){
            name += "（１包" + numberToKanjiString(powderDose) +  "ｇ）";
        }
        return name;
    }

    private String composeDescription(){
        if( pharmaDrug == null ){
            return "";
        }
        return "【効能】" + pharmaDrug.description + "【副作用】" + pharmaDrug.sideeffect;
    }

    private int digitToKanji(int codePoint){
        switch(codePoint){
            case '0': return '０';
            case '1': return '１';
            case '2': return '２';
            case '3': return '３';
            case '4': return '４';
            case '5': return '５';
            case '6': return '６';
            case '7': return '７';
            case '8': return '８';
            case '9': return '９';
            default: return codePoint;
        }
    }

    private int kanjiToDigit(int codePoint){
        switch(codePoint){
            case '０': return '0';
            case '１': return '1';
            case '２': return '2';
            case '３': return '3';
            case '４': return '4';
            case '５': return '5';
            case '６': return '6';
            case '７': return '7';
            case '８': return '8';
            case '９': return '9';
            default: return codePoint;
        }
    }

    private int numToKanji(int codePoint){
        switch(codePoint){
            case '.': return '．';
            default: return digitToKanji(codePoint);
        }
    }

    private String numberStringToKanjiString(String s){
        return s.codePoints().map(this::numToKanji)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String kanjiStringToDigitString(String s){
        return s.codePoints().map(this::numToKanji)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String dosageRep(double numer, double denom){
        double value = drug.amount * numer / denom;
        Double powderPack = powderDrugMap.get(drug.iyakuhincode);
        if( powderPack != null ){
            double dose = value / powderPack;
            if( dose > 0.98 && dose < 1.02 ){
                dose = 1;
            }
            return numberToKanjiString(dose) + "包";
        } else {
            return numberToKanjiString(value) + master.unit;
        }
    }

    private String dosageRep(){
        return dosageRep(1, 1);
    }

    private String numberToKanjiString(double value){
        return numberStringToKanjiString(numberFormat.format(value).replace(",", ""));
    }

    private String numberToKanjiString(int value){
        return numberStringToKanjiString("" + value);
    }

    private String trim(String str){
        return str.replaceAll("(^\\s|　)+|(\\s|　)+$", "");
    }

}
