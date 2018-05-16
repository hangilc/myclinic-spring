package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;

import java.util.HashSet;
import java.util.Set;

import static jp.chang.myclinic.consts.MyclinicConsts.*;

public class SaishinVisit extends VisitBase {

    private ResolvedShinryouMap shinryouMasterMap;
    private RcptItemMap saishinMap = new RcptItemMap();
    private RcptItemMap gairaiKanriMap = new RcptItemMap();
    private RcptItemMap jikangaiMap = new RcptItemMap();
    private RcptItemMap kyuujitsuMap = new RcptItemMap();
    private RcptItemMap shinyaMap = new RcptItemMap();

    SaishinVisit(ResolvedShinryouMap shinryouMasterMap) {
        super(SubShuukei.SUB_SAISHIN);
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou){
        switch(shinryou.getShuukeisaki()){
            case SHUUKEI_SAISHIN_SAISHIN:
                saishinMap.add(shinryou); break;
            case SHUUKEI_SAISHIN_GAIRAIKANRI:
                gairaiKanriMap.add(shinryou); break;
            case SHUUKEI_SAISHIN_JIKANGAI:
                jikangaiMap.add(shinryou); break;
            case SHUUKEI_SAISHIN_KYUUJITSU:
                kyuujitsuMap.add(shinryou); break;
            case SHUUKEI_SAISHIN_SHINYA:
                shinyaMap.add(shinryou); break;
            default:
                throw new RuntimeException("Unknown saishin: " + shinryou);
        }
    }

    void merge(SaishinVisit src){
        saishinMap.merge(src.saishinMap);
        gairaiKanriMap.merge(src.gairaiKanriMap);
        jikangaiMap.merge(src.jikangaiMap);
        kyuujitsuMap.merge(src.kyuujitsuMap);
        shinyaMap.merge(src.shinyaMap);
    }

    void output(){
        outputSaishin();
        outputShuukei(gairaiKanriMap, "gairaikanri");
        outputShuukei(jikangaiMap, "jikangai");
        outputShuukei(kyuujitsuMap, "kyuujitsu");
        outputShuukei(shinyaMap, "shinya");
    }

    private void outputSaishin(){
        int kai = 0;
        int ten = 0;
        Set<Integer> tankaSet = new HashSet<>();
        for(int shinryoucode: saishinMap.keySet()){
            RcptItem item = saishinMap.get(shinryoucode);
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
