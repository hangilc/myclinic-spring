package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;

import java.util.ArrayList;
import java.util.List;

public class ShoshinVisit extends VisitBase {

    private RcptItemMap items = new RcptItemMap();
    private ResolvedShinryouMap shinryouMasterMap;

    ShoshinVisit(ResolvedShinryouMap shinryouMasterMap) {
        super(SubShuukei.SUB_SHOSHIN);
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou) {
        items.add(shinryou);
    }

    void merge(ShoshinVisit src) {
        items.merge(src.items);
    }

    void output() {
        int kai = 0;
        int ten = 0;
        List<String> kasan = new ArrayList<>();
        for(int shinryoucode: items.keySet()){
            RcptItem item = items.get(shinryoucode);
            if( shinryoucode == shinryouMasterMap.初診 ){
                kai += 1;
            } else {
                addTekiyou(item);
            }
            ten += item.getTen();
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
        }
        if( ten > 0 ){
            System.out.printf("shoshin.kai %d\n", kai);
            System.out.printf("shoshin.ten %d\n", ten);
        }
        kasan.forEach(s -> System.out.printf("shoshinkasan %s\n", s));
        outputTekiyou();
    }

}
