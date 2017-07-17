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
    private JPanel auxSubControl;
    private AuxDispRecords dispRecords;
    private AuxVisitsSubControl visitsSubcontrol;
    private AuxDrugsSubControl drugsSubcontrol;

    public RightPane(PharmaQueueFullDTO pharmaQueueFull, java.util.List<DrugFullDTO> drugs, Callbacks callbacks) {
        this.pharmaQueueFull = pharmaQueueFull;
        int width = 330;
        auxSubControl = new JPanel(new MigLayout("fill", "", ""));
        AuxControl auxControl = new AuxControl(pharmaQueueFull.patient.patientId, new AuxControl.Callbacks() {
            @Override
            public void onShowVisits() {
                doShowVisits();
            }

            @Override
            public void onShowDrugs() {
                doShowDrugs();
            }
        });
        setLayout(new MigLayout("", "[" + width + "!]", ""));
        add(new JLabel("投薬"), "growx, wrap");
        {
            Workarea workarea = new Workarea(pharmaQueueFull.patient, drugs, new Workarea.Callbacks() {
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
        if( visitsSubcontrol == null ) {
            Service.api.listVisitIdVisitedAtForPatient(patient.patientId)
                    .thenAccept(visitIds -> {
                        List<RecordPage> pages = RecordPage.divideToPages(visitIds);
                        EventQueue.invokeLater(() -> {
                            this.visitsSubcontrol = new AuxVisitsSubControl(patient, pages, new AuxVisitsSubControl.Callbacks() {
                                @Override
                                public void onShowRecords(List<Integer> visitIds) {
                                    dispRecords.showVisits(visitIds);
                                }
                            });
                            setSubcontrol(visitsSubcontrol);
                            visitsSubcontrol.trigger();
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        alert(t.toString());
                        return null;
                    });
        } else {
            setSubcontrol(visitsSubcontrol);
            visitsSubcontrol.trigger();
        }
    }

    private void doShowDrugs(){
        if( drugsSubcontrol == null ) {
            PatientDTO patient = pharmaQueueFull.patient;
            Service.api.listIyakuhinForPatient(patient.patientId)
                    .thenAccept(result -> {
                        EventQueue.invokeLater(() -> {
                            this.drugsSubcontrol = new AuxDrugsSubControl(patient, result, new AuxDrugsSubControl.Callbacks() {
                                @Override
                                public void onShowRecords(List<Integer> visitIds) {
                                    dispRecords.showVisits(visitIds);
                                }
                            });
                            setSubcontrol(drugsSubcontrol);
                            drugsSubcontrol.trigger();
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        alert(t.toString());
                        return null;
                    });
        } else {
            setSubcontrol(drugsSubcontrol);
            drugsSubcontrol.trigger();
        }
    }

    private void setSubcontrol(JComponent comp){
        auxSubControl.removeAll();
        auxSubControl.setLayout(new MigLayout("fill", "", ""));
        auxSubControl.add(comp, "grow");
        auxSubControl.repaint();
        auxSubControl.revalidate();

    }

    private void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

}
