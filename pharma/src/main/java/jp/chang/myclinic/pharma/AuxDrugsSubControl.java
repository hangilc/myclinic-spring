package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.IyakuhincodeNameDTO;
import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class AuxDrugsSubControl extends JPanel {
    private PatientDTO patient;
    private List<IyakuhincodeNameDTO> iyakuhinList;
    private JPanel navArea;
    private AuxDispRecords dispRecords;

    public AuxDrugsSubControl(PatientDTO patient, List<IyakuhincodeNameDTO> iyakuhinList, AuxDispRecords dispRecords){
        this.patient = patient;
        this.iyakuhinList = iyakuhinList;
        this.dispRecords = dispRecords;
        setLayout(new MigLayout("insets 0", "", ""));
        add(new WrappedText("(" + patient.lastName + patient.firstName + ")", 300), "wrap");
        navArea = new JPanel(new MigLayout("", "", ""));
        add(navArea);
        setNavAreaContent(makeDrugListPane());
        dispRecords.showVisits(Collections.emptyList());
    }

    private void setNavAreaContent(JComponent content){
        navArea.removeAll();
        navArea.add(content);
        navArea.repaint();
        navArea.revalidate();
    }

    private JComponent makeDrugListPane(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 2", "", ""));
        for(IyakuhincodeNameDTO iyakuhin: iyakuhinList){
            WrappedText tt = new WrappedText("・", 300);
            tt.appendLink(iyakuhin.name, () -> {

            });
            panel.add(tt, "wrap");
        }
        return panel;
    }
}
