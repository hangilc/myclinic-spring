package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.RecordPage;
import jp.chang.myclinic.pharma.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RightPane extends JPanel {

    private PharmaQueueFullDTO pharmaQueueFull;
    private List<DrugFullDTO> drugs;
    private Workarea workarea;
    private AuxControl auxControl;

    public interface OnPrescDoneCallback {
        void callback();
    }

    public interface OnCancelCallback {
        void callback();
    }

    public RightPane(PharmaQueueFullDTO pharmaQueueFull, java.util.List<DrugFullDTO> drugs){
        this.pharmaQueueFull = pharmaQueueFull;
        this.drugs = drugs;
        int width = 330;
        JPanel auxSubControl = new JPanel(new MigLayout("", "", ""));
        AuxDispRecords dispRecords = new AuxDispRecords(width);
        auxControl = new AuxControl(new AuxControl.Callbacks(){
            @Override
            public void onShowVisits() {
                auxControl.disableButtons();
                doShowVisits();
            }

            @Override
            public void onShowDrugs() {

            }
        });
        setLayout(new MigLayout("", "[" + width + "!]", ""));
        add(new JLabel("投薬"), "growx, wrap");
        {
            workarea = new Workarea(pharmaQueueFull.patient, drugs);
            workarea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            add(workarea, "growx, wrap");
        }
        {
            JPanel control = new JPanel(new MigLayout("insets 0, fill", "", "[]2[]"));
            auxControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            auxSubControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            control.add(auxControl, "growx, wrap");
            control.add(auxSubControl, "growx");
            add(control, "growx, wrap");
        }
        add(dispRecords, "grow");
    }

    public void setOnPrescDoneCallback(OnPrescDoneCallback callback){
        if( workarea != null ){
            workarea.setOnPrescDoneCallback(callback);
        }
    }

    public void setOnCancelCallback(OnCancelCallback callback){
        if( workarea != null ){
            workarea.setOnCancelCallback(callback);
        }
    }

    private void doShowVisits(){
        PatientDTO patient = pharmaQueueFull.patient;
        Service.api.listVisitIdVisitedAtForPatient(patient.patientId)
                .thenAccept(visitIds -> {
                    List<RecordPage>  pages = RecordPage.divideToPages(visitIds);
                    EventQueue.invokeLater(() -> {
                        auxControl.enableButtons();
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

//    private JComponent makeWorkarea(){
//        Workarea wa = new Workarea(pharmaQueueFull.patient, drugs){
//            @Override
//            public void onPrescDone(){
//                // TODO: update patient list
//                clearRight();
//            }
//
//            @Override
//            public void onCancel(){
//                clearRight();
//            }
//
//            private void clearRight(){
//                // TODO: clear records
//            }
//        };
//        wa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        workarea = wa;
//        return wa;
//    }

}
