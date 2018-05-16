package jp.chang.myclinic.rcpt.create.subshuukei;

import java.util.ArrayList;
import java.util.List;

class VisitBase {

    List<Tekiyou> tekiyouList = new ArrayList<>();

    VisitBase() {

    }

    void addTekiyou(String label, int tanka, int count){
        tekiyouList.add(new Tekiyou(label, tanka, count));
    }

}
