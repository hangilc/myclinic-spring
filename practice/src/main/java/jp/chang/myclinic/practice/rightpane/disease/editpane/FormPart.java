package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.lib.dateinput.DateInputForm;
import jp.chang.myclinic.util.DiseaseUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;

class FormPart extends JPanel {
    private NameLabel nameLabel = new NameLabel();
    private DateInputForm startDateInput = new DateInputForm(Gengou.Current);
    private ReasonSelector reasonSelector = new ReasonSelector();

    FormPart(){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        add(new JLabel("名称："));
        add(nameLabel, "growx, wrap");
        add(new JLabel("開始日："));
        add(startDateInput, "wrap");
        add(new JLabel("転帰："));
        add(reasonSelector, "w 50, wrap");
    }

    void setDisease(DiseaseFullDTO disease){
        String label = DiseaseUtil.getFullName(disease);
        nameLabel.setText(label);
        startDateInput.setValue(LocalDate.parse(disease.disease.startDate));
        reasonSelector.setSelectedItem(DiseaseEndReason.fromCode(disease.disease.endReason));
        revalidate();
        repaint();
    }
}
