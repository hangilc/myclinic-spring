package jp.chang.myclinic.rcpt.resolvedmap;

import jp.chang.myclinic.mastermap.next.ByoumeiByName;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public static ResolvedByoumei fromByoumeiByName(ByoumeiByName byoumeiByName,
                                                    Resolver shoubyoumeiResolver,
                                                    Resolver shuushokugoResolver,
                                                    LocalDate at){
        String shoubyoumeiName = byoumeiByName.getShoubyoumei();
        NameCodePair shoubyoumei = new NameCodePair(shoubyoumeiName, shoubyoumeiResolver.resolve(shoubyoumeiName, at));
        List<NameCodePair> shuushokugoList = byoumeiByName.getShuushokugoList().stream()
                .map(s -> new NameCodePair(s, shuushokugoResolver.resolve(s, at)))
                .collect(Collectors.toList());
        return new ResolvedByoumei(shoubyoumei, shuushokugoList);
    }

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
