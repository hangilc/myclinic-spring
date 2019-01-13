package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.create.lib.ShinryouItem;
import jp.chang.myclinic.rcpt.create.lib.ShinryouItemList;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDate;

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
                    String dateLabel = KanjiDate.toKanji(item.getData().getVisitedAt());
                    tekiyouList.add(item.getData().getName(),item.getTanka(), item.getCount());
                    tekiyouList.add(new TekiyouAux(dateLabel));
                    String itemTekiyou = item.getData().getTekiyou();
                    if( itemTekiyou != null && !itemTekiyou.isEmpty() ){
                        tekiyouList.add(new TekiyouAux(itemTekiyou));
                    }
                } else if ( item.getShinryoucode() == shinryouMasterMap.療養費同意書交付料 ){
                    String dateLabel = KanjiDate.toKanji(item.getData().getVisitedAt());
                    tekiyouList.add(item.getData().getName(),item.getTanka(), item.getCount());
                    String itemTekiyou = item.getData().getTekiyou();
                    String aux;
                    if( itemTekiyou != null && !itemTekiyou.isEmpty() ){
                        aux = String.format("%s。%s。", dateLabel, itemTekiyou);
                    } else {
                        System.err.println("療養費同意書交付料に病名がありません。");
                        aux = dateLabel;
                    }
                    tekiyouList.add(new TekiyouAux(aux));
                } else {
                    tekiyouList.add(item);
                    String itemTekiyou = item.getData().getTekiyou();
                    if( itemTekiyou != null && !itemTekiyou.isEmpty() ){
                        tekiyouList.add(new TekiyouAux(itemTekiyou));
                    }
                }
            });
            tekiyouList.output();
        }
    }

    int getTen(){
        return items.getTen();
    }

    private ShinryouItem<ShidouItemData> createShidouItem(Shinryou shinryou, LocalDate visitedAt){
        ShidouItemData data = new ShidouItemData(shinryou, visitedAt);
        return new ShinryouItem<>(shinryou.getShinryoucode(), shinryou.getTensuu(), data.getTekiyou(), data);
    }

}
