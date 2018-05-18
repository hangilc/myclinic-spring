package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItem;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.HashSet;
import java.util.Set;

import static jp.chang.myclinic.consts.MyclinicConsts.*;

public class SaishinVisit extends VisitBase {

    private ResolvedShinryouMap shinryouMasterMap;
    private ShinryouItemList saishinList = new ShinryouItemList();
    private ShinryouItemList gairaiKanriList = new ShinryouItemList();
    private ShinryouItemList jikangaiList = new ShinryouItemList();
    private ShinryouItemList kyuujitsuList = new ShinryouItemList();
    private ShinryouItemList shinyaList = new ShinryouItemList();

    SaishinVisit(ResolvedShinryouMap shinryouMasterMap) {
        super(SubShuukei.SUB_SAISHIN);
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou){
        ShinryouItem item = createShinryouItem(shinryou);
        switch(shinryou.getShuukeisaki()){
            case SHUUKEI_SAISHIN_SAISHIN:
                saishinList.add(item); break;
            case SHUUKEI_SAISHIN_GAIRAIKANRI:
                gairaiKanriList.add(item); break;
            case SHUUKEI_SAISHIN_JIKANGAI:
                jikangaiList.add(item); break;
            case SHUUKEI_SAISHIN_KYUUJITSU:
                kyuujitsuList.add(item); break;
            case SHUUKEI_SAISHIN_SHINYA:
                shinyaList.add(item); break;
            default:
                throw new RuntimeException("Unknown saishin: " + shinryou);
        }
    }

    void merge(SaishinVisit src){
        saishinList.merge(src.saishinList);
        gairaiKanriList.merge(src.gairaiKanriList);
        jikangaiList.merge(src.jikangaiList);
        kyuujitsuList.merge(src.kyuujitsuList);
        shinyaList.merge(src.shinyaList);
    }

    void output(){
        outputSaishin();
        outputShuukei(gairaiKanriList, "gairaikanri");
        outputShuukei(jikangaiList, "jikangai");
        outputShuukei(kyuujitsuList, "kyuujitsu");
        outputShuukei(shinyaList, "shinya");
    }

    private void outputSaishin(){
        int kai = 0;
        int ten = 0;
        Set<Integer> tankaSet = new HashSet<>();
        for(int shinryoucode: saishinList.keySet()){
            RcptItem item = saishinList.get(shinryoucode);
            if( shinryoucode == shinryouMasterMap.再診 ) {
                kai += 1;
            } else {
                if( shinryoucode == shinryouMasterMap.同日再診 ){
                    kai += 1;
                }
                addTekiyou(item);
            }
            tankaSet.add(item.tanka);
            ten += item.getTen();
        }
        if( tankaSet.size() == 1 ){
            System.out.printf("saishin.saishin.tanka %d\n", tankaSet.iterator().next());
        }
        if( ten > 0 ){
            System.out.printf("saishin.saishin.kai %d\n", kai);
            System.out.printf("saishin.saishin.ten %d\n", ten);
        }
        outputTekiyou();
    }

    private void outputShuukei(RcptItemMap items, String label){
        if( !items.isEmpty() ){
            getShuukeiTanka(items)
                    .ifPresent(tanka -> System.out.printf("saishin.%s.tanka %d\n", label, tanka));
            System.out.printf("saishin.%s.kai %d\n", label, getTotalCount(items));
            System.out.printf("saishin.%s.ten %d\n", label, getTotalTen(items));
        }
    }

}
