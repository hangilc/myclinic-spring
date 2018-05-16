package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;

class RcptItem {
    String name;
    int tanka;
    int count;

    RcptItem(String name, int tanka, int count) {
        this.name = name;
        this.tanka = tanka;
        this.count = count;
    }

    int getTen(){
        return tanka * count;
    }

    static RcptItem of(Shinryou shinryou){
        return new RcptItem(shinryou.getName(), shinryou.getTensuu(), 1);
    }
}
