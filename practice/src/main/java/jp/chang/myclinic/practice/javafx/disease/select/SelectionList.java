package jp.chang.myclinic.practice.javafx.disease.select;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.javafx.parts.searchbox.BasicSearchResultList;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectionList extends BasicSearchResultList<DiseaseFullDTO> {

    private static Logger logger = LoggerFactory.getLogger(SelectionList.class);

    public SelectionList() {
        setConverter(this::converter);
    }

    private String converter(DiseaseFullDTO disease){
        String name = DiseaseUtil.getFullName(disease);
        String reason = DiseaseEndReason.fromCode(disease.disease.endReason).getKanjiRep();
        String startDate = DiseaseUtil.formatDate(disease.disease.startDate);
        String endDate = DiseaseUtil.formatDate(disease.disease.endDate);
        return String.format("[%s] %s (%s - %s)", reason, name, startDate, endDate);
    }

}
