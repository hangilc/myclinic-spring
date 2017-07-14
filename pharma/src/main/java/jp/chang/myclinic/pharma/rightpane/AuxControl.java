package jp.chang.myclinic.pharma.rightpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class AuxControl extends JPanel {

    interface Callbacks {
        void onShowVisits();
        void onShowDrugs();
    }

    private JRadioButton showVisitsButton;
    private JRadioButton showDrugsButton;

    AuxControl(Callbacks callbacks){
        setLayout(new MigLayout("insets 0", "", ""));
        {
            setLayout(new MigLayout("insets 0", "", ""));
            showVisitsButton = new JRadioButton("日にち順");
            showVisitsButton.addActionListener(event -> callbacks.onShowVisits());
            showDrugsButton = new JRadioButton("薬剤別");
            showDrugsButton.addActionListener(event -> callbacks.onShowDrugs());
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(showVisitsButton);
            buttonGroup.add(showDrugsButton);
            showVisitsButton.setSelected(true);
            add(showVisitsButton);
            add(showDrugsButton);
        }
    }

    void enableButtons(){
        showVisitsButton.setEnabled(true);
        showDrugsButton.setEnabled(true);
    }

    void disableButtons(){
        showVisitsButton.setEnabled(false);
        showDrugsButton.setEnabled(false);
    }

}

/*
class AuxControl extends JPanel {

    interface Callbacks {
        void onShowVisits();
        void onShowDrugs();
    }

    private int width;
    private PatientDTO patient;
    private AuxVisitsSubControl dispVisits;
    private AuxDrugsSubControl dispDrugs;
    private JPanel subControl;
    private AuxDispRecords dispRecords;
    private JRadioButton showVisitsButton = new JRadioButton("日にち順");
    private JRadioButton showDrugsButton = new JRadioButton("薬剤別");

    AuxControl(JPanel subControl, AuxDispRecords dispRecords, int width){
        this.subControl = subControl;
        this.dispRecords = dispRecords;
        setLayout(new MigLayout("insets 0", "[" + width + "!]", ""));
        {
            JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(showVisitsButton);
            buttonGroup.add(showDrugsButton);
            showVisitsButton.setSelected(true);
            panel.add(showVisitsButton);
            panel.add(showDrugsButton);
            add(panel);
        }
        bind();
    }

    public void update(PatientDTO patient){
        this.patient = patient;
        dispVisits = null;
        dispDrugs = null;
        showVisitsButton.setEnabled(false);
        Service.api.listVisitIdVisitedAtForPatient(patient.patientId)
                .thenAccept(visitIds -> {
                    List<RecordPage>  pages = RecordPage.divideToPages(visitIds);
                    EventQueue.invokeLater(() -> {
                        showVisitsButton.setSelected(true);
                        dispVisits = new AuxVisitsSubControl(patient, pages, dispRecords);
                        setSubControlContent(dispVisits);
                        showVisitsButton.setEnabled(true);
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void bind(){
        showVisitsButton.addActionListener(event -> doShowVisits());
        showDrugsButton.addActionListener(event -> doShowDrugs());
    }

    private void doShowVisits() {
        if( patient == null ){
            return;
        }
        setSubControlContent(dispVisits);
        dispVisits.updateVisitsArea();
    }

    private void doShowDrugs() {
        if( patient == null ){
            return;
        }
        if( dispDrugs == null ){
            showDrugsButton.setEnabled(false);
            Service.api.listIyakuhinForPatient(patient.patientId)
                    .thenAccept(result -> {
                        EventQueue.invokeLater(() -> {
                            dispDrugs = new AuxDrugsSubControl(patient, result, dispRecords);
                            setSubControlContent(dispDrugs);
                            showDrugsButton.setEnabled(true);
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        alert(t.toString());
                        return null;
                    });
        } else {
            setSubControlContent(dispDrugs);
        }
    }

    private void setSubControlContent(JComponent content){
        subControl.removeAll();
        subControl.add(content);
        subControl.repaint();
        subControl.revalidate();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}

*/