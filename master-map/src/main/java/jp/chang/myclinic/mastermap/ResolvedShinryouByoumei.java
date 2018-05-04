package jp.chang.myclinic.mastermap;

import java.util.List;

public class ResolvedShinryouByoumei {

    public static class NameCodePair {
        public String name;
        public int code;

        NameCodePair(String name, int code){
            this.name = name;
            this.code = code;
        }
    }

    public NameCodePair byoumei;
    public List<NameCodePair> shuushokugoList;

    ResolvedShinryouByoumei(){

    }

    ResolvedShinryouByoumei(NameCodePair byoumei, List<NameCodePair> shuushokugoList) {
        this.byoumei = byoumei;
        this.shuushokugoList = shuushokugoList;
    }

}
