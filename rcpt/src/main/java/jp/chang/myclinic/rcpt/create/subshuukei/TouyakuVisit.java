package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Gaiyou;
import jp.chang.myclinic.rcpt.create.Naifuku;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.create.Tonpuku;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jp.chang.myclinic.consts.MyclinicConsts.*;
import static jp.chang.myclinic.rcpt.create.subshuukei.SubShuukei.SUB_TOUYAKU;

public class TouyakuVisit extends VisitBase {

    private Map<String, ShinryouItemList> shinryouShuukeiMap = new LinkedHashMap<>();
    private List<RcptNaifukuItem> naifukuList = new ArrayList<>();
    private List<RcptTonpukuItem> tonpukuList = new ArrayList<>();
    private List<RcptGaiyouItem> gaiyouList = new ArrayList<>();
    private ResolvedShinryouMap shinryouMasterMap;

    TouyakuVisit(ResolvedShinryouMap shinryouMasterMap) {
        super(SUB_TOUYAKU);
        this.shinryouMasterMap = shinryouMasterMap;
        List.of(SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI, SHUUKEI_TOUYAKU_GAIYOUCHOUZAI,
                SHUUKEI_TOUYAKU_SHOHOU, SHUUKEI_TOUYAKU_MADOKU, SHUUKEI_TOUYAKU_CHOUKI)
                .forEach(shuukei -> shinryouShuukeiMap.put(shuukei, new ShinryouItemList()));
    }

    public void add(Shinryou shinryou){
        shinryouShuukeiMap.get(shinryou.getShuukeisaki()).add(createShinryouItem(shinryou));
    }

    public void add(Naifuku drug){
        for(RcptNaifukuItem item: naifukuList){
            if( item.canExtend(drug) ){
                item.extend(drug);
                return;
            }
        }
        naifukuList.add(new RcptNaifukuItem(drug));
    }

    public void add(Tonpuku drug){
        tonpukuList.add(new RcptTonpukuItem(drug));
    }

    public void add(Gaiyou drug){
        gaiyouList.add(new RcptGaiyouItem(drug));
    }

    void merge(TouyakuVisit src){
        for(String key: shinryouShuukeiMap.keySet()){
            RcptItemMap items = shinryouShuukeiMap.get(key);
            items.merge(src.shinryouShuukeiMap.get(key));
        }
        RcptNaifukuItem.merge(naifukuList, src.naifukuList);
        RcptTonpukuItem.merge(tonpukuList, src.tonpukuList);
        RcptGaiyouItem.merge(gaiyouList, src.gaiyouList);
    }

    void output(){
        outputShuukei("touyaku.naifuku.chouzai",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI));
        outputShuukei("touyaku.gaiyou.chouzai",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_GAIYOUCHOUZAI));
        outputShohouShuukei(shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_SHOHOU));
        outputShuukei("touyaku.madoku",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_MADOKU), false, true);
        outputShuukei("touyaku.chouki",
                shinryouShuukeiMap.get(SHUUKEI_TOUYAKU_CHOUKI), false, true);
    }

    private void outputShohouShuukei(ShinryouItemList items){
        int ten = items.getTen();
        if( ten > 0 ){
            int count = items.stream()
                    .mapToInt(item -> {
                        int shinryoucode = item.getShinryoucode();
                        if( shinryoucode == shinryouMasterMap.処方料 ||
                                shinryoucode == shinryouMasterMap.処方料７ ){
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

}
