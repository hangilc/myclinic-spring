package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class DiseaseEditPane extends JPanel {

    private FormPart formPart;

    public DiseaseEditPane(int width, DiseaseFullDTO disease){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        formPart = new FormPart();
        add(formPart, String.format("w %d, wrap", width));
    }

    public void setDisease(DiseaseFullDTO disease){
        formPart.setDisease(disease);
    }
}
