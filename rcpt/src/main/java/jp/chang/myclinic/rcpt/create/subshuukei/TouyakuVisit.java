package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Gaiyou;
import jp.chang.myclinic.rcpt.create.Naifuku;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.create.Tonpuku;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static jp.chang.myclinic.consts.MyclinicConsts.*;
import static jp.chang.myclinic.rcpt.create.subshuukei.SubShuukei.SUB_TOUYAKU;

public class TouyakuVisit extends VisitBase {

    private Map<String, RcptItemMap> shinryouShuukeiMap = new LinkedHashMap<>();
    private List<RcptNaifukuItem> naifukuList = new ArrayList<>();
    private List<RcptTonpukuItem> tonpukuList = new ArrayList<>();
    private List<RcptGaiyouItem> gaiyouList = new ArrayList<>();

    TouyakuVisit() {
        super(SUB_TOUYAKU);
        List.of(SHUUKEI_TOUYAKU_NAIFUKUTONPUKUCHOUZAI, SHUUKEI_TOUYAKU_GAIYOUCHOUZAI,
                SHUUKEI_TOUYAKU_SHOHOU, SHUUKEI_TOUYAKU_MADOKU, SHUUKEI_TOUYAKU_CHOUKI)
                .forEach(shuukei -> shinryouShuukeiMap.put(shuukei, new RcptItemMap()));
    }

    public void add(Shinryou shinryou){
        shinryouShuukeiMap.get(shinryou.getShuukeisaki()).add(shinryou);
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

    }


}
