package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.VisitIdVisitedAtDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecordPage {
    public List<Integer> visitIds;
    public String tooltip;

    public RecordPage(List<VisitIdVisitedAtDTO> visits){
        visitIds = visits.stream().map(arg -> arg.visitId).collect(Collectors.toList());
        if( visits.size() == 0 ){
            tooltip = "";
        } else {
            tooltip = DateTimeUtil.sqlDateTimeToKanji(visits.get(0).visitedAt);
        }
    }

    public static List<RecordPage> divideToPages(List<VisitIdVisitedAtDTO> visits){
        List<RecordPage> pages = new ArrayList<>();
        int totalItems = visits.size();
        int itemsPerPage = 10;
        for(int i=0;i<totalItems;){
            int j = i + itemsPerPage;
            if( j > totalItems ){
                j = totalItems;
            }
            List<VisitIdVisitedAtDTO> subs = visits.subList(i, j);
            System.out.println("subs: " + subs);
            pages.add(new RecordPage(subs));
            System.out.println("pages: " + pages);
            i = j;
        }
        System.out.println("pages: " + pages);
        return pages;
    }

    @Override
    public String toString() {
        return "RecordPage{" +
                "visitIds=" + visitIds +
                ", tooltip='" + tooltip + '\'' +
                '}';
    }
}
