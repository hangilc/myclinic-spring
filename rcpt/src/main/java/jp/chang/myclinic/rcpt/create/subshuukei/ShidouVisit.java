package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

public class ShidouVisit extends VisitBase {

    private ShinryouItemList items = new ShinryouItemList();

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
            TekiyouList.outputAll(SubShuukei.SUB_SHIDOU, items);
        }
    }

    int getTen(){
        return items.getTen();
    }

}
