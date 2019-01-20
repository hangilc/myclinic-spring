package jp.chang.myclinic.rcpt.resolvedmap2;

import java.util.List;

public class ResolvedByoumei {

    public static class NameCodePair {
        private String name;
        private int code;

        NameCodePair(String name, int code){
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public int getCode() {
            return code;
        }
    }

    private NameCodePair shoubyoumei;
    private List<NameCodePair> shuushokugoList;

    private ResolvedByoumei(NameCodePair shoubyoumei, List<NameCodePair> shuushokugoList) {
        this.shoubyoumei = shoubyoumei;
        this.shuushokugoList = shuushokugoList;
    }

    public NameCodePair getShoubyoumei() {
        return shoubyoumei;
    }

    public List<NameCodePair> getShuushokugoList() {
        return shuushokugoList;
    }
}
