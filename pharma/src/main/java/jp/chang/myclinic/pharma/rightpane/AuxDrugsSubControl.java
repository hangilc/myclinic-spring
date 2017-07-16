package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.IyakuhincodeNameDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.wrappedtext.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class AuxDrugsSubControl extends JPanel {
    private PatientDTO patient;
    private List<IyakuhincodeNameDTO> iyakuhinList;
    private JPanel navArea;
    private AuxDispRecords dispRecords;
    private JComponent drugListPane;

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
                doDrug(iyakuhin.iyakuhincode, iyakuhin.name);
            });
            panel.add(tt, "wrap");
        }
        drugListPane = panel;
        return panel;
    }

    private void doDrug(int iyakuhincode, String drugName){
        if( patient == null ){
            return;
        }
        Service.api.listVisitIdVisitedAtByPatientAndIyakuhincode(patient.patientId, iyakuhincode)
                .thenAccept(result -> {
                    List<RecordPage> pages = RecordPage.divideToPages(result);
                    EventQueue.invokeLater(() -> {
                        JComponent nav = makeDrugNav(pages, drugName);
                        setNavAreaContent(nav);
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private JComponent makeDrugNav(List<RecordPage> pages, String name){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        WrappedText drugName = new WrappedText(name, 260);
        panel.add(drugName, "wrap");
        AuxRecordsNav recNav = new AuxRecordsNav(pages, new AuxRecordsNav.Callbacks(){
            @Override
            public void onPageSelected(RecordPage page) {

            }
        });
        panel.add(recNav, "wrap");
        JButton backToListButton = new JButton("薬剤一覧にもどえる");
        backToListButton.addActionListener((event -> {
            setNavAreaContent(drugListPane);
            dispRecords.clear();
        }));
        panel.add(backToListButton);
        //recNav.updateVisits();
        return panel;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
