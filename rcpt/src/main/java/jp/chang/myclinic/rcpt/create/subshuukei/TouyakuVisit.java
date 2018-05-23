package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Gaiyou;
import jp.chang.myclinic.rcpt.create.Naifuku;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.create.Tonpuku;
import jp.chang.myclinic.rcpt.lib.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jp.chang.myclinic.consts.MyclinicConsts.*;

public class TouyakuVisit extends VisitBase {

    private Map<String, ShinryouItemList<ShinryouItemData>> shinryouShuukeiMap = new LinkedHashMap<>();
    private NaifukuItemList<Naifuku> naifukuList = new NaifukuItemList<>();
    private TonpukuItemList<Tonpuku> tonpukuList = new TonpukuItemList<>();
    private GaiyouItemList<Gaiyou> gaiyouList = new GaiyouItemList<>();
    private ResolvedShinryouMap shinryouMasterMap;

    TouyakuVisit(ResolvedShinryouMap shinryouMasterMap) {
        this.shinryouMasterMap = shinryouMasterMap;
        List.of(SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI, SHUUKEI_TOUYAKU_GAIYOUCHOUZAI,
                SHUUKEI_TOUYAKU_SHOHOU, SHUUKEI_TOUYAKU_MADOKU, SHUUKEI_TOUYAKU_CHOUKI)
                .forEach(shuukei -> shinryouShuukeiMap.put(shuukei, new ShinryouItemList<>()));
    }

    public void add(Shinryou shinryou) {
        shinryouShuukeiMap.get(shinryou.getShuukeisaki()).add(createShinryouItem(shinryou));
    }

    public void add(Naifuku drug) {
        NaifukuItem<Naifuku> item = new NaifukuItem<>(drug.usage, drug.days, drug.iyakuhincode,
                drug.yakka * drug.amount, drug);
        naifukuList.extendOrAdd(item);
    }

    public void add(Tonpuku drug) {
        tonpukuList.add(new TonpukuItem<>(drug.iyakuhincode, drug.usage, drug.amount,
                drug.yakka, drug.days, drug));
    }

    public void add(Gaiyou drug) {
        gaiyouList.add(new GaiyouItem<>(drug.iyakuhincode, drug.usage, drug.amount,
                drug.yakka, drug));
    }

    void merge(TouyakuVisit src) {
        for (String key : shinryouShuukeiMap.keySet()) {
            ShinryouItemList<ShinryouItemData> items = shinryouShuukeiMap.get(key);
            items.merge(src.shinryouShuukeiMap.get(key));
        }
        naifukuList.merge(src.naifukuList);
        tonpukuList.merge(src.tonpukuList);
        gaiyouList.merge(src.gaiyouList);
    }

    void output() {
        outputShuukei("touyaku.naifuku.chouzai",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI));
        outputShuukei("touyaku.gaiyou.chouzai",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_GAIYOUCHOUZAI));
        outputShohouShuukei(shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_SHOHOU));
        outputShuukei("touyaku.madoku",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_MADOKU), false, true);
        outputShuukei("touyaku.chouki",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_CHOUKI), false, false);
        outputShuukei("touyaku.naifuku.yakuzai", naifukuList, false, true);
        outputShuukei("touyaku.tonpuku.yakuzai", tonpukuList, false, true);
        outputShuukei("touyaku.gaiyou.yakuzai", gaiyouList, false, true);
        outputShohouTekiyou(shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_SHOHOU));
        outputNaifukuTekiyou();
        outputTonpukuTekiyou();
        outputGaiyouTekiyou();
    }

    private void outputShohouShuukei(ShinryouItemList<ShinryouItemData> items) {
        int ten = items.getTen();
        if (ten > 0) {
            int count = items.stream()
                    .mapToInt(item -> {
                        int shinryoucode = item.getShinryoucode();
                        if (shinryoucode == shinryouMasterMap.処方料 ||
                                shinryoucode == shinryouMasterMap.処方料７) {
                            return item.getCount();
                        } else {
                            return 0;
                        }
                    })
                    .sum();
            outputShuukei("touyaku.shohou",
                    getShuukeiTanka(items).orElse(null),
                    count,
                    ten);
        }
    }

    private void outputShohouTekiyou(ShinryouItemList<ShinryouItemData> items) {
        TekiyouList.outputAll(SubShuukeiTouyaku.TouyakuShohou,
                items.stream().filter(item ->
                        item.getShinryoucode() != shinryouMasterMap.処方料 &&
                                item.getShinryoucode() != shinryouMasterMap.処方料７)
        );
    }

    private void outputNaifukuTekiyou(){
        TekiyouList tekiyouList = new TekiyouList(SubShuukeiTouyaku.TouyakuNaifuku);
        naifukuList.stream().forEach(tekiyouList::add);
        tekiyouList.output();
    }

    private void outputTonpukuTekiyou(){
        TekiyouList tekiyouList = new TekiyouList(SubShuukeiTouyaku.TouyakuTonpuku);
        tonpukuList.stream().forEach(tekiyouList::add);
        tekiyouList.output();
    }

    private void outputGaiyouTekiyou(){
        TekiyouList tekiyouList = new TekiyouList(SubShuukeiTouyaku.TouyakuGaiyou);
        gaiyouList.stream().forEach(tekiyouList::add);
        tekiyouList.output();
    }

    int getTen(){
        return shinryouShuukeiMap.values().stream()
                .mapToInt(ShinryouItemList::getTen).sum() +
                naifukuList.getTen() +
                tonpukuList.getTen() +
                gaiyouList.getTen();
    }

}
