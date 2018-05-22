package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

public class SonotaVisit extends VisitBase {

    private ShinryouItemList shinryouList = new ShinryouItemList();

    public void add(Shinryou shinryou){
        shinryouList.add(createShinryouItem(shinryou));
    }

    void merge(SonotaVisit src){
        shinryouList.merge(src.shinryouList);
    }

    void output(){
        outputShuukei("shochi", null, shinryouList.getTotalCount(), shinryouList.getTen());
        TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_SONOTA);
        shinryouList.stream().forEach(tekiyouList::add);
        tekiyouList.output();
    }

    int getTen(){
        return shinryouList.getTen();
    }

}
