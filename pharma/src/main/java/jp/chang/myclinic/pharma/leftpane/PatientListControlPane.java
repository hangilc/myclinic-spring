package jp.chang.myclinic.pharma.leftpane;

import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class PatientListControlPane extends JPanel {

    interface Callbacks {
        default void onUpdatePharmaQueue(List<PharmaQueueFullDTO> list){ }
        default void onStartPresc(){ }
    }

    private Icon waitCashierIcon;
    private Icon waitDrugIcon;
    private JCheckBox includePrescribedCheckBox = new JCheckBox("処方済の患者も含める");
    private JButton updatePatientListButton = new JButton("更新");
    private JButton startPrescButton = new JButton("調剤開始");
    private Callbacks callbacks;

    PatientListControlPane(Icon waitCashierIcon, Icon waitDrugIcon, Callbacks callbacks){
        this.waitCashierIcon = waitCashierIcon;
        this.waitDrugIcon = waitDrugIcon;
        this.callbacks = callbacks;
        setLayout(new MigLayout("insets 0, gapy 0", "", ""));
        add(makePatientListSubRow1(), "wrap");
        add(makePatientListSubRow2(), "wrap");
        add(makePatientListSubRow3(), "");
        updatePatientListButton.addActionListener(event -> doUpdatePatientList());
        startPrescButton.addActionListener(event -> doStartPresc());
    }

    PatientListControlPane(Icon waitCashierIcon, Icon waitDrugIcon) {
        this(waitCashierIcon, waitDrugIcon, new Callbacks(){});
    }

        private JComponent makePatientListSubRow1(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JLabel waitCashierLabel = new JLabel("会計待ち");
        waitCashierLabel.setIcon(waitCashierIcon);
        JLabel waitPackLabel = new JLabel("薬渡待ち");
        waitPackLabel.setIcon(waitDrugIcon);
        panel.add(waitCashierLabel);
        panel.add(waitPackLabel);
        return panel;
    }

    private JComponent makePatientListSubRow2(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Insets insets = includePrescribedCheckBox.getInsets();
        insets.left = -1;
        includePrescribedCheckBox.setMargin(insets);
        panel.add(includePrescribedCheckBox, "");
        panel.add(updatePatientListButton);
        return panel;
    }

    private JComponent makePatientListSubRow3(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(startPrescButton);
        return panel;
    }

    private void doUpdatePatientList() {
        CompletableFuture<List<PharmaQueueFullDTO>> pharmaList;
        if( includePrescribedCheckBox.isSelected() ){
            pharmaList = Service.api.listPharmaQueueForToday();
        } else {
            pharmaList = Service.api.listPharmaQueueForPrescription();
        }
        pharmaList.thenAccept(result -> {
            EventQueue.invokeLater(() -> {
                callbacks.onUpdatePharmaQueue(result);
            });
        })
        .exceptionally(t -> {
            t.printStackTrace();
            return null;
        });
    }

    public void triggerPharmaQueueUpdate(){
        doUpdatePatientList();
    }

    private void doStartPresc(){
        callbacks.onStartPresc();
    }

}
