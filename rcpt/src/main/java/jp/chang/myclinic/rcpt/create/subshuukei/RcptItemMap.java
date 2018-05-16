package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;

import java.util.LinkedHashMap;

class RcptItemMap extends LinkedHashMap<Integer, RcptItem> {

    void add(Shinryou shinryou) {
        merge(shinryou.getShinryoucode(),
                RcptItem.of(shinryou),
                (prev, curr) -> {
                    prev.count += 1;
                    return prev;
                });
    }

    void merge(RcptItemMap src) {
        src.forEach((shinryoucode, item) -> {
            merge(shinryoucode, item, (prev, curr) -> {
                prev.count += curr.count;
                return prev;
            });
        });
    }

}
