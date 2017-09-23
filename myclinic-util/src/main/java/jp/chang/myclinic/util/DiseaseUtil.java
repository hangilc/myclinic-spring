package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiseaseUtil {

    public static boolean isPrefix(int shuushokugocode){
        return shuushokugocode < 8000;
    }

    public static String getFullName(ByoumeiMasterDTO byoumeiMaster, List<ShuushokugoMasterDTO> adjList){
        List<String> pre = new ArrayList<>();
        List<String> post = new ArrayList<>();
        adjList.forEach(adjMaster -> {
            String name = adjMaster.name;
            if( isPrefix(adjMaster.shuushokugocode) ){
                pre.add(name);
            } else {
                post.add(name);
            }
        });
        String byoumeiName = byoumeiMaster == null ? "" : byoumeiMaster.name;
        return String.join("", pre) + byoumeiName + String.join("", post);

    }

    public static String getFullName(DiseaseFullDTO diseaseFull){
        return getFullName(diseaseFull.master,
                diseaseFull.adjList.stream().map(adj -> adj.master).collect(Collectors.toList()));
    }
}
