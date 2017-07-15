package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
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
    private Callbacks callbacks;

    public RightPane(PharmaQueueFullDTO pharmaQueueFull, java.util.List<DrugFullDTO> drugs, Callbacks callbacks) {
        this.pharmaQueueFull = pharmaQueueFull;
        this.drugs = drugs;
        this.callbacks = callbacks;
        int width = 330;
        auxSubControl = new JPanel(new MigLayout("", "", ""));
        AuxDispRecords dispRecords = new AuxDispRecords(width);
        auxControl = new AuxControl(pharmaQueueFull.patient.patientId, new AuxControl.Callbacks() {
            @Override
            public void onShowVisits(List<RecordPage> pages) {
                doShowVisits(pages);
            }

            @Override
            public void onShowDrugs() {

            }
        });
        setLayout(new MigLayout("", "[" + width + "!]", ""));
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
        add(dispRecords, "grow");
    }

    private void doShowVisits(List<RecordPage> pages) {
        AuxVisitsSubControl dispVisits = new AuxVisitsSubControl(pharmaQueueFull.patient, pages, new AuxVisitsSubControl.Callbacks(){

        });
        auxSubControl.removeAll();
        auxSubControl.add(dispVisits);
        auxSubControl.repaint();
        auxSubControl.revalidate();
    }

    private void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

}
