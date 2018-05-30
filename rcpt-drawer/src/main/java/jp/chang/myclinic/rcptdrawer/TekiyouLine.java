package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;

class TekiyouLine {

    String index;
    String body;
    String tankaTimesTen;
    HAlign halign = HAlign.Left;

    TekiyouLine(String body){
        this("", body, "");
    }

    TekiyouLine(String index, String body, String tankaTimesTen) {
        this.index = index;
        this.body = body;
        this.tankaTimesTen = tankaTimesTen;
    }

    TekiyouLine setHAlign(HAlign halign){
        this.halign = halign;
        return this;
    }
}
