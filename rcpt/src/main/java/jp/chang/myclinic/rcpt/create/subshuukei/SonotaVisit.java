package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.create.lib.ShinryouItemList;

public class SonotaVisit extends VisitBase {

    private ShinryouItemList<ShinryouItemData> shohousenList = new ShinryouItemList<>();
    private ShinryouItemList<ShinryouItemData> shinryouList = new ShinryouItemList<>();
    private ResolvedShinryouMap shinryouMasterMap;

    public SonotaVisit(ResolvedShinryouMap shinryouMasterMap){
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou){
        int shinryoucode = shinryou.getShinryoucode();
        if( shinryoucode == shinryouMasterMap.処方せん料 ||
                shinryoucode == shinryouMasterMap.処方せん料７ ){
            shohousenList.add(createShinryouItem(shinryou));
        } else {
            shinryouList.add(createShinryouItem(shinryou));
        }
    }

    void merge(SonotaVisit src){
        shohousenList.merge(src.shohousenList);
        shinryouList.merge(src.shinryouList);
    }

    void output() {
        int ten = getTen();
        if (ten > 0) {
            int count = shohousenList.getTotalCount();
            outputShuukei("sonota.shohousen", null,
                    count > 0 ? count : null,
                    ten);
            TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_SONOTA);
            shinryouList.stream().forEach(tekiyouList::add);
            tekiyouList.output();
        }
    }

    int getTen(){
        return shohousenList.getTen() + shinryouList.getTen();
    }

}
