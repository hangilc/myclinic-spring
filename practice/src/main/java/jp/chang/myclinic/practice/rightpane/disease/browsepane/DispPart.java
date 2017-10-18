package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DispPart extends JPanel {

    private NameLabel nameLabel = new NameLabel();
    private JLabel startDateLabel = new JLabel();
    private JLabel endReasonLabel = new JLabel();
    private JLabel endDateLabel = new JLabel();
    private DiseaseFullDTO currentDisease;

    DispPart(int width){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        add(new JLabel("名称："));
        add(nameLabel, "growx, wrap");
        add(new JLabel("開始日："));
        add(startDateLabel, "wrap");
        add(new JLabel("転帰："));
        add(endReasonLabel, "wrap");
        add(new JLabel("終了日："));
        add(endDateLabel);
    }

    void setData(DiseaseFullDTO disease){
        currentDisease = disease;
        startDateLabel.setText(DateTimeUtil.sqlDateToKanji(disease.disease.startDate, DateTimeUtil.kanjiFormatter1));
        nameLabel.setText(DiseaseUtil.getFullName(disease));
        endReasonLabel.setText(DiseaseEndReason.fromCode(disease.disease.endReason).getKanjiRep());
        String endDate = disease.disease.endDate;
        if( "0000-00-00".equals(endDate) || endDate == null ){
            endDateLabel.setText("");
        } else {
            endDateLabel.setText(DateTimeUtil.sqlDateToKanji(endDate, DateTimeUtil.kanjiFormatter1));
        }
    }

    DiseaseFullDTO getData(){
        return currentDisease;
    }
}
