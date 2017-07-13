package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RightPane extends JPanel {

    private PharmaQueueFullDTO pharmaQueueFull;
    private List<DrugFullDTO> drugs;
    private Workarea workarea;
    private AuxControl auxControl;

    public RightPane(PharmaQueueFullDTO pharmaQueueFull, java.util.List<DrugFullDTO> drugs){
        this.pharmaQueueFull = pharmaQueueFull;
        this.drugs = drugs;
        int width = 330;
        JPanel auxSubControl = new JPanel(new MigLayout("", "", ""));
        AuxDispRecords dispRecords = new AuxDispRecords(width);
        auxControl = new AuxControl(auxSubControl, dispRecords, width - 2);
        setLayout(new MigLayout("", "[" + width + "!]", ""));
        add(new JLabel("投薬"), "growx, wrap");
        add(makeWorkarea(), "growx, wrap");
        {
            JPanel control = new JPanel(new MigLayout("insets 0", "", "[]2[]"));
            auxControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            auxSubControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            control.add(auxControl, "growx, wrap");
            control.add(auxSubControl, "growx");
            add(control, "growx, wrap");
        }
        add(dispRecords, "grow");
    }

    private JComponent makeWorkarea(){
        Workarea wa = new Workarea(pharmaQueueFull.patient, drugs){
            @Override
            public void onPrescDone(){
                // TODO: update patient list
                clearRight();
            }

            @Override
            public void onCancel(){
                clearRight();
            }

            private void clearRight(){
                // TODO: clear records
            }
        };
        wa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        workarea = wa;
        return wa;
    }

}
