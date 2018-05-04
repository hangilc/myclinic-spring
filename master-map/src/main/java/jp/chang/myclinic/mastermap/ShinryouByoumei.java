package jp.chang.myclinic.mastermap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static List<String> extractByoumei(List<ShinryouByoumei> src){
        return src.stream().map(sb -> sb.byoumei).collect(Collectors.toList());
    }

    public static Optional<ShinryouByoumei> getDefault(List<ShinryouByoumei> src){
        return src.stream().findFirst();
    }

}
