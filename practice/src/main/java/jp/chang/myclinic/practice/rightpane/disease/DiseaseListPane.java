package jp.chang.myclinic.practice.rightpane.disease;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;

import javax.swing.*;
import java.util.List;

class DiseaseListPane extends JPanel {

    DiseaseListPane(int width, List<DiseaseFullDTO> diseases){
        setLayout(new FixedWidthLayout(width));
        diseases.forEach(d -> {
            WrappedText text = new WrappedText(width, makeLabel(d));
            add(text);
        });
    }

    private String makeLabel(DiseaseFullDTO diseaseFull){
        return DiseaseUtil.getFullName(diseaseFull) + " (" +
                DateTimeUtil.sqlDateToKanji(diseaseFull.disease.startDate, DateTimeUtil.kanjiFormatter5) + ")";
    }
}
