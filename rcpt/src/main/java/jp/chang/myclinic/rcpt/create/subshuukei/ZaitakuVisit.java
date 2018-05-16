package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;

import static jp.chang.myclinic.rcpt.create.subshuukei.SubShuukei.SUB_ZAITAKU;

public class ZaitakuVisit extends VisitBase {

    private ResolvedShinryouMap shinryouMasterMap;
    private RcptItemMap oushinItems = new RcptItemMap();
    private RcptItemMap sonotaItems = new RcptItemMap();

    ZaitakuVisit(ResolvedShinryouMap shinryouMasterMap) {
        super(SUB_ZAITAKU);
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou){
        if( shinryou.getShinryoucode() == shinryouMasterMap.往診 ){
            oushinItems.add(shinryou);
        } else {
            sonotaItems.add(shinryou);
        }
    }

    public void merge(ZaitakuVisit src){
        oushinItems.merge(src.oushinItems);
        sonotaItems.merge(src.sonotaItems);
    }

    void output(){
        outputShuukei("zaitaku.oushin", oushinItems);
        outputShuukei("zaitaku.sonota", sonotaItems, false, false);
        sonotaItems.values().forEach(this::addTekiyou);
        outputTekiyou();
    }

}
