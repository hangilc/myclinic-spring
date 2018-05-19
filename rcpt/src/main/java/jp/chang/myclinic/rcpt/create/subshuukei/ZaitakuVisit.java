package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import static jp.chang.myclinic.rcpt.create.subshuukei.SubShuukei.SUB_ZAITAKU;

public class ZaitakuVisit extends VisitBase {

    private ResolvedShinryouMap shinryouMasterMap;
    private ShinryouItemList oushinItems = new ShinryouItemList();
    private ShinryouItemList sonotaItems = new ShinryouItemList();

    ZaitakuVisit(ResolvedShinryouMap shinryouMasterMap) {
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou){
        if( shinryou.getShinryoucode() == shinryouMasterMap.往診 ){
            oushinItems.add(createShinryouItem(shinryou));
        } else {
            sonotaItems.add(createShinryouItem(shinryou));
        }
    }

    public void merge(ZaitakuVisit src){
        oushinItems.merge(src.oushinItems);
        sonotaItems.merge(src.sonotaItems);
    }

    void output(){
        outputShuukei("zaitaku.oushin", oushinItems);
        outputShuukei("zaitaku.sonota", sonotaItems, false, false);
        TekiyouList.outputAll(SubShuukei.SUB_ZAITAKU, sonotaItems);
    }

}
