package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;

public class ShidouVisit extends VisitBase {

    private RcptItemMap items = new RcptItemMap();

    ShidouVisit() {
        super(SubShuukei.SUB_SHIDOU);
    }

    public void add(Shinryou shinryou){
        items.add(shinryou);
    }

    void merge(ShidouVisit src){
        items.merge(src.items);
    }

    void output(){
        int ten = getTotalTen(items);
        if( ten > 0 ){
            System.out.printf("shidou.ten %d\n", ten);
            items.values().forEach(this::addTekiyou);
            outputTekiyou();
        }
    }

}
