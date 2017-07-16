package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RightPane extends JPanel {

    public interface Callbacks {
        void onPrescDone();

        void onCancel();
    }

    private PharmaQueueFullDTO pharmaQueueFull;
    private List<DrugFullDTO> drugs;
    private Workarea workarea;
    private AuxControl auxControl;
    private JPanel auxSubControl;
    private AuxDispRecords dispRecords;
    private Callbacks callbacks;

    public RightPane(PharmaQueueFullDTO pharmaQueueFull, java.util.List<DrugFullDTO> drugs, Callbacks callbacks) {
        this.pharmaQueueFull = pharmaQueueFull;
        this.drugs = drugs;
        this.callbacks = callbacks;
        int width = 330;
        auxSubControl = new JPanel(new MigLayout("", "", ""));
        auxControl = new AuxControl(pharmaQueueFull.patient.patientId, new AuxControl.Callbacks() {
            @Override
            public void onShowVisits() {
                doShowVisits();
            }

            @Override
            public void onShowDrugs() {

            }
        });
        setLayout(new MigLayout("", "[" + width + "!]", ""));
        //setLayout(new MigLayout("fill", "", ""));
        add(new JLabel("投薬"), "growx, wrap");
        {
            workarea = new Workarea(pharmaQueueFull.patient, drugs, new Workarea.Callbacks() {
                @Override
                public void onPrescDone() {
                    callbacks.onPrescDone();
                }

                @Override
                public void onCancel() {
                    callbacks.onCancel();
                }
            });
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
        dispRecords = new AuxDispRecords();
        add(dispRecords, "grow");
        auxControl.selectShowVisits();
    }

    private void doShowVisits() {
        PatientDTO patient = pharmaQueueFull.patient;
        int patientId = patient.patientId;
        Service.api.listVisitIdVisitedAtForPatient(patientId)
                .thenAccept(visitIds -> {
                    List<RecordPage>  pages = RecordPage.divideToPages(visitIds);
                    EventQueue.invokeLater(() -> {
                        AuxRecordsNav nav = new AuxRecordsNav(pages, page -> {
                            dispRecords.showVisits(page.getVisitIds());
                        });
                        auxSubControl.removeAll();
                        auxSubControl.add(nav);
                        auxSubControl.add(new JLabel("(" + patient.lastName + patient.firstName + ")"));
                        nav.trigger();
                        auxSubControl.repaint();
                        auxSubControl.revalidate();
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

}
