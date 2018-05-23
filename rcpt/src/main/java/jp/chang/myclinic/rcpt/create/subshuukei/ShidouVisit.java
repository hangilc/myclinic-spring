package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;
import jp.chang.myclinic.util.DateTimeUtil;

public class ShidouVisit extends VisitBase {
    private ResolvedShinryouMap shinryouMasterMap;
    private ShinryouItemList items = new ShinryouItemList();

    ShidouVisit(ResolvedShinryouMap shinryouMasterMap){
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou){
        items.add(createShinryouItem(shinryou));
    }

    void merge(ShidouVisit src){
        items.merge(src.items);
    }

    void output(){
        int ten = getTen();
        if( ten > 0 ){
            System.out.printf("shidou.ten %d\n", ten);
            TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_SHIDOU);
            items.stream().forEach(item -> {
                if( item.getShinryoucode() == shinryouMasterMap.診療情報提供料１ ){
                    tekiyouList.add(item.getName(), null, null);
                    tekiyouList.add("" + DateTimeUtil.sqlDateToKanji(item.getDateTimeUtil.kanjiFormatter1));
                } else {
                    tekiyouList.add(item);
                }
            });
            tekiyouList.output();
        }
    }

    int getTen(){
        return items.getTen();
    }

}
