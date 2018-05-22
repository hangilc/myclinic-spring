package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

public class ShochiVisit extends VisitBase {

    private ShinryouItemList shinryouList = new ShinryouItemList();

    public void add(Shinryou shinryou){
        shinryouList.add(createShinryouItem(shinryou));
    }

    public void merge(ShochiVisit src){
        shinryouList.merge(src.shinryouList);
    }

    public void output(){
        outputShuukei("shochi", null, shinryouList.getTotalCount(), shinryouList.getTen());
        TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_SHOCHI);
        shinryouList.stream().forEach(tekiyouList::add);
        tekiyouList.output();
    }

}
