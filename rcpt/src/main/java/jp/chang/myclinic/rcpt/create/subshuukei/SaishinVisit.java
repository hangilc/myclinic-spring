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
        outputShuukei("saishin.gairaikanri", gairaiKanriList);
        outputShuukei("saishin.jikangai", jikangaiList);
        outputShuukei("saishin.kyuujitsu", kyuujitsuList);
        outputShuukei("saishin.shinya", shinyaList);
    }

    private void outputSaishin(){
        class Local {
            private int kai = 0;
        }
        Local local = new Local();
        TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_SAISHIN);
        Set<Integer> tankaSet = new HashSet<>();
        saishinList.forEach(item -> {
            int shinryoucode = item.getShinryoucode();
            if( shinryoucode == shinryouMasterMap.再診 ) {
                local.kai += item.getCount();
            } else {
                if( shinryoucode == shinryouMasterMap.同日再診 ){
                    local.kai += item.getCount();
                }
                tekiyouList.add(item);
            }
            tankaSet.add(item.getTensuu());
        });
        int ten = saishinList.getTen();
        if( ten > 0 ){
            Integer tanka = null;
            if( tankaSet.size() == 1 ){
                tanka = tankaSet.iterator().next();
            }
            outputShuukei("saishin.saishin", tanka, local.kai, ten);
        }
        tekiyouList.output();
    }

    int getTen(){
        return saishinList.getTen() + gairaiKanriList.getTen() +
                jikangaiList.getTen() + kyuujitsuList.getTen() +
                shinyaList.getTen();
    }

}
