package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Gaiyou;
import jp.chang.myclinic.rcpt.create.Naifuku;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.create.Tonpuku;
import jp.chang.myclinic.rcpt.lib.NaifukuItem;
import jp.chang.myclinic.rcpt.lib.NaifukuItemList;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jp.chang.myclinic.consts.MyclinicConsts.*;

public class TouyakuVisit extends VisitBase {

    private Map<String, ShinryouItemList> shinryouShuukeiMap = new LinkedHashMap<>();
    private NaifukuItemList<Naifuku> naifukuList = new NaifukuItemList<>();
    private List<RcptTonpukuItem> tonpukuList = new ArrayList<>();
    private List<RcptGaiyouItem> gaiyouList = new ArrayList<>();
    private ResolvedShinryouMap shinryouMasterMap;

    TouyakuVisit(ResolvedShinryouMap shinryouMasterMap) {
        this.shinryouMasterMap = shinryouMasterMap;
        List.of(SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI, SHUUKEI_TOUYAKU_GAIYOUCHOUZAI,
                SHUUKEI_TOUYAKU_SHOHOU, SHUUKEI_TOUYAKU_MADOKU, SHUUKEI_TOUYAKU_CHOUKI)
                .forEach(shuukei -> shinryouShuukeiMap.put(shuukei, new ShinryouItemList()));
    }

    public void add(Shinryou shinryou) {
        shinryouShuukeiMap.get(shinryou.getShuukeisaki()).add(createShinryouItem(shinryou));
    }

    public void add(Naifuku drug) {
        NaifukuItem<Naifuku> item = new NaifukuItem<>(drug.usage, drug.days, drug.iyakuhincode,
                drug.yakka * drug.amount, drug);
        naifukuList.add(item);
    }

    public void add(Tonpuku drug) {
        tonpukuList.add(new RcptTonpukuItem(drug));
    }

    public void add(Gaiyou drug) {
        gaiyouList.add(new RcptGaiyouItem(drug));
    }

    void merge(TouyakuVisit src) {
        for (String key : shinryouShuukeiMap.keySet()) {
            ShinryouItemList items = shinryouShuukeiMap.get(key);
            items.merge(src.shinryouShuukeiMap.get(key));
        }
        naifukuList.merge(src.naifukuList);
        RcptTonpukuItem.merge(tonpukuList, src.tonpukuList);
        RcptGaiyouItem.merge(gaiyouList, src.gaiyouList);
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
        outputShohouTekiyou(shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_SHOHOU));
        outputNaifukuTekiyou();
    }

    private void outputShohouShuukei(ShinryouItemList items) {
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

    private void outputShohouTekiyou(ShinryouItemList items) {
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

}
