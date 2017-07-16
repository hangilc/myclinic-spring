package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.VisitIdVisitedAtDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class RecordPage {
    private List<Integer> visitIds;
    private String tooltip;

    private RecordPage(List<VisitIdVisitedAtDTO> visits){
        visitIds = visits.stream().map(arg -> arg.visitId).collect(Collectors.toList());
        if( visits.size() == 0 ){
            tooltip = "";
        } else {
            tooltip = DateTimeUtil.toKanji(DateTimeUtil.parseSqlDateTime(visits.get(0).visitedAt).toLocalDate());
        }
    }

    List<Integer> getVisitIds(){
        return visitIds;
    }

    static List<RecordPage> divideToPages(List<VisitIdVisitedAtDTO> visits){
        List<RecordPage> pages = new ArrayList<>();
        int totalItems = visits.size();
        int itemsPerPage = 10;
        for(int i=0;i<totalItems;){
            int j = i + itemsPerPage;
            if( j > totalItems ){
                j = totalItems;
            }
            List<VisitIdVisitedAtDTO> subs = visits.subList(i, j);
            pages.add(new RecordPage(subs));
            i = j;
        }
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
