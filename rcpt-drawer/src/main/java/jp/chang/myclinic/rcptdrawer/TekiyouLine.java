package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;

class TekiyouLine {

    String index;
    String body;
    String tankaTimes;
    HAlign halign = HAlign.Left;

    TekiyouLine(String body){
        this("", body, "");
    }

    TekiyouLine(String index, String body, String tankaTimes) {
        this.index = index;
        this.body = body;
        this.tankaTimes = tankaTimes;
    }

    TekiyouLine setHAlign(HAlign halign){
        this.halign = halign;
        return this;
    }
}
