package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItem;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;
import jp.chang.myclinic.util.DateTimeUtil;

import java.time.LocalDate;

public class ShidouVisit extends VisitBase {
    private ResolvedShinryouMap shinryouMasterMap;
    private ShinryouItemList<ShidouItemData> items = new ShinryouItemList<>();

    ShidouVisit(ResolvedShinryouMap shinryouMasterMap){
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou, LocalDate visitedAt){
        items.add(createShidouItem(shinryou, visitedAt));
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
                    String dateLabel = DateTimeUtil.toKanji(item.getData().getVisitedAt(),
                            DateTimeUtil.kanjiFormatter1);
                    tekiyouList.add(item.getData().getName(),item.getTanka(), item.getCount());
                    //tekiyouList.add("      " + dateLabel, null, null);
                    tekiyouList.add(new TekiyouAux(dateLabel));
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

    private ShinryouItem<ShidouItemData> createShidouItem(Shinryou shinryou, LocalDate visitedAt){
        ShidouItemData data = new ShidouItemData(shinryou.getName(), visitedAt);
        return new ShinryouItem<>(shinryou.getShinryoucode(), shinryou.getTensuu(), data);
    }

}
