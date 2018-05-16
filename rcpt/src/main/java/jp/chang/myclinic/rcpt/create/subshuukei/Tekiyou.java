package jp.chang.myclinic.rcpt.create.subshuukei;

import java.util.List;

class Tekiyou {

    String label;
    int tanka;
    int count;

    Tekiyou(String label, int tanka, int count) {
        this.label = label;
        this.tanka = tanka;
        this.count = count;
    }

    static void output(String subsection, List<Tekiyou> tekiyouList){
        boolean first = true;
        for(Tekiyou tekiyou: tekiyouList){
            String line = String.format("tekiyou %s:%s:%d:%d",
                    first ? subsection : "",
                    tekiyou.label, tekiyou.tanka, tekiyou.count);
            System.out.println(line);
            first = false;
        }
    }

}
