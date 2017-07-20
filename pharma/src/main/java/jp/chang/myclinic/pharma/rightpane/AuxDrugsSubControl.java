package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.IyakuhincodeNameDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.wrappedtext.Strut;
import jp.chang.myclinic.pharma.wrappedtext.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

class AuxDrugsSubControl extends JPanel {

    interface Callbacks {
        void onShowRecords(List<Integer> visitIds);
    }

    private PatientDTO patient;
    private List<IyakuhincodeNameDTO> iyakuhinList;
    private Callbacks callbacks;
    private JPanel navArea;
    private JComponent drugListPane;
    private AuxDrugNav auxDrugNav;
    private int width;

    AuxDrugsSubControl(PatientDTO patient, List<IyakuhincodeNameDTO> iyakuhinList, Callbacks callbacks){
        this.patient = patient;
        this.iyakuhinList = iyakuhinList;
        this.callbacks = callbacks;
        setLayout(new MigLayout("insets 0, fill", "", ""));
        add(new Strut(w -> {
            this.width = w - 28;
            add(new WrappedText("(" + patient.lastName + patient.firstName + ")", width), "wrap");
            navArea = new JPanel(new MigLayout("", "", ""));
            add(navArea);
            drugListPane = makeDrugListPane();
            setNavAreaContent(drugListPane);
        }), "span, growx");
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
            WrappedText tt = new WrappedText("・", width);
            tt.appendLink(iyakuhin.name, () -> {
                doDrug(iyakuhin.iyakuhincode, iyakuhin.name);
            });
            panel.add(tt, "wrap");
        }
        return panel;
    }

    private void doDrug(int iyakuhincode, String drugName){
        Service.api.listVisitIdVisitedAtByPatientAndIyakuhincode(patient.patientId, iyakuhincode)
                .thenAccept(result -> {
                    List<RecordPage> pages = RecordPage.divideToPages(result);
                    EventQueue.invokeLater(() -> {
                        auxDrugNav = new AuxDrugNav(drugName, width, pages, new AuxDrugNav.Callbacks() {
                            @Override
                            public void onShowRecords(List<Integer> visitIds) {
                                callbacks.onShowRecords(visitIds);
                            }

                            @Override
                            public void onBackToDrugList() {
                                auxDrugNav = null;
                                setNavAreaContent(drugListPane);
                                trigger();
                            }
                        });
                        setNavAreaContent(auxDrugNav);
                        auxDrugNav.trigger();
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    void trigger(){
        if( auxDrugNav != null ){
            auxDrugNav.trigger();
        } else {
            callbacks.onShowRecords(Collections.emptyList());
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}