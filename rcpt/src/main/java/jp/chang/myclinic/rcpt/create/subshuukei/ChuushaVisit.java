package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Conduct;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

public class ChuushaVisit extends VisitBase {

    private ShinryouItemList shinryouList = new ShinryouItemList();


    ChuushaVisit() {

    }

    public void add(Shinryou shinryou){
        shinryouList.add(createShinryouItem(shinryou));
    }

    public void add(Conduct conduct){

    }

    public void merge(ChuushaVisit src) {

    }

    public void output() {

    }
}
