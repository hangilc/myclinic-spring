package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Naifuku;
import jp.chang.myclinic.rcpt.lib.NaifukuItem;
import jp.chang.myclinic.rcpt.lib.ShinryouItem;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class TekiyouList {

    private static ResolvedShinryouMap shinryouMasterMap;
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

    static void setShinryouMasterMap(ResolvedShinryouMap shinryouMasterMap) {
        TekiyouList.shinryouMasterMap = shinryouMasterMap;
        setupShinryouAlias();
    }

    private static void setupShinryouAlias() {
        shinryouAliasMap = new HashMap<>();
        shinryouAliasMap.put(shinryouMasterMap.薬剤情報提供, "（薬情）");
        shinryouAliasMap.put(shinryouMasterMap.特定疾患管理, "（特）");
        shinryouAliasMap.put(shinryouMasterMap.特定疾患処方, "（特処）");
        shinryouAliasMap.put(shinryouMasterMap.特定疾患処方管理加算処方せん料, "（特処）");
        shinryouAliasMap.put(shinryouMasterMap.長期処方, "（特処長）");
        shinryouAliasMap.put(shinryouMasterMap.長期投薬加算処方せん料, "（特処長）");
    }

    void add(String name, int tanka, int count) {
        items.add(new StandardTekiyou(name, tanka, count));
    }

    void add(ShinryouItem item) {
        String name;
        if (shinryouAliasMap.containsKey(item.getShinryoucode())) {
            name = shinryouAliasMap.get(item.getShinryoucode());
        } else {
            name = item.getName();
        }
        items.add(new StandardTekiyou(name, item.getTanka(), item.getCount()));
    }

    void add(NaifukuItem<Naifuku> item){
        DrugTekiyou tekiyou = new DrugTekiyou(item.getTanka(), item.getCount());
        item.getDrugs().forEach(drug -> {
            tekiyou.addDrug(drug.name, drug.amount, drug.unit);
        });
        items.add(tekiyou);
    }

    void output() {
        boolean first = true;
        for (Tekiyou tekiyou : items) {
            tekiyou.output(first ? code : "");
            first = false;
        }
    }

    static void outputAll(SubShuukei shuukei, ShinryouItemList shinryouList) {
        outputAll("" + shuukei.getCode(), shinryouList.stream());
    }

    static void outputAll(SubShuukeiTouyaku shuukei, Stream<ShinryouItem> items){
        outputAll("" + shuukei.getCode(), items);
    }

    private static void outputAll(String shuukei, Stream<ShinryouItem> items){
        TekiyouList tekiyouList = new TekiyouList(shuukei);
        items.forEach(tekiyouList::add);
        tekiyouList.output();
    }

}
