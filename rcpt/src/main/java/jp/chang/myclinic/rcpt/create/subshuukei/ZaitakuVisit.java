package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.create.lib.ShinryouItem;
import jp.chang.myclinic.rcpt.create.lib.ShinryouItemList;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;

public class ZaitakuVisit extends VisitBase {

    private ResolvedShinryouMap shinryouMasterMap;
    private ShinryouItemList<ZaitakuItemData> oushinItems = new ShinryouItemList<>();
    private ShinryouItemList<ZaitakuItemData> sonotaItems = new ShinryouItemList<>();

    ZaitakuVisit(ResolvedShinryouMap shinryouMasterMap) {
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou, LocalDate visitedAt){
        if( shinryou.getShinryoucode() == shinryouMasterMap.往診 ){
            oushinItems.add(createZaitakuItem(shinryou, visitedAt));
        } else {
            sonotaItems.add(createZaitakuItem(shinryou, visitedAt));
        }
    }

    public void merge(ZaitakuVisit src){
        oushinItems.merge(src.oushinItems);
        sonotaItems.merge(src.sonotaItems);
    }

    void output(){
        outputShuukei("zaitaku.oushin", oushinItems);
        outputShuukei("zaitaku.sonota", sonotaItems, false, false);
        //TekiyouList.outputAll(SubShuukei.SUB_ZAITAKU, sonotaItems);
        if( sonotaItems.getTen() > 0 ){
            TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_ZAITAKU);
            sonotaItems.stream().forEach(item -> {
                if( item.getShinryoucode() == shinryouMasterMap.訪問看護指示料 ){
                    String dateLabel = DateTimeUtil.toKanji(item.getData().getVisitedAt(),
                            DateTimeUtil.kanjiFormatter1);
                    tekiyouList.add(item.getData().getName(),item.getTanka(), item.getCount());
                    tekiyouList.add(new TekiyouAux(dateLabel));
                } else {
                    tekiyouList.add(item);
                }
            });
            tekiyouList.output();
        }
    }

    int getTen(){
        return oushinItems.getTen() + sonotaItems.getTen();
    }

    private ShinryouItem<ZaitakuItemData> createZaitakuItem(Shinryou shinryou, LocalDate visitedAt){
        ZaitakuItemData data = new ZaitakuItemData(shinryou, visitedAt);
        return new ShinryouItem<>(shinryou.getShinryoucode(), shinryou.getTensuu(), shinryou.getTekiyou(), data);
    }

}
