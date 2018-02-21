package jp.chang.myclinic.practice.javafx.disease.end;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.javafx.parts.CheckBoxList;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiseaseList extends CheckBoxList<DiseaseFullDTO> {

    private static Logger logger = LoggerFactory.getLogger(DiseaseList.class);

    public DiseaseList(List<DiseaseFullDTO> diseases) {
        super(diseases, DiseaseList::getFullName);
    }

    private static String getFullName(DiseaseFullDTO disease){
        String name = DiseaseUtil.getFullName(disease);
        String startDate = DateTimeUtil.sqlDateToKanji(disease.disease.startDate, DateTimeUtil.kanjiFormatter5);
        return name + " (" + startDate + ")";
    }

}
