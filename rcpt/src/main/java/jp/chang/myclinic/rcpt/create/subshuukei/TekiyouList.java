package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.*;
import jp.chang.myclinic.rcpt.lib.*;
import jp.chang.myclinic.util.NumberUtil;
import jp.chang.myclinic.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TekiyouList {
    private static Logger logger = LoggerFactory.getLogger(TekiyouList.class);

    private static Map<Integer, String> shinryouAliasMap;
    private String code;
    private List<Tekiyou> items = new ArrayList<>();

    private TekiyouList(String code){
        this.code = code;
    }

    TekiyouList(SubShuukei subShuukei) {
        this("" + subShuukei.getCode());
    }

    TekiyouList(SubShuukeiTouyaku subShuukei) {
        this("" + subShuukei.getCode());
    }

    TekiyouList(SubShuukeiChuusha subShuukei){
        this("" + subShuukei.getCode());
    }

    private static void setupShinryouAlias() {
        ResolvedShinryouMap shinryouMasterMap = Globals.shinryouMasterMap;
        shinryouAliasMap = new HashMap<>();
        shinryouAliasMap.put(shinryouMasterMap.薬剤情報提供, "（薬情）");
        shinryouAliasMap.put(shinryouMasterMap.特定疾患管理, "（特）");
        shinryouAliasMap.put(shinryouMasterMap.特定疾患処方, "（特処）");
        shinryouAliasMap.put(shinryouMasterMap.特定疾患処方管理加算処方せん料, "（特処）");
        shinryouAliasMap.put(shinryouMasterMap.長期処方, "（特処長）");
        shinryouAliasMap.put(shinryouMasterMap.長期投薬加算処方せん料, "（特処長）");
    }

    private Map<Integer, String> getShinryouAliasMap(){
        if( shinryouAliasMap == null ){
            setupShinryouAlias();
        }
        return shinryouAliasMap;
    }

    void add(Tekiyou tekiyou){
        items.add(tekiyou);
    }

    void add(String name, Integer tanka, Integer count) {
        items.add(new StandardTekiyou(name, tanka, count));
    }

    void add(ShinryouItem<? extends ShinryouItemData> item) {
        String name;
        if (getShinryouAliasMap().containsKey(item.getShinryoucode())) {
            name = getShinryouAliasMap().get(item.getShinryoucode());
        } else {
            name = item.getData().getName();
        }
        items.add(new StandardTekiyou(name, item.getTanka(), item.getCount()));
        if( item.getData().getTekiyou() != null ){
            items.add(new TekiyouAux(item.getData().getTekiyou()));
        }
        if( item.getData().getShoujouShouki() != null ){
            // TODO: add shoujou shouki
        }
    }

    void add(NaifukuItem<Naifuku> item){
        DrugTekiyou tekiyou = new DrugTekiyou(item.getTanka(), item.getCount());
        item.getDrugs().forEach(drug -> {
            tekiyou.addDrug(drug.name, drug.amount, drug.unit);
        });
        items.add(tekiyou);
    }

    void add(TonpukuItem<Tonpuku> item){
        DrugTekiyou tekiyou = new DrugTekiyou(item.getTanka(), item.getCount());
        Tonpuku drug = item.getDrug();
        tekiyou.addDrug(drug.name, drug.amount, drug.unit);
        items.add(tekiyou);
    }

    void add(GaiyouItem<Gaiyou> item){
        DrugTekiyou tekiyou = new DrugTekiyou(item.getTanka(), item.getCount());
        Gaiyou drug = item.getData();
        tekiyou.addDrug(drug.name, drug.amount, drug.unit);
        items.add(tekiyou);
        if( Objects.equals(item.getData().unit, "枚") ){
            int timesPerDay = parseGaiyouTimesPerDay(item.getData().usage, 1);
            int sheetsPerTimes = parseGaiyouSheetsPerTimes(item.getData().usage, 1);
            String digits = String.format("%d", timesPerDay * sheetsPerTimes);
            String kanjiDigits = StringUtil.transliterate(digits, StringUtil::digitToKanji);
            String text = String.format("１日%s枚", kanjiDigits);
            TekiyouAux tekiyouAux = new TekiyouAux(text);
            items.add(tekiyouAux);
        }
    }

    private static Pattern gaiyouTaimesPerDayPattern = Pattern.compile(
            "[1１一]日\\s*([0-9０-９]+)回"
    );

    private Integer parseGaiyouTimesPerDay(String usage, Integer defaultValue){
        Matcher matcher = gaiyouTaimesPerDayPattern.matcher(usage);
        if( matcher.find() ){
            String src = StringUtil.transliterate(matcher.group(1), StringUtil::kanjiToDigit);
            try {
                return Integer.parseInt(src);
            } catch(NumberFormatException ex){
                logger.error("Invalid gaiyou times per day: {}", usage);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    private static Pattern gaiyouSheetsPerTimesPattern = Pattern.compile(
            "[1１一]回\\s*([0-9０-９]+)枚"
    );

    private Integer parseGaiyouSheetsPerTimes(String usage, Integer defaultValue){
        Matcher matcher = gaiyouTaimesPerDayPattern.matcher(usage);
        if( matcher.find() ){
            String src = StringUtil.transliterate(matcher.group(1), StringUtil::kanjiToDigit);
            try {
                return Integer.parseInt(src);
            } catch(NumberFormatException ex){
                logger.error("Invalid gaiyou sheets per times: {}", usage);
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    void add(ConductItem<ShinryouItemData, ConductDrug, ConductKizai> item){
        String label = String.join("、", enumerateLabels(item));
        StandardTekiyou tekiyou = new StandardTekiyou(label, item.getTanka(), item.getCount());
        items.add(tekiyou);
    }

    private List<String> enumerateLabels(ConductItem<ShinryouItemData, ConductDrug, ConductKizai> item){
        List<String> labels = new ArrayList<>();
        labels.addAll(item.getShinryouStream().map(s -> s.getData().getName()).collect(Collectors.toList()));
        labels.addAll(item.getDrugStream().map(this::conductDrugLabel).collect(Collectors.toList()));
        labels.addAll(item.getKizaiStream().map(this::kizaiLabel).collect(Collectors.toList()));
        return labels;
    }

    private String conductDrugLabel(ConductDrugItem<ConductDrug> drug){
        ConductDrug d = drug.getData();
        return String.format("%s %s%s", d.name, NumberUtil.formatNumber(d.amount), d.unit);
    }

    private String kizaiLabel(KizaiItem<ConductKizai> item){
        ConductKizai kizai = item.getData();
        return String.format("%s %s%s", kizai.name, NumberUtil.formatNumber(kizai.amount), kizai.unit);
    }

    void output() {
        boolean first = true;
        for (Tekiyou tekiyou : items) {
            tekiyou.output(first ? code : "");
            first = false;
        }
    }

    static void outputAll(SubShuukei shuukei, ShinryouItemList<ShinryouItemData> shinryouList) {
        outputAll("" + shuukei.getCode(), shinryouList.stream());
    }

    static void outputAll(SubShuukeiTouyaku shuukei, Stream<ShinryouItem<ShinryouItemData>> items){
        outputAll("" + shuukei.getCode(), items);
    }

    private static void outputAll(String shuukei, Stream<ShinryouItem<ShinryouItemData>> items){
        TekiyouList tekiyouList = new TekiyouList(shuukei);
        items.forEach(tekiyouList::add);
        tekiyouList.output();
    }

}
