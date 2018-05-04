package jp.chang.myclinic.mastermap;

import java.util.Collections;
import java.util.List;

public class ShinryouByoumei {

    public String byoumei;
    public List<String> shuushokugoList;

    ShinryouByoumei(String byoumei, List<String> shuushokugoList) {
        this.byoumei = byoumei;
        this.shuushokugoList = shuushokugoList;
    }

    ShinryouByoumei(String byoumei){
        this(byoumei, Collections.emptyList());
    }

}
