package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;
import jp.chang.myclinic.rcpt.lib.ShinryouItem;
import jp.chang.myclinic.rcpt.lib.ShinryouItemList;

import java.util.ArrayList;
import java.util.List;

public class ShoshinVisit extends VisitBase {

    private ShinryouItemList items = new ShinryouItemList();
    private ResolvedShinryouMap shinryouMasterMap;

    ShoshinVisit(ResolvedShinryouMap shinryouMasterMap) {
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou) {
        ShinryouItem item = new ShinryouItem(shinryou.getShinryoucode(), shinryou.getName(),
                shinryou.getTensuu());
        items.add(item);
    }

    void merge(ShoshinVisit src) {
        items.merge(src.items);
    }

    void output() {
        List<String> kasan = new ArrayList<>();
        class Local {
            private int kai = 0;
        }
        Local local = new Local();
        TekiyouList tekiyouList = new TekiyouList(SubShuukei.SUB_SHOSHIN);
        items.forEach(item -> {
            int shinryoucode = item.getShinryoucode();
            if( shinryoucode == shinryouMasterMap.初診 ){
                local.kai += 1;
            } else {
                tekiyouList.add(item);
            }
            if( shinryoucode == shinryouMasterMap.初診時間外加算 ||
                    shinryoucode == shinryouMasterMap.初診乳幼児時間外加算 ){
                kasan.add("jikangai");
            }
            if( shinryoucode == shinryouMasterMap.初診休日加算 ||
                    shinryoucode == shinryouMasterMap.初診乳幼児休日加算 ){
                kasan.add("kyuujitsu");
            }
            if( shinryoucode == shinryouMasterMap.初診深夜加算 ||
                    shinryoucode == shinryouMasterMap.初診乳幼児深夜加算 ){
                kasan.add("shinya");
            }
        });
        int ten = getTen();
        if( ten > 0 ){
            outputShuukei("shoshin", null, local.kai, ten);
        }
        kasan.forEach(s -> System.out.printf("shoshinkasan %s\n", s));
        tekiyouList.output();
    }

    int getTen(){
        return items.getTen();
    }

}
