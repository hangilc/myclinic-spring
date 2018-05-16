package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;
import jp.chang.myclinic.rcpt.create.Shinryou;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoshinVisit extends VisitBase {

    private static class ShoshinItem {
        String name;
        int tanka;
        int count;

        ShoshinItem(String name, int tanka, int count) {
            this.name = name;
            this.tanka = tanka;
            this.count = count;
        }
    }

    private Map<Integer, ShoshinItem> items = new LinkedHashMap<>();
    private ResolvedShinryouMap shinryouMasterMap;

    ShoshinVisit(ResolvedShinryouMap shinryouMasterMap) {
        super(SubShuukei.SUB_SHOSHIN);
        this.shinryouMasterMap = shinryouMasterMap;
    }

    public void add(Shinryou shinryou) {
        items.merge(shinryou.getShinryoucode(),
                new ShoshinItem(shinryou.getName(), shinryou.getTensuu(), 1),
                (prev, curr) -> {
                    prev.count += 1;
                    return prev;
                });
    }

    void merge(ShoshinVisit src) {
        src.items.forEach((shinryoucode, item) -> {
            items.merge(shinryoucode, item, (prev, curr) -> {
                prev.count += curr.count;
                return prev;
            });
        });
    }

    void output() {
        int kai = 0;
        int ten = 0;
        List<String> kasan = new ArrayList<>();
        for(int shinryoucode: items.keySet()){
            ShoshinItem item = items.get(shinryoucode);
            if( shinryoucode == shinryouMasterMap.初診 ){
                kai += 1;
            } else {
                addTekiyou(item.name, item.tanka, item.count);
            }
            ten += item.tanka * item.count;
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
