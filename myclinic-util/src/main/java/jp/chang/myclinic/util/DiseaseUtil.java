package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.DiseaseFullDTO;

import java.util.ArrayList;
import java.util.List;

public class DiseaseUtil {

    public static boolean isPrefix(int shuushokugocode){
        return shuushokugocode < 8000;
    }

    public static String getFullName(DiseaseFullDTO diseaseFull){
        List<String> pre = new ArrayList<>();
        List<String> post = new ArrayList<>();
        diseaseFull.adjList.forEach(adjFull -> {
            String name = adjFull.master.name;
            if( isPrefix(adjFull.diseaseAdj.shuushokugocode) ){
                pre.add(name);
            } else {
                post.add(name);
            }
        });
        return String.join("", pre) + diseaseFull.master.name + String.join("", post);
    }
}
