package jp.chang.myclinic.rcpt.create.subshuukei;

import java.util.ArrayList;
import java.util.List;

class VisitBase {

    private SubShuukei subShuukei;
    private List<Tekiyou> tekiyouList = new ArrayList<>();

    VisitBase(SubShuukei subShuukei) {
        this.subShuukei = subShuukei;
    }

    void addTekiyou(String label, int tanka, int count){
        tekiyouList.add(new Tekiyou(label, tanka, count));
    }

    void outputTekiyou(){
        Tekiyou.output("" + subShuukei.getCode(), tekiyouList);
    }

}
