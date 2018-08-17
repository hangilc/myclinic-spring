package jp.chang.myclinic.mastermap.next;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ByoumeiByName {

    private String shoubyoumei;
    private List<String> shuushokugoList;

    ByoumeiByName(String shoubyoumei, List<String> shuushokugoList) {
        this.shoubyoumei = shoubyoumei;
        this.shuushokugoList = shuushokugoList;
    }

    ByoumeiByName(String shoubyoumei){
        this(shoubyoumei, new ArrayList<>());
    }

    ByoumeiByName(List<String> names){
        this(names.get(0), names.subList(1, names.size()));
    }

    public String getShoubyoumei(){
        return shoubyoumei;
    }

    public List<String> getShuushokugoList(){
        return Collections.unmodifiableList(shuushokugoList);
    }

    @Override
    public String toString() {
        return "ByoumeiByName{" +
                "shoubyoumei='" + shoubyoumei + '\'' +
                ", shuushokugoList=" + shuushokugoList +
                '}';
    }
}
