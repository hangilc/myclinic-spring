package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;

import java.util.EnumSet;

class TekiyouLine {

    String index;
    String body;
    String tankaTimes;
    EnumSet<TekiyouLineOpt> opts = EnumSet.noneOf(TekiyouLineOpt.class);

    TekiyouLine(String body){
        this("", body, "");
    }

    TekiyouLine(String index, String body, String tankaTimes) {
        this.index = index;
        this.body = body;
        this.tankaTimes = tankaTimes;
    }

    TekiyouLine setAlignRight(){
        opts.add(TekiyouLineOpt.AlignRight);
        return this;
    }
}